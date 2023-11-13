package hexagonal.businessLogic;

import hexagonal.ports.GUI.GUIPort;

public class Main {
    public static void main(String[] args) {
        /**
         * Il fatto che la classe GUIport implementi la Logic e' una dependency injection
         * che possiamo tenere ?
         */
        ILogic escooter = new Logic();
        new GUIPort(8070, 80, escooter).start();;
    }
}
