import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * StockChart - A graphical visualization of stock price data using Java Swing
 * This class creates a line chart showing hourly stock prices
 */
public class StockChart extends JPanel {
    
    // ArrayLists to store price and time data
    private ArrayList<Double> prices;
    private ArrayList<String> times;
    private String symbol;
    private static JFrame chartFrame = null;
    private static StockChart chartPanel = null;
    
    /**
     * Constructor for StockChart
     * @param prices ArrayList of stock prices
     * @param times ArrayList of time labels
     * @param symbol Stock ticker symbol
     */
    public StockChart(ArrayList<Double> prices, ArrayList<String> times, String symbol) {
        this.prices = prices;
        this.times = times;
        this.symbol = symbol;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
    }
    
    /**
     * Paints the chart on the panel
     * This method is called automatically by Swing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Check if we have data to display
        if (prices == null || prices.isEmpty()) {
            g.drawString("No data to display", 50, 50);
            return;
        }
        
        // Cast to Graphics2D for better rendering
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Define chart dimensions and margins
        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int labelPadding = 25;
        
        // Calculate min and max prices for scaling
        double minPrice = Double.MAX_VALUE;
        double maxPrice = Double.MIN_VALUE;
        
        for (double price : prices) {
            if (price < minPrice) minPrice = price;
            if (price > maxPrice) maxPrice = price;
        }
        
        // Add some padding to the price range
        double priceRange = maxPrice - minPrice;
        minPrice -= priceRange * 0.1;
        maxPrice += priceRange * 0.1;
        priceRange = maxPrice - minPrice;
        
        // Draw title
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.BLACK);
        String title = symbol + " - 1-Minute Price Chart (Live Updates)";
        FontMetrics metrics = g2.getFontMetrics();
        int titleWidth = metrics.stringWidth(title);
        g2.drawString(title, (width - titleWidth) / 2, 30);
        
        // Draw axes
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        
        // Y-axis (vertical)
        g2.drawLine(padding + labelPadding, padding, padding + labelPadding, height - padding - labelPadding);
        
        // X-axis (horizontal)
        g2.drawLine(padding + labelPadding, height - padding - labelPadding, 
                   width - padding, height - padding - labelPadding);
        
        // Draw Y-axis labels (prices)
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        int numYLabels = 10;
        for (int i = 0; i <= numYLabels; i++) {
            int x = padding;
            int y = height - ((i * (height - 2 * padding - labelPadding)) / numYLabels + padding + labelPadding);
            double priceLabel = minPrice + (priceRange * i / numYLabels);
            
            g2.setColor(Color.BLACK);
            String label = String.format("$%.2f", priceLabel);
            metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(label);
            g2.drawString(label, x - labelWidth, y + 5);
            
            // Draw horizontal grid lines
            g2.setColor(new Color(200, 200, 200));
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(padding + labelPadding + 1, y, width - padding, y);
            g2.setStroke(new BasicStroke(2));
        }
        
        // Draw X-axis labels (times)
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i < times.size(); i++) {
            if (i % Math.max(1, times.size() / 10) == 0) { // Show every nth label to avoid crowding
                int x = padding + labelPadding + ((i * (width - 2 * padding - labelPadding)) / (times.size() - 1));
                int y = height - padding - labelPadding;
                
                g2.drawString(times.get(i), x - 15, y + 20);
                
                // Draw small tick mark
                g2.drawLine(x, y, x, y + 5);
            }
        }
        
        // Draw the price line
        g2.setColor(new Color(0, 100, 255)); // Blue line
        g2.setStroke(new BasicStroke(3));
        
        for (int i = 0; i < prices.size() - 1; i++) {
            int x1 = padding + labelPadding + ((i * (width - 2 * padding - labelPadding)) / (prices.size() - 1));
            int y1 = height - padding - labelPadding - 
                    (int) ((prices.get(i) - minPrice) / priceRange * (height - 2 * padding - labelPadding));
            
            int x2 = padding + labelPadding + (((i + 1) * (width - 2 * padding - labelPadding)) / (prices.size() - 1));
            int y2 = height - padding - labelPadding - 
                    (int) ((prices.get(i + 1) - minPrice) / priceRange * (height - 2 * padding - labelPadding));
            
            g2.drawLine(x1, y1, x2, y2);
        }
        
        // Draw data points
        g2.setColor(new Color(255, 100, 0)); // Orange points
        for (int i = 0; i < prices.size(); i++) {
            int x = padding + labelPadding + ((i * (width - 2 * padding - labelPadding)) / (prices.size() - 1));
            int y = height - padding - labelPadding - 
                    (int) ((prices.get(i) - minPrice) / priceRange * (height - 2 * padding - labelPadding));
            
            g2.fillOval(x - 4, y - 4, 8, 8);
        }
        
        // Draw legend
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.BLACK);
        g2.drawString("Latest: $" + String.format("%.2f", prices.get(prices.size() - 1)), 
                     width - 150, height - 20);
    }
    
    /**
     * Updates the chart data and refreshes the display
     * @param newPrices ArrayList of stock prices
     * @param newTimes ArrayList of time labels
     */
    public void updateData(ArrayList<Double> newPrices, ArrayList<String> newTimes) {
        this.prices = newPrices;
        this.times = newTimes;
        repaint(); // Redraw the chart with new data
    }
    
    /**
     * Creates and displays the chart window
     * @param prices ArrayList of stock prices
     * @param times ArrayList of time labels
     * @param symbol Stock ticker symbol
     */
    public static void displayChart(ArrayList<Double> prices, ArrayList<String> times, String symbol) {
        SwingUtilities.invokeLater(() -> {
            // If chart window already exists, just update it
            if (chartFrame != null && chartFrame.isVisible()) {
                chartPanel.updateData(prices, times);
            } else {
                // Create new chart window
                chartPanel = new StockChart(prices, times, symbol);
                chartFrame = new JFrame("Stock Price Chart - Live Updates");
                chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                chartFrame.add(chartPanel);
                chartFrame.pack();
                chartFrame.setLocationRelativeTo(null); // Center on screen
                chartFrame.setVisible(true);
            }
        });
    }
}
