package documente.beans;

public class Articol {

	private String cod;
	private String nume;
	private String tipDocumente;

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getTipDocumente() {
		return tipDocumente;
	}

	public void setTipDocumente(String tipDocumente) {
		this.tipDocumente = tipDocumente;
	}

	@Override
	public String toString() {
		return "Articol [cod=" + cod + ", nume=" + nume + ", tipDocumente=" + tipDocumente + "]";
	}

}
