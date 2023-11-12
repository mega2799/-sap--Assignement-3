package hexagonal.adapters.mongoDB;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import hexagonal.businessLogic.Entities.User;

public class MongoConnectorAdapter {
    // crea una classe java che si connetta ad un instanza di mongoDB.
    // la classe deve avere un metodo che restituisca un oggetto di tipo
    // MongoCollection<Escooter> che rappresenta la collection "escooter" del
    // database "escooterDB"
    // la classe deve avere un metodo che restituisca un oggetto di tipo
    // MongoCollection<User> che rappresenta la collection "user" del database
    // "escooterDB"
    // la classe deve avere un metodo che restituisca un oggetto di tipo
    // MongoCollection<Ride> che rappresenta la collection "ride" del database
    // "escooterDB"
    private MongoClient mongoClient;
    private MongoDatabase repository;
    static Logger logger = Logger.getLogger("[EScooter DB Connector]");

    public MongoConnectorAdapter(String databaseName) {
        logger.setLevel(Level.INFO);
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        try (MongoClient mongoClient = MongoClients.create("mongodb://root:password@127.0.0.1:27072")) {
            MongoDatabase database = mongoClient.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
            this.logger.log(Level.INFO, "Connected to MongoDB instance");
            MongoCollection<User> collection = database.getCollection("user", User.class);
            collection.insertOne(new User("frank", "Matteo", "santoro"));
        }
    }

}
