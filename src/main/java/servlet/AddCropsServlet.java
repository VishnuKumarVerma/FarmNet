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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/addCrops")
public class AddCropsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AddCropsServlet() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        // Establishes the database connection
        DatabaseConnection.getConnection();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Farmer form data
        String farmerName = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String location = req.getParameter("location");
        String phone = req.getParameter("phone");

        // Crop form data
        String cropName = req.getParameter("crop_name");
        String quantity = req.getParameter("quantity");
        String expectedPrice = req.getParameter("expected_price");
        String harvestDate = req.getParameter("harvest_date");
        String region = req.getParameter("region");

        Connection conn = null;
        PreparedStatement farmerStmt = null;
        PreparedStatement cropStmt = null;
        ResultSet rs = null;

        try {
            // Get database connection
            conn = DatabaseConnection.getConnection();
            
            // Insert farmer details into the farmers table
            String farmerInsertQuery = "INSERT INTO farmers (name, email, password, location, phone) VALUES (?, ?, ?, ?, ?)";
            farmerStmt = conn.prepareStatement(farmerInsertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            farmerStmt.setString(1, farmerName);
            farmerStmt.setString(2, email);
            farmerStmt.setString(3, password);
            farmerStmt.setString(4, location);
            farmerStmt.setString(5, phone);
            farmerStmt.executeUpdate();

            // Retrieve the generated farmer_id
            rs = farmerStmt.getGeneratedKeys();
            int farmerId = 0;
            if (rs.next()) {
                farmerId = rs.getInt(1);
            }

            // Insert crop details into the crops table
            String cropInsertQuery = "INSERT INTO crops (farmer_id, crop_name, quantity, expected_price, harvest_date, region) VALUES (?, ?, ?, ?, ?, ?)";
            cropStmt = conn.prepareStatement(cropInsertQuery);
            cropStmt.setInt(1, farmerId);
            cropStmt.setString(2, cropName);
            cropStmt.setInt(3, Integer.parseInt(quantity));
            cropStmt.setDouble(4, Double.parseDouble(expectedPrice));
            cropStmt.setString(5, harvestDate);
            cropStmt.setString(6, region);
            cropStmt.executeUpdate();

            // Success response
            PrintWriter pw = resp.getWriter();
            resp.setContentType("text/html");
            pw.println("<html><body><center>");
            pw.println("<h2>Farmer and Crops added successfully!</h2>");
            pw.println("<a href='http://localhost:8080/FarmNet/pricePredictionData'>Go To Crops Data</a>");
            pw.println("</center></body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
            // Error response
            PrintWriter pw = resp.getWriter();
            resp.setContentType("text/html");
            pw.println("<html><body><center>");
            pw.println("<h2>Error occurred: " + e.getMessage() + "</h2>");
            pw.println("</center></body></html>");
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (farmerStmt != null) farmerStmt.close();
                if (cropStmt != null) cropStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
