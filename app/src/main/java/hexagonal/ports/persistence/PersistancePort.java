package hexagonal.ports.persistence;

import java.util.List;
import java.util.Optional;

import hexagonal.adapters.mongoDB.MongoConnectorAdapter;
import hexagonal.businessLogic.Entities.EScooter;
import hexagonal.businessLogic.Entities.Ride;
import hexagonal.businessLogic.Entities.User;

public class PersistancePort {

    MongoConnectorAdapter mongoConnectorAdapter;

    public PersistancePort(String string) {
        this.mongoConnectorAdapter = new MongoConnectorAdapter("Escooter");
    }

    public void saveUser(User user) {
        this.mongoConnectorAdapter.saveUser(user);
    }

    public Optional<User> getUser(String userId) {
        return this.mongoConnectorAdapter.getUser(userId);
    }

    public void saveEScooter(EScooter eScooter) {
        this.mongoConnectorAdapter.saveEScooter(eScooter);
    }

    public Optional<EScooter> getEScooter(String id) {
        return this.mongoConnectorAdapter.getEScooter(id);
    }

    public Object saveRide(Ride ride) {
        return this.mongoConnectorAdapter.saveRide(ride);
    }

    public Optional<Ride> getRide(String rideId) {
        return this.mongoConnectorAdapter.getRide(rideId);
    }

    public List<Ride> getOngoingRides() {
        return this.mongoConnectorAdapter.getOngoingRides();
    }

}
