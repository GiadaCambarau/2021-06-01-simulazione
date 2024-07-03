package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Arco;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Genes> getEssential(Map<String,Genes> mappa){
		String sql = "SELECT distinct g.GeneID "
				+ "FROM genes g "
				+ "WHERE g.Essential = \"Essential\"";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Genes g = new Genes(res.getString("GeneID"), mappa.get(res.getString("GeneID")).getEssential() ,mappa.get(res.getString("GeneID")).getChromosome() );
				result.add(g);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Arco> getArchi(Map<String,Genes> mappa) {
		String sql = "SELECT i.GeneID1 as g1, i.GeneID2 as g2, i.Expression_Corr as corr, g1.Chromosome, g2.Chromosome "
				+ "FROM interactions i, genes g1, genes g2 "
				+ "WHERE i.GeneID1  != i.GeneID2 AND g1.GeneID = i.GeneID1 AND g2.GeneID = i.GeneID2 AND g1.Essential = \"Essential\" AND g2.Essential = \"Essential\" "
				+ "GROUP BY  i.GeneID1, i.GeneID2";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Arco a = new Arco(mappa.get(res.getString("g1")),mappa.get(res.getString("g2")), res.getDouble("corr"));
					result.add(a);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
				
	}
	


	
}
