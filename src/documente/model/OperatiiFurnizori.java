package documente.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import documente.beans.Furnizor;
import documente.beans.Reper;
import documente.connection.DBManager;

public class OperatiiFurnizori {

	public List<Furnizor> cautaFurnizor(String numeFurnizor) {

		List<Furnizor> listFurnizori = new ArrayList<>();

		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select distinct l.lifnr, l.name1 from sapprd.eina a, sapprd.lfa1 l ");
		sqlString.append(" where a.mandt = '900' and upper(l.name1) like ('" + numeFurnizor.toUpperCase() + "%') ");
		sqlString.append(" and a.mandt = l.mandt and a.lifnr = l.lifnr and  a.loekz <> 'X' and l.SPERR <> 'X' ");
		sqlString.append(" and l.SPERM <> 'X' and l.SPERQ <> '01' order by l.name1 ");

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString.toString())) {

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

		Furnizor furnizor = new Furnizor();
		furnizor.setCodFurnizor("-1");
		furnizor.setNumeFurnizor("Selectati un furnizor");
		listFurnizori.add(0, furnizor);

		return listFurnizori;
	}

	public List<Reper> getSinteticeFurnizor(String codFurnizor, String codDepart) {

		List<Reper> listSintetice = new ArrayList<>();

		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select distinct b.sintetic, c.nume ");
		sqlString.append(" from sapprd.eina e, articole b, sintetice c where e.mandt = '900' and e.matnr = b.cod ");
		sqlString.append(" and b.blocat <> '01' and e.loekz <> 'X' and ");
		sqlString.append(" c.cod = b.sintetic and e.lifnr=? and substr(b.grup_vz,0,2) =? order by c.nume ");

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString.toString())) {

			ps.setString(1, codFurnizor);
			ps.setString(2, codDepart.substring(0, 2));
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Reper art = new Reper();
				art.setCod(rs.getString(1));
				art.setNume(rs.getString(2));
				listSintetice.add(art);
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return listSintetice;

	}

	public List<Reper> getArticoleFurnizor(String codFurnizor, String sintetice) {

		List<Reper> listSintetice = new ArrayList<>();

		StringBuilder sqlString = new StringBuilder();

		String lstSint = "";

		if (!sintetice.contains(","))
			lstSint = "'" + sintetice + "'";
		else {
			for (String strSnt : sintetice.split(",")) {
				if (lstSint.isEmpty())
					lstSint = "'" + strSnt + "'";
				else
					lstSint += "," + "'" + strSnt + "'";

			}
		}

		sqlString.append(" select distinct decode(length(e.matnr),18,substr(e.matnr,-8),e.matnr)  cod_art, b.nume ");
		sqlString.append(" from sapprd.eina e, articole b, sintetice c where e.mandt = '900' and e.matnr = b.cod ");
		sqlString.append(" and b.blocat <> '01' and e.loekz <> 'X' and ");
		sqlString.append(" c.cod = b.sintetic and e.lifnr=? and b.sintetic in (" + lstSint + ") order by b.nume ");

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString.toString())) {

			ps.setString(1, codFurnizor);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Reper art = new Reper();
				art.setCod(rs.getString(1));
				art.setNume(rs.getString(2));
				listSintetice.add(art);
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return listSintetice;

	}

}
