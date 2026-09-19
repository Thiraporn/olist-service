package com.ana.olist.controllers;

import com.ana.common.security.libs.payload.MessageResponse;
import com.ana.olist.entities.FactOrder;
import com.ana.olist.reports.OrdersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/olist")
@RequiredArgsConstructor
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/orders")
    public ResponseEntity<?> getOrders(HttpServletRequest request) {
        List<FactOrder> orders = ordersService.getAllOrder();
        return ResponseEntity.ok(orders);
    }


}
