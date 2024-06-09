package control;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DriverManagerConnectionPool;
import model.UserBean;

@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Register() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String indirizzo = request.getParameter("indirizzo");
		String telefono = request.getParameter("telefono");
		String numero = request.getParameter("numero");
		String intestatario = request.getParameter("intestatario");
		String CVV = request.getParameter("CVV");
		String ruolo = "registeredUser";

		try {
			Connection con = DriverManagerConnectionPool.getConnection();
			String hashedPassword = hashPassword(password);
			String sql = "INSERT INTO UserAccount (email, passwordUser, nome, cognome, indirizzo, telefono, numero, intestatario, CVV, ruolo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, hashedPassword);
			ps.setString(3, nome);
			ps.setString(4, cognome);
			ps.setString(5, indirizzo);
			ps.setString(6, telefono);
			ps.setString(7, numero);
			ps.setString(8, intestatario);
			ps.setString(9, CVV);
			ps.setString(10, ruolo);

			ps.executeUpdate();
			DriverManagerConnectionPool.releaseConnection(con);
		} catch (Exception e) {
			throw new ServletException("Registration failed", e);
		}
		response.sendRedirect(request.getContextPath() + "/loginPage.jsp");
	}

	private String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] messageDigest = md.digest(password.getBytes());
		BigInteger number = new BigInteger(1, messageDigest);
		String hashtext = number.toString(16);

		while (hashtext.length() < 64) {
			hashtext = "0" + hashtext;
		}
		
		return hashtext;
	}
}
