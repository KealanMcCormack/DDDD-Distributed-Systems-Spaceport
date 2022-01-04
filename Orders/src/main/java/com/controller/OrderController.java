package com.controller;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import messages.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired()
    @Qualifier("OrderActor")
    ActorRef orderActor;

    @PostMapping("/add")
    public void add(@RequestBody OrderRequest orderRequest){
        orderActor.tell(orderRequest, null);
    }


}
