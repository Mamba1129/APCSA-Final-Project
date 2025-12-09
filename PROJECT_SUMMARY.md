# Stock Price Tracker - Live Updates

## Project Overview
A real-time stock price tracking application built in Java that displays live stock data with graphical visualization. The program fetches data from financial APIs and updates a chart every minute with 1-minute interval price data.

## Features
- **Real-time Stock Quotes**: Fetches current price, high, low, and open values from Finnhub API
- **1-Minute Historical Data**: Retrieves intraday price data with 1-minute intervals from Yahoo Finance API
- **Live Updating Chart**: Java Swing-based graphical chart that refreshes automatically every 60 seconds
- **Continuous Monitoring**: Runs in an infinite loop, providing constant updates during market hours

## Technologies Used
- **Language**: Java
- **APIs**: 
  - Finnhub API (real-time stock quotes)
  - Yahoo Finance API (1-minute historical/intraday data)
- **Libraries**: 
  - `org.json` (JSON parsing)
  - Java Swing (GUI/graphical chart)
  - Java networking (`HttpURLConnection`, `BufferedReader`)
- **Data Structures**: ArrayList (for storing prices and time labels)

## File Structure
```
APCSA-Final-Project-main/
├── DailyOHLC.java          # Main program - fetches data and controls update loop
├── StockChart.java         # Swing chart visualization component
├── Project.java            # Original placeholder file
├── lib/
│   └── json-20231013.jar   # JSON parsing library
└── README.md               # Setup instructions
```

## How It Works

### 1. Data Fetching
The program uses two methods to gather stock data:

**`getCurrentData()`**
- Connects to Finnhub API
- Retrieves real-time quote data (current price, high, low, open)
- Displays in terminal with timestamp

**`getHistoricalData()`**
- Connects to Yahoo Finance API
- Fetches 1-minute interval data for the last 24 hours
- Parses JSON response into ArrayLists
- Sends data to chart for visualization

### 2. Live Updates
- Main loop runs continuously (`while(true)`)
- Fetches fresh data every 60 seconds
- Updates both terminal output and graphical chart
- Uses `Thread.sleep(60000)` for 1-minute intervals

### 3. Chart Visualization
- Java Swing `JPanel` with custom `paintComponent()` method
- Displays: axes, grid lines, price labels, time labels, line chart, data points
- Auto-scales Y-axis based on price range
- Reuses same window - calls `repaint()` to refresh with new data

## How to Run

### Prerequisites
- Java 25+ (or Java 11+)
- `lib/json-20231013.jar` library

### Compile
```powershell
javac -cp "lib\json-20231013.jar" StockChart.java DailyOHLC.java
```

### Run
```powershell
java -cp ".;lib\json-20231013.jar" DailyOHLC
```

### Stop
Press `Ctrl+C` in the terminal to stop the live updates

## Data Sources

### Finnhub API
- **Endpoint**: `https://finnhub.io/api/v1/quote`
- **Purpose**: Real-time stock quotes
- **Free Tier**: 60 API calls/minute
- **Data Returned**: Current price, high, low, open, previous close

### Yahoo Finance API
- **Endpoint**: `https://query1.finance.yahoo.com/v8/finance/chart/{symbol}`
- **Purpose**: Historical and intraday price data
- **Free Tier**: Unlimited (no API key required)
- **Interval**: 1-minute candles
- **Data Returned**: Open, High, Low, Close, Volume (OHLC)

## Stock Tracked
- **Symbol**: SPY (SPDR S&P 500 ETF Trust)
- **Market**: NYSE Arca
- **Description**: Tracks the S&P 500 index

## Output Example

### Terminal Output
```
============================================================
          LIVE UPDATE - 11:15:13
============================================================

=== REAL-TIME DATA (Finnhub) ===
[11:15:13] Stock: SPY
[11:15:13] Current Price: $683.73
[11:15:13] Today's High: $685.385
[11:15:13] Today's Low: $682.82
[11:15:13] Open: $684.52

=== TODAY'S 1-MINUTE DATA (Yahoo Finance) ===

Time: 11:15
  Open:   $681.91
  High:   $682.46
  Low:    $681.91
  Close:  $682.42
  Volume: 535,863
...

Waiting 60 seconds for next update...
```

### Graphical Chart
- Window title: "Stock Price Chart - Live Updates"
- Chart title: "SPY - 1-Minute Price Chart (Live Updates)"
- Blue line connecting price points
- Orange dots marking data points
- Price scale on Y-axis
- Time labels on X-axis
- Latest price displayed in bottom right

## Key Programming Concepts

### Data Structures
- **ArrayList<Double>**: Stores closing prices for chart
- **ArrayList<String>**: Stores time labels for X-axis
- **JSONObject**: Parses API responses
- **JSONArray**: Extracts arrays from JSON data

### Control Flow
- **Infinite Loop**: `while(true)` for continuous monitoring
- **Conditionals**: Error handling, null checks for market hours
- **Try-Catch**: Exception handling for network errors, JSON parsing, thread interruption

### Object-Oriented Design
- **StockChart Class**: Extends JPanel, encapsulates chart rendering logic
- **Static Methods**: `displayChart()` creates/updates window, maintains singleton pattern
- **Private Methods**: `getCurrentData()`, `getHistoricalData()` organize code

### Networking
- **HTTP Connections**: `HttpURLConnection` for API requests
- **Input Streams**: `BufferedReader`, `InputStreamReader` to read responses
- **URL Construction**: `String.format()` to build API URLs with parameters

### GUI Programming
- **Swing Components**: JFrame, JPanel, Graphics2D
- **Event Dispatch Thread**: `SwingUtilities.invokeLater()`
- **Custom Rendering**: Override `paintComponent()` for drawing
- **Anti-aliasing**: RenderingHints for smooth graphics

## Limitations
- Only tracks one stock (SPY) - hardcoded symbol
- Yahoo Finance 1-minute data limited to last 7 days
- Chart window doesn't auto-scale X-axis labels optimally for very large datasets
- No error recovery if API is down
- No user input - must modify source code to change stock or interval

## Future Enhancements (Potential)
- User input for stock symbol selection
- Multiple stock comparison
- Technical indicators (moving averages, RSI, MACD)
- Save/export chart as image
- Historical data beyond 7 days
- Customizable update intervals
- Alert notifications for price thresholds

## AP Computer Science A Rubric Coverage

### Currently Implemented (45-55/100):
- ✅ Input/Output (8/8)
- ✅ Loops (4/4)
- ✅ Conditionals (4/4)
- ✅ Exception Handling (4/4)
- ✅ String Manipulation (4/4)
- ⚠️ Methods (3/4) - all void, no return values
- ⚠️ ArrayLists (1/4) - basic usage only
- ⚠️ Computational Thinking (6/8)
- ⚠️ Code Quality (8/12)
- ⚠️ Testing (3/8)

### Missing for Higher Score:
- ❌ Object-Oriented Programming (1/12) - no inheritance, limited OOP
- ❌ Arrays (0/4) - only ArrayLists used
- ❌ 2D Arrays (0/4)
- ❌ Recursion (0/4)
- ❌ Progress Journal (0/8)
- ❌ Comprehensive Comments (missing 4 points)

## Author
- GitHub: [Mamba1129](https://github.com/Mamba1129/APCSA-Final-Project)
- Course: AP Computer Science A
- Date: December 2025

## License
Educational project - free to use and modify
