# Sample project 

... for running test against a browser running in Docker.

## How to run tests

> ./gradlew test


## Result

Currently, this test fails because of some connectivity issue:

```java
org.openqa.selenium.remote.http.ConnectionFailedException: Unable to establish websocket connection to http://172.17.0.2:4444/session/73724745df4560f13daf1760f1a86c31/se/cdp
Build info: version: '4.9.0', revision: 'd7057100a6'
System info: os.name: 'Mac OS X', os.arch: 'aarch64', os.version: '13.3.1', java.version: '17.0.7'
Driver info: driver.version: unknown
	at app//org.openqa.selenium.remote.http.netty.NettyWebSocket.<init>(NettyWebSocket.java:102)
	at app//org.openqa.selenium.remote.http.netty.NettyWebSocket.lambda$create$3(NettyWebSocket.java:128)
	at app//org.openqa.selenium.remote.http.netty.NettyClient.openSocket(NettyClient.java:107)
	at app//org.openqa.selenium.devtools.Connection.<init>(Connection.java:82)
	at app//org.openqa.selenium.devtools.SeleniumCdpConnection.<init>(SeleniumCdpConnection.java:34)
	at app//org.openqa.selenium.devtools.SeleniumCdpConnection.lambda$create$0(SeleniumCdpConnection.java:56)
	at java.base@17.0.7/java.util.Optional.map(Optional.java:260)
	at app//org.openqa.selenium.devtools.SeleniumCdpConnection.create(SeleniumCdpConnection.java:54)
	at app//org.openqa.selenium.devtools.SeleniumCdpConnection.create(SeleniumCdpConnection.java:47)
	at app//org.openqa.selenium.devtools.DevToolsProvider.getImplementation(DevToolsProvider.java:50)
	at app//org.openqa.selenium.devtools.DevToolsProvider.getImplementation(DevToolsProvider.java:31)
	at app//org.openqa.selenium.remote.Augmenter.augment(Augmenter.java:188)
	at app//org.openqa.selenium.remote.Augmenter.augment(Augmenter.java:164)
	at app//org.selenide.BrowserInDockerTest.unwrap(BrowserInDockerTest.java:36)
	at app//org.selenide.BrowserInDockerTest.getDevTools(BrowserInDockerTest.java:32)
	at app//org.selenide.BrowserInDockerTest.connectToBrowserInDocker(BrowserInDockerTest.java:25)
```