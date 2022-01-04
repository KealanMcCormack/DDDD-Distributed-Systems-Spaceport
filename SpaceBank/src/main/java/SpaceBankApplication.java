import actor.SpaceBankActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import messages.Init;

public class SpaceBankApplication {

    public static void main(String[] args){
        ActorSystem system = ActorSystem.create();
        ActorRef ref = system.actorOf(Props.create(SpaceBankActor.class), "SpaceBank");
        ref.tell(new Init(), null);
    }
}
