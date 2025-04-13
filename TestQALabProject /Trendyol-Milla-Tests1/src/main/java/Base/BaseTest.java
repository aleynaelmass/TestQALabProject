package Base;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;


public class BaseTest{



    protected WebDriver driver; // protected olarak tanımlıyoruz, böylece alt sınıflar erişebilir

    @Before
    public void setUpTest() {
        String browser = System.getProperty("browser", "chrome");
        driver = BaseLibrary.setUpDriver(browser);  // 'driver' burada doğru şekilde başlatılacak.
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();  // Testler bittiğinde tarayıcıyı kapatıyoruz.
        }
    }
}
