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
			sqlString = " select cod, nume, sintetic from articole ";

		} else
			sqlString = " select cod, nume, cod cod2 from sintetice ";

		if (codArticol.equals("cod")) {
			if (tipArticol.equals("articol"))
				textArticol = "0000000000" + textArticol;

			sqlString += " where cod like '" + textArticol + "%' and rownum < 30 order by cod ";
		} else
			sqlString += " where upper(nume)  like upper('" + textArticol + "%') and rownum < 30order by nume ";

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString)) {

			ResultSet rs = ps.executeQuery();

			OperatiiDocumente opDocumente = new OperatiiDocumente();

			while (rs.next()) {
				Articol articol = new Articol();
				articol.setCod(rs.getString(1).replaceFirst("^0*", ""));
				articol.setNume(rs.getString(2));
				articol.setTipDocumente(opDocumente.getTipDocSintetic(rs.getString(3)));
				listArticole.add(articol);

			}

		} catch (Exception ex) {
			System.out.println(ex.toString());

		}

		Articol articol = new Articol();
		articol.setCod("-1");
		articol.setNume("Selectati un articol");
		articol.setTipDocumente("");
		listArticole.add(0, articol);

		return listArticole;
	}

	public List<ArticolDocument> getArticoleDocument(String nrDocument) {

		String sqlQuery = " select p.matnr, ar.nume, p.zlifnr, k.fkdat from sapprd.vbrk k, sapprd.vbrp p, sapprd.lfa1 l, articole ar "
				+ " where k.mandt = '900' and k.vbeln = ? and ar.cod = p.matnr and k.mandt = p.mandt "
				+ " and k.vbeln = p.vbeln and p.matnr not like '00000000003%' and k.vbtyp = 'M' and p.mandt = l.mandt and p.zlifnr = l.lifnr";

		List<ArticolDocument> listArticole = new ArrayList<>();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlQuery)) {

			ps.setString(1, nrDocument);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ArticolDocument art = new ArticolDocument();
				art.setCodArticol(rs.getString(1));
				art.setNumeArticol(rs.getString(2));
				art.setCodFurnizor(rs.getString(3));
				art.setDataEmitere(rs.getString(4));
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

		// de adaugat criteriu de valabilitate

		String sqlString = " select codarticol, tipdocument, furnizor, startvalid, stopvalid from documente where codarticol in ("
				+ strArticole.toString() + ") order by codarticol ";

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY)) {

			List<String> tipDocs = new ArrayList<>();
			ResultSet rs = ps.executeQuery();

			for (ArticolDocument art : listArticole) {

				while (rs.next()) {

					if (rs.getString(1).equals(art.getCodArticol().replaceFirst("^0*", ""))
							&& rs.getString(3).equals(art.getCodFurnizor()))
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

	public List<Furnizor> getFurnizori(String codArticol, String tipArticol) {

		List<Furnizor> listFurnizori = new ArrayList<>();

		String sqlQuery = "";

		if (tipArticol.equals("articol")) {
			sqlQuery = " select distinct a.lifnr, l.name1 from sapprd.eina a, sapprd.lfa1 l where a.mandt = '900' and a.matnr = ? "
					+ " and a.mandt = l.mandt and a.lifnr = l.lifnr and lower(l.name1) not like '%anulat%' "
					+ " and  a.loekz <> 'X' and l.SPERR <> 'X' and l.SPERM <> 'X' and l.SPERQ <> '01'"
					+ " order by l.name1 ";
		} else if (tipArticol.equals("sintetic") || tipArticol.equals("artsint")) {
			sqlQuery = " select distinct a.lifnr, l.name1 from sapprd.eina a, sapprd.lfa1 l where a.mandt = '900' and a.matnr in "
					+ " ( select cod from articole where sintetic = ? ) "
					+ " and  a.loekz <> 'X' and l.SPERR <> 'X' and l.SPERM <> 'X' and l.SPERQ <> '01'"
					+ " and a.mandt = l.mandt and a.lifnr = l.lifnr and lower(l.name1) not like '%anulat%' order by l.name1 ";
		}

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlQuery)) {

			if (codArticol.length() == 8)
				codArticol = "0000000000" + codArticol;

			ps.setString(1, codArticol);

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

	public List<Articol> getSintetice(String codSintetic) {

		List<Articol> listArticole = new ArrayList<>();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select a.cod, a.nume from sintetice a where lower(a.cod) like '"
								+ codSintetic.toLowerCase() + "%' order by a.cod ")) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Articol art = new Articol();
				art.setCod(rs.getString(1));
				art.setNume(rs.getString(2));
				listArticole.add(art);
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		Articol articol = new Articol();
		articol.setCod("-1");
		articol.setNume("Selectati un sintetic");
		listArticole.add(0, articol);

		return listArticole;

	}

	public boolean isSinteticSarja(String codSintetic) {

		boolean isSintSarja = false;

		String sqlString = " select 1 from sintetice_sarja where sintetic =? ";

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY)) {

			ps.setString(1, codSintetic);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				isSintSarja = true;

			}

		} catch (Exception ex) {
			System.out.println(ex.toString());

		}

		return isSintSarja;
	}

	public boolean setSinteticSarja(String codSintetic, String tipOp) {

		boolean setSintSarja = true;

		String sqlString = " insert into sintetice_sarja(sintetic) values (?)";

		if (tipOp.equals("sterge"))
			sqlString = "delete from sintetice_sarja where sintetic = ? ";

		try (Connection conn = new DBManager().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY)) {

			ps.setString(1, codSintetic);

			int rowCount = ps.executeUpdate();

			if (rowCount == 0) {
				setSintSarja = false;
			}

		} catch (Exception ex) {
			setSintSarja = false;
			System.out.println(ex.toString());

		}

		return setSintSarja;
	}

	public String getSinteticArticol(String codArticol) {
		String codSintetic = "";

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select sintetic from articole a where lower(a.cod) = ? ")) {

			if (codArticol.length() == 8)
				codArticol = "0000000000" + codArticol;

			ps.setString(1, codArticol);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				codSintetic = rs.getString(1);

			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return codSintetic;
	}

	public List<Articol> getArticoleSintetic(String codSintetic) {

		List<Articol> listArticole = new ArrayList<>();

		String sqlString = " select cod, nume from articole where sintetic = ? order by nume ";

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString)) {

			ps.setString(1, codSintetic);
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

		return listArticole;
	}

	public List<Articol> getNumeRepere(String sintetice, String articole) {
		List<Articol> listRepere = new ArrayList<>();

		String strRepere = "";

		if (!sintetice.contains(","))
			strRepere = "'" + sintetice + "'";
		else {
			for (String strSnt : sintetice.split(",")) {
				if (strRepere.isEmpty())
					strRepere = "'" + strSnt + "'";
				else
					strRepere += "," + "'" + strSnt + "'";

			}
		}

		String artAnt = "0000000000";

		if (!articole.contains(","))
			strRepere += "," + "'" + artAnt + articole + "'";
		else {
			for (String strSnt : articole.split(",")) {
				if (strRepere.isEmpty())
					strRepere = "'" + artAnt + strSnt + "'";
				else
					strRepere += "," + "'" + artAnt + strSnt + "'";

			}
		}

		StringBuilder sqlString = new StringBuilder();
		sqlString.append(" select cod, nume from articole where cod in (");
		sqlString.append(strRepere);
		sqlString.append(" ) union ");
		sqlString.append(" select cod, nume from sintetice where cod in (");
		sqlString.append(strRepere);
		sqlString.append(" ) ");

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString.toString())) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Articol articol = new Articol();
				articol.setCod(rs.getString(1).replaceFirst("^0*", ""));
				articol.setNume(rs.getString(2));
				listRepere.add(articol);

			}

		} catch (Exception ex) {
			System.out.println(ex.toString());

		}

		return listRepere;
	}

}
