package appiumandroid.codecoverage;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sridhar Reddy Suram
 * Used to locate the elements and its utilities 
 *
 */
public class LocatorUtils {
	public static AndroidDriver<WebElement> driver;
	private static final Logger LOG = LoggerFactory.getLogger(LocatorUtils.class);

	public LocatorUtils(AndroidDriver<WebElement> driver) {
		LocatorUtils.driver = driver;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	/**
	 * 
	 * @param by
	 * @param waitTime
	 * @param visibility
	 * @return  based on the elementVisbility it will retun the value either true of false
	 */
	public static boolean waitForElementToLoad(final By by, int waitTime, boolean elementVisibility) {
		if (elementVisibility) {
			for (int count = 0; count < waitTime/2; count++) {
				if (isElementLocated(by, 1)) {
					LOG.info("Element found");
					return true;
				} else {
					LOG.info("Element not located");
				}
			}
			LOG.info("Element not located");
			return false;
		}
		if (!elementVisibility) {
			for (int count = 0; count < waitTime/3; count++) {
				if (isElementLocated(by, 1)) {
					LOG.info("Element still available on screen");
				} else {
					LOG.info("Element disappeared");
					return true;
				}
			}
			LOG.info("Element still available on screen - Not disappeared");
			return false;
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return false;
	}
	
	public static boolean isElementLocated(final By by, int maxWaitTime) {
		maxWaitTime =1;
		WebElement element = null;
		LOG.info("By= " + by.toString());
		for (int count = 0; count < maxWaitTime; count ++) {
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			try {
				element = driver.findElement(by);
				LOG.info("Element located");
				return true;
			   } catch (Exception e) {
				   LOG.info("Element not located");
			  }
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return false;
	}
	
	public static boolean isElementLocated(final By by) {
		return isElementLocated(by, 30);
	}
	
	public WebElement waitForElement(final By by, int waitTime, boolean elementVisibility) {
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, 5);

		LOG.debug("By= " + by.toString());
		for (int attempt = 0; attempt < waitTime; attempt++) {
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			try {
				element = driver.findElement(by);
				if (elementVisibility) {
					break;
				} else if (!elementVisibility) {
					LOG.debug("Started waiting for element to disappear..."); 
					long startTime = System.currentTimeMillis();
					Wait<AndroidDriver<WebElement>> customFluentWait = 
							new FluentWait<AndroidDriver<WebElement>>(driver)
							.withTimeout(waitTime, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS);
					customFluentWait.until(ExpectedConditions.invisibilityOfElementLocated(by));
					long endTime = System.currentTimeMillis();
					element = null;
					LOG.debug("Element disappeared after {} seconds.", (endTime - startTime) / 1000);
					break;
				}
			} catch (Exception exc) {
				if (!elementVisibility) {
					break;
				}
			}
		}

		if (elementVisibility) {
			int count = 0, maxAttempt = 10;
			do {
				count++;
				if (count > maxAttempt)
					break;
				if (element != null) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(by));
				}
			} while (element != null && !element.isDisplayed());
		} else {
			if (element != null) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
				element = null;
			}
		}

		return element;
	}
	
	/**
	 * This method does a swipe upwards
	 */
	public static void scrollDown() {
		LOG.info("SCROLL DOWN:");
		scroll(0.8);
	}
	
	/**
	 * This method does a swipe downwards
	 */
	public static void scrollUp() {
		LOG.info("SCROLL UP:");
		scroll(0.2);
	}
	
	public static boolean scrollUpAndLocateElement(By by, int maxNoTimesToScroll) {
		for (int count = 0; count < maxNoTimesToScroll; count++) {
			if (isElementLocated(by, 10)) {
				return true;
			}
			scrollUp();
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	private static void scroll(Double diff) {
		Dimension dimensions = DriverUtility.androidDriver.manage().window().getSize();
		int height = dimensions.getHeight();
		int width = dimensions.getWidth();
		double scrollStartX = width * 0.5;
		double scrollStartY = height * 0.5;

		double scrollEndX = width * 0.5;
		double scrollEndY = height * 0.5;
		scrollEndY = height * diff;

		int startX = (int) scrollStartX;
		int startY = (int) scrollStartY;
		int endX = (int) scrollEndX;
		int endY = (int) scrollEndY;
		Point point = new Point(startX, startY);
		Point destination = new Point(endX, endY);
	    TouchAction ta = new TouchAction(DriverUtility.androidDriver);
	    Duration duration = Duration.ofMillis(1500);
	    ta.press(PointOption.point(point.getX(), point.getY())).waitAction(WaitOptions.waitOptions(duration)).moveTo(PointOption.point(destination.getX() , destination.getY())).release().perform();
	}
	
	public static WebElement getElement(By by) {
		return driver.findElement(by);
	}
	
	public static void click(By by) {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		WebElement element = getElement(by);
		element.click();
	}
	
}
	
