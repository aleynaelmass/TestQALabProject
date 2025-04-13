package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;

import java.time.Duration;

public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;

    By emailInput = By.id("login-email");
    By passwordInput = By.id("login-password-input");
    By loginButton = By.cssSelector("button[type='submit']");
    By errorMessage = By.cssSelector(".message");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).sendKeys(password);
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void errorMessageControl(String expectedMessage) {
        String actualMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    public HomePage loginValidUser(String email, String password) {
        enterEmail(email);
        clickLoginButton();
        enterPassword(password);
        clickLoginButton();
        return new HomePage(driver);
    }

    public void loginInvalidPassword(String email, String invalidPassword) {
        enterEmail(email);
        clickLoginButton();
        enterPassword(invalidPassword);
        clickLoginButton();
    }
}
