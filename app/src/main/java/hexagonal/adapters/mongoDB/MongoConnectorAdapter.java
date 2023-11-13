package hexagonal.adapters.mongoDB;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Optional;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import hexagonal.businessLogic.Entities.EScooter;
import hexagonal.businessLogic.Entities.Ride;
import hexagonal.businessLogic.Entities.User;
import io.vertx.core.file.OpenOptions;

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
    private MongoDatabase database;
    static Logger logger = Logger.getLogger("[EScooter DB Connector]");

    public MongoConnectorAdapter(String databaseName) {
        logger.setLevel(Level.INFO);
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoClient mongoClient = MongoClients.create("mongodb://root:password@127.0.0.1:27072");
        this.database = mongoClient.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
        // MongoCollection<User> collection = database.getCollection("user", User.class);
        this.logger.log(Level.INFO, "Connected to MongoDB instance");
    }

    public InsertOneResult saveUser(User user) {
        return this.database.getCollection("user", User.class).insertOne(user);
    }

    public InsertOneResult saveEScooter(EScooter escooter) {
        return this.database.getCollection("escooter", EScooter.class).insertOne(escooter);
    }

    public InsertOneResult saveRide(Ride ride) {
        return this.database.getCollection("ride", Ride.class).insertOne(ride);
    }

    public Optional<User> getUser(String id) {
        return Optional.of(this.database.getCollection("user", User.class).find(eq("_id", id)).first());
    }

    public Optional<EScooter> getEScooter(String id) {
        return Optional.of(this.database.getCollection("escooter", EScooter.class).find(eq("_id", id)).first());
    }

    public Optional<Ride> getRide(String id) {
        return Optional.of(this.database.getCollection("ride", Ride.class).find(eq("_id", id)).first());
    }

    public List<Ride> getOngoingRides() {
        return this.database.getCollection("ride", Ride.class).find(eq("ongoing", true)).into(List.of());
    }

}
