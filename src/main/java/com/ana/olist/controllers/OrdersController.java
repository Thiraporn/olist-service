package com.ana.olist.controllers;

import com.ana.common.security.libs.payload.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/olist")
@RequiredArgsConstructor
public class OrdersController {
    // ตัวอย่าง GET เพื่อทดสอบ
    @GetMapping("/healthcheck")
    public ResponseEntity<?> doHealthcheck() {
        log.debug("{} {} {}ms","API Health Check Ok!");
        return ResponseEntity.ok(new MessageResponse("API Health Check Ok!"));
    }

    @PostMapping("/orders")
    public ResponseEntity<?> getOrders(HttpServletRequest request) {
        return ResponseEntity.ok(new MessageResponse("This is the Olist orders page"));
    }


}
