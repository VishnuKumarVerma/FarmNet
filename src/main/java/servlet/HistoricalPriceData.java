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
import java.sql.SQLException;

@WebServlet("/historicalPriceData")
public class HistoricalPriceData extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public HistoricalPriceData() {
        super();

    }

	public void init(ServletConfig config) throws ServletException {

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Retrieve data from the request
        String cropName = req.getParameter("crop_name");
        String season = req.getParameter("season");
        String region = req.getParameter("region");
        String price = req.getParameter("price");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Get database connection
            conn = DatabaseConnection.getConnection();

            // SQL query to insert data
            String sql = "INSERT INTO historical_prices (crop_name, season, region, price) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cropName);
            pstmt.setString(2, season);
            pstmt.setString(3, region);
            pstmt.setDouble(4, Double.parseDouble(price));

            // Execute the query
            int rowsAffected = pstmt.executeUpdate();

            // Success response
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<html><body><center>");
            if (rowsAffected > 0) {
                out.println("<h2>Historical Price Data Added Successfully!</h2>");
            } else {
                out.println("<h2>Failed to Add Historical Price Data!</h2>");
            }
            out.println("</center></body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
            // Error response
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<html><body><center>");
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
            out.println("</center></body></html>");
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}

}
