package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;

public class UserModel {
	public synchronized Collection<String> retrieveAllEmail() {
		Collection<String> collection = new LinkedList<String>();
		Connection con = null;
		PreparedStatement ps = null;
		String sql = "SELECT email FROM UserAccount";
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			ps = con.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				collection.add(rs.getString(1));
			}
			return collection;
		}
		catch (Exception e) {
			return collection;
		}
		finally {
			if (con != null) {
				DriverManagerConnectionPool.releaseConnection(con);
			}
		}
	}
	
	public synchronized UserBean insert(UserBean bean, String emailOld) {
		String email = bean.getEmail();
		String nome = bean.getNome();
		String cognome = bean.getCognome();
		String indirizzo = bean.getIndirizzo();
		String telefono = bean.getTelefono();
		String carta = bean.getNumero();
		String intestatario = bean.getIntestatario();
		String cvv = bean.getCvv();
		bean.setRole("registeredUser");
		try {
			Connection con = DriverManagerConnectionPool.getConnection();
			String sql = "UPDATE UserAccount SET email = ?, nome = ?, cognome = ?, indirizzo = ?, telefono = ?, numero = ?, intestatario = ?, CVV = ? WHERE email = ?";
			
			//Modifica UserAccount
			String oldEmail = emailOld;
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, nome);
			ps.setString(3, cognome);
			ps.setString(4, indirizzo);
			ps.setString(5, telefono);
			ps.setString(6, carta);
			ps.setString(7, intestatario);
			ps.setString(8, cvv);
			ps.setString(9, oldEmail);
			
			ps.executeUpdate();
			con.commit();
			DriverManagerConnectionPool.releaseConnection(con);
			return bean;
		}
		catch (Exception e) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
			return bean;
		}
	}
}