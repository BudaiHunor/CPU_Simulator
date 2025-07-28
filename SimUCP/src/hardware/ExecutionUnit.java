package hardware;

import utility.ALUOperation;

public class ExecutionUnit extends HardwareComponent {
	/// fields
	private int operand1;
	private int operand2;
	private ALUOperation opALU;

	/// getters
	// input signals
	public int getOperand1() {
		return this.operand1;
	}

	public int getOperand2() {
		return this.operand2;
	}

	public ALUOperation getALUOperation() {
		return this.opALU;
	}

	// internal signals

	// output signals
	public int getALUResult() {
		switch (this.getALUOperation()) {
		case ZERO:
			return 0;
		case ADD:
			return this.getOperand1() + this.getOperand2();
		case SUB:
			return this.getOperand1() - this.getOperand2();
		case SEL1:
			return this.getOperand1();
		case SEL2:
			return this.getOperand2();
		case SHLL:
			return this.getOperand1() << this.getOperand2();
		case SHLR:
			return this.getOperand1() >> this.getOperand2();
		case SHAL:
			return this.getOperand1() << this.getOperand2();
		case SHAR:
			return this.getOperand1() >>> this.getOperand2();
		case ROL:
			return Integer.rotateLeft(this.getOperand1(), this.getOperand2());
		case ROR:
			return Integer.rotateRight(this.getOperand1(), this.getOperand2());
		case AND:
			return this.getOperand1() & this.getOperand2();
		case OR:
			return this.getOperand1() | this.getOperand2();
		case XOR:
			return this.getOperand1() ^ this.getOperand2();
		default:
			return 0;
		}
	}

	public boolean getZero() {
		return (this.getALUResult() == 0);
	}

	/// setters
	// input signals
	public void setOperand1(int op1) {
		this.operand1 = op1;
	}

	public void setOperand2(int op2) {
		this.operand2 = op2;
	}

	public void setALUOperation(ALUOperation op) {
		this.opALU = op;
	}

	// internals signals

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
	public ExecutionUnit() {
		super();

		// input signals
		this.setOperand1(0);
		this.setOperand2(0);
		this.setALUOperation(ALUOperation.ZERO);

		// stabilize
		this.stabilize();
	}
}
