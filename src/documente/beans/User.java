package documente.beans;

public class User {

	private String nume;
	private String filiala;
	private String codPers;
	private String codDepart;
	private boolean successLogon;
	private String logonMessage;
	private String tipAngajat;
	private String tipAcces;
	private String unitLog;
	

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public boolean isSuccessLogon() {
		return successLogon;
	}

	public void setSuccessLogon(boolean successLogon) {
		this.successLogon = successLogon;
	}

	public String getLogonMessage() {
		return logonMessage;
	}

	public void setLogonMessage(String logonMessage) {
		this.logonMessage = logonMessage;
	}

	public String getCodPers() {
		return codPers;
	}

	public void setCodPers(String codPers) {
		this.codPers = codPers;
	}

	public String getCodDepart() {
		return codDepart;
	}

	public void setCodDepart(String codDepart) {
		this.codDepart = codDepart;
	}

	public String getFiliala() {
		return filiala;
	}

	public void setFiliala(String filiala) {
		this.filiala = filiala;
	}

	public String getTipAngajat() {
		return tipAngajat;
	}

	public void setTipAngajat(String tipAngajat) {
		this.tipAngajat = tipAngajat;
	}

	public String getTipAcces() {
		return tipAcces;
	}

	public void setTipAcces(String tipAcces) {
		this.tipAcces = tipAcces;
	}

	public String getUnitLog() {
		return unitLog;
	}

	public void setUnitLog(String unitLog) {
		this.unitLog = unitLog;
	}



	@Override
	public String toString() {
		return "User [nume=" + nume + ", filiala=" + filiala + ", codPers=" + codPers + ", codDepart=" + codDepart
				+ ", successLogon=" + successLogon + ", logonMessage=" + logonMessage + ", tipAngajat=" + tipAngajat
				+ ", tipAcces=" + tipAcces + ", unitLog=" + unitLog + "]";
	}



}
