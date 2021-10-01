package documente.connection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import documente.beans.Login;
import documente.beans.User;
import documente.helpers.HelperUser;
import documente.utils.Utils;

public class UserDAO {

	public User validateUser(Login login) {

		User user = new User();

		String storedProcedure = "{ call web_pkg.wlogin(?,?,?,?,?,?,?,?,?,?) }";
		int logonStatus = 0;

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				CallableStatement callableStatement = conn.prepareCall(storedProcedure);) {

			callableStatement.setString(1, login.getUsername().trim());
			callableStatement.setString(2, login.getPassword().trim());

			callableStatement.registerOutParameter(3, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(10, java.sql.Types.NUMERIC);

			callableStatement.execute();
			logonStatus = callableStatement.getInt(3);

			if (logonStatus == 3) {

				user.setFiliala(callableStatement.getString(5));
				String codAgent = callableStatement.getString(8);

				for (int i = 0; i < 8 - callableStatement.getString(8).length(); i++) {
					codAgent = "0" + codAgent;
				}

				user.setCodPers(codAgent);
				user.setNume(callableStatement.getString(9));
				user.setTipAcces(callableStatement.getString(6));
				user.setUnitLog("BU90");
				user.setTipAngajat("Asistent DV");
				

				String numeDepart = callableStatement.getString(4);
				String codDepart = Utils.getDepart(numeDepart);

				user.setCodDepart(codDepart);
				user.setUnitLog(getUnitLogAngajat(conn, codAgent));

				user.setSuccessLogon(true);

			} else {
				user.setSuccessLogon(false);
				user.setLogonMessage(HelperUser.getLogonStatus(logonStatus));
			}
		} catch (SQLException e) {
			System.out.println(Utils.getStackTrace(e));

			user.setSuccessLogon(false);
			user.setLogonMessage(HelperUser.getLogonStatus(logonStatus));

			return user;

		} catch (Exception e) {
			System.out.println(Utils.getStackTrace(e));
		}

		return user;
	}
	
	
	private static String getUnitLogAngajat(Connection conn, String angajatId) {

		String unitLog = null;

		try (PreparedStatement stmt = conn.prepareStatement("select filiala from personal  where cod=?")) {

			stmt.setString(1, angajatId);

			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {

				unitLog = rs.getString("filiala");
			}

		} catch (Exception ex) {
			System.out.println(Utils.getStackTrace(ex));
		}

		return unitLog;
	}

}
