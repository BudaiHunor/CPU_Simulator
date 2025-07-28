package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import hardware.Processor;
import utility.Coder;

public class Simulation extends JPanel {
	private JFrame frame;

	private Processor processor;

	Processor getProcessor() {
		return this.processor;
	}

	boolean setProcessor(Processor pro) {
		if (pro == null) {
			return false;
		}
		this.processor = pro;
		return true;
	}

	boolean isSimulation() {
		return this.frame != null;
	}

	void startSimulation() {
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.frame.setSize(1200, 650);
		this.frame.setResizable(false);

		this.frame.add(this);
		this.frame.setVisible(true);
	}

	void stopSimulation() {
		if (this.frame != null && this.isSimulation()) {
			this.frame.setVisible(false);
			this.frame.dispose();
		}
	}

	// drawing methods
	private void drawClockSign(Graphics g, int x, int y, int size) {
		int[] xs = { x, x, x + size };
		int[] ys = { y, y + size, y + size / 2 };
		g.drawPolygon(xs, ys, xs.length);
	}

	private void drawMultiplexer(Graphics g, int sel, String name, int x, int y, int height, int width, float cut) {
		float val = (int) (Math.min(1, Math.max(0, cut)) * height / 2);

		int[] xs = { x, x + width, x + width, x };
		int[] ys = { y, (int) (y + val), (int) (y + height - val), y + height };
		g.drawPolygon(xs, ys, xs.length);

		int nr = (int) Math.round(Math.pow(2, sel));
		for (int i = 0; i < nr; i++) {
			g.drawString(Integer.toString(i), x, y + (i + 1) * height / (nr + 1) + 4);
		}

		g.drawString(name, x + width / 2 - 7 * name.length() / 2, y + height / 2 + 4);
	}

	private void drawMultiplexer(Graphics g, int sel, String name, int x, int y, int height, int width) {
		this.drawMultiplexer(g, sel, name, x, y, height, width, 0.5f);
	}

	private void drawDemultiplexer(Graphics g, int sel, String name, int x, int y, int height, int width, float cut) {
		float val = (int) (Math.min(1, Math.max(0, cut)) * height / 2);

		int[] xs = { x, x + width, x + width, x };
		int[] ys = { (int) (y + val), y, y + height, (int) (y + height - val) };
		g.drawPolygon(xs, ys, xs.length);

		int nr = (int) Math.round(Math.pow(2, sel));
		for (int i = 0; i < nr; i++) {
			String selId = Integer.toString(i);
			g.drawString(selId, x + width - 7 * selId.length(), y + (i + 1) * height / (nr + 1) + 4);
		}

		g.drawString(name, x + width / 2 - 8 * name.length() / 2, y + height / 2 + 4);
	}

	private void drawDemultiplexer(Graphics g, int sel, String name, int x, int y, int height, int width) {
		this.drawDemultiplexer(g, sel, name, x, y, height, width, 0.5f);
	}

	private void drawRegister(Graphics g, boolean hasEn, String name, int x, int y, int height, int width) {
		int[] xs = { x, x + width, x + width, x };
		int[] ys = { y, y, y + height, y + height };
		g.drawPolygon(xs, ys, xs.length);

		this.drawClockSign(g, x, y + height - 10, 10);

		if (hasEn) {
			g.drawString("en", x, y + height * 2 / 3 + 4);
			g.drawString("in", x, y + height / 3 + 4);
			g.drawString("out", x + width - 18, y + height / 3 + 4);
		} else {
			g.drawString("in", x, y + height / 2 + 4);
			g.drawString("out", x + width - 18, y + height / 2 + 4);
		}

		g.drawString(name, x + width / 2 - 7 * name.length() / 2, y + height / 2 + 4);
	}

	private void drawOperator(Graphics g, String name, int x, int y, int height, int width, float cut) {
		float val = (int) (Math.min(1, Math.max(0, cut)) * height / 2);
		float v = val / 2;

		int[] xs = { x, x + width, x + width, x, x, (int) (x + v), x, };
		int[] ys = { y, (int) (y + val), (int) (y + height - val), y + height, (int) (y + height / 2 + v / 2),
				y + height / 2, (int) (y + height / 2 - v / 2) };
		g.drawPolygon(xs, ys, xs.length);

		g.drawString(name, x + width / 2 - 7 * name.length() / 2, y + height / 2 + 4);
	}

	private void drawOperator(Graphics g, String name, int x, int y, int height, int width) {
		this.drawOperator(g, name, x, y, height, width, 0.5f);
	}

	private void drawMemory(Graphics g, int nrAddr, int nrRead, int nrWrite, String name, int x, int y, int height,
			int width) {
		int[] xs = { x, x + width, x + width, x };
		int[] ys = { y, y, y + height, y + height };
		g.drawPolygon(xs, ys, xs.length);

		if (nrWrite > 0) {
			this.drawClockSign(g, x, y + height - 10, 10);
		}

		int space = 1, nr = nrAddr + space + nrWrite + 1;
		for (int i = 1; i <= nrRead; i++) {
			String addr = "Addr" + ((nrRead == 1) ? "" : i);
			String data = "RdData" + ((nrRead == 1) ? "" : i);
			g.drawString(addr, x, y + i * (height / nr) + 4);
			g.drawString(data, x + width - 7 * data.length(), y + i * (height / nr) + 4);
		}
		for (int i = 1; i <= nrAddr - nrRead; i++) {
			g.drawString("WrAddr" + ((nrAddr - nrRead == 1) ? "" : i), x, y + (nrRead + space + i) * height / nr);
		}
		for (int i = 1; i <= nrWrite; i++) {
			String data = "WrData" + ((nrWrite == 1) ? "" : i);
			String wr = "Wr" + ((nrWrite == 1) ? "" : i);
			g.drawString(data, x, y + (nrAddr + space + i) * (height / nr));
			g.drawString(wr, x + i * width / (nrWrite + 1) - 10 * wr.length() / 2, y + height);
		}

		g.drawString(name, x + width / 2 - 7 * name.length() / 2, y + (nrRead + 1) * (height / nr) + 4);
	}

	// paintComponent override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// actual code
		int[] offsets = { 0, 250, 750, 1000 };

		this.setBackground(Color.LIGHT_GRAY);
		int nrWalls = 4, h = 500, w = 30;
		// register walls
		for (int i = 1; i < nrWalls; i++) {
			this.drawRegister(g, false, "", offsets[i] - w / 2, 50, h, w);
		}

		// drawing
		// IF
		this.drawRegister(g, false, "PC", offsets[0] + 50, 250, 30, 60);
		this.drawMemory(g, 1, 1, 0, "Memory", offsets[0] + 130, 225, 120, 80);
		this.drawOperator(g, "+", offsets[0] + 130, 150, 50, 30);

		g.drawLine(offsets[0] + 110, 265, offsets[0] + 130, 265);
		g.drawLine(offsets[0] + 120, 190, offsets[0] + 120, 265);
		g.drawLine(offsets[0] + 120, 190, offsets[0] + 130, 190);
		g.fillOval(offsets[0] + 120 - 2, 265 - 2, 4, 4);

		g.drawLine(offsets[0] + 120, 160, offsets[0] + 130, 160);
		g.drawString("1", offsets[0] + 110, 164);

		// pc+1 back to pc
		g.drawLine(offsets[0] + 160, 175, offsets[0] + 170, 175);
		g.drawLine(offsets[0] + 170, 175, offsets[0] + 170, 140);
		g.drawLine(offsets[0] + 170, 140, offsets[0] + 35, 140);
		g.drawLine(offsets[0] + 35, 140, offsets[0] + 35, 265);
		g.drawLine(offsets[0] + 35, 265, offsets[0] + 50, 265);

		g.drawLine(offsets[0] + 210, 265, offsets[1] - w / 2, 265);

		// ID
		this.drawMemory(g, 3, 2, 1, "Registers", offsets[1] + 100, 250, 150, 90);
		this.drawMemory(g, 3, 2, 1, "Memory", offsets[1] + 250, 250, 150, 90);
		this.drawMultiplexer(g, 1, "MUX", offsets[1] + 400, 50, 60, 40);
		this.drawMultiplexer(g, 1, "MUX", offsets[1] + 400, 125, 60, 40);
		this.drawMultiplexer(g, 2, "MUX", offsets[1] + 400, 200, 100, 40);

		g.drawLine(offsets[1] + w / 2, 265, offsets[1] + 75, 265);
		g.fillOval(offsets[1] + 75 - 2, 265 - 2, 4, 4);
		g.drawLine(offsets[1] + 75, 40, offsets[1] + 75, 470);

		// writes (bottom)
		this.drawDemultiplexer(g, 1, "DEM", offsets[1] + 35, 493, 60, 40);

		g.drawLine(offsets[1] + 75, 513, offsets[1] + 145, 513);
		g.drawLine(offsets[1] + 145, 513, offsets[1] + 145, 400);
		g.drawLine(offsets[1] + 75, 533, offsets[1] + 295, 533);
		g.drawLine(offsets[1] + 295, 533, offsets[1] + 295, 400);

		// address (top)
		g.fillOval(offsets[1] + 75 - 2, 275 - 2, 4, 4);
		g.fillOval(offsets[1] + 75 - 2, 300 - 2, 4, 4);
		g.drawLine(offsets[1] + 75, 275, offsets[1] + 100, 275);
		g.drawLine(offsets[1] + 75, 300, offsets[1] + 100, 300);

		g.drawLine(offsets[1] + 190, 275, offsets[1] + 250, 275);
		g.drawLine(offsets[1] + 190, 300, offsets[1] + 250, 300);

		g.drawLine(offsets[1] + 75, 40, offsets[1] + 420, 40);
		g.drawLine(offsets[1] + 420, 40, offsets[1] + 420, 57);
		g.fillOval(offsets[1] + 420 - 2, 40 - 2, 4, 4);
		g.drawLine(offsets[1] + 420, 40, offsets[1] + 460, 40);
		g.drawLine(offsets[1] + 460, 40, offsets[1] + 460, 60);
		g.drawLine(offsets[1] + 460, 60, offsets[2] - w / 2, 60);
		g.fillOval(offsets[1] + 75 - 2, 70 - 2, 4, 4);
		g.drawLine(offsets[1] + 75, 70, offsets[1] + 400, 70);
		g.fillOval(offsets[1] + 220 - 2, 275 - 2, 4, 4);
		g.drawLine(offsets[1] + 220, 275, offsets[1] + 220, 90);
		g.drawLine(offsets[1] + 220, 90, offsets[1] + 400, 90);
		g.drawLine(offsets[1] + 440, 80, offsets[2] - w / 2, 80);

		g.fillOval(offsets[1] + 75 - 2, 120 - 2, 4, 4);
		g.drawLine(offsets[1] + 75, 120, offsets[1] + 420, 120);
		g.drawLine(offsets[1] + 420, 120, offsets[1] + 420, 132);
		g.fillOval(offsets[1] + 220 - 2, 145 - 2, 4, 4);
		g.drawLine(offsets[1] + 220, 145, offsets[1] + 400, 145);
		g.drawLine(offsets[1] + 340, 270, offsets[1] + 350, 270);
		g.drawLine(offsets[1] + 350, 270, offsets[1] + 350, 165);
		g.drawLine(offsets[1] + 350, 165, offsets[1] + 400, 165);
		g.drawLine(offsets[1] + 440, 155, offsets[2] - w / 2, 155);

		g.fillOval(offsets[1] + 75 - 2, 195 - 2, 4, 4);
		g.drawLine(offsets[1] + 75, 195, offsets[1] + 420, 195);
		g.drawLine(offsets[1] + 420, 195, offsets[1] + 420, 212);
		g.fillOval(offsets[1] + 235 - 2, 300 - 2, 4, 4);
		g.drawLine(offsets[1] + 235, 300, offsets[1] + 235, 220);
		g.drawLine(offsets[1] + 235, 220, offsets[1] + 400, 220);
		g.drawLine(offsets[1] + 390, 240, offsets[1] + 400, 240);
		g.drawString("0", offsets[1] + 380, 244);
		g.drawLine(offsets[1] + 340, 300, offsets[1] + 365, 300);
		g.drawLine(offsets[1] + 365, 300, offsets[1] + 365, 260);
		g.drawLine(offsets[1] + 365, 260, offsets[1] + 400, 260);
		g.fillOval(offsets[1] + 75 - 2, 420 - 2, 4, 4);
		g.drawLine(offsets[1] + 75, 420, offsets[1] + 340, 420);
		this.drawMemory(g, 0, 0, 0, "Ext", offsets[1] + 340, 410, 20, 30);
		g.drawLine(offsets[1] + 370, 420, offsets[1] + 380, 420);
		g.drawLine(offsets[1] + 380, 420, offsets[1] + 380, 280);
		g.drawLine(offsets[1] + 380, 280, offsets[1] + 400, 280);
		g.drawLine(offsets[1] + 440, 250, offsets[2] - w / 2, 250);

		// control unit
		this.drawMemory(g, 0, 0, 0, "CU", offsets[1] + 310, 450, 100, 40);
		g.drawLine(offsets[1] + 75, 470, offsets[1] + 310, 470);
		g.drawLine(offsets[1] + 350, 470, offsets[1] + 355, 470);
		g.drawLine(offsets[1] + 355, 470, offsets[1] + 355, 430);
		g.drawLine(offsets[1] + 350, 490, offsets[2] - w / 2, 490);
		g.drawLine(offsets[1] + 350, 510, offsets[2] - w / 2, 510);
		g.drawLine(offsets[1] + 350, 530, offsets[2] - w / 2, 530);

		// EX
		this.drawOperator(g, "ALU", offsets[2] + 75, 200, 200, 120);
		g.drawLine(offsets[2] + w / 2, 155, offsets[2] + 55, 155);
		g.drawLine(offsets[2] + 55, 155, offsets[2] + 55, 250);
		g.drawLine(offsets[2] + 55, 250, offsets[2] + 75, 250);
		g.fillOval(offsets[2] + 55 - 2, 155 - 2, 4, 4);
		g.drawLine(offsets[2] + 55, 155, offsets[3] - w / 2, 155);

		g.drawLine(offsets[2] + w / 2, 250, offsets[2] + 35, 250);
		g.drawLine(offsets[2] + 35, 250, offsets[2] + 35, 350);
		g.drawLine(offsets[2] + 35, 350, offsets[2] + 75, 350);

		g.drawLine(offsets[2] + w / 2, 490, offsets[2] + 135, 490);
		g.drawLine(offsets[2] + 135, 490, offsets[2] + 135, 375);

		g.drawLine(offsets[2] + 195, 300, offsets[3] - w / 2, 300);

		g.drawLine(offsets[2] + w / 2, 60, offsets[3] - w / 2, 60);
		g.drawLine(offsets[2] + w / 2, 80, offsets[3] - w / 2, 80);
		g.drawLine(offsets[2] + w / 2, 510, offsets[3] - w / 2, 510);
		g.drawLine(offsets[2] + w / 2, 530, offsets[3] - w / 2, 530);

		// WB
		this.drawMultiplexer(g, 1, "MUX", offsets[3] + 50, 250, 100, 60);
		g.drawLine(offsets[3] + w / 2, 300, offsets[3] + 25, 300);
		g.drawLine(offsets[3] + 25, 300, offsets[3] + 25, 283);
		g.drawLine(offsets[3] + 25, 283, offsets[3] + 50, 283);

		g.drawLine(offsets[3] + w / 2, 155, offsets[3] + 35, 155);
		g.drawLine(offsets[3] + 35, 155, offsets[3] + 35, 317);
		g.drawLine(offsets[3] + 35, 317, offsets[3] + 50, 317);
		g.drawLine(offsets[3] + 110, 300, offsets[3] + 130, 300);
		g.drawLine(offsets[3] + 130, 300, offsets[3] + 130, 570);
		g.drawLine(offsets[3] + 130, 570, offsets[1] + 90, 570);
		g.drawLine(offsets[1] + 90, 570, offsets[1] + 90, 370);
		g.drawLine(offsets[1] + 90, 370, offsets[1] + 100, 370);
		g.fillOval(offsets[1] + 235 - 2, 570 - 2, 4, 4);
		g.drawLine(offsets[1] + 235, 570, offsets[1] + 235, 370);
		g.drawLine(offsets[1] + 235, 370, offsets[1] + 250, 370);

		g.drawLine(offsets[3] + w / 2, 510, offsets[3] + 80, 510);
		g.drawLine(offsets[3] + 80, 510, offsets[3] + 80, 337);

		g.drawLine(offsets[3] + w / 2, 530, offsets[3] + 30, 530);
		g.drawLine(offsets[3] + 30, 530, offsets[3] + 30, 560);
		g.drawLine(offsets[3] + 30, 560, offsets[1] + 25, 560);
		g.drawLine(offsets[1] + 25, 560, offsets[1] + 25, 523);
		g.drawLine(offsets[1] + 25, 523, offsets[1] + 35, 523);
		g.drawLine(offsets[3] + w / 2, 60, offsets[3] + 30, 60);
		g.drawLine(offsets[3] + 30, 60, offsets[3] + 30, 25);
		g.drawLine(offsets[3] + 30, 25, offsets[1] + 55, 25);
		g.drawLine(offsets[1] + 55, 25, offsets[1] + 55, 500);

		g.drawLine(offsets[3] + w / 2, 80, offsets[3] + 140, 80);
		g.drawLine(offsets[3] + 140, 80, offsets[3] + 140, 580);
		g.drawLine(offsets[3] + 140, 580, offsets[1] + 85, 580);
		g.drawLine(offsets[1] + 85, 580, offsets[1] + 85, 345);
		g.drawLine(offsets[1] + 85, 345, offsets[1] + 100, 345);
		g.fillOval(offsets[1] + 220 - 2, 580 - 2, 4, 4);
		g.drawLine(offsets[1] + 220, 580, offsets[1] + 220, 345);
		g.drawLine(offsets[1] + 220, 345, offsets[1] + 250, 345);

		// TODO values
		// IF
		g.drawString(Integer.toString(this.getProcessor().getInstructionFetch().getProgramCounter()), offsets[0] + 100,
				235);
		g.drawString(Integer.toString(this.getProcessor().getInstructionFetch().getProgramCounterNext()),
				offsets[0] + 40, 235);
		g.drawString(Integer.toHexString(this.getProcessor().getInstructionFetch().getInstruction()), offsets[0] + 180,
				220);

		// IF/ID
		Color c = g.getColor();
		g.setColor(this.getBackground());
		g.fillRect(offsets[1] - 14, 295, 28, 12);
		g.setColor(c);
		g.drawString("Instr", offsets[1] - 12, 270);

		// ID
		g.drawString(Integer.toHexString(this.getProcessor().getIF_ID_Instruction()), offsets[1] + 18, 260);
		g.drawString(Coder.REGISTERS[this.getProcessor().getInstructionDecode().getAddress1() % Coder.REGISTERS.length]
				.toUpperCase(), offsets[1] + 77, 270);
		g.drawString(Coder.REGISTERS[this.getProcessor().getInstructionDecode().getAddress2() % Coder.REGISTERS.length]
				.toUpperCase(), offsets[1] + 77, 295);
		g.drawString("" + this.getProcessor().getInstructionDecode().getRegister1(), offsets[1] + 190, 270);
		g.drawString("" + this.getProcessor().getInstructionDecode().getRegister2(), offsets[1] + 190, 295);
		g.drawString("" + this.getProcessor().getInstructionDecode().getMemory1(), offsets[1] + 310, 245);
		g.drawString("" + this.getProcessor().getInstructionDecode().getMemory2(), offsets[1] + 340, 310);
		g.drawString("" + this.getProcessor().getInstructionDecode().getExtImmediate(), offsets[1] + 380, 420);
		g.drawString(Integer.toString(this.getProcessor().getInstructionDecode().getOperand1()), offsets[1] + 440, 150);
		g.drawString("" + (this.getProcessor().getInstructionDecode().getType1() ? 1 : 0), offsets[1] + 425, 55);
		g.drawString(Integer.toString(this.getProcessor().getInstructionDecode().getOperand2()), offsets[1] + 440, 245);
		g.drawString("" + (this.getProcessor().getInstructionDecode().getType1() ? 1 : 0), offsets[1] + 425, 130);
		g.drawString(Integer.toString(this.getProcessor().getInstructionDecode().getWriteAddressOut()),
				offsets[1] + 440, 75);
		g.drawString("" + (this.getProcessor().getInstructionDecode().getType3() ? 1 : 0), offsets[1] + 425, 210);
		g.drawString("" + (this.getProcessor().getInstructionDecode().getType2() ? 1 : 0), offsets[1] + 431, 210);
		g.drawString("OPCode", offsets[1] + 240, 470);
		g.drawString("" + this.getProcessor().getInstructionDecode().getOPCode(), offsets[1] + 310, 470);
		g.drawString("extSign=" + (this.getProcessor().getInstructionDecode().getExtSign() ? 1 : 0), offsets[1] + 355,
				470);
		g.drawString("ALUop=" + this.getProcessor().getInstructionDecode().getALUOperation(), offsets[1] + 355, 490);
		g.drawString("memNotRes=" + (this.getProcessor().getInstructionDecode().getMemNotRes() ? 1 : 0),
				offsets[1] + 355, 510);
		g.drawString("write=" + (this.getProcessor().getInstructionDecode().getWriteOut() ? 1 : 0), offsets[1] + 355,
				530);
		g.drawString("" + (this.getProcessor().getInstructionDecode().getRegWrite() ? 1 : 0), offsets[1] + 150, 412);
		g.drawString("" + (this.getProcessor().getInstructionDecode().getMemWrite() ? 1 : 0), offsets[1] + 300, 412);

		// ID/EX
		c = g.getColor();
		g.setColor(this.getBackground());
		g.fillRect(offsets[2] - 14, 295, 28, 12);
		g.setColor(c);
		g.drawString("OP1", offsets[2] - 12, 160);
		g.drawString("OP2", offsets[2] - 12, 255);
		g.drawString("WA", offsets[2] - 10, 85);
		g.drawString("type1", offsets[2] - 14, 65);
		g.drawString("OP", offsets[2] - 10, 495);
		g.drawString("MNR", offsets[2] - 14, 515);
		g.drawString("WR", offsets[2] - 10, 535);

		// EX
		// g.drawString("" + this.getProcessor().getExecutionUnit().getOperand1(), 825,
		// 250);
		// g.drawString("" + this.getProcessor().getExecutionUnit().getOperand2(), 825,
		// 350);
		g.drawString("" + this.getProcessor().getID_EX_Operand1(), offsets[2] + 20, 150);
		g.drawString("" + this.getProcessor().getID_EX_Operand2(), offsets[2] + 20, 245);
		g.drawString("" + this.getProcessor().getID_EX_WriteAddress(), offsets[2] + 20, 75);
		g.drawString("" + (this.getProcessor().getID_EX_Type1() ? 1 : 0), offsets[2] + 20, 55);
		g.drawString("" + this.getProcessor().getID_EX_ALUOperation(), offsets[2] + 20, 490);
		g.drawString("" + (this.getProcessor().getID_EX_MemNotResult() ? 1 : 0), offsets[2] + 20, 510);
		g.drawString("" + (this.getProcessor().getID_EX_Write() ? 1 : 0), offsets[2] + 20, 530);
		g.drawString("" + this.getProcessor().getExecutionUnit().getALUResult(), offsets[2] + 200, 295);

		// EX/WB
		c = g.getColor();
		g.setColor(this.getBackground());
		g.fillRect(offsets[3] - 14, 295, 28, 12);
		g.setColor(c);
		g.drawString("RES", offsets[3] - 12, 305);
		g.drawString("MEM", offsets[3] - 14, 160);
		g.drawString("WA", offsets[3] - 10, 85);
		g.drawString("type1", offsets[3] - 14, 65);
		g.drawString("MNR", offsets[3] - 14, 515);
		g.drawString("WR", offsets[3] - 10, 535);

		// WB
		g.drawString("" + this.getProcessor().getEX_WB_Result(), offsets[3] + 16, 280);
		g.drawString("" + this.getProcessor().getEX_WB_Memory(), offsets[3] + 16, 150);
		g.drawString("" + this.getProcessor().getWriteBack().getWriteData(), offsets[3] + 110, 295);
		g.drawString("" + this.getProcessor().getEX_WB_WriteAddress(), offsets[3] + 18, 75);
		g.drawString("" + (this.getProcessor().getEX_WB_Type1() ? 1 : 0), offsets[3] + 35, 55);
		g.drawString("" + (this.getProcessor().getEX_WB_MemNotRes() ? 1 : 0), offsets[3] + 18, 510);
		g.drawString("" + (this.getProcessor().getEX_WB_Write() ? 1 : 0), offsets[3] + 18, 530);
	}

	// constructors
	Simulation(Processor pro) {
		super();

		this.setProcessor(pro);

		this.frame = new JFrame("Simulation");

		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Coordonates x=" + e.getX() + ", y=" + e.getY() + "");
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}
}
