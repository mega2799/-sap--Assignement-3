package hexagonal.ports.persistence;

import hexagonal.businessLogic.ILogic;
import hexagonal.ports.IPort;

public class PersistancePort implements IPort {
    private ILogic logic;


    public PersistancePort(ILogic escooter) {
        this.logic = escooter;
    }


    @Override
    public void start() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }
    
}
