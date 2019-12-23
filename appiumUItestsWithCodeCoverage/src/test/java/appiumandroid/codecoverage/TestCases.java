package appiumandroid.codecoverage;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import appiumandroid.codecoverage.screens.MainScreen;

public class TestCases extends BaseTestClass {
	private static final Logger LOG = LoggerFactory.getLogger(TestCases.class);
	private String appAcitivity = "suram.sridhar/suram.sridhar.JacocoInstrumentation";
	private String broadCaseIntent  = "shell am broadcast -a suram.sridhar.EndEmmaBroadcast";
	private String apkFilePath = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "app-debug.apk";

	MainScreen mainScreen;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClassSetup() throws IOException {
		//Screen Object population
		mainScreen = new MainScreen();
		
		LOG.info("Install App along with permissions enabled:");
		adbUtilities.installApplication(apkFilePath);
		
		LOG.info("Launch Instumented Activity.");
		adbUtilities.launchIntrumentedActivity(appAcitivity);	
		
		LOG.info("Wait for App to launch");
		DriverUtility.delay(DriverUtility.wait_10_sec);
	}
	
	@Test
	public void addTest() {
		LOG.info("Enter Number 1");
		mainScreen.enterNumber1("100");
		
		LOG.info("Enter Number 2");
		mainScreen.enterNumber2("200");
		
		LOG.info("Click on SUM button");
		mainScreen.clickOnSumButton();
		
		LOG.info("Get Result");
		String result = mainScreen.getResult();
		Assert.assertTrue(result.equalsIgnoreCase("300"), "SUM Test case failed");
	}
	
	@Test
	public void subtractionTest() {
		LOG.info("Enter Number 1");
		mainScreen.enterNumber1("300");
		
		LOG.info("Enter Number 2");
		mainScreen.enterNumber2("100");
		
		LOG.info("Click on Subtraction button");
		mainScreen.clickOnsubtractionButton();
		
		LOG.info("Get Result");
		String result = mainScreen.getResult();
		Assert.assertTrue(result.equalsIgnoreCase("300"), "Subtraction Test case failed");
	}
	
	
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		LOG.info("Before closing the app, generate the code coverage so that it will write to /sdcard/Download/coverage.ec");
		LOG.info("Triger broadcast reciever to activate the code coverage code");
		adbUtilities.sendBroadCaseEvent(broadCaseIntent);
		
		LOG.info("WAIT FOR 20 seconds to generate the coverage.ec file on the device");
		DriverUtility.delay(DriverUtility.wait_20_sec);
		
		LOG.info("PULL coverage.ec file from device to local path"); 
		adbUtilities.pullFileFromSDCard("/sdcard/Download/coverage.ec");
	}
}
