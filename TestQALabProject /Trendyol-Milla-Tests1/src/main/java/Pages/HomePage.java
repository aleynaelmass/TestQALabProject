package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomePage {
    WebDriver driver;
    WebDriverWait wait;

    By hesabimText = By.cssSelector("p.link-text:first-of-type");
    By searchInput = By.cssSelector("input[data-testid='suggestion']");
    By searchIcon = By.cssSelector("i[data-testid='search-icon']");
    By searchResultCards = By.cssSelector(".p-card-wrppr");
    By suggestionElements = By.cssSelector(
            "[data-testid='suggestions-container'], " +
                    "[data-testid='search-history-list'], " +
                    "[data-testid='homepage-suggestion-discover-item'], " +
                    ".main-carousel-wrapper, " +
                    "[data-type='product-card']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean isAccountVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement hesabimElement = wait.until(ExpectedConditions.visibilityOfElementLocated(hesabimText)); // Bu satırı kullanıyoruz
            return hesabimElement.isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Timeout: Hesabım görünmedi.");
            return false;
        }
    }




    public List<String> searchProduct(String keyword) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput)).sendKeys(keyword);
        driver.findElement(searchIcon).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchResultCards));

        List<WebElement> cards = driver.findElements(searchResultCards);
        List<String> titles = new ArrayList<>();
        for (WebElement card : cards) {
            String title = card.getAttribute("title");
            if (title != null && !title.trim().isEmpty()) {
                titles.add(title.trim());
            }
        }
        return titles;
    }

    public boolean searchWithEmptyInput() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys("");
        driver.findElement(searchIcon).click();

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchResultCards));
            List<WebElement> cards = driver.findElements(searchResultCards);
            return cards.isEmpty();
        } catch (Exception e) {
            return true; // sonuç gelmemesi beklenen durumdur
        }
    }

    public boolean searchSpecialCharacters(String keyword) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        searchBox.clear();
        searchBox.sendKeys(keyword);

        driver.findElement(searchIcon).click(); // submit yerine ikon tıklaması

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(searchResultCards),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".no-rslt-text.no-rslt-title"))
            ));

            List<WebElement> results = driver.findElements(searchResultCards);
            List<WebElement> noResults = driver.findElements(By.cssSelector(".no-rslt-text.no-rslt-title"));

            return !results.isEmpty() || !noResults.isEmpty(); // her iki durumda da test başarılı
        } catch (TimeoutException e) {
            System.out.println("Arama sonucu bulunamadı veya sistem tepki vermedi.");
            return false;
        }
    }



    public boolean checkSearchSuggestions() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.click();
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(suggestionElements));
            return true;
        } catch (Exception e) {
            return false;
        }
    }



}
