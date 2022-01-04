package com.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.actor.OrderActor;
import com.typesafe.config.ConfigFactory;
import messages.Init;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ActorSystem actorSystem(){
        return ActorSystem.create();
    }

    @Bean(name = {"OrderActor"})
    public ActorRef orderActor(){
        ActorRef ref = actorSystem().actorOf(OrderActor.propsDefault());
        ref.tell(new Init(), null);
        return ref;
    }


}
