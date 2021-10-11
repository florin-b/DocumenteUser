package documente.beans;

import java.util.List;

public class SinteticDocument {

	private String codSintetic;
	private List<String> tipuriDocumente;

	public String getCodSintetic() {
		return codSintetic;
	}

	public void setCodSintetic(String codSintetic) {
		this.codSintetic = codSintetic;
	}

	public List<String> getTipuriDocumente() {
		return tipuriDocumente;
	}

	public void setTipuriDocumente(List<String> tipuriDocumente) {
		this.tipuriDocumente = tipuriDocumente;
	}

	@Override
	public String toString() {
		return "SinteticDocument [codSintetic=" + codSintetic + ", tipuriDocumente=" + tipuriDocumente + "]";
	}

}
