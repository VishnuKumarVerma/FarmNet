package servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DatabaseConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/pricePredictionData")
public class PredictPriceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public PredictPriceServlet() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        DatabaseConnection.getConnection();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        // Database connection
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Query for the crops table
            String cropsQuery = "SELECT * FROM crops";
            Statement cropsStmt = conn.createStatement();
            ResultSet cropsResultSet = cropsStmt.executeQuery(cropsQuery);

            // Query for the historical prices table
            String pricesQuery = "SELECT * FROM historical_prices";
            Statement pricesStmt = conn.createStatement();
            ResultSet pricesResultSet = pricesStmt.executeQuery(pricesQuery);

            // Store historical prices in a map (key: crop_name + region, value: List of prices)
            Map<String, Double> historicalPricesMap = new HashMap<>();
            while (pricesResultSet.next()) {
                String key = pricesResultSet.getString("crop_name") + "_" + pricesResultSet.getString("region");
                double price = pricesResultSet.getDouble("price");
                // Simple average calculation for each crop and region combination
                historicalPricesMap.merge(key, price, (existingPrice, newPrice) -> (existingPrice + newPrice) / 2);
            }

            // HTML output
            out.println("<html><body>");
            out.println("<h1>Crops Data</h1>");
            out.println("<table border='1'><tr><th>Crop ID</th><th>Farmer ID</th><th>Crop Name</th><th>Quantity</th><th>Harvest Date</th><th>Region</th><th>Expected Price (per Unit)</th><th>Predicted Price (per Unit)</th><th>Price Reasonable?</th></tr>");

            while (cropsResultSet.next()) {
                int cropId = cropsResultSet.getInt("crop_id");
                int farmerId = cropsResultSet.getInt("farmer_id");
                String cropName = cropsResultSet.getString("crop_name");
                int quantity = cropsResultSet.getInt("quantity");
                Date harvestDate = cropsResultSet.getDate("harvest_date");
                String region = cropsResultSet.getString("region");
                double expectedPrice = cropsResultSet.getDouble("expected_price");

                // Create key to fetch the historical price data
                String key = cropName + "_" + region;
                Double predictedPrice = historicalPricesMap.get(key);
                String priceReasonable = "No Data";

                // Calculate the predicted price and determine if expected price is reasonable
                if (predictedPrice != null) {
                    // For simplicity, we compare if the expected price is within 10% of the predicted price
                    double priceDifference = Math.abs(expectedPrice - predictedPrice);
                    if (priceDifference <= (predictedPrice * 0.10)) {
                        priceReasonable = "Yes";
                    } else {
                        priceReasonable = "No";
                    }
                }

                out.println("<tr>");
                out.println("<td>" + cropId + "</td>");
                out.println("<td>" + farmerId + "</td>");
                out.println("<td>" + cropName + "</td>");
                out.println("<td>" + quantity + "</td>");
                out.println("<td>" + harvestDate + "</td>");
                out.println("<td>" + region + "</td>");
                out.println("<td>" + expectedPrice + "</td>");
                out.println("<td>" + (predictedPrice != null ? predictedPrice : "N/A") + "</td>");
                out.println("<td>" + priceReasonable + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error connecting to the database.");
        }
    }
}
