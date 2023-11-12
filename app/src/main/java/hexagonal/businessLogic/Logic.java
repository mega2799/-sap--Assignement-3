package hexagonal.businessLogic;

import java.util.Optional;

import hexagonal.adapters.mongoDB.MongoConnectorAdapter;
import hexagonal.businessLogic.Entities.EScooter;
import hexagonal.businessLogic.Entities.Ride;
import hexagonal.businessLogic.Entities.User;
import hexagonal.ports.IPort;

public class Logic implements ILogic {
    private MongoConnectorAdapter persitencePort;
    
    public Logic() {
        this.persitencePort = new MongoConnectorAdapter("Escooter");
    }

    @Override
    public void addNewUser(String id, String name, String surname) {
        this.persitencePort.saveUser(new User(id, name, surname));
    }

    @Override
    public Optional<User> getUser(String userId) {
        return this.persitencePort.getUser(userId);
    }

    @Override
    public void addNewEScooter(String id) {
        this.persitencePort.saveEScooter(new EScooter(id));
    }

    @Override
    public Optional<EScooter> getEScooter(String id) {
        return this.persitencePort.getEScooter(id);
    }

    @Override
    public String startNewRide(String id, User user, EScooter escooter) {
        // qui non si dovrebbe ritornare nulla, per ora torna una stringa
        return this.persitencePort.saveRide(new Ride(id, user, escooter)).toString();
    }

    @Override
    public Optional<Ride> getRide(String rideId) {
        return this.persitencePort.getRide(rideId);
    }
    
}
