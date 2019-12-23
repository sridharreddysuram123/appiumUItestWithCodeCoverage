package appiumandroid.androidenterprise;

import org.openqa.selenium.By;

import appiumandroid.codecoverage.LocatorUtils;

public class AEProvisionScreen {

	public void clickOnNext() {
		LocatorUtils.click(By.id("com.afwsamples.testdpc:id/suw_navbar_next"));
	}

	
	public void clickOnSkip() {
		LocatorUtils.click(By.id("com.afwsamples.testdpc:id/add_account_skip"));
	}
	
	
	public void clickOnFinish() {
		LocatorUtils.click(By.id("com.afwsamples.testdpc:id/suw_navbar_next"));
	}
	
	public void enableSystemAppCamera() {
		LocatorUtils.scrollUpAndLocateElement(By.name("Enable system apps"), 3);
		LocatorUtils.click(By.name("Enable system apps"));
		LocatorUtils.click(By.name("Camera"));
	}
}

