package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in
     * instructions
     */
    @Test
    public void testCase01() {

        Wrappers.NavigateToURL(driver, "https://www.flipkart.com/");
        Wrappers.searchProduct(driver, "Washing Machine");
        Wrappers.sortBy(driver, "Popularity");
        //Here we are retrive the list of rating after converting string into double
        List<Double> ratingsList = Wrappers.getProductRating(driver);
        //Here we are passing the all products rating and filter the rating with lessthan or equal to 4
        int coutofRatings = Wrappers.coutOfRating(ratingsList, 4);
        Assert.assertTrue(coutofRatings > 0, "Expected at least one item with rating <= 4");
        System.out.println("The number of products with rating less than or equal to 4 is : " +coutofRatings);
    }

    @Test
    public void testCase02() {
        Wrappers.NavigateToURL(driver, "https://www.flipkart.com/");
        Wrappers.searchProduct(driver, "iPhone");
        //Passing the Discount we needed and retriv the items with discount 
        Map<String, Integer> items = Wrappers.getItemsWithDiscountAbove(driver, 17);

        for (Map.Entry<String, Integer> e : items.entrySet()) {
            System.out.println("Title: " + e.getKey() + " discount: " + e.getValue());

        }

    }

    @Test
    public void testCase03() throws Exception {
        Wrappers.NavigateToURL(driver, "https://www.flipkart.com/");
        Wrappers.searchProduct(driver, "Coffee Mug");

        Wrappers.applyFilter(driver, "4");

        List<String[]> top5 = Wrappers.getTopProductsByReviews(driver, 5);

        int rank = 1;
        for (String[] str : top5) {
            System.out.println("#" + rank++ + " Title: " + str[0] );
            System.out.println("Image URL: " + str[1]);
            System.out.println(" Reviews: " + str[2]);
            

        }

    }

    /*
     * Do not change the provided methods unless necessary, they will help in
     * automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest() {
        driver.close();
        driver.quit();

    }
}