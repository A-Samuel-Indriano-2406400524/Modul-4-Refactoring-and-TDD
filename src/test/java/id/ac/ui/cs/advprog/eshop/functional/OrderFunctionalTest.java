package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    @Autowired
    private OrderService orderService;

    private String baseUrl;

    @BeforeEach
    void setupTest(){
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createOrderPage_isCorrect(ChromeDriver driver){
        driver.get(baseUrl + "/order/create");

        assertEquals("Create New Order", driver.getTitle());
        assertEquals("Create New Order", driver.findElement(By.tagName("h3")).getText());
    }

    @Test
    void orderHistoryPage_isCorrect(ChromeDriver driver){
        driver.get(baseUrl + "/order/history");

        assertEquals("Order History", driver.getTitle());
        assertEquals("Find Order History", driver.findElement(By.tagName("h3")).getText());
    }

    @Test
    void orderHistoryPost_isCorrect(ChromeDriver driver){
        String author = "Functional Test Author";
        String orderId = UUID.randomUUID().toString();
        orderService.createOrder(new Order(orderId, createProducts(), 1708560000L, author));

        driver.get(baseUrl + "/order/history");
        driver.findElement(By.id("authorInput")).sendKeys(author);
        driver.findElement(By.tagName("button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//tbody/tr[td[text()='" + orderId + "']]")));

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains(orderId));
        assertTrue(pageSource.contains(author));
    }

    private List<Product> createProducts(){
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Functional Test Product");
        product.setProductQuantity(1);
        products.add(product);
        return products;
    }
}