package hardware;

public abstract class HardwareComponent {
	// clock rise
	abstract public void clockRise();

	// clock fall
	abstract public void clockFall();

	// stabilize
	abstract public void stabilize();

	// update
	final public void update() {
		this.clockRise();
		this.stabilize();
		this.clockFall();
		this.stabilize();
	}

	// constructors
	public HardwareComponent() {
	}
}
