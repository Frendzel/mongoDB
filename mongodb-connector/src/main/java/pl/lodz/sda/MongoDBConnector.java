package pl.lodz.sda;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MongoDBConnector {
    public static void main(String[] args) throws IOException {
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        propertiesLoader.init();
        ServerAddress address = new ServerAddress(propertiesLoader.getHost(), propertiesLoader.getPort());
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        MongoCredential credential = MongoCredential.createCredential(propertiesLoader.getUser(),
                "biblioteka", propertiesLoader.getPass().toCharArray());
        credentials.add(credential);
        MongoClient client = new MongoClient(address, credentials);
        MongoDatabase database = client.getDatabase("biblioteka");
        database.getName();
        MongoCollection<Document> documentCollection = database.getCollection("grades");
    }

    /**
     * 1. Dodać logowanie i pisać junity
     * 2. Dodać kolekcję do bazy danych
     * 3. Wyświetlić liczbę dokumentów w kolekcji
     * 4. Identyfikatory studentów większe od 100
     * 5. Oceny studentów większe o id > 100 z typem oceny = exam
     * 6. Wyświetl identyfikatory studentów ze średnią
     */
}
