package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;
import lombok.Getter;

@Getter
public class Payment {
    String id;
    Order order;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, Order order, String method, String status,
            Map<String, String> paymentData) {
        this.id = id;
        this.order = order;
        this.method = method;
        this.status = status;
        this.paymentData = paymentData;
    }

    public void setStatus(String status) {
        if (status.equals("PENDING") || status.equals("SUCCESS") || status.equals("REJECTED")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}