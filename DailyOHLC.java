import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class DailyOHLC {
    public static void main(String[] args) {
        String apiKey = "d4bo8shr01qoua30olr0d4bo8shr01qoua30olr0d4bo8shr01qoua30olrg";
        String symbol = "SPY";
        
        // Continuous loop to update data every minute
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("          LIVE UPDATE - " + new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()));
            System.out.println("=".repeat(60) + "\n");
            
            // First, get current real-time data from Finnhub
            getCurrentData(symbol, apiKey);
            
            // Then get historical OHLC data from Yahoo Finance
            getHistoricalData(symbol);
            
            // Wait 60 seconds before next update
            try {
                System.out.println("\nWaiting 60 seconds for next update...");
                Thread.sleep(60000); // 60 seconds = 60000 milliseconds
            } catch (InterruptedException e) {
                System.out.println("Update interrupted");
                break;
            }
        }
    }
    
    // Method to get current real-time stock data
    private static void getCurrentData(String symbol, String apiKey) {
        try {
            String urlString = String.format(
                "https://finnhub.io/api/v1/quote?symbol=%s&token=%s",
                symbol, apiKey);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            BufferedReader in;
            
            if (responseCode == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                System.out.println("API Error Code: " + responseCode);
                return;
            }
            
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());

            // Quote endpoint returns: c (current), h (high), l (low), o (open), pc (previous close)
            double currentPrice = json.getDouble("c");
            double high = json.getDouble("h");
            double low = json.getDouble("l");
            double open = json.getDouble("o");
            long timestamp = System.currentTimeMillis();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
            String time = sdf.format(new java.util.Date(timestamp));
            
            System.out.println("=== REAL-TIME DATA (Finnhub) ===");
            System.out.println("[" + time + "] Stock: " + symbol);
            System.out.println("[" + time + "] Current Price: $" + currentPrice);
            System.out.println("[" + time + "] Today's High: $" + high);
            System.out.println("[" + time + "] Today's Low: $" + low);
            System.out.println("[" + time + "] Open: $" + open);
            System.out.println();

        } catch (Exception e) {
            System.out.println("Error fetching current data: " + e.getMessage());
        }
    }
    
    // Method to get historical OHLC data
    private static void getHistoricalData(String symbol) {
        
        try {
            // Get today's data with 1-minute intervals
            long now = System.currentTimeMillis() / 1000;
            long todayStart = now - (24 * 60 * 60); // Last 24 hours
            
            String urlString = String.format(
                "https://query1.finance.yahoo.com/v8/finance/chart/%s?period1=%d&period2=%d&interval=1m",
                symbol, todayStart, now);
            
            System.out.println("=== TODAY'S 1-MINUTE DATA (Yahoo Finance) ===\n");
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            
            // Parse JSON response
            JSONObject json = new JSONObject(response.toString());
            JSONObject chart = json.getJSONObject("chart");
            JSONArray result = chart.getJSONArray("result");
            
            if (result.length() > 0) {
                JSONObject data = result.getJSONObject(0);
                JSONArray timestamps = data.getJSONArray("timestamp");
                
                // Get quote data
                JSONObject indicators = data.getJSONObject("indicators");
                JSONArray quote = indicators.getJSONArray("quote");
                JSONObject quoteData = quote.getJSONObject(0);
                
                JSONArray opens = quoteData.getJSONArray("open");
                JSONArray highs = quoteData.getJSONArray("high");
                JSONArray lows = quoteData.getJSONArray("low");
                JSONArray closes = quoteData.getJSONArray("close");
                JSONArray volumes = quoteData.getJSONArray("volume");
                
                // Create ArrayLists for the chart
                ArrayList<Double> priceList = new ArrayList<>();
                ArrayList<String> timeList = new ArrayList<>();
                
                // Display 1-minute data for today
                System.out.println("=== 1-Minute Data for " + symbol + " (Last 24 Hours) ===\n");
                
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
                
                for (int i = 0; i < timestamps.length(); i++) {
                    long timestamp = timestamps.getLong(i);
                    java.util.Date date = new java.util.Date(timestamp * 1000L);
                    
                    // Check for null values (market closed hours)
                    if (!closes.isNull(i)) {
                        double closePrice = closes.getDouble(i);
                        String timeLabel = sdf.format(date);
                        
                        // Add to chart data
                        priceList.add(closePrice);
                        timeList.add(timeLabel);
                        
                        System.out.println("Time: " + timeLabel);
                        System.out.println("  Open:   $" + String.format("%.2f", opens.getDouble(i)));
                        System.out.println("  High:   $" + String.format("%.2f", highs.getDouble(i)));
                        System.out.println("  Low:    $" + String.format("%.2f", lows.getDouble(i)));
                        System.out.println("  Close:  $" + String.format("%.2f", closePrice));
                        System.out.println("  Volume: " + String.format("%,d", volumes.getLong(i)));
                        System.out.println();
                    }
                }
                
                // Latest data (last entry)
                int lastIndex = timestamps.length() - 1;
                System.out.println("=== LATEST HOUR ===");
                if (!opens.isNull(lastIndex)) {
                    System.out.println("Open:   $" + opens.getDouble(lastIndex));
                    System.out.println("High:   $" + highs.getDouble(lastIndex));
                    System.out.println("Low:    $" + lows.getDouble(lastIndex));
                    System.out.println("Close:  $" + closes.getDouble(lastIndex));
                    System.out.println("Volume: " +volumes.getLong(lastIndex));
                } else {
                    System.out.println("Market is closed or no data available for today yet.");
                }
                
                // Display the graphical chart
                System.out.println("\nOpening price chart window...");
                StockChart.displayChart(priceList, timeList, symbol);
                
            } else {
                System.out.println("No data found");
            }


            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }



    }
}
