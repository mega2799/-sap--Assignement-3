package hexagonal.businessLogic;

import java.util.List;
import java.util.Optional;

import hexagonal.adapters.mongoDB.MongoConnectorAdapter;
import hexagonal.businessLogic.Entities.EScooter;
import hexagonal.businessLogic.Entities.Ride;
import hexagonal.businessLogic.Entities.User;
import hexagonal.businessLogic.exceptions.EScooterNotFoundException;
import hexagonal.businessLogic.exceptions.RideAlreadyEndedException;
import hexagonal.businessLogic.exceptions.RideNotFoundException;
import hexagonal.businessLogic.exceptions.RideNotPossibleException;
import hexagonal.businessLogic.exceptions.UserIdAlreadyExistingException;
import hexagonal.businessLogic.exceptions.UserNotFoundException;
import hexagonal.ports.IPort;
import hexagonal.ports.persistence.PersistancePort;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Logic implements ILogic {
    private PersistancePort persitencePort;
    
    public Logic() {
        this.persitencePort = new PersistancePort("Escooter");
    }

    @Override
    public void addNewUser(String id, String name, String surname) throws UserIdAlreadyExistingException {
		Optional<User> user = this.persitencePort.getUser(id);
		if (user.isEmpty()) {
            this.persitencePort.saveUser(new User(id, name, surname));
		} else {
			throw new UserIdAlreadyExistingException();
		}
    }

    @Override
    public Optional<User> getUser(String userId) throws UserNotFoundException {
		Optional<User> user = this.persitencePort.getUser(userId);
		if (user.isPresent()) {
			return user;
		} else {
			throw new UserNotFoundException();
		}
    }

    @Override
    public void addNewEScooter(String id) {
        this.persitencePort.saveEScooter(new EScooter(id));
    }

    @Override
    public Optional<EScooter> getEScooter(String id) throws EScooterNotFoundException {
		Optional<EScooter> escooter = this.persitencePort.getEScooter(id);
		if (escooter.isPresent()) {
			return escooter;
		} else {
			throw new EScooterNotFoundException();
		}
    }

    /**
     * Qui ho dovuto modificare la funzione dato che ho portato tutto nella business logic
     * @throws RideNotPossibleException
     */
    @Override
    public String startNewRide(String id, String userId, String escooterId) throws RideNotPossibleException {
		Optional<User> user = this.persitencePort.getUser(userId);
		Optional<EScooter> escooter = this.persitencePort.getEScooter(escooterId); 
		if (user.isPresent() && escooter.isPresent()) {
			EScooter sc = escooter.get();
			if (sc.isAvailable()) {
				this.persitencePort.saveRide(new Ride(id, user.get(), escooter.get()));
                return id;
			} else {
				throw new RideNotPossibleException();
			}
		} else {
			throw new RideNotPossibleException();
		}
    }

    @Override
    public Optional<Ride> getRide(String rideId) throws RideNotFoundException {
		Optional<Ride> ride = this.persitencePort.getRide(rideId);
		if (ride.isPresent()) {
			return ride;
		} else {
			throw new RideNotFoundException();
		}
    }

	public void endRide(String rideId) throws RideNotFoundException, RideAlreadyEndedException {
		Optional<Ride> ride = this.persitencePort.getRide(rideId);
		if (ride.isPresent()) {
			Ride ri = ride.get();
			if (ri.isOngoing()) {
				ri.end();
			} else {
				throw new RideAlreadyEndedException();
			}
		} else {
			throw new RideNotFoundException();
		}
	}

	@Override
	public List<Ride> getOngoingRides() {
		return this.persitencePort.getOngoingRides();
	}
    
}
