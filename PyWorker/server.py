from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
import json
from worker import get_stock_data, get_news, get_historical_data
import yfinance as yf
from collections import defaultdict

app = FastAPI()


class HoldingItem(BaseModel):
    symbol: str
    quantity: int


class PortfolioHistoryRequest(BaseModel):
    holdings: List[HoldingItem]
    cash: float = 0.0

@app.get("/get-news/{symbol}")
async def get_news_endpoint(symbol: str):
    return get_news(symbol)

@app.get("/get-stock-data/{symbol}")
async def get_stock_data_endpoint(symbol: str):
    return get_stock_data(symbol)

# @app.get("/price-multi/{symbols}")
# def multi_stocks_price_data(symbols: str):
#     symbol_list = [s.strip().upper() for s in symbols.split(",")]
#     prices = {}

#     for symbol in symbol_list:
#         try:
#             ticker = yf.Ticker(symbol.upper())
#             current_price = ticker.fast_info['last_price']
#             prices[symbol] = float(current_price)
#         except Exception:
#             prices[symbol] = "Error"

#     return prices

@app.get("/price-single/{symbol}")
async def get_single_price(symbol: str):
    try:
        ticker = yf.Ticker(symbol.upper())
        current_price = ticker.fast_info['last_price']
        return float(current_price)
    except Exception:
        return {"error": f"Could not fetch price for {symbol}"}

@app.get("/stock-history/{ticker}")
async def get_history_endpoint(ticker: str):
    try:
        payload = get_historical_data(ticker, interval="1d")
        return payload
    except Exception as e:
        return {"error": str(e)}


@app.get("/quote-change/{symbol}")
async def get_quote_change(symbol: str):
    """Return previous close and 24h change % for a symbol."""
    try:
        ticker = yf.Ticker(symbol.upper())
        info = ticker.info
        current = info.get("currentPrice") or info.get("regularMarketPrice")
        prev = info.get("regularMarketPreviousClose") or info.get("previousClose")
        if current is None or prev is None:
            hist = ticker.history(period="5d", interval="1d")
            if hist is not None and len(hist) >= 2:
                current = float(hist["Close"].iloc[-1])
                prev = float(hist["Close"].iloc[-2])
            else:
                return {"previousClose": 0.0, "changePercent": 0.0}
        current = float(current)
        prev = float(prev)
        change_pct = ((current - prev) / prev * 100) if prev else 0.0
        return {"previousClose": prev, "changePercent": round(change_pct, 2)}
    except Exception as e:
        return {"previousClose": 0.0, "changePercent": 0.0}


@app.post("/portfolio-history")
async def portfolio_history(req: PortfolioHistoryRequest):
    """Return portfolio value over last 30 days (labels and values) for chart."""
    try:
        date_to_value = defaultdict(float)
        for h in req.holdings:
            try:
                ticker = yf.Ticker(h.symbol.upper())
                hist = ticker.history(period="1mo", interval="1d")
                if hist is not None and not hist.empty:
                    for date_idx, row in hist.iterrows():
                        date_to_value[date_idx.strftime("%Y-%m-%d")] += float(row["Close"]) * h.quantity
            except Exception:
                continue
        if not date_to_value:
            return {"labels": [], "values": []}
        sorted_dates = sorted(date_to_value.keys())
        values = [round(date_to_value[d] + req.cash, 2) for d in sorted_dates]
        return {"labels": sorted_dates, "values": values}
    except Exception as e:
        return {"labels": [], "values": []}