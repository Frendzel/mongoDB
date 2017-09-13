package pl.lodz.sda;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.log4j.Logger.getLogger;

public class MongoDBConnector {

    final static Logger logger = getLogger(MongoDBConnector.class);

    private PropertiesLoader propertiesLoader = new PropertiesLoader();

    private MongoClient client;

    private void prepareConfiguration() {
        ServerAddress address = new ServerAddress(propertiesLoader.getHost(), propertiesLoader.getPort());
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        MongoCredential credential = MongoCredential.createCredential(propertiesLoader.getUser(),
                propertiesLoader.getSchema(), propertiesLoader.getPass().toCharArray());
        credentials.add(credential);
        client = new MongoClient(address, credentials);
    }

    private void init() {
        try {
            propertiesLoader.init();
            prepareConfiguration();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private MongoDatabase connect() {
        init();
        return client.getDatabase(propertiesLoader.getSchema());
    }

    public FindIterable<Document> findDocuments(String collectionName,
                                                Bson filter,
                                                Bson projection) {
        MongoDatabase connect = connect();
        MongoCollection<Document> collection = connect.getCollection(collectionName);
        return collection.find(filter).projection(projection);
    }
//
//
//    public static void main(String[] args) throws IOException {
//
//        MongoCollection<Document> documentCollection = database.getCollection("grades");
//        /*student_id: {
//            $gt: 100
//        }*/
//        // budowanie strasznego bsona
//        Document bson = new Document().append("student_id",
//                new Document().append("$gt", 100));
//
//        logger.info("nasz bson: " + bson.toJson());
//
//        FindIterable<Document> documents = documentCollection.
//                find(bson).
//                projection(
//                        new Document().
//                                append("student_id", 1)
//                                .append("_id", 0));
//        MongoCursor<Document> iterator = documents.iterator();
//
//        JsonWriterSettings.Builder withIndents =
//                JsonWriterSettings.builder().indent(true);
//
//        while (iterator.hasNext()) {
//            logger.info("Nasz dokument: " + iterator.next().
//                    toJson(withIndents.build()));
//        }
//        iterator.close();
//
//        // korzystanie z MongoDB Api
//        FindIterable<Document> documentsThroughMongoDbApi =
//                documentCollection.find(
//                        gt("student_id", 100));
////        logger.info());
//
//    }

    /**
     * 1. Dodać logowanie i pisać junity
     * 2. Dodać kolekcję do bazy danych
     * https://www.mkyong.com/mongodb/java-mongodb-insert-a-document/
     * 3. Wyświetlić liczbę dokumentów w kolekcji
     * 4. Identyfikatory studentów większe od 100
     * 5. Oceny studentów większe o id > 100 z
     *   typem oceny = exam
     * 6. Wyświetl identyfikatory studentów ze średnią
     * 7. Posortuj wyświetlone powyżej identyfikatory
     * 8. Zupdatuj wszystkie oceny z egzaminu o 1 jeżeli zawierają się w przedziale 10 - 20
     * https://docs.mongodb.com/manual/reference/operator/update/max/#up._S_max
     */
}
