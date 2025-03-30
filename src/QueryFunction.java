import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QueryFunction {
	
	public static int classID;
	public static int matID;
	public static boolean findClass = false;
	
	protected static Connection connect() {
		Connection con = null; 
		try {
			// load device
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:data.db");
			Statement stmt = con.createStatement();
			
			String emploieTempClasse = "CREATE TABLE IF NOT EXISTS EmploiTempsClasse ("
					+ "    idEmploiTempsClasse INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "    idClasse INTEGER NOT NULL,"
					+ "    lundiCours TEXT,   "
					+ "    mardiCours TEXT,"
					+ "    mercrediCours TEXT,"
					+ "    jeudiCours TEXT,"
					+ "    vendrediCours TEXT,"
					+ "    FOREIGN KEY (idClasse) REFERENCES Classe(idClasse),"
					+ ");";
			stmt.execute(emploieTempClasse);
			// create classroom table
			String classTable = "CREATE TABLE IF NOT EXISTS Classe ("
					+ "    idClasse INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "    nom TEXT NOT NULL,"
					+ "    cycle TEXT NOT NULL"
					+ ");";
			stmt.execute(classTable);
			// create matiere table
			String matiere = "CREATE TABLE IF NOT EXISTS matiere("
					+ "idMatiere INTEGER NOT NULL,"
					+ "nom_matiere VARCHAR(20) NOT NULL,"
					+ "idClasse INTEGER NOT NULL,"
					+ "FOREIGN KEY (idClasse) REFERENCES Classe(idClasse),"
					+ "PRIMARY KEY(idMatiere AUTOINCREMENT)"
					+ ");";
			stmt.execute(matiere);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	// add classroom
	protected static void addClasse(String name, String cycle, Connection con) throws SQLException{
		PreparedStatement ps = null;
		try {
			
			ps = con.prepareStatement("INSERT INTO Classe(nom, cycle) VALUES(?, ?)");
			ps.setString(1, name);
			ps.setString(2, cycle);
			ps.execute();
			ps.close();
			System.out.println("SUCCESS : nouvelle classe enregistrer");
			
		} finally {
			if(ps!=null)
				ps.close();
		}
	}

	// select all classroom
	protected static List<String> viewClassroom(Connection con) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		List<String> listClassroom = new ArrayList<String>();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT DISTINCT nom FROM Classe");
			while(rs.next())
				listClassroom.add(rs.getString("nom"));
					
		} finally{
			if(stmt != null)
				stmt.close();
			if(rs != null)
				rs.close();
		}
		return listClassroom;
		
	}
	
	// add matiere
	protected static void insertMatiere(String className, String nomMatiere, Connection con) throws SQLException{
		// get classroom id
		String query1 = "SELECT DISTINCT idClasse FROM Classe WHERE nom='" + className+"'";
		// insert matière
		String query2 = "INSERT INTO matiere(nom_matiere, idClasse) VALUES(?,?)";
		// count all mat
		String query3 = "SELECT DISTINCT COUNT(nom_matiere) FROM matiere";
		
		try(Statement stmt = con.createStatement(); PreparedStatement ps = con.prepareStatement(query2)){
			
			ResultSet rs = stmt.executeQuery(query1);
			while (rs.next()) 
				classID = rs.getInt("idClasse");
			
			
			ResultSet rs2 = stmt.executeQuery(query3);
			if(rs2.getInt(1) > 14) {
				System.out.println("INFO: cette classe possede suffisament de matière");
			}
			else {
				// insertion de la matière
				ps.setString(1, nomMatiere);
				ps.setInt(2, classID);
				ps.execute();
				System.out.println("SUCCESS: Matière "+ nomMatiere+" enregistrez :) !");
			}	
			
			rs2.close();
			rs.close();
			
			
		}
	}
	// verify if exist classroom
	protected static boolean verifyClassroom(String className, Connection con) throws SQLException {
	    String query = "SELECT 1 FROM Classe WHERE nom = ?";
	    PreparedStatement pstmt = con.prepareStatement(query);
	    pstmt.setString(1, className);
	    
	    ResultSet rs = pstmt.executeQuery();
	    boolean exists = rs.next(); // Vérifie si une ligne a été retournée

	    rs.close();
	    pstmt.close();
	    
	    return exists;
	}

	// view all matiere by classroom
	protected static List<String> viewMatiere(String classroom, Connection con) throws SQLException{
		String query1 = "SELECT DISTINCT idClasse FROM Classe WHERE nom='" + classroom+"'";
		
		List<String> ListOfMat = new ArrayList<String>();
		
		try (Statement stmt=con.createStatement();){
			
			ResultSet rs1 = stmt.executeQuery(query1);
			while (rs1.next()) 
				matID = rs1.getInt("idClasse");
			
			String query2 = "SELECT DISTINCT nom_matiere FROM matiere WHERE idClasse='"+matID+"'";
			ResultSet rs2 = stmt.executeQuery(query2);
			while(rs2.next())
				ListOfMat.add(rs2.getString("nom_matiere"));
			
		}
		
		return ListOfMat;
	}
	// count number mat
	protected static int countMatiere(String classroom, Connection con) throws SQLException{
		//select classroom id
		String query1 = "SELECT idClasse FROM Classe WHERE nom=?"; 
		// count matiere
		String query2 = "SELECT DISTINCT COUNT(nom_matiere) WHERE idClasse=?";
		
		PreparedStatement pstmt1 = con.prepareStatement(query1);
		pstmt1.setString(1, classroom);
		
		// select classroom id
		ResultSet res1 = pstmt1.executeQuery();
		int idClass = res1.getInt("idClasse");
		
		//count number mat by class
		PreparedStatement pstmt2 = con.prepareStatement(query2);
		pstmt2.setInt(1, idClass);
		
		ResultSet res2 = pstmt2.executeQuery();
		int someMat = res2.getInt(1);
		
		res2.close();
		pstmt2.close();
		res1.close();
		pstmt1.close();
		
		return someMat;
	}
	// insert timestable
	protected static void insertTimeTable(String classroom, String anneScolaire, Connection con) throws SQLException{
		
	}
	
}
