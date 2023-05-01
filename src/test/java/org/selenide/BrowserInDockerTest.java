package org.selenide;

import org.junit.jupiter.api.Test;
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
 * > docker run -p 4444:4444 -p 7900:7900 --shm-size="2g" selenium/standalone-chromium
 * or
 * > docker run -p 4444:4444 -p 7900:7900 --shm-size="2g" seleniarm/standalone-chromium (on Mac M1)
 */
public class BrowserInDockerTest {
  @Test
  public void connectToBrowserInDocker() throws MalformedURLException {
    RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444"), new ChromeOptions());
    driver.navigate().to("https://selenide.org");
    DevTools devTools = getDevTools(driver);
    devTools.clearListeners();
    devTools.close();
    driver.quit();
  }

  private DevTools getDevTools(WebDriver driver) {
    return ((HasDevTools) unwrap(driver)).getDevTools();
  }

  private WebDriver unwrap(WebDriver webDriver) {
    return new Augmenter().augment(webDriver);
  }
}
