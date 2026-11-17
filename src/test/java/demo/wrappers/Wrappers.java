package demo.wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.security.PublicKey;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    static WebDriverWait wait;

    public static void NavigateToURL(WebDriver driver, String URL) {
        try {
            driver.get(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void searchProduct(WebDriver driver, String ProductName) {
        try {
            WebElement searchBar = driver.findElement(By.xpath("//form[@action='/search']//input[@name='q']"));
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(searchBar));
            searchBar.clear();
            searchBar.sendKeys(ProductName);
            searchBar.sendKeys(Keys.ENTER);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void sortBy(WebDriver driver, String sortByText) {
        try {

            WebElement sortByElement = driver.findElement(
                    By.xpath("//div[contains(@class,'WNv7PR') and contains(text(),'" + sortByText + "')]"));
            wait.until(ExpectedConditions.visibilityOf(sortByElement));
            sortByElement.click();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Double> getProductRating(WebDriver driver) {
        List<Double> ratingList = new ArrayList<>();
        try {
            Thread.sleep(3000);
            List<WebElement> ratings = driver
                    .findElements(By.xpath("//div[@class='a7saXW']/child::span[contains(@id,'productRating_')]"));
            wait.until(ExpectedConditions.visibilityOfAllElements(ratings));
            for (WebElement element : ratings) {
                String ratingText = element.getText();
                Double ratingInNum = Double.parseDouble(ratingText);
                ratingList.add(ratingInNum);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ratingList;
    }

    public static int coutOfRating(List<Double> ratingList, int rating) {
        int count = 0;
        try {
            for (int i = 0; i < ratingList.size(); i++) {
                if (ratingList.get(i) <= 4) {
                    count++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int parseInt(String text) {
        String digits = text.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? -1 : Integer.parseInt(digits);
    }

    public static final By ProductCard = By.xpath("//div[@class='jIjQ8S']");

    public static Map<String, Integer> getItemsWithDiscountAbove(WebDriver driver, int minDisc) {
        Map<String, Integer> result = new LinkedHashMap<>();

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ProductCard));
            List<WebElement> ProductCards = driver.findElements(ProductCard);
            

            for (WebElement Product : ProductCards) {
                List<WebElement> disElement = Product.findElements(By.xpath(".//span[contains(text(),'% off')]"));
                if (disElement.isEmpty()) {
                    continue;
                }
                int discount = parseInt(disElement.get(0).getText());
                if (discount > minDisc) {
                    WebElement titleEl = Product.findElement(By.xpath(".//div[@class='RG5Slk']"));
                    String title = titleEl.getText();
                    result.put(title, discount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static final By cardsBy = By.xpath("//div[@class='RGLWAk']");

    public static void applyFilter(WebDriver driver, String filter) throws Exception  {
       
        Thread.sleep(3000);
        By filterElBy = By.xpath("//div[contains(text(),'" + filter + "')]");
        
        WebElement FilterElement = wait.until(ExpectedConditions.elementToBeClickable(filterElBy));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", FilterElement);
        FilterElement.click();
        
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cardsBy));
    }

    public static List<String[]> getTopProductsByReviews(WebDriver driver, int TopN) throws Exception {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cardsBy));
        Thread.sleep(3000);
        List<WebElement> cards = driver.findElements(cardsBy);
        

        List<String[]> Products = new ArrayList<>();
        try {
            for (WebElement card : cards) {

                List<WebElement> reviewEle = card
                        .findElements(
                                By.xpath(".//span[starts-with(normalize-space(.),'(') and contains(text(),')')]"));
                if (reviewEle.isEmpty()) {
                    continue;
                }
                int reviews = parseInt(reviewEle.get(0).getText());
                WebElement imgElement = card.findElement(By.xpath(".//img[@class='UCc1lI']"));
                WebElement titleElement = card.findElement(By.xpath(".//a[@class='pIpigb']"));
                String title = titleElement.getText();
                String imgUrl = imgElement.getAttribute("src");

                Products.add(new String[] { title, imgUrl, String.valueOf(reviews) });
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }

        Products.sort((a, b) -> Integer.compare(Integer.parseInt(b[2]), Integer.parseInt(a[2])));

        return Products.subList(0, Math.min(TopN, Products.size()));

    }

}
