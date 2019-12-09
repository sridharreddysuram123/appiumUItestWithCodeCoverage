package appiumandroid.codecoverage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sridhar Reddy Suram Use this class to run any type of system
 *         commands.
 *
 */
public class RunCommands {
	
	public static final String ADB_PATH = "/Users/ssuram/Library/Android/sdk/platform-tools/adb %s";
	private static final Logger LOG = LoggerFactory.getLogger(RunCommands.class);

	public void runCommand(String command) {
		String output = "";
		Process pr;
		String line;
		try {
			pr = Runtime.getRuntime().exec(command);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				output = output + " " + line;
			}
			buffer.close();
		} catch (Exception e) {
			System.out.println("Got Exception while executing Runcommands");
			e.printStackTrace();
		}
	}

	public String runAdbCommand(String command) {
		String line = "";
		String output = "";
		Process process;
		String commandToExecute = String.format(ADB_PATH, command);
		LOG.info("Executing ADB Command " + commandToExecute);
		try {
			process = Runtime.getRuntime().exec(commandToExecute);
			process.waitFor(90, TimeUnit.SECONDS); // Max 90 Seconds to execute command

			if (process.isAlive()) {
				process.destroy();
			} else {
				BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while ((line = buffer.readLine()) != null) {
					LOG.info(line);
					output = output + " " + line;
				}
				buffer.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public void screenShot(String filePath) {

		ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "adb.exe", "exec-out", "screencap");
		builder.redirectOutput(new File(filePath));
		builder.redirectError(new File(filePath));
		try {
			Process p = builder.start();
			p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ELKRJLKDJSDs");
		} // throws IOException
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
