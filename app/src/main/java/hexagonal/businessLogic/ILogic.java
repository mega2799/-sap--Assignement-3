package hexagonal.businessLogic;

import java.util.Optional;

import hexagonal.businessLogic.Entities.EScooter;
import hexagonal.businessLogic.Entities.Ride;
import hexagonal.businessLogic.Entities.User;

public interface ILogic {
   	void addNewUser(String id, String name, String surname);
	Optional<User> getUser(String userId);

	void addNewEScooter(String id);
	Optional<EScooter> getEScooter(String id);
	
	String startNewRide(String id, User user, EScooter escooter);
	Optional<Ride> getRide(String rideId); 
}
