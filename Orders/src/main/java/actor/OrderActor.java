package actor;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import database.TransactionDatabase;
import messages.Init;
import messages.OrderRequest;

public class OrderActor extends AbstractActor{

    private TransactionDatabase transactionDatabase;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OrderRequest.class,
                        msg -> {
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
