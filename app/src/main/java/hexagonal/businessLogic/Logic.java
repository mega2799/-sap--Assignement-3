package hexagonal.businessLogic;

import java.util.Optional;

import hexagonal.businessLogic.Entities.EScooter;
import hexagonal.businessLogic.Entities.Ride;
import hexagonal.businessLogic.Entities.User;
import hexagonal.ports.IPort;

public class Logic implements ILogic {
    private IPort persitencePort;
    private IPort viewPort;
    
    @Override
    public void addNewUser(String id, String name, String surname) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addNewUser'");
    }

    @Override
    public Optional<User> getUser(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
    }

    @Override
    public void addNewEScooter(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addNewEScooter'");
    }

    @Override
    public Optional<EScooter> getEScooter(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEScooter'");
    }

    @Override
    public String startNewRide(User user, EScooter escooter) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startNewRide'");
    }

    @Override
    public Optional<Ride> getRide(String rideId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRide'");
    }
    
}
