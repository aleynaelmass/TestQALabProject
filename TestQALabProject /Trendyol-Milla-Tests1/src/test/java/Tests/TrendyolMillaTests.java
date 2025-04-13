package Tests;

import Base.BaseTest;
import Pages.LoginPage;
import Pages.HomePage;
import Pages.ProductPage;
import Base.TestData;

import io.qameta.allure.*;

import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Epic("Trendyol Milla Testleri")
@Feature("Kullanıcı Giriş ve Ürün Arama")
public class TrendyolMillaTests extends BaseTest {

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setupClass() {
        System.out.println("Allure raporu için testler başlıyor...");
    }

    @Test
    @Story("Geçerli kullanıcı girişi")
    @Description("Doğru kullanıcı adı ve şifre ile giriş yapılır. Kullanıcı başarıyla ana sayfaya yönlendirilmelidir ve hesabı görünmelidir.")
    @Severity(SeverityLevel.CRITICAL)
    public void testLogin() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = loginPage.loginValidUser(TestData.validEmail, TestData.validPassword);
        Assert.assertTrue("Kullanıcı hesabı görünür olmalı", homePage.isAccountVisible());
    }

    @Test
    @Story("Geçersiz şifre ile giriş")
    @Description("Yanlış şifre kullanıldığında, hata mesajı gösterilmeli ve kullanıcı giriş yapamamalıdır. Hata mesajı 'E-posta adresiniz ve/veya şifreniz hatalı' olmalıdır.")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidPasswordLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginInvalidPassword(TestData.validEmail, TestData.invalidPassword);
        loginPage.errorMessageControl("E-posta adresiniz ve/veya şifreniz hatalı.");
    }

    @Test
    @Story("Ürün araması")
    @Description("Kullanıcı, pantolon araması yapar. Arama sonuçları doğrulanmalı ve her ürün başlığında 'pantolon' veya 'jean' kelimesi bulunmalıdır.")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginAndProductSearch() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = loginPage.loginValidUser(TestData.validEmail, TestData.validPassword);
        Assert.assertTrue("Kullanıcı hesabı görünür olmalı", homePage.isAccountVisible());

        List<String> productTitles = homePage.searchProduct("Pantolon");
        for (String title : productTitles) {
            Assert.assertTrue("Ürün başlığı geçerli değil: " + title,
                    title.toLowerCase().contains("pantolon") || title.toLowerCase().contains("jean"));
        }
    }

    @Test
    @Story("Boş arama")
    @Description("Kullanıcı arama kutusuna boş bir değer girdiğinde, uygun şekilde boş sonuç döndürülmeli ve kullanıcıya arama önerileri gösterilmelidir.")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptySearch() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = loginPage.loginValidUser(TestData.validEmail, TestData.validPassword);
        Assert.assertTrue("Kullanıcı hesabı görünür olmalı", homePage.isAccountVisible());

        boolean isEmpty = homePage.searchWithEmptyInput();
        Assert.assertTrue("Boş arama sonucunda ürün listesi dönmemeli", isEmpty);
    }

    @Test
    @Story("Özel karakterli arama")
    @Description("Kullanıcı, arama kutusuna özel karakterli bir ifade girdiğinde, arama sonuçları uygun şekilde döndürülmelidir. Sistem özel karakterleri doğru şekilde işlemelidir.")
    @Severity(SeverityLevel.NORMAL)
    public void testSpecialCharacterSearch() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = loginPage.loginValidUser(TestData.validEmail, TestData.validPassword);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Assert.assertTrue("Kullanıcı hesabı görünür olmalı", homePage.isAccountVisible());

        boolean resultShown = homePage.searchSpecialCharacters("$Laptop!");
        Assert.assertTrue("Özel karakterli aramada ürün bulunmalı veya düzgün tepki verilmeli", resultShown);
    }

    @Test
    @Story("Boş aramada öneriler")
    @Description("Kullanıcı, arama kutusuna boş bir değer girdiğinde, arama önerileri veya keşifler kullanıcıya doğru şekilde gösterilmelidir.")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptySearchShowsSuggestions() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = loginPage.loginValidUser(TestData.validEmail, TestData.validPassword);
        Assert.assertTrue("Kullanıcı hesabı görünür olmalı", homePage.isAccountVisible());

        boolean suggestionVisible = homePage.checkSearchSuggestions();
        Assert.assertTrue("Boş aramada öneri/keşif gösterilmeli", suggestionVisible);
    }

    @Test
    @Story("Sepete ürün ekleme")
    @Description("Kullanıcı, ürün sayfasına gidip, mevcut bir ürünü sepete ekler. Sepet sayısı doğru şekilde artmalıdır.")
    @Severity(SeverityLevel.NORMAL)
    public void testSepeteUrunEkleme() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginValidUser(TestData.validEmail, TestData.validPassword);

        ProductPage productPage = new ProductPage(driver);
        productPage.goToProductPage();

        int before = productPage.getBasketCount();
        productPage.selectAvailableSizeAndAddToBasket();
        int after = productPage.getBasketCount();

        Assert.assertEquals("Sepet sayısı artmalı", before + 1, after);
    }

    @Test
    @Story("Aynı ürünü iki kez sepete ekleme")
    @Description("Kullanıcı aynı ürünü iki kez sepete eklemeyi denediğinde, sepet sayısı iki katına çıkmalıdır.")
    @Severity(SeverityLevel.NORMAL)
    public void testAyniUrunIkiKezSepeteEkleme() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginValidUser(TestData.validEmail, TestData.validPassword);

        ProductPage productPage = new ProductPage(driver);
        productPage.goToProductPage();

        int before = productPage.getBasketCount();
        productPage.selectAvailableSizeAndAddToBasket();
        int mid = productPage.getBasketCount();
        productPage.selectAvailableSizeAndAddToBasket();
        int after = productPage.getBasketCount();

        Assert.assertEquals("Ürün iki kez eklenmiş olmalı", before + 2, after);
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Allure için testler tamamlandı.");
    }
}
