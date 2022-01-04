import actor.OrderActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import messages.Init;
import org.springframework.boot.SpringApplication;

public class OrderApplication {

    public static void main(String[] args){
        SpringApplication.run(OrderApplication.class, args);

        ActorSystem system = ActorSystem.create();
        ActorRef ref = system.actorOf(Props.create(OrderActor.class), "Orders");
        ref.tell(new Init(), null);
    }

}
