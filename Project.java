import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class Project {
    public static void main(String[] args) {
        String apiKey = "d4bo8shr01qoua30olr0d4bo8shr01qoua30olrg"; // <-- Replace
        String symbol = "SPY";

        System.out.println("Fetching live stock data for " + symbol + "...");
        System.out.println("Press Ctrl+C to stop.\n");

        // Refresh every 10 seconds
        while (true) {
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
                    in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                if (responseCode != 200) {
                    JSONObject errorJson = new JSONObject(response.toString());
                    System.out.println("Error: " + errorJson.getString("error"));
                    Thread.sleep(10000);
                    continue;
                }
                
                JSONObject json = new JSONObject(response.toString());

                // Quote endpoint returns: c (current), h (high), l (low), o (open), pc (previous close)
                double currentPrice = json.getDouble("c");
                double high = json.getDouble("h");
                double low = json.getDouble("l");
                double open = json.getDouble("o");
                long timestamp = System.currentTimeMillis();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
                String time = sdf.format(new java.util.Date(timestamp));
                
                System.out.println("[" + time + "] Stock: " + symbol);
                System.out.println("[" + time + "] Current Price: $" + currentPrice);
                System.out.println("[" + time + "] Today's High: $" + high);
                System.out.println("[" + time + "] Today's Low: $" + low);
                System.out.println("[" + time + "] Open: $" + open);
                System.out.println("---");
                
                // Wait 10 seconds before next update
                Thread.sleep(10000);

            } catch (InterruptedException e) {
                System.out.println("Program interrupted.");
                break;
            } catch (java.io.IOException e) {
                System.out.println("Network error: " + e.getMessage());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ie) {
                    break;
                }
            } catch (org.json.JSONException e) {
                System.out.println("JSON parsing error: " + e.getMessage());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ie) {
                    break;
                }
            }
        }
    }
}
