package org.selenide;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

@Testcontainers
public class BrowserInTestContainersTest {

  @Container
  public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>(chromeImage())
    .withCapabilities(new ChromeOptions())
    .withEnv("SE_NODE_GRID_URL", "http://localhost:4444/");

  @Test
  public void connectToBrowserInDocker() {
    ChromeOptions options = new ChromeOptions();
    RemoteWebDriver driver = new RemoteWebDriver(chrome.getSeleniumAddress(), options, false);
    driver.navigate().to("https://selenide.org");

    hackCdpUrl(driver);

    DevTools devTools = getDevTools(driver);
    devTools.createSessionIfThereIsNotOne(driver.getWindowHandle());
    devTools.clearListeners();
    devTools.close();
    driver.quit();
  }

  /**
   * This is a workaround for a bug in Selenium. 
   * When Grid is running in Docker on port 4444, it's bound to some random port on host machine (say, 63647).
   * 
   * On startup, ChromeDriver gets capability "se:cdp"="ws://localhost:4444/session/11cf1e54378f5491ed8cdc552ec8c99c/se/cdp".
   * But it's available only inside docker container.
   * 
   * To use CDP on host machine, we need to use URL "ws://localhost:63647/session/11cf1e54378f5491ed8cdc552ec8c99c/se/cdp".
   */
  private void hackCdpUrl(RemoteWebDriver driver) {
    MutableCapabilities capabilities = (MutableCapabilities) driver.getCapabilities();
    Integer gridPort = chrome.getMappedPort(4444);
    String currentCdpUrl = (String) capabilities.getCapability("se:cdp");
    String cdpUrlForHostMachine = currentCdpUrl.replace("4444", String.valueOf(gridPort));
    capabilities.setCapability("se:cdp", cdpUrlForHostMachine);
  }

  private DevTools getDevTools(WebDriver driver) {
    return ((HasDevTools) unwrap(driver)).getDevTools();
  }

  private WebDriver unwrap(WebDriver webDriver) {
    return new Augmenter().augment(webDriver);
  }

  @Nonnull
  @CheckReturnValue
  public static DockerImageName chromeImage() {
    return isArmArchitecture() ?
      DockerImageName.parse("seleniarm/standalone-chromium")
        .asCompatibleSubstituteFor("selenium/standalone-chrome") :
      DockerImageName.parse("selenium/standalone-chrome");
  }

  private static boolean isArmArchitecture() {
    return System.getProperty("os.arch").equals("aarch64");
  }

}
