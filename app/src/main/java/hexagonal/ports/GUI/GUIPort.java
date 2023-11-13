package hexagonal.ports.GUI;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import hexagonal.adapters.vertx.EScooterManServer;
import hexagonal.businessLogic.ILogic;
import hexagonal.businessLogic.exceptions.EScooterNotFoundException;
import hexagonal.businessLogic.exceptions.RideAlreadyEndedException;
import hexagonal.businessLogic.exceptions.RideNotFoundException;
import hexagonal.businessLogic.exceptions.RideNotPossibleException;
import hexagonal.businessLogic.exceptions.UserIdAlreadyExistingException;
import hexagonal.businessLogic.exceptions.UserNotFoundException;
import hexagonal.ports.IPort;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class GUIPort implements IPort{
	/**
	 * Il fatto che la porta utilizzi le eccezzioni della business
	 * Logic e' corretto ?
	 */
	private int port;
    private ILogic logic;
    static Logger logger = Logger.getLogger("[EScooter Server]");	


    public GUIPort(int port, ILogic logic) {
        this.port = port;
        this.logic = logic;
		logger.setLevel(Level.INFO);
    }


    @Override
    public void start() {
    	Vertx vertx = Vertx.vertx();
		EScooterManServer myVerticle = new EScooterManServer(port, this);
		vertx.deployVerticle(myVerticle);		
    }

	public void registerNewUser(String id, String name, String surname) {
        try {
			this.logic.addNewUser(id, name, surname);
		} catch (UserIdAlreadyExistingException e) {
			e.printStackTrace();
		}
	}

	public JsonObject getUserInfo(String id) {
		try {
			return this.logic.getUser(id).get().toJson();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return JsonObject.of();
		}
	}

	
	public void registerNewEScooter(String id) {
		this.logic.addNewEScooter(id);
	}

	public JsonObject getEScooterInfo(String id) {
		try {
			return this.logic.getEScooter(id).get().toJson();
		} catch (EScooterNotFoundException e) {
			e.printStackTrace();
			return JsonObject.of();
		}
	}

	public String startNewRide(String userId, String escooterId) {
		try {
			return this.logic.startNewRide(UUID.randomUUID().toString(), userId, escooterId);
		} catch (RideNotPossibleException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public JsonObject getRideInfo(String id) {
		try {
			return this.logic.getRide(id).get().toJson();
		} catch (RideNotFoundException e) {
			e.printStackTrace();
			return JsonObject.of();
		}
	}

	public void endRide(String rideId) {
		try {
			this.logic.endRide(rideId);
		} catch (RideNotFoundException | RideAlreadyEndedException e) {
			e.printStackTrace();
		}
	}
}
