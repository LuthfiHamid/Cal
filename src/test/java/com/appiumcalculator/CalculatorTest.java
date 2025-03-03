package com.appiumcalculator;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

public class CalculatorTest {
    AndroidDriver driver;
    DesiredCapabilities capabilities;
    UiAutomator2Options options;
    ExtentReports extent;
    ExtentTest test;

    public void launchApp() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("adb",
                "-s",
                "am",
                "start",
                "-n","com.sec.adroid.app.popupcalculator/.Calculator");

        Process pc = pb.start();
        pc.waitFor();
    }

    @BeforeTest
    public void setup() throws InterruptedException, IOException{
        launchApp();

        capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("appium:deviceName", "sdk_gphone64_arm64");
        capabilities.setCapability("appium:automationName", "UiAutomator2");

        options = new UiAutomator2Options();
        driver= new AndroidDriver(new URL("http://127.0.0.1:4723"), options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.hideKeyboard();
    }

    @DataProvider(name = "dataProvider")
    public Object[][] dataProvider() {
        return new Object[][]{
                {10, 20, 30},
                {200, 30, 230}
        };
    }

    @Test(dataProvider = "dataProvider")
    public void testAddition(int val1, int val2, int expectedResult) {
        test = extent.createTest("Test Addition: " + val1 + " + " + val2);

        // Operasi Penjumlahan
        driver.findElement(By.id("com.sec.android.app.popupcalculator:id/digit_" + val1)).click();
        driver.findElement(By.id("com.sec.android.app.popupcalculator:id/op_add")).click();
        driver.findElement(By.id("com.sec.android.app.popupcalculator:id/digit_" + val2)).click();
        driver.findElement(By.id("com.sec.android.app.popupcalculator:id/eq")).click();

        String resultText = driver.findElement(By.id("com.sec.android.app.popupcalculator:id/result")).getText();
        int result = Integer.parseInt(resultText);

        // Verifikasi hasil
        Assert.assertEquals(result, expectedResult);
        test.pass("Addition successful: " + val1 + " + " + val2 + " = " + result);
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
        extent.flush();
    }

}
