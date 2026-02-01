from fastapi import FastAPI, WebSocket, WebSocketDisconnect
import asyncio
import json
from worker import get_stock_data, get_news, get_historical_data
import yfinance as yf

app = FastAPI()

@app.get("/get-news/{symbol}")
async def get_news_endpoint(symbol: str):
    return get_news(symbol)

@app.get("/get-stock-data/{symbol}")
async def get_stock_data_endpoint(symbol: str):
    return get_stock_data(symbol)

@app.websocket("/ws/stock/{symbols}")
async def stock_websocket(websocket: WebSocket, symbols: str):
    await websocket.accept()
    # Convert "AAPL,TSLA,BTC-USD" into ['AAPL', 'TSLA', 'BTC-USD']
    symbol_list = [s.strip().upper() for s in symbols.split(",")]
    
    try:
        while True:
            payload = {}
            for symbol in symbol_list:
                try:
                    # Using fast_info for the most immediate 'per-second' price
                    price = yf.Ticker(symbol).fast_info['last_price']
                    payload[symbol] = round(price, 2)
                except Exception:
                    payload[symbol] = "Error"

            await websocket.send_json({
                "type": "LIVE_PRICE_UPDATE",
                "prices": payload
            })

            await asyncio.sleep(1) 
            
    except WebSocketDisconnect:
        print(f"Stream disconnected for: {symbols}")

@app.websocket("/ws/price/{symbol}")
async def price_websocket(websocket: WebSocket, symbol: str):
    await websocket.accept()
    ticker = yf.Ticker(symbol.upper())
    
    try:
        while True:
            # Direct hit to fast_info for per-second accuracy
            current_price = ticker.fast_info['last_price']
            
            await websocket.send_json({
                "symbol": symbol.upper(),
                "price": round(current_price, 2)
            })

            # 1 second delay to mimic real-time floor updates
            await asyncio.sleep(1) 
            
    except WebSocketDisconnect:
        pass

@app.websocket("/ws/stock/{ticker}")
async def websocket_endpoint(websocket: WebSocket, ticker: str):
    await websocket.accept()
    try:
        while True:
            payload = get_historical_data(ticker, interval="1m", period="1d")
            await websocket.send_json(payload)
            await asyncio.sleep(5)
    except WebSocketDisconnect:
        pass
