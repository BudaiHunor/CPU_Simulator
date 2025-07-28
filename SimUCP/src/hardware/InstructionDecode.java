package hardware;

import utility.ALUOperation;

public class InstructionDecode extends HardwareComponent {
	/// statics
	private static int NR_BITS = 10, NR_REG = 7;

	private static int memory_size() {
		return 1 << InstructionDecode.NR_BITS;
	}

	/// fields
	private ControlUnit controlUnit;
	private int instruction;
	private boolean write;
	private int writeAddress;
	private int writeData;
	private boolean type1in;
	private int[] registers;
	private int[] memory;

	/// getters
	private ControlUnit getControlUnit() {
		return this.controlUnit;
	}

	// input signals
	public int getInstruction() {
		return this.instruction;
	}

	public boolean getWriteIn() {
		return this.write;
	}

	public int getWriteAddressIn() {
		return this.writeAddress;
	}

	public int getWriteData() {
		return this.writeData;
	}

	public boolean getType1In() {
		return this.type1in;
	}

	// internal signals
	public int getOPCode() {
		return this.getInstruction() >>> 27;
	}

	public boolean getType1() {
		return (((this.getInstruction() << 5) >>> 31) != 0);
	}

	public boolean getType2() {
		return (((this.getInstruction() << 6) >>> 31) != 0);
	}

	public boolean getType3() {
		return (((this.getInstruction() << 7) >>> 31) != 0);
	}

	public int getAddress1() {
		return (this.getInstruction() << 8) >>> 28;
	}

	public int getAddress2() {
		return (this.getInstruction() << 12) >>> 28;
	}

	public int getZeroImmediate() {
		return (this.getInstruction() << 16) >>> 16;
	}

	public int getSignImmediate() {
		return (this.getInstruction() << 16) >> 16;
	}

	public int getExtImmediate() {
		if (this.getExtSign()) {
			// sign
			return this.getSignImmediate();
		} else {
			// zero
			return this.getZeroImmediate();
		}
	}

	public boolean getExtSign() {
		return this.getControlUnit().getExtSign();
	}

	public int getRegister(int index) {
		return this.registers[index % InstructionDecode.NR_REG];
	}

	public int getFromMemory(int address) {
		return this.memory[address % InstructionDecode.memory_size()];
	}

	public int getRegister1() {
		return this.getRegister(this.getAddress1());
	}

	public int getRegister2() {
		return this.getRegister(this.getAddress2());
	}

	public int getMemory1() {
		return this.getFromMemory(this.getRegister1());
	}

	public int getMemory2() {
		return this.getFromMemory(this.getRegister2());
	}

	public boolean getRegWrite() {
		return this.getWriteIn() && !this.getType1In();
	}

	public boolean getMemWrite() {
		return this.getWriteIn() && this.getType1In();
	}

	// output signals
	public int getOperand1() {
		if (this.getType1()) {
			return this.getMemory1();
		} else {
			return this.getRegister1();
		}
//		int reg = this.getRegister(this.getAddress1());
//		if (this.getType1()) {
//			// memory
//			return this.getFromMemory(reg);
//		} else {
//			// register
//			return reg;
//		}
	}

	public int getOperand2() {
		if (this.getType3()) {
			if (this.getType2()) {
				// immediate t23="11"
				return this.getExtImmediate();
			} else {
				// zero t23="01"
				return 0;
			}
		} else {
			if (this.getType2()) {
				// memory t23="10"
				return this.getMemory2();
			} else {
				// register t23="00"
				return this.getRegister2();
			}
		}
//		if (this.getType3()) {
//			if (this.getType2()) {
//				// immediate t23="11"
//				return this.getExtImmediate(this.getExtSign(), this.getShortImmediate());
//			} else {
//				// zero t23="01"
//				return 0;
//			}
//		} else {
//			int reg = this.getRegister(this.getAddress2());
//			if (this.getType2()) {
//				// memory t23="10"
//				return this.getFromMemory(reg);
//			} else {
//				// register t23="00"
//				return reg;
//			}
//		}
	}

	public int getWriteAddressOut() {
		if (this.getType1()) {
			// memory address
			return this.getRegister1();// (this.getAddress1());
		} else {
			// register index
			return this.getAddress1();
		}
	}

	public boolean getWriteOut() {
		return this.getControlUnit().getWrite();
	}

	public ALUOperation getALUOperation() {
		return this.getControlUnit().getALUOperation();
	}

	public boolean getMemNotRes() {
		return this.getControlUnit().getMemNotRes();
	}

	public boolean getType1Out() {
		return this.getType1();
	}

	/// setters
	private void setControlUnit(ControlUnit cu) {
		this.controlUnit = cu;
	}

	// input signals
	public void setInstruction(int instr) {
		this.instruction = instr;
		this.getControlUnit().setOPCode(this.getOPCode());
	}

	public void setWriteIn(boolean w) {
		this.write = w;
	}

	public void setWriteAddressIn(int address) {
		this.writeAddress = address;
	}

	public void setWriteData(int data) {
		this.writeData = data;
	}

	public void setType1In(boolean t) {
		this.type1in = t;
	}

	// internal signals
	private void setRegister(int index, int value) {
		if (index == 0) {
			return;
		}
		this.registers[index % InstructionDecode.NR_REG] = value;
	}

	private void resetRegisters() {
		this.registers = new int[InstructionDecode.NR_REG];
	}

	private void setInMemory(int address, int value) {
		this.memory[address % InstructionDecode.memory_size()] = value;
	}

	private void resetMemory() {
		this.memory = new int[InstructionDecode.memory_size()];
	}

	// output signals

	// clock rise
	public void clockRise() {
		this.getControlUnit().clockRise();
	}

	// clock fall
	public void clockFall() {
		this.getControlUnit().clockFall();
		if (this.getRegWrite()) {
			this.setRegister(this.getWriteAddressIn(), this.getWriteData());
		}
		if (this.getMemWrite()) {
			this.setInMemory(this.getWriteAddressIn(), this.getWriteData());
		}
	}

	// stabilize
	public void stabilize() {
		this.getControlUnit().stabilize();
	}

	// constructors
	public InstructionDecode() {
		super();
		this.setControlUnit(new ControlUnit());

		// input signals
		this.setInstruction(0);
		this.setWriteIn(false);
		this.setWriteAddressIn(0);
		this.setWriteData(0);
		this.setType1In(false);

		// internal signals
		this.resetRegisters();
		this.resetMemory();

		// stabilize
		this.stabilize();
	}
}
