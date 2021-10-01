package documente.model;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.PdfBrushes;
import com.spire.pdf.graphics.PdfFont;
import com.spire.pdf.graphics.PdfFontFamily;
import com.spire.pdf.graphics.PdfStringFormat;
import com.spire.pdf.graphics.PdfTextAlignment;
import com.spire.pdf.graphics.PdfTilingBrush;

import documente.beans.Document;
import documente.beans.DocumentTip;
import documente.beans.RezultatDocArticol;
import documente.beans.Status;
import documente.connection.DBManager;

public class OperatiiDocumente {

	public void adaugaDocument(String caleDocument) {

		System.out.println("Start");
		String url = "jdbc:postgresql://10.1.0.4:5432/postgres";

		Properties props = new Properties();
		props.setProperty("user", "qualitydoc");
		props.setProperty("password", "1quality2doc");
		props.setProperty("ssl", "false");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, props);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		File file = new File(caleDocument);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);

			PreparedStatement ps;

			ps = conn.prepareStatement("INSERT INTO documente VALUES (?, ?, ?)");
			ps.setString(1, "111111");
			ps.setString(2, "categ 123");
			ps.setBinaryStream(3, fis, (int) file.length());

			ps.executeUpdate();
			ps.close();
			fis.close();

			conn.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public Status adaugaDocument(String codArticol, String tipDocument, String document) {

		Status status = new Status();
		status.setSucces(true);

		byte[] byteDoc = Base64.getDecoder().decode(document);

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO documente VALUES (?, ?, ?)")) {

			ps.setString(1, codArticol);
			ps.setString(2, tipDocument);
			ps.setBinaryStream(3, new ByteArrayInputStream(byteDoc), (int) byteDoc.length);

			int rowCount = ps.executeUpdate();

			if (rowCount == 0) {
				status.setSucces(false);
				status.setMsg("Datele nu au fost salvate.");
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
			status.setSucces(false);
			status.setMsg(ex.toString());
		}

		return status;

	}

	public Status adaugaDocument(String codArticol, String tipDocument, byte[] document, String dataStart,
			String dataStop, String furnizor, String nrSarja, String unitLog) {

		Status status = new Status();

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO documente VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

			ps.setString(1, codArticol);
			ps.setString(2, tipDocument);
			ps.setBinaryStream(3, new ByteArrayInputStream(document), (int) document.length);
			ps.setString(4, dataStart);
			ps.setString(5, dataStop);
			ps.setString(6, furnizor);
			ps.setString(7, nrSarja);
			ps.setString(8, unitLog);

			//stergeDocExistent(conn, codArticol, tipDocument, furnizor);

			int rowCount = ps.executeUpdate();

			if (rowCount > 0)
				status.setSucces(true);

		} catch (Exception ex) {
			System.out.println(ex.toString());
			status.setSucces(false);
			status.setMsg(ex.toString());
		}

		return status;

	}

	private Status stergeDocExistent(Connection conn, String codArticol, String tipDocument, String furnizor) {

		Status status = new Status();

		try (PreparedStatement ps = conn
				.prepareStatement("delete from documente where codarticol = ? and tipdocument = ? and furnizor = ? ")) {

			ps.setString(1, codArticol);
			ps.setString(2, tipDocument);
			ps.setString(3, furnizor);

			int rowCount = ps.executeUpdate();

			if (rowCount > 0)
				status.setSucces(true);

		} catch (Exception ex) {
			System.out.println(ex.toString());
			status.setSucces(false);
			status.setMsg(ex.toString());
		}

		return status;

	}
	
	
	public Status stergeDocument(String codArticol, String codSarja, String tipDocument, String furnizor, String startValid, String stopValid){
		
		Status status = new Status();
		
		String sqlString = " delete from documente where codarticol = ? and tipdocument = ? and furnizor = ? and startvalid = ? and stopvalid = ? ";
		
		if (!codSarja.equals("-1"))
			sqlString = " delete from documente where codarticol = ? and tipdocument = ? and furnizor = ? and startvalid = ? and stopvalid = ? and nrsarja = ? ";
		
		
		try (Connection conn = new DBManager().getConnection(); PreparedStatement ps = conn
				.prepareStatement(sqlString)) {

			ps.setString(1, codArticol);
			ps.setString(2, tipDocument);
			ps.setString(3, furnizor);
			ps.setString(4, startValid);
			ps.setString(5, stopValid);
			
			if (!codSarja.equals("-1"))
				ps.setString(6, codSarja);
			

			int rowCount = ps.executeUpdate();

			if (rowCount > 0)
				status.setSucces(true);

		} catch (Exception ex) {
			System.out.println(ex.toString());
			status.setSucces(false);
			status.setMsg(ex.toString());
		}
		
		
		return status;
	}

	public RezultatDocArticol getDocumenteArticol(String codArticol, String tipArticol) {

		RezultatDocArticol rezultat = new RezultatDocArticol();

		List<Document> listDocumente = new ArrayList<>();

		if (tipArticol.equalsIgnoreCase("artsint")) {
			rezultat.setNrSarja(false);
			rezultat.setListDocumente(listDocumente);
			return rezultat;
		}
		else if (tipArticol.equalsIgnoreCase("sintetic")) {
			listDocumente = getDocumenteReper(codArticol);
			rezultat.setNrSarja(false);
		} else {

			List<Document> listDocumenteSintetic = getDocumenteReper(getSinteticArticol(codArticol));
			List<Document> listDocumenteArticol = getDocumenteReper(codArticol);

			Iterator<Document> listIterator = listDocumenteSintetic.listIterator();

			for (Document docArt : listDocumenteArticol) {

				while (listIterator.hasNext()) {
					if (docArt.getTip().equals(listIterator.next().getTip())) {
						listIterator.remove();
					}
				}

			}

			for (Document docSint : listDocumenteSintetic) {
				listDocumenteArticol.add(docSint);
			}

			listDocumente = listDocumenteArticol;

			OperatiiArticole opArticole = new OperatiiArticole();
			rezultat.setNrSarja(opArticole.isSinteticSarja(opArticole.getSinteticArticol(codArticol)));

		}

		rezultat.setListDocumente(listDocumente);

		return rezultat;

	}

	public List<Document> getDocumenteReper(String codArticol) {

		List<Document> listDocumente = new ArrayList<>();
		List<DocumentTip> listTipDoc = new ArrayList<>();

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"select tipdocument, startvalid, stopvalid, furnizor, nrSarja from documente where codarticol = ? order by tipdocument ")) {

			ps.setString(1, codArticol);

			ResultSet rs = ps.executeQuery();
			String tipDocument = "";

			Document doc = new Document();
			DocumentTip tipDoc = new DocumentTip();

			while (rs.next()) {

				tipDoc = new DocumentTip();

				if (!rs.getString(1).equals(tipDocument) && !tipDocument.isEmpty()) {
					doc = new Document();
					doc.setTip(tipDocument);
					doc.setListDocumente(listTipDoc);
					listDocumente.add(doc);
					listTipDoc = new ArrayList<>();
				}

				tipDoc.setDataStartVal(rs.getString(2));
				tipDoc.setDataStopVal(rs.getString(3));
				tipDoc.setCodFurnizor(rs.getString(4));
				tipDoc.setNumeFurnizor(getNumeFurnizor(rs.getString(4)));
				tipDoc.setNrSarja(rs.getString(5));
				listTipDoc.add(tipDoc);

				tipDocument = rs.getString(1);

			}

			doc = new Document();
			doc.setTip(tipDocument);
			doc.setListDocumente(listTipDoc);
			listDocumente.add(doc);

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return listDocumente;

	}

	public static String getSinteticArticol(String codArticol) {

		String localCodArt = codArticol;
		String codSintetic = "";

		if (codArticol.length() == 8)
			localCodArt = "0000000000" + codArticol;

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement("select sintetic from articole where cod  = ? ")) {

			ps.setString(1, localCodArt);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				codSintetic = rs.getString(1);
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return codSintetic;

	}

	private String getNumeFurnizor(String codFurnizor) {

		String numeFurnizor = "Nedefinit";

		String sqlQuery = " select  name1 from sapprd.lfa1 where mandt='900' and lifnr = ? ";

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlQuery)) {

			ps.setString(1, codFurnizor);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				numeFurnizor = rs.getString(1);
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return numeFurnizor;

	}

	public String getDocument(String codArticol, String tipDocument) {

		byte[] docBytes = null;

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select document from documente where codarticol = ? and tipdocument = ? ")) {

			ps.setString(1, codArticol);
			ps.setString(2, tipDocument);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				docBytes = rs.getBytes(1);
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return new String(docBytes, StandardCharsets.UTF_8);

	}

	public byte[] getDocumentByte(String codArticol, String tipDocument, String codFurnizor) {

		byte[] docBytes = null;

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"select document from documente where codarticol = ? and tipdocument = ? and furnizor = ? ")) {

			ps.setString(1, codArticol);
			ps.setString(2, tipDocument);
			ps.setString(3, codFurnizor);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				docBytes = rs.getBytes(1);
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return addWaterMark(docBytes);

	}

	private byte[] addWaterMark(byte[] docBytes) {

		PdfDocument pdf = new PdfDocument();
		pdf.loadFromBytes(docBytes);

		for (int i = 0; i < pdf.getPages().getCount(); i++) {
			PdfPageBase page = pdf.getPages().get(i);
			OperatiiDocumente.insertWatermark(page, "Arabesque " + getDateStamp());
		}

		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		pdf.saveToStream(byteOutStream);

		return byteOutStream.toByteArray();

	}

	private String getDateStamp() {

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		return myDateObj.format(myFormatObj);
	}

	public static void insertWatermark(PdfPageBase page, String watermark) {
		Dimension2D dimension2D = new Dimension();
		dimension2D.setSize(page.getCanvas().getClientSize().getWidth() / 2,
				page.getCanvas().getClientSize().getHeight() / 3);
		PdfTilingBrush brush = new PdfTilingBrush(dimension2D);
		brush.getGraphics().setTransparency(0.3F);
		brush.getGraphics().save();
		brush.getGraphics().translateTransform((float) brush.getSize().getWidth() / 2,
				(float) brush.getSize().getHeight() / 2);
		brush.getGraphics().rotateTransform(-45);
		brush.getGraphics().drawString(watermark, new PdfFont(PdfFontFamily.Helvetica, 24), PdfBrushes.getViolet(), 0,
				0, new PdfStringFormat(PdfTextAlignment.Center));
		brush.getGraphics().restore();
		brush.getGraphics().setTransparency(1);
		Rectangle2D loRect = new Rectangle2D.Float();
		loRect.setFrame(new Point2D.Float(0, 0), page.getCanvas().getClientSize());
		page.getCanvas().drawRectangle(brush, loRect);
	}

	public List<DocumentTip> getDocumenteArticolTip(String codArticol, String tipDocument) {

		List<DocumentTip> listDocumente = new ArrayList<>();

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"select startvalid, stopvalid, furnizor, nrsarja from documente where codarticol = ? and tipdocument = ?  ")) {

			ps.setString(1, codArticol);
			ps.setString(2, tipDocument);

			ResultSet rs = ps.executeQuery();

			DocumentTip tipDoc;
			while (rs.next()) {

				tipDoc = new DocumentTip();
				tipDoc.setDataStartVal(rs.getString(1));
				tipDoc.setDataStopVal(rs.getString(2));
				tipDoc.setCodFurnizor(rs.getString(3));
				tipDoc.setNumeFurnizor(getNumeFurnizor(rs.getString(3)));
				tipDoc.setNrSarja(rs.getString(4));
				listDocumente.add(tipDoc);

			}

			if (listDocumente.isEmpty() && codArticol.length() >= 8)
				listDocumente = getDocumenteSinteticTip(codArticol, tipDocument);

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return listDocumente;
	}

	public List<DocumentTip> getDocumenteSinteticTip(String codArticol, String tipDocument) {

		List<DocumentTip> listDocumente = new ArrayList<>();

		String localCodArt = codArticol;
		String codSintetic = "";

		if (codArticol.length() == 8)
			localCodArt = "0000000000" + codArticol;

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement("select sintetic from articole where cod  = ? ")) {

			ps.setString(1, localCodArt);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				codSintetic = rs.getString(1);
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		listDocumente = getDocumenteArticolTip(codSintetic, tipDocument);

		return listDocumente;

	}

	public Status adaugaTipDocSintetic(String codSintetic, String tipDocument) {

		Status status = new Status();
		status.setSucces(true);

		List<String> listTipDoc = new ArrayList<>();

		if (!tipDocument.trim().isEmpty())
			listTipDoc = Arrays.asList(tipDocument.split(","));

		try (Connection conn = new DBManager().getConnection()) {

			stergeTipDocSintetic(conn, codSintetic);

			PreparedStatement ps = null;

			for (String tipDoc : listTipDoc) {

				ps = conn.prepareStatement("INSERT INTO docsintetice VALUES (?, ?)");

				ps.setString(1, codSintetic);
				ps.setString(2, tipDoc);

				int rowCount = ps.executeUpdate();

				if (rowCount > 0)
					status.setSucces(true);

			}

			if (ps != null)
				ps.close();

		} catch (Exception ex) {
			status.setSucces(false);
			status.setMsg(ex.toString());
			System.out.println(ex.toString());
		}

		return status;
	}

	private Status stergeTipDocSintetic(Connection conn, String codSintetic) {

		Status status = new Status();

		try (PreparedStatement ps = conn.prepareStatement("delete from docsintetice where codsintetic = ?  ")) {

			ps.setString(1, codSintetic);

			int rowCount = ps.executeUpdate();

			if (rowCount > 0)
				status.setSucces(true);

		} catch (Exception ex) {
			System.out.println(ex.toString());
			status.setSucces(false);
			status.setMsg(ex.toString());
		}

		return status;

	}

	public String getTipDocSintetic(String codSintetic) {
		String tipDocs = "";

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select tipdocument from docsintetice where codsintetic = ? ")) {

			ps.setString(1, codSintetic);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				if (tipDocs.isEmpty())
					tipDocs = rs.getString(1);
				else
					tipDocs += "," + rs.getString(1);
			}
		}

		catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return tipDocs;
	}

}
