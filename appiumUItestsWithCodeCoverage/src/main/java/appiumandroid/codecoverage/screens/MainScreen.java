package appiumandroid.codecoverage.screens;

import org.openqa.selenium.By;

import appiumandroid.codecoverage.LocatorUtils;

public class MainScreen {
	
	String number1 = "suram.sridhar:id/number1";
	String number2 = "suram.sridhar:id/number2";
	String result = "suram.sridhar:id/result";
	String addition = "suram.sridhar:id/sum";
	String subtraction = "suram.sridhar:id/subtract";
	
	
	public void enterNumber1(String number) {
		LocatorUtils.driver.findElement(By.id(number1)).clear();
		LocatorUtils.click(By.id(number1));
		LocatorUtils.driver.findElement(By.id(number1)).sendKeys(new String(number));
	}
	
	public void enterNumber2(String number) {
		LocatorUtils.driver.findElement(By.id(number2)).clear();
		LocatorUtils.click(By.id(number2));
		LocatorUtils.driver.findElement(By.id(number2)).sendKeys(new String(number));
	}
	
	
	public void clickOnSumButton() {
		LocatorUtils.click(By.id(addition));
	}
	
	
	public void clickOnsubtractionButton() {
		LocatorUtils.click(By.id(subtraction));
	}
	
	public String getResult() {
		return LocatorUtils.driver.findElement(By.id(result)).getText();
	}
}
