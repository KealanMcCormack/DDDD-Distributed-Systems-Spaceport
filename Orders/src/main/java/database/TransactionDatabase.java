package database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import message.OrderRequest;
import org.bson.Document;

/*
 This will act as an api for the database
 */
public class TransactionDatabase {

    private final MongoDatabase db;

    public TransactionDatabase(){
        String uri = System.getenv("MONGODB_CONNSTRING");

        MongoClient client = MongoClients.create(uri);

        db = client.getDatabase("Orders");

        db.createCollection("Transactions");
    }

    public void insert(OrderRequest orderRequest){
        MongoCollection<Document> transactionsCollection = db.getCollection("Transactions");
        Document document = new Document(orderRequest.orderId, orderRequest.products);
        transactionsCollection.insertOne(document);
    }

    public void getAllTransactions(){
        MongoCollection<Document> transactionsCollection = db.getCollection("Transactions");
    }
}
