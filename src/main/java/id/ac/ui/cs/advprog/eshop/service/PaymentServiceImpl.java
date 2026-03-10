package id.ac.ui.cs.advprog.eshop.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData){
        String status = "REJECTED";
        if (method.equals("Voucher Code")){
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null && voucherCode.length() == 16 && voucherCode.startsWith("ESHOP")){
                int digitCount = 0;
                for (char character : voucherCode.toCharArray()){
                    if (Character.isDigit(character)) {
                        digitCount += 1;
                    }
                }

                if (digitCount == 8){
                    status = "SUCCESS";
                }
            }
        } else if (method.equals("Bank Transfer")){
            String bankName = paymentData.get("bankName");
            String referenceCode = paymentData.get("referenceCode");
            if (bankName != null && !bankName.isEmpty() && referenceCode != null && !referenceCode.isEmpty()){
                status = "SUCCESS";
            }
        }

        Payment payment = new Payment(UUID.randomUUID().toString(), order, method, status, paymentData);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status){
        payment.setStatus(status);
        if (status.equals("SUCCESS")){
            payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        } else if (status.equals("REJECTED")){
            payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        }
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId){
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }
}