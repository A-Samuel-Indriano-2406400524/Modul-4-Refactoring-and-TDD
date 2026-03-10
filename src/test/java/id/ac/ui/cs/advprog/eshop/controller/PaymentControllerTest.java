package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
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
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private Model model;

    @InjectMocks
    private PaymentController controller;

    private Payment payment;

    @BeforeEach
    void setUp(){
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Product");
        product.setProductQuantity(1);
        Order order = new Order("order-1", List.of(product), 1708560000L, "Author");
        payment = new Payment("payment-1", order, "Voucher Code", "SUCCESS", Map.of("voucherCode", "ESHOP1234ABC5678"));
    }

    @Test
    void testPaymentDetailPage(){
        assertEquals("paymentDetailForm", controller.paymentDetailPage());
    }

    @Test
    void testPaymentDetailByIdPage(){
        when(paymentService.getPayment("payment-1")).thenReturn(payment);

        String viewName = controller.paymentDetailByIdPage("payment-1", model);

        assertEquals("paymentDetail", viewName);
        verify(model).addAttribute("payment", payment);
    }

    @Test
    void testPaymentAdminListPage(){
        List<Payment> payments = List.of(payment);
        when(paymentService.getAllPayments()).thenReturn(payments);

        String viewName = controller.paymentAdminListPage(model);

        assertEquals("paymentAdminList", viewName);
        verify(model).addAttribute("payments", payments);
    }

    @Test
    void testPaymentAdminDetailPage(){
        when(paymentService.getPayment("payment-1")).thenReturn(payment);

        String viewName = controller.paymentAdminDetailPage("payment-1", model);

        assertEquals("paymentAdminDetail", viewName);
        verify(model).addAttribute("payment", payment);
    }

    @Test
    void testPaymentAdminSetStatus(){
        Payment updatedPayment = new Payment("payment-1", payment.getOrder(), "Voucher Code", "REJECTED", payment.getPaymentData());
        when(paymentService.getPayment("payment-1")).thenReturn(payment);
        when(paymentService.setStatus(payment, "REJECTED")).thenReturn(updatedPayment);

        String viewName = controller.paymentAdminSetStatus("payment-1", "REJECTED", model);

        assertEquals("paymentAdminDetail", viewName);
        verify(model).addAttribute("payment", updatedPayment);
    }
}