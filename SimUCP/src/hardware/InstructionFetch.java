package hardware;

public class InstructionFetch extends HardwareComponent {
	/// statics
	private static int NR_BITS = 10;

	private static int memory_size() {
		return 1 << InstructionFetch.NR_BITS;
	}

	/// fields
	private int programCounter;
	private int memory[];

	/// getters
	// input signals

	// internal signals
	public int getProgramCounter() {
		return this.programCounter;
	}

	public int getProgramCounterNext() {
		return this.getProgramCounter() + 1;
	}

	public int getValueFromMemory(int address) {
		return this.memory[address % InstructionFetch.memory_size()];
	}

	// output signals
	public int getInstruction() {
		return this.getValueFromMemory(this.getProgramCounter());
	}

	/// setters
	// input signals

	// internal signals
	private void setProgramCounter(int pc) {
		this.programCounter = pc;
	}

	public void setValueInMemory(int address, int value) {
		this.memory[address % InstructionFetch.memory_size()] = value;
	}

	private void resetMemory() {
		this.memory = new int[1 << InstructionFetch.NR_BITS];
	}

	// output signals

	// clock rise
	public void clockRise() {
		this.setProgramCounter(this.getProgramCounterNext());
	}

	// clock fall
	public void clockFall() {
	}

	// stabilize
	public void stabilize() {
	}

	// constructors
	public InstructionFetch() {
		super();

		// internal signals
		this.setProgramCounter(0);
		this.resetMemory();

		// stabilize
		this.stabilize();
	}
}
