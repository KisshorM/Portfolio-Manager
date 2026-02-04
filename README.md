# Milkyway Portfolio Management System

A comprehensive stock portfolio management application built with Spring Boot, featuring real-time stock data, trading capabilities, and watchlist management.

## Features

### 🏠 Dashboard
- **Portfolio Overview**: View current holdings with real-time prices
- **Buying Power**: Track available cash balance
- **Live Price Updates**: Real-time stock prices via WebSocket
- **Buy Stocks**: Purchase new stocks with instant execution

### 📊 Performance Tracking
- **Holdings Analysis**: Detailed view of current positions
- **Watchlist Sentiment**: Track stocks you're monitoring
- **Transaction History**: Complete audit trail of all trades
- **Real-time Updates**: Live price feeds and market data

### 🔍 Market Lookup
- **Stock Search**: Search for any stock symbol
- **Company Profiles**: Detailed company information
- **Real-time Quotes**: Current prices and market data
- **Add to Watchlist**: Easily add stocks to your watchlist

### 💼 Trading Features
- **Buy Stocks**: Purchase stocks with real-time pricing
- **Sell Stocks**: Sell existing holdings
- **Cost Basis Tracking**: Average cost calculation for multiple purchases
- **Transaction Logging**: Complete history of all trades

### 📋 Watchlist Management
- **Add Stocks**: Add any stock to your watchlist
- **Remove Stocks**: Remove stocks from watchlist
- **Market Monitoring**: Track price movements

## Technology Stack

- **Backend**: Spring Boot 3.5.10
- **Database**: MySQL 8.0
- **ORM**: Hibernate JPA
- **Frontend**: Thymeleaf templates with vanilla JavaScript
- **Styling**: Custom CSS with modern dark theme
- **Real-time Data**: Finnhub API for stock prices
- **WebSocket**: Real-time price updates

## Prerequisites

- Java 17+
- MySQL 8.0+
- Maven 3.6+

## Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd milkyway
   ```

2. **Database Setup**
   - Create MySQL database: `stockview_db`
   - Update database credentials in `src/main/resources/application.properties`

3. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the Application**
   - Open browser: `http://localhost:8080`
   - Default user: demo/demo

## Database Schema

The application uses the following tables:
- `users`: User accounts and cash balances
- `holdings`: Current stock positions
- `watchlist`: Stocks being monitored
- `transactions`: Complete trade history

## API Endpoints

### Trading
- `POST /api/trade/buy` - Buy stocks
- `POST /api/trade/sell` - Sell stocks

### Watchlist
- `POST /api/watchlist/add` - Add stock to watchlist
- `POST /api/watchlist/remove` - Remove stock from watchlist

## Configuration

### Finnhub API
The application uses Finnhub API for real-time stock data. The API key is configured in `PriceService.java`. For production use, move this to environment variables.

### Database
Configure your MySQL connection in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stockview_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Features in Detail

### Real-time Price Updates
- WebSocket connections to Finnhub for live price feeds
- Automatic updates every few seconds
- Fallback to REST API calls for initial data

### Portfolio Calculations
- Average cost basis for multiple purchases
- Real-time P&L calculations
- Total portfolio value tracking

### Transaction Management
- Complete audit trail
- Buy/Sell transaction types
- Price and quantity tracking
- Timestamp logging

### User Interface
- Modern dark theme
- Responsive design
- Real-time data visualization
- Intuitive navigation

## Development

### Project Structure
```
src/main/java/com/neueda/milkyway/
├── Controller/          # Web controllers
├── Entities/           # JPA entities
├── Repository/         # Data access layer
└── Service/            # Business logic

src/main/resources/
├── templates/          # Thymeleaf views
├── application.properties
└── data.sql           # Sample data
```

### Adding New Features
1. Create entity classes in `Entities/`
2. Add repository interfaces in `Repository/`
3. Implement business logic in `Service/`
4. Create REST endpoints in `Controller/`
5. Add UI components in `templates/`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.
