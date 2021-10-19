package documente.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import documente.beans.Furnizor;
import documente.beans.Login;
import documente.beans.Status;
import documente.beans.User;
import documente.connection.DBManager;
import documente.connection.UserDAO;
import documente.impdocs.ImportDocs;
import documente.model.OperatiiArticole;
import documente.model.OperatiiDocumente;
import documente.model.OperatiiFurnizori;


public class DocTest {

	public static void main(String[] args) throws SQLException, IOException {
		
		
		//testArticoleDocument();
		
		//System.out.println(getArticoleSintetic());
		
		//System.out.println(new UserDAO().validateUser(new Login("NZAHARIA", "82CvVW")));
		

		 //testIn();

		// readFile2();

		//readFile();
		
		//new DocTest().readProperties();
		
		//testLogin();
		
		//testArticole();
		
		//testDocumente();

		//testArticoleDocument();
		
		//testFurnizori();
		
		
		//testSintetice();
		 

		//testDocSintetice();
		
		//testGetDocSintetice();
		
		
				
		//System.out.println(isSinteticSarja());
		
		//System.out.println(setSinteticSarja());
		
		//System.out.println(getFurnizori());
		
		//System.out.println(getSinteticArticol());
		
		//testDocumenteTip();
		
		//testImport();
		
		//testFurnizor();
		
		//testSintFurnizor();
		
		//testArtFurnizor();
		
		testRaportDocumente();
		

	}

	private static byte[] getBytesFromFile() {

		File file = new File("d://temp//cbimage.jpg");
		byte[] fileData = new byte[(int) file.length()];
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			in.read(fileData);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// InputStream myInputStream = new ByteArrayInputStream(fileData);

		// return myInputStream;

		return fileData;

	}

	private static void testIn() {

		byte[] myData = getBytesFromFile();
		
		
		String strDoc =  new String(myData, StandardCharsets.UTF_8);
		
		byte[] byteDoc = strDoc.getBytes();
		
		
		
		
		

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO documente VALUES (?, ?, ?)")) {

			ps.setString(1, "7__7");
			ps.setString(2, "test123");
			ps.setBinaryStream(3, new ByteArrayInputStream(byteDoc), (int) byteDoc.length);

			ps.executeUpdate();

		} catch (SQLException ex) {
			System.out.println(ex.toString());
		}

		/*
		 * System.out.println("Start"); String url =
		 * "jdbc:postgresql://10.1.0.4:5432/postgres";
		 * 
		 * Properties props = new Properties(); props.setProperty("user",
		 * "qualitydoc"); props.setProperty("password", "1quality2doc");
		 * props.setProperty("ssl", "false"); Connection conn =
		 * DriverManager.getConnection(url, props);
		 * 
		 * File file = new File("d://temp//cbimage.jpg"); FileInputStream fis =
		 * new FileInputStream(file);
		 * 
		 * PreparedStatement ps = conn.prepareStatement(
		 * "INSERT INTO documente VALUES (?, ?, ?)"); ps.setString(1, "54321");
		 * ps.setString(2, "test123");
		 * 
		 * ps.setBinaryStream(3, new ByteArrayInputStream(myData), (int)
		 * myData.length);
		 * 
		 * ps.executeUpdate(); ps.close(); fis.close();
		 * 
		 * conn.close();
		 * 
		 */

		System.out.println("Stop");

	}

	private static void readFile2() throws IOException {

		System.out.println("Start");

		File file = new File("d://temp//2019.pdf");
		byte[] fileData = new byte[(int) file.length()];
		FileInputStream in = new FileInputStream(file);
		in.read(fileData);
		in.close();

		InputStream is = new ByteArrayInputStream(fileData);
		String mimeType = URLConnection.guessContentTypeFromStream(is);

		System.out.println("Stop");

		System.out.println("Data file: " + fileData + " ,  type: " + mimeType);

	}

	private static void readFile() throws SQLException, IOException {

		System.out.println("Start");
		String url = "jdbc:postgresql://10.1.0.4:5432/postgres";

		Properties props = new Properties();
		props.setProperty("user", "qualitydoc");
		props.setProperty("password", "1quality2doc");
		props.setProperty("ssl", "false");
		Connection conn = DriverManager.getConnection(url, props);

		PreparedStatement ps = conn.prepareStatement("SELECT document FROM documente WHERE codarticol = '10900020' and tipdocument = '2' ");

		ResultSet rs = ps.executeQuery();

		byte[] imgBytes = null;
		while (rs.next()) {
			imgBytes = rs.getBytes(1);

			System.out.println("img: " + imgBytes);

		}

		FileOutputStream fos = new FileOutputStream(new File("d://temp//test321.pdf"));
		fos.write(imgBytes);
		fos.close();

		rs.close();
		ps.close();
		conn.close();

		System.out.println("Stop");

	}

	private void testOut() throws SQLException {

		String url = "jdbc:postgresql://10.1.0.4:5432/postgres";

		Properties props = new Properties();
		props.setProperty("user", "qualitydoc");
		props.setProperty("password", "1quality2doc");
		props.setProperty("ssl", "false");
		Connection conn = DriverManager.getConnection(url, props);

		PreparedStatement stmt = conn.prepareStatement("select * from documente");

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println("articol: " + rs.getString(1));
			System.out.println("document: " + rs.getString(3));

		}

		rs.close();
		stmt.close();

		conn.close();

	}
	
	private void readProperties() {
		
		Properties prop = new Properties();
		String propFile = "resources/config.properties";
		
		try{
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFile);
		
		if (inputStream != null) {
			prop.load(inputStream);
			
			String dbEngine = prop.getProperty("db_engine");
			
			System.out.println("engine = " + dbEngine);
			
		}
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		
	}
	
	
	private static void testLogin(){
		
		Login login = new Login("androsd", "112");
		User user = new UserDAO().validateUser(login);
		System.out.println("user: " + user);
		
	}
	
	
	private static void testArticole(){
		
		System.out.println(new OperatiiArticole().getListArticole("sintetic", "cod", "100"));
		
	}
	
	private static void testDocumente(){
		System.out.println(new OperatiiDocumente().getDocumenteArticol("10400015", "articol"));
	}
	
	
	private static void testDocumenteTip(){
		System.out.println(new OperatiiDocumente().getDocumenteArticolTip("10400010", "1"));
	}

	
	private static void testDocumentArticol(){
		System.out.println(new OperatiiDocumente().getDocument("10900046","3"));
	}
	
	
	private static void testArticoleDocument(){
		System.out.println(new OperatiiArticole().getArticoleDocument("8608477961"));
	}

	private static void testFurnizori(){
		System.out.println(new OperatiiArticole().getFurnizori("10400010","articol"));
	}	
	
	private static void testSintetice(){
		System.out.println(new OperatiiArticole().getSintetice("900"));
	}
	
	private static void testDocSintetice(){
		System.out.println(new OperatiiDocumente().adaugaTipDocSintetic("123", "2,3"));
	}
	
	
	private static void testGetDocSintetice(){
		System.out.println(new OperatiiDocumente().getTipDocSintetic("900"));
	}
	
	private static boolean isSinteticSarja(){
		return new OperatiiArticole().isSinteticSarja("100");
	}
	
	private static boolean setSinteticSarja(){
		return new OperatiiArticole().setSinteticSarja("100","sterge");
	}
	
	private static List<Furnizor> getFurnizori(){
		return new OperatiiArticole().getFurnizori("763","sintetic");
	}
	
	private static String getSinteticArticol(){
		return new OperatiiArticole().getSinteticArticol("10900032");
	}
	
	private static Status stergeDocument(){
		return new OperatiiDocumente().stergeDocument("10400010", "-1", "1", "4011000048","27.09.2021","27.09.2021");
	}
	
	private static String getArticoleSintetic(){
		return new OperatiiArticole().getArticoleSintetic("413_1").toString();
	}
	
	private static void testFurnizor(){
		new OperatiiFurnizori().cautaFurnizor("FAB");
	}
	
	private static void testSintFurnizor(){
		System.out.println(new OperatiiFurnizori().getSinteticeFurnizor("4011001237", "09"));
	}
	
	private static void testArtFurnizor(){
		System.out.println(new OperatiiFurnizori().getArticoleFurnizor("4012000155", "910_1,910_2"));
	}
	
	
	private static void testRaportDocumente(){
		System.out.println(new OperatiiDocumente().getDocumenteFurnizor("4011100016", "403",""));
	}
	
	private static void testImport(){
		try{
		new ImportDocs().importFromExcel();
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
	
}
