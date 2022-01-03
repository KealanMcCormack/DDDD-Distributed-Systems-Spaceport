package actor;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import messages.Init;
import messages.OrderRequest;
import messages.Product;
import service.PriceMonitoringService;

public class Monitor extends AbstractActor {
    PriceMonitoringService priceMonitoringService;
    
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Init.class,
                        msg -> {
                            priceMonitoringService = msg.getService();
                        })
                .match(Product.class,
                        msg -> {
                            priceMonitoringService.priceChange(msg);
                        })
                .build();
    }
}
