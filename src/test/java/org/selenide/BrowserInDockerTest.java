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

public class BrowserInDockerTest {
  @Test
  public void connectToBrowserInDocker() throws MalformedURLException {
    RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444"), new ChromeOptions());
    driver.navigate().to("https://selenide.org");
    DevTools devTools = getDevTools(driver);
    devTools.createSessionIfThereIsNotOne(driver.getWindowHandle());
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
