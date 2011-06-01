import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.apache.commons.cli.*;

public class MiCi {
	public static void main(String[] args) {
		try {
			Options options = options();
			CommandLineParser parser = new PosixParser();
			CommandLine cmd = parser.parse( options, args );
			if (cmd.hasOption("h")) {
				new HelpFormatter().printHelp("java -jar mici.jar [options]", "\n", options, "");
				System.exit(0);
			}
			// default sleep length is 10 minutes
			long sleepTimeInMillis = cmd.hasOption('s') ? Long.parseLong(cmd.getOptionValue('s')) * 1000 : 600000;
			MiCi wka = new MiCi();
			wka.resetMousePosition = cmd.hasOption("r");
			wka.quiet = cmd.hasOption("q") || false == cmd.hasOption("v");
			while(true) {
				wka.moveMouse();
				wka.pressShiftKey();					
				Thread.sleep(sleepTimeInMillis);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}

	private static Options options() {
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt("help").withDescription("print this message").create('h'));
		options.addOption(OptionBuilder.withLongOpt("verbose").withDescription("output mouse movement and keyboard interaction messages").create('v'));
		options.addOption(OptionBuilder.withLongOpt("quiet").withDescription("don't output any messages (default)").create('q'));
		options.addOption(OptionBuilder.withLongOpt("reset-mouse").withDescription("reset mouse position after moving it to 0,0 (default is off)").create('r'));
		options.addOption(OptionBuilder.withArgName("integer")
			.hasArg()
			.withDescription("number of seconds to sleep before moving the mouse again (default is 600)")
			.withLongOpt("sleep-time")
			.create('s'));
		return options;
	}

	private Robot robot;
	boolean clickMouse = false;
	boolean quiet = true;
	boolean resetMousePosition = false;

	public MiCi() throws Exception { this(new Robot()); }

	public MiCi(Robot r) throws Exception { this.robot = r; }

	public void moveMouse() {
		Point currentPosition = MouseInfo.getPointerInfo().getLocation();

		robot.mouseMove(0, 0);
		if (clickMouse) {
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}

		robot.mouseMove((int) currentPosition.getX(), (int) currentPosition.getY());

		if (!quiet) {
			System.out.println("moved mouse: " + new Date());
		}
	}

	public void pressShiftKey() {
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.keyRelease(KeyEvent.VK_SHIFT);
		if (quiet == false) {
			System.out.println("pressed SHIFT key: " + new Date());
		}
	}
}
