package pl.lodz.sda;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.junit.Test;

import java.util.ArrayList;

import static org.bson.json.JsonWriterSettings.builder;
import static org.junit.Assert.assertTrue;

public class MongoDBConnectorTest {

    private static final String GRADES = "grades";

    private static final Logger logger = Logger.getLogger(MongoDBConnectorTest.class);

    MongoDBConnector mongoDBConnector = new MongoDBConnector();

    @Test
    public void findDocuments() throws Exception {
        //given
        Document bson = new Document("student_id",
                new Document("$gt", 100));
        Document studentId =
                new Document().append("student_id", 1).
                        append("_id", 0);

        //when
        FindIterable<Document> documents = mongoDBConnector.
                findDocuments(GRADES, bson, studentId);

        //then
        MongoCursor<Document> iterator = documents.iterator();

        JsonWriterSettings.Builder withIndents =
                builder().indent(true);

        while (iterator.hasNext()) {
            Document document = iterator.next();
            assertTrue(document.containsKey("student_id"));
            assertTrue(((Integer)
                    document.get("student_id")) > 100);
            logger.info("Nasz dokument: " + document.
                    toJson(withIndents.build()));
        }
        iterator.close();

    }

//    //
//     * Oceny studentów większe o id > 100 z
//     *   typem oceny = exam
//     *
//    db.grades.find({
//        $and: [{
//            student_id: {
//                $gt: 100
//            }
//        }, {
//            type: "exam"
//        }]
//    })

    @Test
    public void findDocumentsWithStudentIdsGt100AndScoreTypeEqualsExam() {
        //given
        Bson studentId = Filters.gt("student_id", 100);
        Bson typeExam = new Document("type", "exam");
        Bson and = Filters.and(studentId, typeExam);
        Document projection =
                new Document().append("student_id", 1).
                        append("_id", 0);

        //when
        FindIterable<Document> documents = mongoDBConnector.
                findDocuments(GRADES, and, projection);

        //then
        MongoCursor<Document> iterator = documents.iterator();

        JsonWriterSettings.Builder withIndents =
                builder().indent(true);

        while (iterator.hasNext()) {
            Document document = iterator.next();
            assertTrue(document.containsKey("student_id"));
            assertTrue(((Integer)
                    document.get("student_id")) > 100);
            logger.info("Nasz dokument: " + document.
                    toJson(withIndents.build()));
        }
        iterator.close();
    }

///
// db.grades.aggregateDocuments([{ $match: {}},
// { $group: { _id: “$student_id”, srednia: {$avg: “$score”} } }])
//

    @Test
    public void calculateStudentsIdAvg() {
        //given
        ArrayList<Bson> pipeline = new ArrayList<Bson>();
        Bson match = Aggregates.match(new Document());
        Bson group = Aggregates.group("$student_id",
                Accumulators.avg("srednia",
                        "$score"));
        pipeline.add(match);
        pipeline.add(group);
        //when
        AggregateIterable<Document> results =
                mongoDBConnector.aggregateDocuments(GRADES, pipeline);
        //then
        JsonWriterSettings.Builder withIndents =
                builder().indent(true);
        MongoCursor<Document> iterator = results.iterator();
        while(iterator.hasNext()){
            Document avgScore = iterator.next();
            logger.info(avgScore.toJson(withIndents.build()));
        }
        iterator.close();


    }

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
}