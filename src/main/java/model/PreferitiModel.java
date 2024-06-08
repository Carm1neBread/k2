package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;

public class PreferitiModel {
	public synchronized Collection<ProductBean> updatePreferiti(ProductBean bean, Collection<ProductBean> collection) {
		Collection<ProductBean> lista = new LinkedList<ProductBean>();
		
		if (collection != null && collection.size() != 0) {
			Iterator<?> it = collection.iterator();
			while (it.hasNext()) {
				ProductBean prodotto = (ProductBean) it.next();
				if (prodotto.getCodice() == bean.getCodice()) {
					prodotto.setNome(bean.getNome());
					prodotto.setDescrizione(bean.getDescrizione());
					prodotto.setPrezzo(bean.getPrezzo());
					prodotto.setSpedizione(bean.getSpedizione());
					prodotto.setTipologia(bean.getTipologia());
					prodotto.setTag(bean.getTag());
				}
				lista.add(prodotto);
			}
		}
		return lista;
	}
	
	public synchronized Collection<ProductBean> ottieni(String email) {
			String userEmail = email;
			String sql = "SELECT * FROM Prodotto WHERE deleted = 'false' AND codice IN (SELECT codiceProdotto FROM Preferiti WHERE emailCliente = ?)";
			ProductBean bean;
			Collection<ProductBean> preferiti = new LinkedList<ProductBean>(); 
			Connection con = null;
			PreparedStatement ps = null;
			
			try {
				con = DriverManagerConnectionPool.getConnection();
				ps = con.prepareStatement(sql);
				ps.setString(1, userEmail);
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					bean = new ProductBean();
					bean.setCodice(rs.getInt("codice"));
					bean.setNome(rs.getString("nome"));
					bean.setDescrizione(rs.getString("descrizione"));
					bean.setPrezzo(rs.getDouble("prezzo"));
					bean.setSpedizione(rs.getDouble("speseSpedizione"));
					bean.setEmail(rs.getString("emailVenditore"));
					bean.setTag(rs.getString("tag"));
					bean.setTipologia(rs.getString("nomeTipologia"));
					bean.setData(rs.getDate("dataAnnuncio"));
					bean.setImmagine(rs.getString("model"));
					
					preferiti.add(bean);
				}
				
				return preferiti;
			}
			catch (Exception e){
				return preferiti;
			}
			finally {
				if (con != null) {
					DriverManagerConnectionPool.releaseConnection(con);
				}	
			}
	}
	
	public synchronized Collection<ProductBean> doRetrieveAll(String where, List<Object> params) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Collection<ProductBean> products = new LinkedList<ProductBean>();
	    
	    String selectSQL = "SELECT * FROM Product";
	    if (where != null && !where.equals("")) {
	        selectSQL += " WHERE " + where;
	    }
	    
	    try {
	        connection = DriverManagerConnectionPool.getConnection();
	        preparedStatement = connection.prepareStatement(selectSQL);
	        
	        // Impostazione dei parametri nella query preparata
	        for (int i = 0; i < params.size(); i++) {
	            preparedStatement.setObject(i + 1, params.get(i));
	        }
	        
	        resultSet = preparedStatement.executeQuery();
	        
	        while (resultSet.next()) {
	            ProductBean bean = new ProductBean();
	            bean.setCodice(resultSet.getInt("codice"));
	            bean.setNome(resultSet.getString("nome"));
	            bean.setDescrizione(resultSet.getString("descrizione"));
	            bean.setPrezzo(resultSet.getDouble("prezzo"));
	            bean.setSpedizione(resultSet.getDouble("speseSpedizione"));
	            bean.setEmail(resultSet.getString("emailVenditore"));
	            bean.setTag(resultSet.getString("tag"));
	            bean.setTipologia(resultSet.getString("nomeTipologia"));
	            bean.setData(resultSet.getDate("dataAnnuncio"));
	            bean.setImmagine(resultSet.getString("model"));
	            
	            products.add(bean);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (resultSet != null)
	                resultSet.close();
	            if (preparedStatement != null)
	                preparedStatement.close();
	            if (connection != null)
	                DriverManagerConnectionPool.releaseConnection(connection);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    return products;
	}

}
