package com.dojo.orders.controllers;

import com.azure.storage.blob.BlobClient;
import com.dojo.customers.entities.Customer;
import com.dojo.customers.services.CustomerBlobService;
import com.dojo.orders.entities.Order;
import com.dojo.orders.entities.OrderDetail;
import com.dojo.orders.services.OrderBlobService;
import com.dojo.orders.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);
    private OrderService orderService;
    private OrderBlobService storageService;

    public OrderController(OrderService orderService, OrderBlobService storageService){
        this.orderService = orderService;
        this.storageService = storageService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> listAll() {
        logger.info("Ordenes consultados: "+Map.of("total",orderService.listAllOrders().size()));
        return ResponseEntity.ok(orderService.listAllOrders());
    }

    @GetMapping
    public ResponseEntity<List<Order>> getCustomerByPage(Pageable pageable) {
        Page<Order> orderPage = orderService.getPageByOrder(pageable);
        logger.info("Ordenes consultados: "+ Map.of("total", orderPage.getNumberOfElements()));
        return ResponseEntity.ok(orderPage.getContent());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> listOrdersByUsername(@PathVariable String username) {
        List<Order> orders = orderService.getOrdersByUsername(username);
        logger.info("Cliente con username: "+username+" . Ordenes: "+ orders);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> listOrdersByCustomer( @PathVariable Long id){
        List<Order> orders = orderService.getOrdersByCustomer(id);
        logger.info("Cliente con id: "+id+" . Ordenes: "+ orders);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id){
        Optional<Order> optional =orderService.getOrderById(id);
        if(optional.isEmpty()){
            logger.warn(String.format("Orden con id: %d no encontrado!",id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensaje","Orden no econtrado!"));
        }
        Order order = optional.get();
        logger.info("Orden con id: "+id+" encontrado. Orden: "+order.toString());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<?> createOrder(@PathVariable Long customerId, @RequestBody List<OrderDetail> details) {
        Order o=orderService.save(customerId, details);
        if(o==null){
            String message="Cliente con id: "+customerId+" no encontrado!";
            logger.warn(message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensaje",message));
        }
        logger.info("Nueva orden creada: "+ o.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(o);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id ) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if(optionalOrder.isPresent()){
            orderService.delete(id);
            logger.info(String.format("Orden con id: %d eliminado con éxito!",id));
            return ResponseEntity.ok("Orden eliminado con éxito!");
        }
        logger.warn(String.format("Orden con id: %d no encontrado!",id));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensaje","Orden no econtrado!"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id,@RequestBody Order order){
        Order orderUpdated = orderService.updateStatus(id, order.getStatusOrder());
        logger.info(String.format("Orden con id: %d actualizado con éxito!",id));
        return ResponseEntity.ok(orderUpdated);
    }

    @GetMapping("/blob/{name}")
    public ResponseEntity<?> getBlobUrl(@PathVariable String name) {
        Optional<BlobClient> optionalBlobClient = storageService.findFileBlob(name);
        if (optionalBlobClient.isPresent()){
            String urlBlob=optionalBlobClient.get().getBlobUrl();
            logger.info("Url del blob encontrado: "+urlBlob);
            return ResponseEntity.ok(urlBlob);
        }
        logger.info("No se encontró archivo Blob!");
        return ResponseEntity.notFound().build();
    }
}
