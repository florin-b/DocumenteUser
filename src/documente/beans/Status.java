package documente.beans;

public class Status {
	private boolean succes;
	private String msg;

	public boolean isSucces() {
		return succes;
	}

	public void setSucces(boolean succes) {
		this.succes = succes;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Status [succes=" + succes + ", msg=" + msg + "]";
	}

	
}
