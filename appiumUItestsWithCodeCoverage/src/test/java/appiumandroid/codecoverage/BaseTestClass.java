package appiumandroid.codecoverage;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import io.appium.java_client.android.AndroidDriver;

/**
 * 
 * @author Sridhar Reddy Suram
 *
 * Start Appium server and create session with the connected Android device (Make sure only 1 device is connected - 
 * Didn't add support for multiple devices yet).
 */
public class BaseTestClass {
	String appium_settings = "io.appium.settings";
	String appium_server = "io.appium.uiautomator2.server";
	String appium_test = "io.appium.uiautomator2.server.test";
	String appium_unlock = "io.appium.unlock";
	String uninstallApp = "uninstall %s";
	public AdbUtilities adbUtilities;	

	private static final Logger LOG = LoggerFactory.getLogger(BaseTestClass.class);

	
	@BeforeTest(alwaysRun = true)
	public void beforeTestSetup(final ITestContext context) throws Exception {

		Properties props = new Properties();
		String workingDir = System.getProperty("user.dir");
		props.load(new FileInputStream(workingDir + "/log4j.properties"));
		PropertyConfigurator.configure(props);

		adbUtilities = new AdbUtilities();
		adbUtilities.uninstallAppFromDevice(appium_server);
		adbUtilities.uninstallAppFromDevice(appium_settings);
		adbUtilities.uninstallAppFromDevice(appium_test);
		adbUtilities.uninstallAppFromDevice(appium_unlock);

		AppiumServer appiumServer = new AppiumServer();
		String hostedAppuiumServer = appiumServer.startAppiumServerAndGetURL();
		LOG.info("APPIUM SERVER STARTED LISTING TO: " + hostedAppuiumServer);

		LOG.info("CREATE SESSION WITH THE ABOVE DEVICE");
		AndroidDriver<WebElement> androidDriver = new AndroidDriver<WebElement>(new URL(hostedAppuiumServer), buildCapabilities());
		DriverUtility.androidDriver = androidDriver; //Set driver at gloabl level.
		LocatorUtils.driver = androidDriver;
		
        context.setAttribute("adbUtilities", this.adbUtilities);
	}

	@BeforeClass(alwaysRun = true)
	public void beforeClassBaseTest(final ITestContext context) {
		this.adbUtilities = (AdbUtilities) context.getAttribute("adbUtilities");
	}
	
	public DesiredCapabilities buildCapabilities() {
		LOG.info("DEVICE CAPABILITIES");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("deviceName", adbUtilities.getDeviceManufacturer() + "_" + adbUtilities.getDeviceModel());
		capabilities.setCapability("udid", adbUtilities.getSerialNumber().trim());
		capabilities.setCapability("platformVersion", adbUtilities.getOSVersion());
		capabilities.setCapability("session-override", "true");
		capabilities.setCapability("debug-log-spacing", "true");
		capabilities.setCapability("noSign", "true");
		capabilities.setCapability("newCommandTimeout", "2000000");
		capabilities.setCapability("appPackage", "com.android.settings");
		capabilities.setCapability("appActivity", ".Settings");
		capabilities.setCapability("deviceReadyTimeout", "120000");
		capabilities.setCapability("automationName", "UiAutomator2");
		capabilities.setCapability("uiautomator2ServerInstallTimeout", "1000000");
		return capabilities;
	}
}
