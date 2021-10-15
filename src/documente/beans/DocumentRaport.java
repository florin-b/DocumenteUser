package documente.beans;

public class DocumentRaport {

	private String codReper;
	private String numeReper;
	private String tipDocument;
	private String dataStartValid;
	private String dataStopValid;
	private String nrSarja;
	private String filiala;

	public String getCodReper() {
		return codReper;
	}

	public void setCodReper(String codReper) {
		this.codReper = codReper;
	}

	public String getNumeReper() {
		return numeReper;
	}

	public void setNumeReper(String numeReper) {
		this.numeReper = numeReper;
	}

	public String getTipDocument() {
		return tipDocument;
	}

	public void setTipDocument(String tipDocument) {
		this.tipDocument = tipDocument;
	}

	public String getDataStartValid() {
		return dataStartValid;
	}

	public void setDataStartValid(String dataStartValid) {
		this.dataStartValid = dataStartValid;
	}

	public String getDataStopValid() {
		return dataStopValid;
	}

	public void setDataStopValid(String dataStopValid) {
		this.dataStopValid = dataStopValid;
	}

	public String getNrSarja() {
		return nrSarja;
	}

	public void setNrSarja(String nrSarja) {
		this.nrSarja = nrSarja;
	}

	public String getFiliala() {
		return filiala;
	}

	public void setFiliala(String filiala) {
		this.filiala = filiala;
	}

	@Override
	public String toString() {
		return "DocumentRaport [codReper=" + codReper + ", numeReper=" + numeReper + ", tipDocument=" + tipDocument
				+ ", dataStartValid=" + dataStartValid + ", dataStopValid=" + dataStopValid + ", nrSarja=" + nrSarja
				+ ", filiala=" + filiala + "]";
	}

	
	
}
