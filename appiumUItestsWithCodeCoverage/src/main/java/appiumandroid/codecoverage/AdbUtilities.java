package appiumandroid.codecoverage;

import java.io.File;
import java.io.IOException;

/**
 * @author Sridhar Reddy Suram
 * Through adb Commands we will interact with the connected Android device.
 * 
 */
public class AdbUtilities {

	RunCommands runCommands;
	
	String deviceOS = "shell getprop ro.build.version.release";
	String deviceModel = "shell getprop ro.product.model";
	String deviceManufacturer = "shell getprop ro.product.manufacturer";
	
	public AdbUtilities() {
		runCommands = new RunCommands();
	}

	public void openApplication(String appLauncherActivity) {
		runCommands.runAdbCommand("shell am start -n " + appLauncherActivity);
	}

	public void forceStopApp(String appPackageId) {
		runCommands.runAdbCommand("shell am force-stop " + appPackageId);
	}

	public void installApplication(String apkFilePath) {
		System.out.println("INSTALLING APP - FILE PATH IS: " + apkFilePath);
		runCommands.runAdbCommand("install -g -r " + apkFilePath);
	}

	public void uninstallAppFromDevice(String appPackageId) {
		runCommands.runAdbCommand("uninstall " + appPackageId);
	}

	public void enableRuntimePermission(String permissionName) {
		runCommands.runAdbCommand("shell pm grant " + permissionName);
	}

	public String getOSVersion() {
		return runCommands.runAdbCommand(deviceOS);
	}

	public String getDeviceModel() {
		return runCommands.runAdbCommand(deviceModel);
	}

	public String getDeviceManufacturer() {
		return runCommands.runAdbCommand(deviceManufacturer);
	}

	public void takeScreenShot(String screenShotFolder, String fileName) {
		runCommands.runAdbCommand("shell screencap /sdcard/" +fileName);
		runCommands.runAdbCommand("pull /sdcard/"+fileName+" " + screenShotFolder);	
//		runCommands.runAdbCommand("exec-out screencap -p >>>> \"" + filePathToSave +"\"");
	}
	
	public String getSerialNumber() {
		return runCommands.runAdbCommand("get-serialno");
	}
	
	public void launchIntrumentedActivity(String activityName) throws IOException {
		//suram.sridhar/suram.sridhar.JacocoInstrumentation
		String command = "adb shell am instrument -e coverage true -w %s";
		Runtime.getRuntime().exec(String.format(command, activityName));
	}
	
	public void sendBroadCaseEvent(String command) {
		runCommands.runAdbCommand(command);
	}
	
	/**
	 * Pull file from Android SD Card device to local system. (Project DIR/codeCoverageFiles)
	 * @param filePath
	 */
	public void pullFileFromSDCard(String filePath) {
		String command = "pull %s " + System.getProperty("user.dir") + File.separator + "codeCoverageFiles" + File.separator;
		runCommands.runAdbCommand(String.format(command, filePath));
	}
}
