package com.artison.bakery_backend.controller;

import com.artison.bakery_backend.model.CustomerOrder;
import com.artison.bakery_backend.repository.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private CustomerOrderRepository orderRepository;

    @PostMapping("/place")
    public CustomerOrder placeOrder(@RequestBody CustomerOrder order) {
        return orderRepository.save(order);
    }

    @GetMapping("/all")
    public List<CustomerOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @PutMapping("/{id}/status")
    public CustomerOrder updateStatus(@PathVariable Long id, @RequestBody String newStatus) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(newStatus);
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        if(orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Double>> getAnalytics() {
        List<CustomerOrder> allOrders = orderRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        double daily = 0;
        double weekly = 0;
        double monthly = 0;
        double yearly = 0;

        for(CustomerOrder o : allOrders) {
            if(o.getOrderDate() != null) {
                if(o.getOrderDate().isAfter(now.minusDays(1))) daily += o.getTotalAmount();
                if(o.getOrderDate().isAfter(now.minusWeeks(1))) weekly += o.getTotalAmount();
                if(o.getOrderDate().isAfter(now.minusMonths(1))) monthly += o.getTotalAmount();
                if(o.getOrderDate().isAfter(now.minusYears(1))) yearly += o.getTotalAmount();
            }
        }

        return ResponseEntity.ok(Map.of(
                "daily", daily,
                "weekly", weekly,
                "monthly", monthly,
                "yearly", yearly
        ));
    }
}
