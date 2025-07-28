package hardware;

import java.util.Arrays;
import java.util.List;

import utility.ALUOperation;

public class ControlUnit extends HardwareComponent {
	/// fields
	private int OPCode;

	/// getters
	// input signals
	public int getOPCode() {
		return this.OPCode;
	}

	// internal signals

	// output signals
	public boolean getWrite() {
		Integer[] values = { 0, 1, 2, 4, 5, 6, 8, 9, 10, 11, 12, 13 };
		List<Integer> list = Arrays.asList(values);
		return list.contains(this.getOPCode());
	}

	public boolean getExtSign() {
		Integer[] values = { 0, 1, 2 };
		List<Integer> list = Arrays.asList(values);
		return list.contains(this.getOPCode());
	}

	public ALUOperation getALUOperation() {
		switch (this.getOPCode()) {
		case 0:
			return ALUOperation.ADD;
		case 1:
			return ALUOperation.SUB;
		case 2:
			return ALUOperation.SEL2;
		case 4:
			return ALUOperation.AND;
		case 5:
			return ALUOperation.OR;
		case 6:
			return ALUOperation.XOR;
		case 8:
			return ALUOperation.SHLL;
		case 9:
			return ALUOperation.SHLR;
		case 10:
			return ALUOperation.ROL;
		case 11:
			return ALUOperation.ROR;
		case 12:
			return ALUOperation.SHAL;
		case 13:
			return ALUOperation.SHAR;
		default:
			return ALUOperation.ZERO;
		}
	}

	public boolean getMemNotRes() {
		return false;
	}

	/// setters
	// input signals
	public void setOPCode(int code) {
		this.OPCode = code;
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
	public ControlUnit() {
		super();

		// input signals
		this.setOPCode(0);

		// stabilize
		this.stabilize();
	}
}
