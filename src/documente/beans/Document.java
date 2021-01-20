package documente.beans;

import java.util.List;

public class Document {

	private String tip;
	private String nume;
	private List<DocumentTip> listDocumente;

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public List<DocumentTip> getListDocumente() {
		return listDocumente;
	}

	public void setListDocumente(List<DocumentTip> listDocumente) {
		this.listDocumente = listDocumente;
	}

	@Override
	public String toString() {
		return "Document [tip=" + tip + ", nume=" + nume + ", listDocumente=" + listDocumente + "]";
	}

	
	
}
