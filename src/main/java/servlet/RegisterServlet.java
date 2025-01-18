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
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RegisterServlet() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
//    	to establishe the connection
        DatabaseConnection.getConnection();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get form data from the request
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String pword = req.getParameter("pword");
        String location = req.getParameter("location");
        String phone = req.getParameter("phone");
        String role = req.getParameter("role");

        // Prepare and execute the SQL query to insert data into the 'user' table
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
                "INSERT INTO user (name, email, pword, location, phone, role) VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, pword);
            ps.setString(4, location);
            ps.setString(5, phone);
            ps.setString(6, role);

            // Execute the insert operation
            int result = ps.executeUpdate();
            if (result > 0) {
                // On successful registration
                PrintWriter pw = resp.getWriter();
                pw.println("<html><body bgcolor=black text=white><center>");
                pw.println("<h2>Registration Successful!</h2>");
                pw.println("<a href='/FarmNet/frontend/html/login.html'>Login</a>");
                pw.println("</center></body></html>");
            } else {
                // If no rows were affected
                PrintWriter pw = resp.getWriter();
                pw.println("<html><body bgcolor=black text=white><center>");
                pw.println("<h2>Registration Failed. Try again.</h2>");
                pw.println("<a href='register.html'>Go Back</a>");
                pw.println("</center></body></html>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            PrintWriter pw = resp.getWriter();
            pw.println("<html><body bgcolor=black text=white><center>");
            pw.println("<h2>Error during registration: " + e.getMessage() + "</h2>");
            pw.println("<a href='register.html'>Go Back</a>");
            pw.println("</center></body></html>");
        }
    }
}
