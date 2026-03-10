package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage(){
        return "createOrder";
    }

    @GetMapping("/history")
    public String orderHistoryPage(){
        return "orderHistoryForm";
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam("author") String author, Model model){
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "orderHistoryList";
    }

    @GetMapping("/pay/{orderId}")
    public String orderPayPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "paymentOrderForm";
    }

    @PostMapping("/pay/{orderId}")
    public String orderPayPost(
            @PathVariable String orderId,
            @RequestParam("method") String method,
            @RequestParam(value = "voucherCode", required = false) String voucherCode,
            @RequestParam(value = "bankName", required = false) String bankName,
            @RequestParam(value = "referenceCode", required = false) String referenceCode,
            Model model) {
        Order order = orderService.findById(orderId);
        Map<String, String> paymentData = new HashMap<>();

        if (method.equals("Voucher Code")) {
            paymentData.put("voucherCode", voucherCode);
        } else if (method.equals("Bank Transfer")) {
            paymentData.put("bankName", bankName);
            paymentData.put("referenceCode", referenceCode);
        }

        Payment payment = paymentService.addPayment(order, method, paymentData);
        model.addAttribute("payment", payment);
        return "paymentOrderSuccess";
    }
}