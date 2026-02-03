from fastapi import FastAPI
import json
from worker import get_stock_data, get_news, get_historical_data, get_live_price
import yfinance as yf

app = FastAPI()

@app.get("/get-news/{symbol}")
async def get_news_endpoint(symbol: str):
    return get_news(symbol)

@app.get("/get-stock-data/{symbol}")
async def get_stock_data_endpoint(symbol: str):
    return get_stock_data(symbol)

@app.get("/price-multi/{symbols}")
def multi_stocks_price_data(symbols: str):
    symbol_list = [s.strip().upper() for s in symbols.split(",")]
    prices = {}

    for symbol in symbol_list:
        try:
            price = get_live_price(symbol)
            prices[symbol] = round(price, 2)
        except Exception:
            prices[symbol] = "Error"

    return prices

@app.get("/price-single/{symbol}")
async def get_single_price(symbol: str):
    try:
        ticker = yf.Ticker(symbol.upper())
        current_price = ticker.fast_info['last_price']
        return {
            "symbol": symbol.upper(),
            "price": round(current_price, 2)
        }
    except Exception:
        return {"error": f"Could not fetch price for {symbol}"}

@app.get("/stock-history/{ticker}")
async def get_history_endpoint(ticker: str):
    try:
        payload = get_historical_data(ticker, interval="1d")
        return payload
    except Exception as e:
        return {"error": str(e)}