package documente.beans;

public class DocumentTip {

	private String dataStartVal;
	private String dataStopVal;
	private String codFurnizor;
	private String numeFurnizor;

	public String getDataStartVal() {
		return dataStartVal;
	}

	public void setDataStartVal(String dataStartVal) {
		this.dataStartVal = dataStartVal;
	}

	public String getDataStopVal() {
		return dataStopVal;
	}

	public void setDataStopVal(String dataStopVal) {
		this.dataStopVal = dataStopVal;
	}

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
		return "DocumentTip [dataStartVal=" + dataStartVal + ", dataStopVal=" + dataStopVal + ", codFurnizor="
				+ codFurnizor + ", numeFurnizor=" + numeFurnizor + "]";
	}



}
