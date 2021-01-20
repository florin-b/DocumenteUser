package documente.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class Utils {

	public static String getStackTrace(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

	public String getConnectionData() {

		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream("mobigest/resource/db_connect.txt");

		StringBuilder result = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			while (reader.ready()) {
				String line = reader.readLine();
				result.append(line);
				result.append("#");
			}
		} catch (IOException e) {

		}

		return result.toString();

	}

	public static Date getDate(String stringDate) {
		DateFormat dateFormat = new SimpleDateFormat("dd.MMM.yy HH:mm:ss", new Locale("en"));
		Date date = new Date();

		try {
			date = dateFormat.parse(stringDate);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	public static String getPlateNrFromJson(String jsonString) {
		String plateNr = "-1";

		Object obj;
		try {
			obj = new JSONParser().parse(jsonString);
			org.json.simple.JSONObject jo = (org.json.simple.JSONObject) obj;
			JSONArray ja = (JSONArray) jo.get("results");

			if (ja.isEmpty()) {
				return "-1";

			}

			org.json.simple.JSONObject articolObject = (org.json.simple.JSONObject) ja.get(0);
			plateNr = (String) articolObject.get("plate");

		} catch (ParseException e) {

		}

		return plateNr;
	}

	public static String dateDiff(String dateStart, String dateStop) {

		StringBuilder result = new StringBuilder();

		if (dateStart.length() == 0 || dateStop.length() == 0)
			return result.toString();

		try {

			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh:mm:ss", new Locale("en"));

			Date d1 = dateFormat.parse(dateStart);
			Date d2 = dateFormat.parse(dateStop);

			long diff = d2.getTime() - d1.getTime();

			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			if (diffDays > 0) {
				result.append(diffDays);
				result.append(" zile ");
			}

			if (diffHours > 0) {
				result.append(diffHours);
				result.append(" ore ");
			}

			if (diffMinutes > 0) {
				result.append(diffMinutes);
				result.append(" minute");
			} else {
				if (diffMinutes != 0) {
					diffMinutes = 60 - Math.abs(diffMinutes);
					result.append(diffMinutes);
					result.append(" minute");
				}
			}

		} catch (Exception e) {
			System.out.println(Utils.getStackTrace(e));
		}

		return result.toString();

	}

	public static String dateDiff(Date dateStart, Date dateStop) {

		StringBuilder result = new StringBuilder();

		if (dateStart == null || dateStop == null)
			return result.toString();

		try {

			long diff = dateStop.getTime() - dateStart.getTime();

			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			if (diffDays > 0) {
				result.append(diffDays);
				result.append(" zile ");
			}

			if (diffHours > 0) {
				result.append(diffHours);
				result.append(" ore ");
			}

			if (diffMinutes > 0) {
				result.append(diffMinutes);

				result.append(" min");

			} else {
				if (diffMinutes != 0) {
					diffMinutes = 60 - Math.abs(diffMinutes);
					result.append(diffMinutes);

					result.append(" min");

				}
			}

		} catch (Exception e) {
			System.out.println(Utils.getStackTrace(e));
		}

		return result.toString();

	}

	public static int dateDiffMinutes(Date dateStart, Date dateStop) {

		long diffMinutes = 0;

		if (dateStart == null || dateStop == null)
			return -1;

		try {

			long diff = dateStop.getTime() - dateStart.getTime();

			diffMinutes = diff / (60 * 1000) % 60;

			if (diffMinutes > 0) {

			} else {
				if (diffMinutes != 0) {
					diffMinutes = 60 - Math.abs(diffMinutes);
				}
			}

		} catch (Exception e) {
			System.out.println(Utils.getStackTrace(e));
		}

		return (int) diffMinutes;

	}



	public static boolean isAngajatVanzari(String tipAngajat) {

		boolean isVanzari;

		switch (tipAngajat) {
		case "AV":
		case "SD":
		case "KA":
		case "CV":
		case "DV":
		case "DD":
		case "SMG":
		case "SMR":
		case "SMW":
		case "SDIP":
			isVanzari = true;
			break;
		default:
			isVanzari = false;
			break;
		}

		return isVanzari;

	}
	
	
	public static String getDepart(String numeDepart) {
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

	
	public static String generateQs(String strValues) {
		StringBuilder items = new StringBuilder("(");

		String[] arrayValues = strValues.split(",");

		for (int i = 0; i < arrayValues.length; i++) {
			if (i != 0)
				items.append(", ");
			items.append("?");
		}

		items.append(")");

		return items.toString();
	}

}
