from fastapi import FastAPI, Request
from fastapi.templating import Jinja2Templates
import yfinance as yf

app = FastAPI()

templates = Jinja2Templates(directory="frontend")

def get_ticker_info(ticker):
    return ticker.info

@app.get("/test/{symbol}")
async def test_endpoint(request: Request, symbol: str):
    ticker = yf.Ticker(symbol)
    # Fetching the info once to avoid multiple API calls
    info = ticker.info 
    
    return templates.TemplateResponse(
        name="sample.html", 
        context={
            "request": request, 
            "info": info,
            "symbol": symbol.upper()
        }
    )



















