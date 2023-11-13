package hexagonal.businessLogic;

import java.util.Optional;

import hexagonal.businessLogic.Entities.EScooter;
import hexagonal.businessLogic.Entities.Ride;
import hexagonal.businessLogic.Entities.User;
import hexagonal.businessLogic.exceptions.EScooterNotFoundException;
import hexagonal.businessLogic.exceptions.RideAlreadyEndedException;
import hexagonal.businessLogic.exceptions.RideNotFoundException;
import hexagonal.businessLogic.exceptions.RideNotPossibleException;
import hexagonal.businessLogic.exceptions.UserIdAlreadyExistingException;
import hexagonal.businessLogic.exceptions.UserNotFoundException;

public interface ILogic {
   	void addNewUser(String id, String name, String surname) throws UserIdAlreadyExistingException;
	Optional<User> getUser(String userId) throws UserNotFoundException;

	void addNewEScooter(String id);
	Optional<EScooter> getEScooter(String id) throws EScooterNotFoundException;
	
	String startNewRide(String id, String userId, String escooterId) throws RideNotPossibleException;
	Optional<Ride> getRide(String rideId) throws RideNotFoundException; 
	void endRide(String rideId) throws RideNotFoundException, RideAlreadyEndedException;
}
