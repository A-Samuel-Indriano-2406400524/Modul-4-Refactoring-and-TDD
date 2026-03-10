package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController controller;

    private Order order;

    @BeforeEach
    void setUp(){
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Product");
        product.setProductQuantity(1);
        order = new Order("order-1", List.of(product), 1708560000L, "Author");
    }

    @Test
    void testCreateOrderPage(){
        assertEquals("createOrder", controller.createOrderPage());
    }

    @Test
    void testOrderHistoryPage(){
        assertEquals("orderHistoryForm", controller.orderHistoryPage());
    }

    @Test
    void testOrderHistoryPost(){
        List<Order> orders = List.of(order);
        when(orderService.findAllByAuthor("Author")).thenReturn(orders);

        String viewName = controller.orderHistoryPost("Author", model);

        assertEquals("orderHistoryList", viewName);
        verify(model).addAttribute("orders", orders);
    }

    @Test
    void testOrderPayPage(){
        when(orderService.findById("order-1")).thenReturn(order);

        String viewName = controller.orderPayPage("order-1", model);

        assertEquals("paymentOrderForm", viewName);
        verify(model).addAttribute("order", order);
    }

    @Test
    void testOrderPayPostVoucherCode(){
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-1", order, "Voucher Code", "SUCCESS", paymentData);
        when(orderService.findById("order-1")).thenReturn(order);
        when(paymentService.addPayment(order, "Voucher Code", paymentData)).thenReturn(payment);

        String viewName = controller.orderPayPost(
                "order-1",
                "Voucher Code",
                "ESHOP1234ABC5678",
                null,
                null,
                model);

        assertEquals("paymentOrderSuccess", viewName);
        verify(model).addAttribute("payment", payment);
    }

    @Test
    void testOrderPayPostBankTransfer(){
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF123");
        Payment payment = new Payment("payment-2", order, "Bank Transfer", "SUCCESS", paymentData);
        when(orderService.findById("order-1")).thenReturn(order);
        when(paymentService.addPayment(order, "Bank Transfer", paymentData)).thenReturn(payment);

        String viewName = controller.orderPayPost(
                "order-1",
                "Bank Transfer",
                null,
                "BCA",
                "REF123",
                model);

        assertEquals("paymentOrderSuccess", viewName);
        verify(model).addAttribute("payment", payment);
    }

    @Test
    void testOrderPayPostUnknownMethod() {
        Map<String, String> paymentData = new HashMap<>();
        Payment payment = new Payment("payment-3", order, "Cash", "REJECTED", paymentData);
        when(orderService.findById("order-1")).thenReturn(order);
        when(paymentService.addPayment(order, "Cash", paymentData)).thenReturn(payment);

        String viewName = controller.orderPayPost(
                "order-1",
                "Cash",
                null,
                null,
                null,
                model);

        assertEquals("paymentOrderSuccess", viewName);
        verify(model).addAttribute("payment", payment);
    }
}