package hardware;

import utility.ALUOperation;

public class Processor extends HardwareComponent {
	/// fields
	// main parts
	private InstructionFetch instrFetch;
	private InstructionDecode instrDecode;
	private ExecutionUnit executionUnit;
	private WriteBack writeBack;
	// IF/ID
	private Register<Integer> if_id_instruction;
	// ID/EX
	private Register<Integer> id_ex_operand1, id_ex_operand2, id_ex_writeAddress;
	private Register<ALUOperation> id_ex_ALUOperation;
	private Register<Boolean> id_ex_write, id_ex_memNotRes, id_ex_type1;
	// EX/WB
	private Register<Integer> ex_wb_memory, ex_wb_result, ex_wb_writeAddress;
	private Register<Boolean> ex_wb_write, ex_wb_memNotRes, ex_wb_type1;

	/// getters
	// main parts
	public InstructionFetch getInstructionFetch() {
		return this.instrFetch;
	}

	public InstructionDecode getInstructionDecode() {
		return this.instrDecode;
	}

	public ExecutionUnit getExecutionUnit() {
		return this.executionUnit;
	}

	public WriteBack getWriteBack() {
		return this.writeBack;
	}

	// registers
	public int getIF_ID_Instruction() {
		return this.if_id_instruction.getOutput();
	}

	public int getID_EX_Operand1() {
		return this.id_ex_operand1.getOutput();
	}

	public int getID_EX_Operand2() {
		return this.id_ex_operand2.getOutput();
	}

	public int getID_EX_WriteAddress() {
		return this.id_ex_writeAddress.getOutput();
	}

	public boolean getID_EX_Write() {
		return this.id_ex_write.getOutput();
	}

	public ALUOperation getID_EX_ALUOperation() {
		return this.id_ex_ALUOperation.getOutput();
	}

	public boolean getID_EX_MemNotResult() {
		return this.id_ex_memNotRes.getOutput();
	}

	public boolean getID_EX_Type1() {
		return this.id_ex_type1.getOutput();
	}

	public int getEX_WB_Memory() {
		return this.ex_wb_memory.getOutput();
	}

	public int getEX_WB_Result() {
		return this.ex_wb_result.getOutput();
	}

	public int getEX_WB_WriteAddress() {
		return this.ex_wb_writeAddress.getOutput();
	}

	public boolean getEX_WB_Write() {
		return this.ex_wb_write.getOutput();
	}

	public boolean getEX_WB_MemNotRes() {
		return this.ex_wb_memNotRes.getOutput();
	}

	public boolean getEX_WB_Type1() {
		return this.ex_wb_type1.getOutput();
	}

	/// setters
	// main parts
	private void setInstructionFetch(InstructionFetch iff) {
		this.instrFetch = iff;
	}

	private void setInstructionDecode(InstructionDecode id) {
		this.instrDecode = id;
	}

	private void setExecutionUnit(ExecutionUnit ex) {
		this.executionUnit = ex;
	}

	private void setWriteBack(WriteBack wb) {
		this.writeBack = wb;
	}

	// registers

	// clock rise
	public void clockRise() {
		this.getInstructionFetch().clockRise();

		this.if_id_instruction.clockRise();

		this.getInstructionDecode().clockRise();

		this.id_ex_operand1.clockRise();
		this.id_ex_operand2.clockRise();
		this.id_ex_writeAddress.clockRise();
		this.id_ex_write.clockRise();
		this.id_ex_memNotRes.clockRise();
		this.id_ex_ALUOperation.clockRise();
		this.id_ex_type1.clockRise();

		this.getExecutionUnit().clockRise();

		this.ex_wb_memory.clockRise();
		this.ex_wb_result.clockRise();
		this.ex_wb_writeAddress.clockRise();
		this.ex_wb_write.clockRise();
		this.ex_wb_memNotRes.clockRise();
		this.ex_wb_type1.clockRise();

		this.getWriteBack().clockRise();
	}

	// clock fall
	public void clockFall() {
		this.getInstructionFetch().clockFall();

		this.if_id_instruction.clockFall();

		this.getInstructionDecode().clockFall();

		this.id_ex_operand1.clockFall();
		this.id_ex_operand2.clockFall();
		this.id_ex_writeAddress.clockFall();
		this.id_ex_write.clockFall();
		this.id_ex_memNotRes.clockFall();
		this.id_ex_ALUOperation.clockFall();
		this.id_ex_type1.clockFall();

		this.getExecutionUnit().clockFall();

		this.ex_wb_memory.clockFall();
		this.ex_wb_result.clockFall();
		this.ex_wb_writeAddress.clockFall();
		this.ex_wb_write.clockFall();
		this.ex_wb_memNotRes.clockFall();
		this.ex_wb_type1.clockFall();

		this.getWriteBack().clockFall();
	}

	// stabilize
	public void stabilize() {
		this.if_id_instruction.setInput(this.getInstructionFetch().getInstruction());

		this.getInstructionDecode().setInstruction(this.if_id_instruction.getOutput());

		this.id_ex_operand1.setInput(this.getInstructionDecode().getOperand1());
		this.id_ex_operand2.setInput(this.getInstructionDecode().getOperand2());
		this.id_ex_writeAddress.setInput(this.getInstructionDecode().getWriteAddressOut());
		this.id_ex_write.setInput(this.getInstructionDecode().getWriteOut());
		this.id_ex_ALUOperation.setInput(this.getInstructionDecode().getALUOperation());
		this.id_ex_memNotRes.setInput(this.getInstructionDecode().getMemNotRes());
		this.id_ex_type1.setInput(this.getInstructionDecode().getType1Out());

		this.getExecutionUnit().setOperand1(this.id_ex_operand1.getOutput());
		this.getExecutionUnit().setOperand2(this.id_ex_operand2.getOutput());
		this.getExecutionUnit().setALUOperation(this.id_ex_ALUOperation.getOutput());
		this.ex_wb_memory.setInput(this.id_ex_operand1.getOutput());
		this.ex_wb_writeAddress.setInput(this.id_ex_writeAddress.getOutput());
		this.ex_wb_write.setInput(this.id_ex_write.getOutput());
		this.ex_wb_memNotRes.setInput(this.id_ex_memNotRes.getOutput());
		this.ex_wb_type1.setInput(this.id_ex_type1.getOutput());

		this.ex_wb_result.setInput(this.getExecutionUnit().getALUResult());

		this.getWriteBack().setData(this.ex_wb_memory.getOutput());
		this.getWriteBack().setResult(this.ex_wb_result.getOutput());
		this.getWriteBack().setDataNotResult(this.ex_wb_memNotRes.getOutput());
		this.getInstructionDecode().setWriteIn(this.ex_wb_write.getOutput());
		this.getInstructionDecode().setWriteAddressIn(this.ex_wb_writeAddress.getOutput());
		this.getInstructionDecode().setType1In(this.ex_wb_type1.getOutput());

		this.getInstructionDecode().setWriteData(this.getWriteBack().getWriteData());
	}

	// load instruction
	public void loadInstruction(int address, int code) {
		this.getInstructionFetch().setValueInMemory(address, code);

		// update the values in the processor
		this.stabilize();
	}

	// constructors
	public Processor() {
		super();

		this.setInstructionFetch(new InstructionFetch());
		this.setInstructionDecode(new InstructionDecode());
		this.setExecutionUnit(new ExecutionUnit());
		this.setWriteBack(new WriteBack());

		this.if_id_instruction = new Register<Integer>(0);

		this.id_ex_operand1 = new Register<Integer>(0);
		this.id_ex_operand2 = new Register<Integer>(0);
		this.id_ex_writeAddress = new Register<Integer>(0);
		this.id_ex_ALUOperation = new Register<ALUOperation>(ALUOperation.ZERO);
		this.id_ex_write = new Register<Boolean>(false);
		this.id_ex_memNotRes = new Register<Boolean>(false);
		this.id_ex_type1 = new Register<Boolean>(false);

		this.ex_wb_memory = new Register<Integer>(0);
		this.ex_wb_result = new Register<Integer>(0);
		this.ex_wb_writeAddress = new Register<Integer>(0);
		this.ex_wb_write = new Register<Boolean>(false);
		this.ex_wb_memNotRes = new Register<Boolean>(false);
		this.ex_wb_type1 = new Register<Boolean>(false);

		// stabilize
		this.stabilize();
	}
}
