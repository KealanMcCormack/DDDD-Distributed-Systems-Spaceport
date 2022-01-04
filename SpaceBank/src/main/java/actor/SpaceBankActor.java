package actor;

import akka.actor.AbstractActor;
import core.BankStatus;
import messages.BankRequest;
import messages.BankResponse;
import messages.OrderRequest;
import messages.Init;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SpaceBankActor extends AbstractActor {


    private static final Logger logger = LoggerFactory.getLogger(SpaceBankActor.class);
    private long transactionCounter;
    private Map<String , BankResponse> transactions;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(BankRequest.class,
                        msg -> {
                            //Mocked Bank to simulate communication with external service
                            logger.info("SpaceBank| Received Transaction request charge: {}, receiver customer ID: {}, payee customer ID: {}", msg.getCharge(), msg.getReceiverId(), msg.getPayeeID());
                            BankResponse bankResponse = new BankResponse(msg.getReceiverId(), msg.getCharge(), msg.getPayeeID(), "SB"+transactionCounter, BankStatus.PROCESSED);
                            transactions.put(bankResponse.getBankTransactionID(), bankResponse);
                            getSender().tell(bankResponse, getSelf());
                        }
                )
                .match(Init.class,
                        msg -> {
                            logger.info("SpaceBank Actor init| Mock Space Bank");
                            transactionCounter = 0;
                            transactions = new HashMap<>();
                        }
                ).build();
    }
    
}
