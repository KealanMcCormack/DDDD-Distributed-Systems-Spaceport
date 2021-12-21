import actor.OrderActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class OrderApplication {
    ActorSystem system = ActorSystem.create();
    ActorRef ref = system.actorOf(Props.create(OrderActor.class), "Orders");
}
