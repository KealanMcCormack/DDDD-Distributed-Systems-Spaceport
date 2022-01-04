package com.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import messages.BankResponse;
import messages.OrderRequest;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class BankReceiptDatabase {
    private final MongoDatabase db;

    public BankReceiptDatabase(){
        String uri = System.getenv("MONGODB_CONNSTRING");

        MongoClient client = MongoClients.create(uri);

        db = client.getDatabase("Orders");

        db.createCollection("BankReceipts");
    }

    public void insert(BankResponse bankResponse){
        MongoCollection<Document> transactionsCollection = db.getCollection("BankReceipts");
        Document document = new Document(bankResponse.getBankTransactionID(), bankResponse);
        transactionsCollection.insertOne(document);
    }

    public ArrayList<Object> getAllReceipts(){
        ArrayList<Object> allReceipts = new ArrayList<>();
        MongoCollection<Document> receiptsCollection = db.getCollection("BankReceipts");
        Bson filter = Filters.empty();
        receiptsCollection.find(filter).forEach(doc -> allReceipts.add(doc.toJson()));
        return allReceipts;
    }

    public ArrayList<Object> getBasedOnID(String orderId){
        ArrayList<Object> allReceipts = new ArrayList<>();
        MongoCollection<Document> receiptsCollection = db.getCollection("BankReceipts");
        Bson filter = Filters.eq("orderID", orderId);
        receiptsCollection.find(filter).forEach(doc -> allReceipts.add(doc.toJson()));
        return allReceipts;
    }
}
