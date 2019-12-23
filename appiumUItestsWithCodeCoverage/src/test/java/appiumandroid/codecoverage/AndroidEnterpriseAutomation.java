package appiumandroid.codecoverage;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import appiumandroid.androidenterprise.AEProvisionScreen;

public class AndroidEnterpriseAutomation extends BaseTestClass {
	private static final Logger LOG = LoggerFactory.getLogger(TestCases.class);
	private String launcherActivity = "com.afwsamples.testdpc/.SetupManagementLaunchActivity";
	private String apkFilePath = System.getProperty("user.dir") + File.separator + "resources" + File.separator
			+ "androidEnterprise.apk";
	private String playStoreActivity = "com.android.vending/.AssetBrowserActivity";
	AEProvisionScreen aeProvisionScreen;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClassSetup() throws Exception {
		LOG.info("Delete existing AE Account from the device");
		adbUtilities.deleteWorkProfile();
	
		// Screen Object population
		aeProvisionScreen = new AEProvisionScreen();

		LOG.info("Install App along with permissions enabled:");
		adbUtilities.installApplication(apkFilePath);

		LOG.info("Launch Instumented Activity.");
		adbUtilities.launchActivity(launcherActivity);

		LOG.info("Wait for App to launch");
		DriverUtility.delay(DriverUtility.wait_10_sec);
		
		LOG.info("DO AFW PROVISION");
		aeProvisionScreen.clickOnNext();
		DriverUtility.delay(DriverUtility.wait_10_sec);
		aeProvisionScreen.clickOnSkip();;
		DriverUtility.delay(DriverUtility.wait_10_sec);
		aeProvisionScreen.clickOnFinish();
		
	}
	
	@Test
	public void isAEProivisoned() {
		Assert.assertTrue(adbUtilities.isWorkProfileExists(), "Work profile not exists on the device");
	}

	@Test
	public void launchAEPlaystoreApp() throws Exception {
		LOG.info("Launch AE Vesrion Playstore App");
		adbUtilities.launchAEApp(playStoreActivity);
		DriverUtility.delay(DriverUtility.wait_10_sec);

		Assert.assertTrue(adbUtilities.getFroegroundActivity().contains("com.android.vending"), "AE Version of Playstore App Not opened");	
	}

}
