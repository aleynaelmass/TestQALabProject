package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductPage {
    WebDriver driver;
    WebDriverWait wait;
    String productUrl = "https://www.trendyol-milla.com/trendyolmilla/bordo-boyundan-baglamali-kemer-detayli-fitted-vucuda-oturan-orme-bluz-twoss24bz00580-p-852805957";

    By basketCount = By.cssSelector(".basket-item-count-container.visible");
    By sizeOptions = By.cssSelector(".sp-itm");
    By addToBasketButton = By.cssSelector("[component-id='1']");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void goToProductPage() {
        driver.get(productUrl);
    }

    public int getBasketCount() {
        try {
            WebElement basketElement = wait.until(ExpectedConditions.visibilityOfElementLocated(basketCount));
            String countText = basketElement.getText().replaceAll("[^0-9]", "");
            return countText.isEmpty() ? 0 : Integer.parseInt(countText);
        } catch (Exception e) {
            System.out.println("Basket count okunamadı: " + e.getMessage());
            return 0;
        }
    }

    public void selectAvailableSizeAndAddToBasket() {
        List<WebElement> sizes = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(sizeOptions));
        for (WebElement size : sizes) {
            if (size.findElements(By.cssSelector(".notice-alarm")).isEmpty()) {
                size.click();
                break;
            }
        }

        try {
            Thread.sleep(1500); // beden seçimi sonrası
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(addToBasketButton));
        addButton.click();

        try {
            Thread.sleep(2000); // sepete ekleme sonrası
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Güncellenmiş sepet sayısı görünene kadar bekle
        wait.until(ExpectedConditions.visibilityOfElementLocated(basketCount));
    }




}
