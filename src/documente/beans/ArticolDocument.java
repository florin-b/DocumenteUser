package documente.beans;

import java.util.List;

public class ArticolDocument {

	private String codArticol;
	private String numeArticol;
	private String codFurnizor;
	private String dataEmitere;
	private List<String> tipDocumente;

	public String getCodArticol() {
		return codArticol;
	}

	public void setCodArticol(String codArticol) {
		this.codArticol = codArticol;
	}

	public String getNumeArticol() {
		return numeArticol;
	}

	public void setNumeArticol(String numeArticol) {
		this.numeArticol = numeArticol;
	}

	public List<String> getTipDocumente() {
		return tipDocumente;
	}

	public void setTipDocumente(List<String> tipDocumente) {
		this.tipDocumente = tipDocumente;
	}

	public String getCodFurnizor() {
		return codFurnizor;
	}

	public void setCodFurnizor(String codFurnizor) {
		this.codFurnizor = codFurnizor;
	}

	public String getDataEmitere() {
		return dataEmitere;
	}

	public void setDataEmitere(String dataEmitere) {
		this.dataEmitere = dataEmitere;
	}

	@Override
	public String toString() {
		return "ArticolDocument [codArticol=" + codArticol + ", numeArticol=" + numeArticol + ", tipDocumente="
				+ tipDocumente + "]";
	}

}
