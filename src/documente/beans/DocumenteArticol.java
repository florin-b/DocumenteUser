package documente.beans;

import java.util.List;

public class DocumenteArticol {

	private String codArticol;
	private List<Document> listDocumente;

	public String getCodArticol() {
		return codArticol;
	}

	public void setCodArticol(String codArticol) {
		this.codArticol = codArticol;
	}

	public List<Document> getListDocumente() {
		return listDocumente;
	}

	public void setListDocumente(List<Document> listDocumente) {
		this.listDocumente = listDocumente;
	}

}
