package com.actor;

import akka.actor.*;
import com.database.TransactionDatabase;
import messages.Init;
import messages.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderActor extends AbstractActor{
    private final Logger logger = LoggerFactory.getLogger(OrderActor.class);

    private TransactionDatabase transactionDatabase;

    public static Props propsDefault(){
        return Props.create(OrderActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OrderRequest.class,
                        msg -> {
                            logger.info("Order request being inserted");
                            transactionDatabase.insert(msg);
                            ActorSystem system = ActorSystem.create();
                            ActorSelection selection = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/SpaceBank");
                            selection.tell(msg, getSelf());
                        })
                .match(Init.class,
                        msg -> {
                            transactionDatabase = new TransactionDatabase();
                        }
                ).build();
    }
}
