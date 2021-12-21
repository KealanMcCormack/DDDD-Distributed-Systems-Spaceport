package actor;

import akka.actor.AbstractActor;
import message.OrderRequest;

public class OrderActor extends AbstractActor{

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OrderRequest.class,
                        msg -> {
                            // Create transaction stuff here
                        }).build();
    }
}
