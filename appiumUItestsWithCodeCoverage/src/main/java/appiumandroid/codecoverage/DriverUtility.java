package appiumandroid.codecoverage;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.android.AndroidDriver;

public class DriverUtility {
	private static final Logger LOG = LoggerFactory.getLogger(DriverUtility.class);

	public static AndroidDriver<WebElement> androidDriver;
	
	//Wait times:
	public static int wait_7_sec = 7;
	public static int wait_10_sec = 10;
	public static int wait_15_sec = 15;
	public static int wait_20_sec = 20;
	public static int wait_30_sec = 30;
	public static int wait_40_sec = 40;
	public static int wait_50_sec = 50;
	public static int wait_60_sec = 60;
	public static int wait_70_sec = 70;
	public static int wait_80_sec = 80;
	public static int wait_90_sec = 90;
	public static int wait_100_sec = 100;
	public static int wait_120_sec = 120;
	public static int wait_150_sec = 150;
	public static int wait_180_sec = 180;
	
	
	public static void delay(int seconds) {
		try {
			LOG.info("WAIT FOR " +seconds +" SECONDS");
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {

		}
	}
}
