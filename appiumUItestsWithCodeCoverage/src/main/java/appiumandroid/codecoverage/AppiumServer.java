package appiumandroid.codecoverage;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

/**
 * 
 * @author Sridhar Reddy Suram
 *
 * Start Appium Server dynamically.
 */
public class AppiumServer {
	private static final Logger LOG = LoggerFactory.getLogger(AppiumServer.class);
	private String NODE_PATH = "/usr/local/bin/node";
	private String APPIUM_PATH = "/usr/local/bin/appium";

	public String startAppiumServerAndGetURL() throws Exception {
		stopServer();
		LOG.info("START APPIUM SERVER");
		AppiumDriverLocalService server;
		AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
		serviceBuilder.usingAnyFreePort();
		serviceBuilder.usingDriverExecutable(new File(NODE_PATH));
		serviceBuilder.withAppiumJS(new File(APPIUM_PATH));
		String appiumLogFile = System.getProperty("user.dir") + File.separator + "appium.log";
		System.out.println("appiumLogFile:" + appiumLogFile);
		serviceBuilder.withLogFile(new File(appiumLogFile));
		server = AppiumDriverLocalService.buildService(serviceBuilder);
		server.start();

		String hostName = "http://127.0.0.1:";
		int port = server.getUrl().getPort();
		String appiumURL = hostName + port + "/wd/hub";

		LOG.info("APPIUM SERVER STARTED AND LISTENING TO.." + appiumURL);
		return appiumURL;
	}

	public static void stopServer() throws Exception {

		Runtime rt = Runtime.getRuntime();
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
			LOG.info("WINDOWS SYSTEM FOUND, KILL EXISTING NODE PROCESS");
			rt.exec("taskkill -IM node");
		} else {
			LOG.info("MACOS FOUND, KILL EXISTING NODE PROCESS");
			rt.exec("kill -9 node ");
		}

	}
}
