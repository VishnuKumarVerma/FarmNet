package servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/role")
public class RoleRedirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public RoleRedirectServlet() {
        super();
    }

	public void init(ServletConfig config) throws ServletException {
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().append("Served at: ").append(req.getContextPath());
		
//		Get the user role from the session
		HttpSession session = req.getSession(false);
		String role = (session != null) ? (String) session.getAttribute("userRole") : null;
		
		String targetPage;
		
		if(role != null) {
			switch(role) {
			case "admin" :
				targetPage = "/FarmNet/frontend/html/admin.html";
				break;
			case "buyer" :
				targetPage = "/FarmNet/frontend/html/buyer.html";
				break;
			case "farmer" :
				targetPage = "/FarmNet/frontend/html/farmer.html";
				break;
			default:
				targetPage = "/FarmNet/frontend/html/error.html";
				break;
			}
		}else {
			targetPage = "/FarmNet/frontend/html/login.htm";
		}
		
//		Forward to the target HTML page
		resp.sendRedirect(targetPage);
	}

}
