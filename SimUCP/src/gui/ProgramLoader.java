package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import hardware.Processor;
import utility.Coder;

public class ProgramLoader {
	// main
	public static void main(String[] args) {
		ProgramLoader controller = new ProgramLoader(new Processor());
		controller.startLoader();
	}

	// GUI part
	JTextArea textArea = new JTextArea(30, 30);
	// JLabel inputText = new JLabel("Input");
	JTextField inputField = new JTextField("");
	JButton loadButton = new JButton("Load");
	JButton simulateButton = new JButton("Simulate");
	JButton nextButton = new JButton("Clock");

	Simulation view;

	Simulation getView() {
		return this.view;
	}

	void setView(Simulation sim) {
		this.view = sim;
	}

	// program part
	private List<String> list;

	List<String> getList() {
		return this.list;
	}

	void setList(List<String> l) {
		this.list = l;
	}

	// component part
	private Processor processor;
	private int index;

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

	// load instruction
	boolean loadInstruction(String instr) {
		// process the instruction String
		String valid = Coder.validate(instr);
		String actual = Coder.convert(valid);
		int code = Coder.encode(actual);

		// stop if the code is incorrect
		if (code == -1) {
			return false;
		}

		// add valid instruction String to text list
		this.getList().add(valid);
		// add instruction code to processor
		this.getProcessor().loadInstruction(index, code);
		index++;

		return true;
	}

	static private String stringList(List<String> list, char ch) {
		String result = "";
		for (String instr : list) {
			result += instr + ch;
		}
		return result;
	}

	public void startLoader() {
		JFrame frame = new JFrame("Program Loader");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(350, 650);
		frame.setResizable(false);

		JPanel container = new JPanel();
		JPanel panel = new JPanel();
		JScrollPane scroll = new JScrollPane(this.textArea);
		JPanel sim = new JPanel();

		// panel.add(this.inputText);
		panel.add(this.inputField);
		panel.add(this.loadButton);

		sim.add(this.simulateButton);
		sim.add(this.nextButton);

		container.add(panel);
		container.add(scroll);
		container.add(sim);
		container.setLayout(new FlowLayout(FlowLayout.CENTER));

		frame.add(container);
		frame.setVisible(true);
	}

	// constructors
	public ProgramLoader(Processor pro) {
		Dimension dim = new Dimension(100, 30);
		// component part
		this.setProcessor(pro);
		index = 0;

		// program part
		this.setList(new ArrayList<String>());

		// GUI part
		this.setView(new Simulation(this.getProcessor()));

		this.inputField.setPreferredSize(new Dimension(150, 30));

		this.loadButton.setPreferredSize(dim);
		this.loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ProgramLoader.this.loadInstruction(ProgramLoader.this.inputField.getText());
				ProgramLoader.this.inputField.setText(null);
				ProgramLoader.this.textArea.setText(ProgramLoader.stringList(ProgramLoader.this.getList(), '\n'));
			}
		});

		this.simulateButton.setPreferredSize(dim);
		this.simulateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// close current simulation if there is one, and start a new simulation
				ProgramLoader.this.view.stopSimulation();
				ProgramLoader.this.view.startSimulation();
			}
		});

		this.nextButton.setPreferredSize(dim);
		this.nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// if there is a simulation then execute the next clock cycle
				// (execute the update method for the processor)
				if (ProgramLoader.this.view.isSimulation()) {
					ProgramLoader.this.getProcessor().update();
					ProgramLoader.this.view.repaint();

					// write
					try {
						FileWriter wr = new FileWriter("src\\log.txt");
						wr.write("The registers:\n");
						for (int i = 0; i < Coder.REGISTERS.length; i++) {
							int v = ProgramLoader.this.getProcessor().getInstructionDecode().getRegister(i);
							if (v != 0) {
								wr.write("" + Coder.REGISTERS[i] + " = " + v + "\n");
							}
						}
						wr.write("\nThe memory:\n");
						for (int i = 0; i < 1024; i++) {
							int v = ProgramLoader.this.getProcessor().getInstructionDecode().getFromMemory(i);
							if (v != 0) {
								wr.write("MEM[" + i + "] = " + v + "\n");
							}
						}
						wr.write("\n");
						wr.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
//					System.out.println("The registers:");
//					for (int i = 0; i < 7; i++) {
//						int v = ProgramLoader.this.getProcessor().getInstructionDecode().getRegister(i);
//						if (v != 0) {
//							System.out.println("" + Coder.REGISTERS[i] + " = " + v);
//						}
//					}
//					System.out.println();
//					System.out.println("The memory:");
//					for (int i = 0; i < 1024; i++) {
//						int v = ProgramLoader.this.getProcessor().getInstructionDecode().getFromMemory(i);
//						if (v != 0) {
//							System.out.println("MEM[" + i + "] = " + v);
//						}
//					}
//					System.out.println("\n");
				}
			}
		});
	}
}
