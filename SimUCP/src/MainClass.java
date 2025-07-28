import gui.ProgramLoader;
import hardware.Processor;

public class MainClass {

	public static void main(String[] args) {
		ProgramLoader controller = new ProgramLoader(new Processor());
		controller.startLoader();
	}

}
