import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class DailyOHLC {
    public static void main(String[] args) {
        String apiKey = "d4bo8shr01qoua30olr0d4bo8shr01qoua30olrg";
        String symbol = "SPY";
        
        // First, get current real-time data from Finnhub
        getCurrentData(symbol, apiKey);
        
        // Then get historical OHLC data from Yahoo Finance
        getHistoricalData(symbol);
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
            // Get last 7 days of data
            long now = System.currentTimeMillis() / 1000;
            long sevenDaysAgo = now - (7 * 24 * 60 * 60);
            
            String urlString = String.format(
                "https://query1.finance.yahoo.com/v8/finance/chart/%s?period1=%d&period2=%d&interval=1d",
                symbol, sevenDaysAgo, now);
            
            System.out.println("=== HISTORICAL DATA (Yahoo Finance) ===\n");
            
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
                
                // Display data
                System.out.println("=== Last 7 Days OHLC Data for " + symbol + " ===\n");
                
                for (int i = 0; i < timestamps.length(); i++) {
                    long timestamp = timestamps.getLong(i);
                    java.util.Date date = new java.util.Date(timestamp * 1000L);
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    
                    // Check for null values (market closed days)
                    if (!opens.isNull(i)) {
                        System.out.println("Date: " + sdf.format(date));
                        System.out.println("  Open:   $" + String.format("%.2f", opens.getDouble(i)));
                        System.out.println("  High:   $" + String.format("%.2f", highs.getDouble(i)));
                        System.out.println("  Low:    $" + String.format("%.2f", lows.getDouble(i)));
                        System.out.println("  Close:  $" + String.format("%.2f", closes.getDouble(i)));
                        System.out.println("  Volume: " + String.format("%,d", volumes.getLong(i)));
                        System.out.println();
                    }
                }
                
                // Today's data (last entry)
                int lastIndex = timestamps.length() - 1;
                System.out.println("=== TODAY'S DATA ===");
                if (!opens.isNull(lastIndex)) {
                    System.out.println("Open:   $" + String.format("%.2f", opens.getDouble(lastIndex)));
                    System.out.println("High:   $" + String.format("%.2f", highs.getDouble(lastIndex)));
                    System.out.println("Low:    $" + String.format("%.2f", lows.getDouble(lastIndex)));
                    System.out.println("Close:  $" + String.format("%.2f", closes.getDouble(lastIndex)));
                    System.out.println("Volume: " + String.format("%,d", volumes.getLong(lastIndex)));
                } else {
                    System.out.println("Market is closed or no data available for today yet.");
                }
                
            } else {
                System.out.println("No data found");
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }


        
    }
}
