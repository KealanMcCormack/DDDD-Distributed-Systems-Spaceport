package database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import org.bson.Document;

import messages.OrderRequest;
import org.bson.conversions.Bson;

import java.util.ArrayList;

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
        Document document = new Document(orderRequest.getOrderId(), orderRequest);
        transactionsCollection.insertOne(document);
    }

    public ArrayList<Object> getAllTransactions(){
        ArrayList<Object> allTransactions = new ArrayList<>();
        MongoCollection<Document> transactionsCollection = db.getCollection("Transactions");
        Bson filter = Filters.empty();
        transactionsCollection.find(filter).forEach(doc -> allTransactions.add(doc.toJson()));
        return allTransactions;
    }

    public ArrayList<Object> getBasedOnID(String orderId){
        ArrayList<Object> allTransactions = new ArrayList<>();
        MongoCollection<Document> transactionsCollection = db.getCollection("Transactions");
        Bson filter = Filters.eq("orderID", orderId);
        transactionsCollection.find(filter).forEach(doc -> allTransactions.add(doc.toJson()));
        return allTransactions;
    }
}
