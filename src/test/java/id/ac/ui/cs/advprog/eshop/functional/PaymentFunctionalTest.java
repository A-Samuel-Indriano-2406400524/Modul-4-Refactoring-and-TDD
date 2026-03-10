package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class PaymentFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void paymentDetailPage_isCorrect(ChromeDriver driver){
        driver.get(baseUrl + "/payment/detail");

        assertTrue(driver.getPageSource().contains("Payment Detail"));
    }

    @Test
    void paymentDetailByIdPage_isCorrect(ChromeDriver driver){
        Payment payment = createPayment("Voucher Code", createVoucherData());

        driver.get(baseUrl + "/payment/detail/" + payment.getId());
        assertTrue(driver.getPageSource().contains(payment.getId()));
    }

    @Test
    void paymentAdminListPage_isCorrect(ChromeDriver driver){
        Payment payment = createPayment("Bank Transfer", createBankTransferData());

        driver.get(baseUrl + "/payment/admin/list");
        assertTrue(driver.getPageSource().contains(payment.getId()));
    }

    @Test
    void paymentAdminDetailPage_isCorrect(ChromeDriver driver){
        Payment payment = createPayment("Bank Transfer", createBankTransferData());

        driver.get(baseUrl + "/payment/admin/detail/" + payment.getId());
        assertTrue(driver.getPageSource().contains(payment.getId()));
    }

    @Test
    void paymentAdminSetStatus_isCorrect(ChromeDriver driver){
        Payment payment = createPayment("Bank Transfer", createBankTransferData());
        
        driver.get(baseUrl + "/payment/admin/detail/" + payment.getId());
        driver.findElement(By.cssSelector("button[value='REJECTED']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "REJECTED"));
        assertTrue(driver.getPageSource().contains("REJECTED"));
    }

    private Payment createPayment(String method, Map<String, String> paymentData){
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(orderId, createProducts(), 1708560000L, "Payment Functional Test");
        orderService.createOrder(order);
        return paymentService.addPayment(order, method, paymentData);
    }

    private List<Product> createProducts(){
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Payment Functional Product");
        product.setProductQuantity(1);
        products.add(product);
        return products;
    }

    private Map<String, String> createVoucherData(){
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        return paymentData;
    }

    private Map<String, String> createBankTransferData(){
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF123456");
        return paymentData;
    }
}