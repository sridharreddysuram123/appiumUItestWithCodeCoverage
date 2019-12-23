package appiumandroid.codecoverage;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sridhar Reddy Suram Through adb Commands we will interact with the
 *         connected Android device.
 * 
 */
public class AdbUtilities {
	private static final Logger LOG = LoggerFactory.getLogger(AdbUtilities.class);

	RunCommands runCommands;
	String deviceOS = "shell getprop ro.build.version.release";
	String deviceModel = "shell getprop ro.product.model";
	String deviceManufacturer = "shell getprop ro.product.manufacturer";
	String adbPath = "/Users/ssuram/Library/Android/sdk/platform-tools/adb ";

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
		runCommands.runAdbCommand("shell screencap /sdcard/" + fileName);
		runCommands.runAdbCommand("pull /sdcard/" + fileName + " " + screenShotFolder);
		// runCommands.runAdbCommand("exec-out screencap -p >>>> \"" + filePathToSave
		// +"\"");
	}

	public String getSerialNumber() {
		return runCommands.runAdbCommand("get-serialno");
	}

	public void launchIntrumentedActivity(String activityName) throws IOException {
		// suram.sridhar/suram.sridhar.JacocoInstrumentation
		String command = " shell am instrument -e coverage true -w %s";
		runCommands.triggerCommand(String.format(command, activityName));
		// Runtime.getRuntime().exec(String.format(command, activityName));
	}

	public void sendBroadCaseEvent(String command) {
		runCommands.runAdbCommand(command);
	}

	/**
	 * Pull file from Android SD Card device to local system. (Project
	 * DIR/codeCoverageFiles)
	 * 
	 * @param filePath
	 */
	public void pullFileFromSDCard(String filePath) {
		String command = "pull %s " + System.getProperty("user.dir") + File.separator + "codeCoverageFiles"
				+ File.separator;
		runCommands.runAdbCommand(String.format(command, filePath));
	}

	/**
	 * Get Android Enterprise User account ID as string
	 * 
	 * @return Work profile/ AE User id.
	 * @throws Exception
	 *             If AE account not found.
	 */
	public String getWorkProfileAccountID() throws Exception {
		String users = runCommands.runAdbCommand("shell pm list users").toLowerCase();
		LOG.info("users: " +users);
		String usersArray[] = users.split("userinfo");
		String userId = null;
		for (String user : usersArray) {
			LOG.info("User:: " + user);
			if (user.contains("work profile")) {
				userId = user.substring(1, user.indexOf("work profile") - 1);
				LOG.info("Work profile account found and the User id: " + userId);
				return userId;
			}
			if (user.contains("workspace")) {
				userId = user.substring(1, user.indexOf("workspace") - 1);
				LOG.info("Work profile account found and the User id: " + userId);
				return userId;
			}
			
			if (user.contains("profile")) {
				userId = user.substring(1, 3);
				LOG.info("Work profile account found and the User id: " + userId);
				return userId;
			}
		}
		throw new Exception("AE Account not exists on the device");
	}

	/**
	 * Remove App from Work profile / AE User.
	 * 
	 * @param appPackageId
	 *            to remove from the AE Space.
	 * @throws Exception
	 *             if
	 */
	public void removeAppFromWorkPofileAccount(String appPackageId) throws Exception {
		String command = " shell pm uninstall --user " + getWorkProfileAccountID() + "  " + appPackageId;
		runCommands.runAdbCommand(command);
	}

	/**
	 * Install the given app into the workprofile/AE user.
	 * 
	 * @param apkFilePath
	 *            App apk file path.
	 * @throws Exception
	 */
	public void installAppInsideWorkProfile(String apkFilePath) throws Exception {
		String command = "install -r --user " + getWorkProfileAccountID() + "  " + apkFilePath;
		runCommands.runAdbCommand(command);
	}

	/**
	 * Method to validate if the Device is already having a AE user or not.
	 * 
	 * @return True if the AE Account exists on the device.
	 */
	public boolean isWorkProfileExists() {
		String activeUsers = runCommands.runAdbCommand(" shell pm list users").toLowerCase();
		if (activeUsers.contains("profile") || activeUsers.contains("workspace")) {
			LOG.info("Work profile - Android Enterprise account exists on the device");
			return true;
		} else {
			LOG.info("Work profile - Android Enterprise account not found on the device");
			return false;
		}
	}

	/**
	 * Delete existing work profile account from device.
	 * 
	 * @throws Exception
	 */
	public void deleteWorkProfile() throws Exception {
		try {
			if (isWorkProfileExists()) {
				LOG.info("Delete work profile");
				runCommands.runAdbCommand("shell pm remove-user " + getWorkProfileAccountID());
			}
		} catch (Exception e) {
			LOG.debug("AE Account not existis on the device.");
		}
	}

	/**
	 * Launch app by its given activity
	 * 
	 * @param activityName
	 * @throws IOException
	 */
	public void launchActivity(String activity) throws IOException {
		String command = " shell am start -n %s --activity-clear-top";
		runCommands.triggerCommand(String.format(command, activity));
	}

	/**
	 * Launch Android Enterprise App by using given activity name.
	 * 
	 * @param activityName
	 * @throws IOException
	 */
	public void launchAEApp(String activity) throws Exception {
		String command = " shell am start --user " + getWorkProfileAccountID() + " -n %s --activity-clear-top";
		runCommands.triggerCommand(String.format(command, activity));
	}

	/**
	 * Get Android OS API Version
	 * 
	 * @return
	 */
	public int getOSApiVersion() {
		return Integer.parseInt(runCommands.runAdbCommand(" shell getprop ro.build.version.sdk").trim());
	}

	/**
	 * Get Current displayed screen activity name.
	 * 
	 * @return Foreground activity Name.
	 */
	public String getFroegroundActivity() {
		if (getOSApiVersion() > 28) {
			return runCommands.runAdbCommand(" shell dumpsys window displays | grep mCurrentFocus").trim();
		} else {
			return runCommands.runAdbCommand(" shell dumpsys window windows | grep mCurrentFocus").trim();
		}
	}

}
