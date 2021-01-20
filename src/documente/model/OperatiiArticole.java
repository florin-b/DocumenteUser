package documente.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import documente.beans.Articol;
import documente.beans.ArticolDocument;
import documente.beans.Furnizor;
import documente.connection.DBManager;

public class OperatiiArticole {

	public List<Articol> getListArticole(String tipArticol, String codArticol, String textArticol) {

		List<Articol> listArticole = new ArrayList<>();

		String sqlString = "";

		if (tipArticol.equals("articol")) {
			sqlString = " select cod, nume from my_docs.articole ";

		} else
			sqlString = " select cod, nume from my_docs.sintetice ";

		if (codArticol.equals("cod")) {
			if (tipArticol.equals("articol"))
				textArticol = "0000000000" + textArticol;

			sqlString += " where cod like '" + textArticol + "%' order by cod ";
		} else
			sqlString += " where upper(nume)  like upper('" + textArticol + "%') order by nume ";

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString)) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Articol articol = new Articol();
				articol.setCod(rs.getString(1).replaceFirst("^0*", ""));
				articol.setNume(rs.getString(2));
				listArticole.add(articol);

			}

		} catch (Exception ex) {
			System.out.println(ex.toString());

		}

		Articol articol = new Articol();
		articol.setCod("-1");
		articol.setNume("Selectati un articol");
		listArticole.add(0, articol);

		return listArticole;
	}

	public List<ArticolDocument> getArticoleDocument(String nrDocument) {

		List<ArticolDocument> listArticole = new ArrayList<>();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"select a.cod, b.nume from sapprd.zcomdet_tableta a, articole b where a.id = ? and a.cod = b.cod order by a.poz ")) {

			ps.setString(1, nrDocument);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ArticolDocument art = new ArticolDocument();
				art.setCodArticol(rs.getString(1));
				art.setNumeArticol(rs.getString(2));
				listArticole.add(art);
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		getDocumenteArticole(listArticole);

		return listArticole;

	}

	private void getDocumenteArticole(List<ArticolDocument> listArticole) {

		StringBuilder strArticole = new StringBuilder();

		for (ArticolDocument art : listArticole) {

			if (strArticole.toString().isEmpty()) {
				strArticole.append("'");
				strArticole.append(art.getCodArticol().replaceFirst("^0*", ""));
				strArticole.append("'");
			} else {
				strArticole.append(",");
				strArticole.append("'");
				strArticole.append(art.getCodArticol().replaceFirst("^0*", ""));
				strArticole.append("'");
			}

		}

		String sqlString = " select codarticol, tipdocument from my_docs.documente where codarticol in ("
				+ strArticole.toString() + ") order by codarticol ";

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY)) {

			List<String> tipDocs = new ArrayList<>();
			ResultSet rs = ps.executeQuery();

			for (ArticolDocument art : listArticole) {

				while (rs.next()) {

					if (rs.getString(1).equals(art.getCodArticol().replaceFirst("^0*", "")))
						tipDocs.add(rs.getString(2));

				}

				art.setTipDocumente(tipDocs);
				tipDocs = new ArrayList<>();
				rs.beforeFirst();
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());

		}

	}

	public List<Furnizor> getFurnizori(String codArticol) {

		List<Furnizor> listFurnizori = new ArrayList<>();
		
		String sqlQuery = " select lifn2, a.name1 from sapprd.WYT3 v, sapprd.lfa1 a where v.mandt = '900' and a.mandt = '900' and v.parvw = 'WL' " + 
						  " and v.mandt = a.mandt and v.lifn2 = a.lifnr and rownum < 10 order by a.name1 ";		 
		
		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlQuery)) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				
				Furnizor furnizor = new Furnizor();
				
				furnizor.setCodFurnizor(rs.getString(1));
				furnizor.setNumeFurnizor(rs.getString(2));
				listFurnizori.add(furnizor);
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return listFurnizori;

	}

}
