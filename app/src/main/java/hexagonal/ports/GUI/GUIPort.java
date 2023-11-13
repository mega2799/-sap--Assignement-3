package hexagonal.ports.GUI;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import hexagonal.adapters.vertx.EScooterManServer;
import hexagonal.adapters.vertxdashboard.EScooterDashboard;
import hexagonal.businessLogic.ILogic;
import hexagonal.businessLogic.Entities.Ride;
import hexagonal.businessLogic.exceptions.EScooterNotFoundException;
import hexagonal.businessLogic.exceptions.RideAlreadyEndedException;
import hexagonal.businessLogic.exceptions.RideNotFoundException;
import hexagonal.businessLogic.exceptions.RideNotPossibleException;
import hexagonal.businessLogic.exceptions.UserIdAlreadyExistingException;
import hexagonal.businessLogic.exceptions.UserNotFoundException;
import hexagonal.ports.IPort;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class GUIPort implements IPort{
	/**
	 * Il fatto che la porta utilizzi le eccezioni della business
	 * Logic e' corretto ?
	 */
	private int serverPort;
	private int dashboardPort;
    private ILogic logic;
    static Logger logger = Logger.getLogger("[EScooter Server]");	


    public GUIPort(int serverPort, int dashboardPort,  ILogic logic) {
		this.dashboardPort = dashboardPort;
        this.serverPort = serverPort;
        this.logic = logic;
		logger.setLevel(Level.INFO);
    }


    @Override
    public void start() {
    	Vertx vertx = Vertx.vertx();
		//Vertx per il web server
		EScooterManServer myVerticle = new EScooterManServer(serverPort, this);
		vertx.deployVerticle(myVerticle);		
		//Vertx per la dashboard
		EScooterDashboard dashboardVerticle = new EScooterDashboard(dashboardPort, this);
		vertx.deployVerticle(dashboardVerticle);
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


	public JsonArray getOnGoingRides() {
		return JsonArray.of(this.logic.getOngoingRides());
	}
}
