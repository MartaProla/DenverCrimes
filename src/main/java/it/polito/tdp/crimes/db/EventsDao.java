package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	public List<Integer>getMeseCrimini(){
		String sql = 	"SELECT DISTINCT (MONTH(reported_date)) AS mese  " + 
						"FROM `events` " + 
						"ORDER BY (MONTH(reported_date)) ASC";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> result = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					result.add(res.getInt("mese"));
					
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			conn.close();
			return result ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}
	public List<String>getCategoriaReato(){
			String sql ="SELECT DISTINCT offense_category_id AS categoria FROM `events`";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			List<String> result = new ArrayList<>() ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					result.add(res.getString("categoria"));
					
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return result ;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
				}
	
		}
	public List<Adiacenza> getAdiacenze(int mese, String evento) {
		String sql=	"SELECT e1.offense_type_id AS of1, e2.offense_type_id AS of2, COUNT(DISTINCT(e1.neighborhood_id)) AS tot "+
					"FROM events e1, events e2 "+
					"WHERE  e1.offense_type_id!= e2.offense_type_id "+
					"AND MONTH(e1.reported_date)= ?  AND MONTH(e2.reported_date)= ? "+
					"AND e1.offense_category_id= ? AND e2.offense_category_id= ? "+
					"AND e1.neighborhood_id=e2.neighborhood_id "+
					"GROUP BY e1.offense_type_id, e2.offense_type_id";
		List<Adiacenza>adiacenze=new LinkedList<>();
					try {
						Connection conn = DBConnect.getConnection() ;
						PreparedStatement st = conn.prepareStatement(sql) ;
						st.setInt(1, mese);
						st.setInt(2, mese);
						st.setString(3, evento);
						st.setString(4, evento);
						
						List<String> result = new ArrayList<>() ;
						ResultSet res = st.executeQuery() ;
						
						while(res.next()) {
							try {
								Adiacenza a =new Adiacenza(res.getString("of1"),res.getString("of2"),res.getDouble("tot"));
								adiacenze.add(a);
								
							} catch (Throwable t) {
								t.printStackTrace();
								System.out.println(res.getInt("id"));
							}
						}
						
						conn.close();
						return adiacenze ;
					
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null ;
							}
				
	}

}
