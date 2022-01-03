package actor;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import messages.Init;
import service.PriceMonitoringService;

public class Main {
    public static void main(String... args){
        ActorSystem system = ActorSystem.create();
        ActorRef ref = system.actorOf(Props.create(Monitor.class), "PriceMonitorActor");
        ref.tell(new Init(new PriceMonitoringService()), null);
        //ActorSelection selection =
        //system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
        //selection.tell("register", ref);
    }
}
