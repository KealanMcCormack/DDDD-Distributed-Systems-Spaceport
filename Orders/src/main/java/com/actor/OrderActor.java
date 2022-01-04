package com.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.database.BankReceiptDatabase;
import core.BankStatus;
import com.database.TransactionDatabase;
import messages.BankRequest;
import messages.BankResponse;
import akka.actor.*;
import messages.Init;
import messages.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderActor extends AbstractActor{

    private final Logger logger = LoggerFactory.getLogger(OrderActor.class);
    private TransactionDatabase transactionDatabase;
    private BankReceiptDatabase receiptDatabase;
    private ActorSystem system;
    private final String spacePortID = "SP1234";

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

                            ActorSelection selection = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/SpaceBank");

                            BankRequest bankRequest;
                            if(msg.getProduct().getAmount() > 1){
                                bankRequest = new BankRequest(msg.getCustomerId(), msg.getProduct().getTotalPrice(), spacePortID, msg.getOrderId());
                            }
                            else {
                                bankRequest = new BankRequest(spacePortID, msg.getProduct().getTotalPrice(), msg.getCustomerId(), msg.getOrderId());
                            }

                            selection.tell(bankRequest, getSelf());
                        })
                .match(BankResponse.class,
                        msg -> {
                            logger.info("Order Actor Bank Receipt| Bank Receipt: {}", msg);
                            receiptDatabase.insert(msg);
                            if (msg.getBankTransactionStatus() == BankStatus.DECLINED){
                                logger.error("Order Actor Bank Decline| Action Required by staff| Bank Receipt: {}", msg);
                            }
                        })
                .match(Init.class,
                        msg -> {
                            system = ActorSystem.create();
                            transactionDatabase = new TransactionDatabase();
                            receiptDatabase = new BankReceiptDatabase();
                        }
                ).build();
    }
}
