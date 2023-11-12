package hexagonal.businessLogic;

import hexagonal.ports.IPort;
import hexagonal.ports.GUI.GUIPort;
import hexagonal.ports.persistence.PersistancePort;

public class Main {
    public static void main(String[] args) {
        ILogic escooter = new Logic();
        new GUIPort(8081, escooter).start();;
    }
}
