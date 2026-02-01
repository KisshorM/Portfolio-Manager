import yfinance as yf
import json
import re
from model import predict_sentiment
import matplotlib.pyplot as plt
import io
import base64
import asyncio
from fastapi import FastAPI, WebSocket, WebSocketDisconnect

app = FastAPI()

# news fetching function
def get_news(ticker_symbol):
    ticker = yf.Ticker(ticker_symbol)
    news = ticker.news
    result_list = []
    for item in news[:5]:
        content = item.get("content", {})
        click_url = content.get("clickThroughUrl")
        raw_description = content.get("description", "")
        clean_description = re.sub(r'<[^>]*>', '', raw_description) if raw_description else ""
        predict_sentiment_label = predict_sentiment(clean_description)
        entry = {
            "title": content.get("title"),
            "raw_description": raw_description,
            "description": clean_description,
            "summary": content.get("summary"),
            "publication_date": content.get("pubDate"),
            "url": click_url.get("url") if click_url else None,
            "sentiment": predict_sentiment_label
        }
        result_list.append(entry)
    return json.dumps(result_list, indent=4)

# stock profile fetching function
def get_stock_data(ticker_symbol):
    ticker = yf.Ticker(ticker_symbol)
    info = ticker.info
    result = {
        "website": info.get("website"),
        "sector": info.get("sector"),
        "industry": info.get("industry"),
        "longBusinessSummary": info.get("longBusinessSummary"),
        "totalEmployees": info.get("fullTimeEmployees"),
        "auditRisk": info.get("auditRisk"),
        "boardRisk": info.get("boardRisk"),
        "compensationRisk": info.get("compensationRisk"),
        "shareHolderRightsRisk": info.get("shareHolderRightsRisk"),
        "overallRisk": info.get("overallRisk"),
        "longname": info.get("longName"),
        "currentPrice": info.get("currentPrice"),
        "totalRevenue": info.get("totalRevenue"),
        "debtToEquity": info.get("debtToEquity"),
        "revenuePerShare": info.get("revenuePerShare"),
        "returnOnAssets": info.get("returnOnAssets"),
        "returnOnEquity": info.get("returnOnEquity"),
        "grossProfits": info.get("grossProfits"),
        "freeCashflow": info.get("freeCashflow"),
        "operatingCashflow": info.get("operatingCashflow"),
        "earningsGrowth": info.get("earningsGrowth"),
        "revenueGrowth": info.get("revenueGrowth"),
        "grossMargins": info.get("grossMargins"),
        "ebitdaMargins": info.get("ebitdaMargins"),
        "operatingMargins": info.get("operatingMargins"),
        "marketCap": info.get("marketCap"),
        "profitMargins": info.get("profitMargins"),
    }
    return json.dumps(result, indent=4)

# historical data fetching function
def get_historical_data(ticker_symbol, interval, period="3mo"):
    ticker = yf.Ticker(ticker_symbol)
    hist = ticker.history(period=period, interval=interval)
    if hist.empty:
        return {"data": [], "graph": None}
    hist_json = hist.reset_index()
    hist_json['Date'] = hist_json['Date'].dt.strftime('%Y-%m-%d %H:%M:%S')
    data_records = hist_json.to_dict(orient='records')
    plt.style.use('dark_background')
    fig, ax = plt.subplots(figsize=(12, 6), facecolor='#0D1117')
    ax.set_facecolor('#0D1117')
    ax.plot(hist.index, hist['Close'], color='#00ff41', linewidth=2, label='Close Price')
    ax.fill_between(hist.index, hist['Close'], min(hist['Close']) * 0.98, color='#00ff41', alpha=0.1)
    ax.grid(color='#30363d', linestyle='--', linewidth=0.5)
    ax.spines['top'].set_visible(False)
    ax.spines['right'].set_visible(False)
    ax.spines['left'].set_color('#30363d')
    ax.spines['bottom'].set_color('#30363d')
    plt.title(f"{ticker_symbol} Performance", color='white', fontsize=16, fontweight='bold', pad=20)
    plt.ylabel("Price (USD)", color='#8b949e')
    plt.xlabel("Timeline", color='#8b949e')
    plt.xticks(color='#8b949e')
    plt.yticks(color='#8b949e')
    buf = io.BytesIO()
    plt.savefig(buf, format='png', bbox_inches='tight', dpi=120, facecolor=fig.get_facecolor())
    plt.close(fig)
    buf.seek(0)
    image_base64 = base64.b64encode(buf.read()).decode('utf-8')
    return {
        "data": data_records,
        "graph": f"data:image/png;base64,{image_base64}"
    }

def get_live_price(ticker_symbol):
    return yf.Ticker(ticker_symbol).fast_info['last_price']