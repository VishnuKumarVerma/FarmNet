package servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.DatabaseConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public LoginServlet() {
        super();

    };

	public void init(ServletConfig config) throws ServletException {
		//	To establise the database connection
		DatabaseConnection.getConnection();
	}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set response type
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        // Retrieve form data
        String email = req.getParameter("email");
        String pword = req.getParameter("pword");
        String role = req.getParameter("role");

        // Debugging: Check received input
        System.out.println("Email: " + email);
        System.out.println("Password: " + pword);
        System.out.println("Role: " + role);
        
        // Validate input
        if (email == null || pword == null || role == null) {
            out.println("<h3 style='color: red;'>All fields are required!</h3>");
            return;
        }

        try {
            // Establish database connection
            // Prepare SQL query to validate user credentials
            String sql = "SELECT name, role FROM user WHERE email = ? AND pword = ? AND role = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, pword);
            ps.setString(3, role);

            // Execute query
            ResultSet rs = ps.executeQuery();

            PrintWriter pw = resp.getWriter();
			pw.println("<html><body bgcolor=black text=white><center>");
			if(rs.next()) 
			{
				pw.println("<a href='/FarmNet/frontend/html/home.html'>Home</a>");
//				  Store role in session
			        HttpSession session = req.getSession();
			        session.setAttribute("userRole", role);
			        
//			        Redirect to the role based page
			        resp.sendRedirect("/FarmNet/role");
			        
			}
			else {
				pw.println("User Not Valid");
			}
			pw.println("</center></body><html>");

            // Close resources
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h3 style='color: red;'>An error occurred while processing your request. Please try again later.</h3>");
        } finally {
            out.close();
        }
    }
}
