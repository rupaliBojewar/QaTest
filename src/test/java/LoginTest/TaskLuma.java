package LoginTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TaskLuma {
    WebDriver driver;
    WebDriverWait wait;

    Actions act;
    String highestPriceItem;
    String lowestPriceItem;

    public TaskLuma() {
        driver = new ChromeDriver();
    }


    public void login() throws InterruptedException {
        driver.get("https://magento.softwaretestingboard.com/customer/account/");
        driver.manage().window().maximize();
        driver.findElement(By.id("email")).sendKeys("rupali.bojewar@gmail.com");
        driver.findElement(By.id("pass")).sendKeys("Rupali@1070");
        driver.findElement(By.xpath("(//button[@name=\"send\"])[1]")).click();
        Thread.sleep(2000);
    }

    public void goToBags() throws InterruptedException {
        act = new Actions(driver);

        WebElement gearField = driver.findElement(By.xpath("//*[text()=\"Gear\"]"));
        act.moveToElement(gearField).perform();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//*[text()=\"Bags\"]")).click();
        Thread.sleep(5000);
    }

    @Test
    public void verifyEmptyCart() throws InterruptedException {
        login();
        goToBags();
        driver.findElement(By.xpath("//a[@class=\"action showcart\"]")).click();
        String pageString = driver.getPageSource();
        if (pageString.contains("You have no items in your shopping cart.")) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }
    }


//        String cartCount = (driver.findElement(By.xpath("//span[@class=\"counter-number\"]")).getText());
//        System.out.println(cartCount);
//        Assert.assertEquals(cartCount,"0");


    //String Actualmsg=driver.findElement(By.xpath("//strong[@class=\"subtitle empty\"]")).getText();
    //System.out.println(Actualmsg);
    //Assert.assertEquals(Actualmsg,"You have no items in your shopping cart.");
    @Test
    public void verifyHighestItemInWishList() throws InterruptedException {
        login();
        goToBags();

        List<WebElement> pricelist = driver.findElements(By.xpath("//li[@class=\"item product product-item\"]/child::div/child::div/child::*/child::span/child::span/child::span"));

        ArrayList priceArray = new ArrayList();

        for (WebElement ele : pricelist
        ) {
            priceArray.add(ele.getText().replaceAll("[$,a-z,A-Z]", ""));

        }
        Collections.sort(priceArray);

//        for (Object ele:priceArray)
//        {
//            System.out.println(ele);}

        System.out.println(Collections.max(priceArray));
        String maxPrice = Collections.max(priceArray).toString();

        String minPrice = Collections.min(priceArray).toString();
        String highestPriceItem = driver.findElement(By.xpath("//span[text()=\"$" + maxPrice + "\"]/ancestor::li/child::div/child::a/following-sibling::div/child::strong/a")).getText();

        System.out.println(highestPriceItem);
        act.moveToElement(driver.findElement(By.xpath("//span[text()=\"$" + maxPrice + "\"]/ancestor::li/child::div/child::a/following-sibling::div/child::strong/a"))).perform();
        driver.findElement(By.xpath("//a[contains(text(),\"" + highestPriceItem + "\")]//parent::strong/following-sibling::div[@class=\"product-item-inner\"]/child::div/child::div[@class=\"actions-secondary\"]/a[@title=\"Add to Wish List\"]")).click();
        //driver.findElement(By.xpath("//a[contains(text(),\""+highestPriceItem+"\")]/parent::strong/following-sibling::div[@class=\"product-item-inner\"]/descendant::button")).click();

        List<WebElement> wishListItems = driver.findElements(By.xpath("//div[@class=\"products-grid wishlist\"]/descendant::strong/child::a"));
        ArrayList wishList = new ArrayList();

        for (WebElement el : wishListItems) {
            wishList.add(el.getText());
        }

        for (Object s : wishList) {
            System.out.println(s);
            if (s.equals(highestPriceItem)) {
                Assert.assertTrue(true);
            } else {
                Assert.assertTrue(false);
            }
        }


    }

    @Test
    public void addLowestItemInWishList() throws InterruptedException {
        login();
        goToBags();

        List<WebElement> pricelist = driver.findElements(By.xpath("//li[@class=\"item product product-item\"]/child::div/child::div/child::*/child::span/child::span/child::span"));

        ArrayList priceArray = new ArrayList();

        for (WebElement ele : pricelist
        ) {
            priceArray.add(ele.getText().replaceAll("[$,a-z,A-Z]", ""));

        }
        for (Object ele:priceArray) {
         System.out.println(ele);}
        Collections.sort(priceArray);

        String minPrice = Collections.min(priceArray).toString();

        System.out.println(Collections.min(priceArray));

        //String lowestPriceItem = driver.findElement(By.xpath("//span[text()=\"$" + minPrice + "\"]/ancestor::li/child::div/child::a/following-sibling::div/child::strong/a")).getText();
        String lowestPriceItem = driver.findElement(By.xpath("//span[text()=\"$24.00\"]/ancestor::li/child::div/child::a/following-sibling::div/child::strong/a")).getText();

        System.out.println(lowestPriceItem);
        act.moveToElement(driver.findElement(By.xpath("//span[text()=\"$24.00\"]/ancestor::li/child::div/child::a/following-sibling::div/child::strong/a"))).perform();
        driver.findElement(By.xpath("//a[contains(text(),\"" + lowestPriceItem + "\")]//parent::strong/following-sibling::div[@class=\"product-item-inner\"]/child::div/child::div[@class=\"actions-secondary\"]/a[@title=\"Add to Wish List\"]")).click();
        //driver.findElement(By.xpath("//a[contains(text(),\""+highestPriceItem+"\")]/parent::strong/following-sibling::div[@class=\"product-item-inner\"]/descendant::button")).click();
        driver.findElement(By.xpath("//*[contains(text(),\""+lowestPriceItem+"\")]/ancestor::div[@class=\"block block-wishlist\"]//button/child::span")).click();
    }

    @Test
    public void addressValidation() throws InterruptedException {
        addLowestItemInWishList();
        //verifyHighestItemInWishList();

        System.out.println(lowestPriceItem);

        driver.findElement(By.xpath("//*[contains(text(),\""+ lowestPriceItem +"\")]/ancestor::div[@class=\"block block-wishlist\"]//button/child::span")).click();

        //driver.findElement(By.xpath("//button[@title=\"Add All to Cart\"]")).click();

        //driver.quit();

    }

    public void waitForElement(WebElement element) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
}

