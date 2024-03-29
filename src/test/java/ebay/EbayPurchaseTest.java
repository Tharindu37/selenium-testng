package ebay;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class EbayPurchaseTest {
    private WebDriver driver;

    @BeforeTest
    public void setUp(){
        //Initialize Chrome Web Driver
        driver= new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void navigateToEbay(){
        driver.get("https://www.ebay.com/");
        Assert.assertTrue(driver.getCurrentUrl().contains("ebay.com"),"Failed to navigate to eBay.");
    }

    @Test(priority = 2)
    public void  selectCategory(){
        WebElement categoryDropdown= new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("gh-shop-a")));
        categoryDropdown.click();

        WebElement cellPhonesAccessoriesOption=new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Cell phones & accessories")));
        cellPhonesAccessoriesOption.click();

        WebElement selectedCategory = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='seo-breadcrumb-text']/span[text()='Cell Phones & Accessories']")));
        Assert.assertTrue(selectedCategory.getText().contains("Cell Phones & Accessories"),
                "Failed to select 'Cell Phones & Accessories' category.");
    }

    @Test(priority = 3)
    public void searchMobilePhone(){
        WebElement searchBox=driver.findElement(By.id("gh-ac"));
        searchBox.sendKeys("Mobile Phone");
        searchBox.sendKeys(Keys.ENTER);
    }

    @Test(priority = 4)
    public void chooseFirstResults(){
        List<WebElement> searchResults = driver.findElements(By.xpath("//li[@class='s-item s-item__pl-on-bottom']"));
        Assert.assertTrue(searchResults.size() >= 5);
        for (int i = 1; i <= 5; i++) {
            System.out.println("Result " + i + ": " + searchResults.get(i).getText());
        }
        WebElement firstResult = searchResults.get(1);
        WebElement linkElement = firstResult.findElement(By.tagName("a"));
        String hrefAttributeValue = linkElement.getAttribute("href");
        System.out.println("Href Attribute Value: " + hrefAttributeValue);
        driver.get(hrefAttributeValue);
    }

    @Test(priority = 5)
    public void retrieveAndDisplayItemDetails(){
        WebElement productNameElement = driver.findElement(By.xpath("//h1[@class='x-item-title__mainTitle']//span[@class='ux-textspans ux-textspans--BOLD']"));
        String productName = productNameElement.getText();
        System.out.println("Product Name: " + productName);

        WebElement priceElement = driver.findElement(By.xpath("//div[@class='x-price-primary']//span[@class='ux-textspans']"));
        String price = priceElement.getText();
        System.out.println("Price: " + price);


        WebElement colorDropdown = driver.findElement(By.id("x-msku__select-box-1000"));
        Select dropdown = new Select(colorDropdown);
        dropdown.selectByIndex(1);

        WebElement categoryDropdown = driver.findElement(By.id("x-msku__select-box-1001"));
        Select dropdown1 = new Select(categoryDropdown);
        dropdown1.selectByIndex(1);

        WebElement quantityInput = driver.findElement(By.id("qtyTextBox"));
        quantityInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), "2");

    }

    @Test(priority = 6)
    public void addItemToCart(){
        WebElement addToCartButton = driver.findElement(By.cssSelector("a[data-testid='ux-call-to-action'][href='https://cart.payments.ebay.com/sc/add']"));
        addToCartButton.click();

    }

    @Test(priority = 7)
    public void viewShoppingCart(){
        WebElement itemDetails = driver.findElement(By.cssSelector("div.grid-item-information"));
        String itemName = itemDetails.findElement(By.cssSelector("h3.item-title a")).getText();
        String itemVariation = itemDetails.findElement(By.cssSelector("div.item-variations span")).getText();

        System.out.println(itemName);
        System.out.println(itemVariation);

    }

    @Test(priority = 8)
    public void checkout(){
        WebElement itemNameElement = driver.findElement(By.cssSelector(".cart-summary-line-item:first-child span.text-display-span span span"));
        String itemName = itemNameElement.getText();
        System.out.println("Item Name: " + itemName);

        WebElement itemPriceElement = driver.findElement(By.cssSelector(".cart-summary-line-item:last-child span.text-display-span span span"));
        String itemPrice = itemPriceElement.getText();
        System.out.println("Item Price: " + itemPrice);

        WebElement checkoutButton = driver.findElement(By.cssSelector("button[data-test-id='cta-top']"));
        checkoutButton.click();

    }

    @AfterTest
    public void closeBrowser(){
        driver.quit();
    }
}
