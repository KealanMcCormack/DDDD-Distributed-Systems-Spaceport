package actor;

import akka.actor.AbstractActor;
import database.TransactionDatabase;
import message.OrderRequest;

public class OrderActor extends AbstractActor{

    TransactionDatabase transactionDatabase;

    public OrderActor(){
        transactionDatabase = new TransactionDatabase();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OrderRequest.class,
                        msg -> {
                            transactionDatabase.insert(msg);
                        }).build();
    }
}
