package com.ana.olist.reports;

import com.ana.olist.entities.FactOrder;
import com.ana.olist.repository.FactOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrdersService {

    @Autowired
    private FactOrderRepository factOrderRepository;

    public List<FactOrder> getAllOrder(){
        log.info("Getting all orders from fact_orders");
        List<FactOrder> orders = factOrderRepository.findAll();
        log.info("Total orders: {}", orders.size());

        return orders;
    }
}
