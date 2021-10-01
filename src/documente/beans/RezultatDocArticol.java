package documente.beans;

import java.util.List;

public class RezultatDocArticol {

	private boolean isNrSarja;
	private List<Document> listDocumente;

	public boolean isNrSarja() {
		return isNrSarja;
	}

	public void setNrSarja(boolean isNrSarja) {
		this.isNrSarja = isNrSarja;
	}

	public List<Document> getListDocumente() {
		return listDocumente;
	}

	public void setListDocumente(List<Document> listDocumente) {
		this.listDocumente = listDocumente;
	}

	@Override
	public String toString() {
		return "RezultatDocArticol [isNrSarja=" + isNrSarja + ", listDocumente=" + listDocumente + "]";
	}
	
	

}
