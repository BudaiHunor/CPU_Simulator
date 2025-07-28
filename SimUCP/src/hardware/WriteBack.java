package hardware;

public class WriteBack extends HardwareComponent {
	/// fields
	private int data;
	private int result;
	private boolean dataNotRes;

	/// getters
	// input signals
	public int getData() {
		return this.data;
	}

	public int getResult() {
		return this.result;
	}

	public boolean getDataNotResult() {
		return this.dataNotRes;
	}

	// internal signals

	// output signals
	public int getWriteData() {
		if (this.getDataNotResult()) {
			return this.getData();
		} else {
			return this.getResult();
		}
	}

	/// setters
	// input signals
	public void setData(int data) {
		this.data = data;
	}

	public void setResult(int res) {
		this.result = res;
	}

	public void setDataNotResult(boolean dnr) {
		this.dataNotRes = dnr;
	}

	// internal signals

	// output signals

	// clock rise
	public void clockRise() {
	}

	// clock fall
	public void clockFall() {
	}

	// stabilize
	public void stabilize() {
	}

	// constructors
	public WriteBack() {
		super();

		// input signals
		this.setData(0);
		this.setResult(0);
		this.setDataNotResult(false);

		// stabilize
		this.stabilize();
	}
}
