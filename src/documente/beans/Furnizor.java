package documente.beans;

public class Furnizor {

	private String codFurnizor;
	private String numeFurnizor;

	public String getCodFurnizor() {
		return codFurnizor;
	}

	public void setCodFurnizor(String codFurnizor) {
		this.codFurnizor = codFurnizor;
	}

	public String getNumeFurnizor() {
		return numeFurnizor;
	}

	public void setNumeFurnizor(String numeFurnizor) {
		this.numeFurnizor = numeFurnizor;
	}

	@Override
	public String toString() {
		return "Furnizor [codFurnizor=" + codFurnizor + ", numeFurnizor=" + numeFurnizor + "]";
	}
	
	

}
