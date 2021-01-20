package documente.helpers;

public class HelperUser {

	public static String getLogonStatus(int msgId) {

		String logonStatus;

		switch (msgId) {
		case 0:
			logonStatus = "Cont inexistent";
			break;
		case 1:
			logonStatus = "Cont blocat 60 minute";
			break;
		case 2:
			logonStatus = "Parola incorecta";
			break;
		case 4:
			logonStatus = "Cont inactiv";
			break;
		default:
			logonStatus = "Eroare necunoscuta";
			break;

		}

		return logonStatus;

	}

	public static String getCodDepart(String numeDepart) {
		String codDepart;

		switch (numeDepart) {
		case "CHIM":
			codDepart = "07";
			break;

		case "DIVE":
			codDepart = "10";
			break;

		case "ELEC":
			codDepart = "05";
			break;

		case "FERO":
			codDepart = "02";
			break;

		case "GIPS":
			codDepart = "06";
			break;

		case "INST":
			codDepart = "08";
			break;

		case "LEMN":
			codDepart = "01";
			break;

		case "MATE":
			codDepart = "04";
			break;

		case "PARC":
			codDepart = "03";
			break;

		case "HIDR":
			codDepart = "09";
			break;

		default:
			codDepart = "00";
			break;
		}

		return codDepart;

	}

}
