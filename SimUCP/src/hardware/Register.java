package hardware;

public class Register<T> extends HardwareComponent {
	/// fields
	private T input;
	private T value;

	/// getters
	// input signals
	public T getInput() {
		return this.input;
	}

	// internal signals
	public T getValue() {
		return this.value;
	}

	// output signals
	public T getOutput() {
		return this.getValue();
	}

	/// setters
	// input signals
	public void setInput(T input) {
		this.input = input;
	}

	// internal signals
	private void setValue(T value) {
		this.value = value;
	}

	// output signals

	// clock rise
	public void clockRise() {
		this.setValue(this.getInput());
	}

	// clock fall
	public void clockFall() {
	}

	// stabilize
	public void stabilize() {
	}

	// constructors
	public Register(T zero) {
		super();

		// input signals
		this.setInput(zero);

		// internal signals
		this.setValue(zero);

		// stabilize
		this.stabilize();
	}
}
