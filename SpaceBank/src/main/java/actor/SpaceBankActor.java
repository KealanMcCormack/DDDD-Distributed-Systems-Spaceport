package actor;

import akka.actor.AbstractActor;
import messages.OrderRequest;

public class SpaceBankActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OrderRequest.class,
                        msg -> {
                            getSender().tell("Payment Received", getSelf());
                        }).build();
    }
    
}
