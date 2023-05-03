package org.selenide;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Before running this test, execute from command line:
 * docker run -p 5555:4444 --shm-size="2g" -e "SE_NODE_GRID_URL=http://localhost:4444" selenium/standalone-chrome
 * 
 * Or on Mac M1:
 * docker run -p 5555:4444 --shm-size="2g" -e "SE_NODE_GRID_URL=http://localhost:4444" seleniarm/standalone-chromium
 */
public class BrowserInDockerWithDifferentPortTest {
  @Test
  public void connectToBrowserInDocker() throws MalformedURLException {
    RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:5555"), new ChromeOptions(), false);
    driver.navigate().to("https://selenide.org");

    hackCdpUrl(driver);

    DevTools devTools = getDevTools(driver);
    devTools.createSessionIfThereIsNotOne(driver.getWindowHandle());
    devTools.clearListeners();
    devTools.close();
    driver.quit();
  }

  /**
   * This is a workaround for an issue with different ports. 
   * When Grid is running in Docker on port 4444, it's bound to port 5555 on host machine.
   *
   * On startup, ChromeDriver gets capability "se:cdp"="ws://localhost:4444/session/11cf1e54378f5491ed8cdc552ec8c99c/se/cdp".
   * But it's available only inside docker container.
   *
   * To use CDP on host machine, we need to use URL "ws://localhost:5555/session/11cf1e54378f5491ed8cdc552ec8c99c/se/cdp".
   */
  private void hackCdpUrl(RemoteWebDriver driver) {
    MutableCapabilities capabilities = (MutableCapabilities) driver.getCapabilities();
    String currentCdpUrl = (String) capabilities.getCapability("se:cdp");
    String cdpUrlForHostMachine = currentCdpUrl.replace("4444", "5555");
    capabilities.setCapability("se:cdp", cdpUrlForHostMachine);
  }

  private DevTools getDevTools(WebDriver driver) {
    return ((HasDevTools) unwrap(driver)).getDevTools();
  }

  private WebDriver unwrap(WebDriver webDriver) {
    return new Augmenter().augment(webDriver);
  }
}
