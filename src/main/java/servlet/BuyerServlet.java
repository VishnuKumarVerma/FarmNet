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

@WebServlet("/buyCrops")
public class BuyerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public BuyerServlet() {
        super();
    }
                                                                                                
    @Override
    public void init(ServletConfig config) throws ServletException {
        DatabaseConnection.getConnection();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Query to fetch reasonable crops only
            String query = "SELECT c.crop_id, f.name, f.phone, f.location, c.crop_name, c.quantity, c.harvest_date, c.region, c.expected_price " +
                           "FROM crops c " +
                           "JOIN farmers f ON c.farmer_id = f.farmer_id " +
                           "WHERE c.expected_price BETWEEN (SELECT AVG(price) * 0.90 FROM historical_prices WHERE crop_name = c.crop_name AND region = c.region) " +
                           "AND (SELECT AVG(price) * 1.10 FROM historical_prices WHERE crop_name = c.crop_name AND region = c.region);";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            out.println("<html><body>");
            out.println("<h1>Available Crops for Purchase</h1>");
            out.println("<table border='1'><tr><th>Crop ID</th><th>Farmer Name</th><th>Phone Number</th><th>Location</th><th>Crop Name</th><th>Quantity</th><th>Harvest Date</th><th>Region</th><th>Expected Price</th><th>Action</th></tr>");

            while (rs.next()) {
                int cropId = rs.getInt("crop_id");
                String farmerName = rs.getString("name");
                String phoneNumber = rs.getString("phone");
                String location = rs.getString("location");
                String cropName = rs.getString("crop_name");
                int quantity = rs.getInt("quantity");
                Date harvestDate = rs.getDate("harvest_date");
                String region = rs.getString("region");
                double expectedPrice = rs.getDouble("expected_price");

                out.println("<tr>");
                out.println("<td>" + cropId + "</td>");
                out.println("<td>" + farmerName + "</td>");
                out.println("<td>" + phoneNumber + "</td>");
                out.println("<td>" + location + "</td>");
                out.println("<td>" + cropName + "</td>");
                out.println("<td>" + quantity + "</td>");
                out.println("<td>" + harvestDate + "</td>");
                out.println("<td>" + region + "</td>");
                out.println("<td>" + expectedPrice + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error fetching data.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int buyerId = Integer.parseInt(request.getParameter("buyer_id"));
        int cropId = Integer.parseInt(request.getParameter("crop_id"));
        int farmerId = Integer.parseInt(request.getParameter("farmer_id"));
        double price = Double.parseDouble(request.getParameter("price"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Insert transaction into the database
            String insertQuery = "INSERT INTO transactions (buyer_id, farmer_id, crop_id, price, status) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            pstmt.setInt(1, buyerId);
            pstmt.setInt(2, farmerId);
            pstmt.setInt(3, cropId);
            pstmt.setDouble(4, price);
            pstmt.setString(5, "Pending");

            int rowsInserted = pstmt.executeUpdate();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");

            if (rowsInserted > 0) {
                out.println("<h1>Purchase Successful</h1>");
                out.println("<p>Transaction details:</p>");
                out.println("<ul>");
                out.println("<li>Buyer ID: " + buyerId + "</li>");
                out.println("<li>Farmer ID: " + farmerId + "</li>");
                out.println("<li>Crop ID: " + cropId + "</li>");
                out.println("<li>Price: " + price + "</li>");
                out.println("<li>Status: Pending</li>");
                out.println("</ul>");
            } else {
                out.println("<h1>Purchase Failed</h1>");
            }

            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error processing the transaction.");
        }
    }
}
