package hexagonal.ports.GUI;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import hexagonal.adapters.vertx.EScooterManServer;
import hexagonal.businessLogic.ILogic;
import hexagonal.businessLogic.Entities.EScooter;
import hexagonal.businessLogic.Entities.Ride;
import hexagonal.businessLogic.Entities.User;
import hexagonal.ports.IPort;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class GUIPort implements IPort{

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

	public void registerNewUser(String id, String name, String surname) throws UserIdAlreadyExistingException {
        this.logic.addNewUser(id, name, surname);
		// Optional<User> user = domainLayer.getUser(id);
		// if (user.isEmpty()) {
		// 	domainLayer.addNewUser(id, name, surname);
		// } else {
		// 	throw new UserIdAlreadyExistingException();
		// }
	}

	public JsonObject getUserInfo(String id) throws UserNotFoundException  {
		Optional<User> user = this.logic.getUser(id);
		if (user.isPresent()) {
			return user.get().toJson();
		} else {
			throw new UserNotFoundException();
		}
	}

	
	public void registerNewEScooter(String id) throws UserIdAlreadyExistingException {
		this.logic.addNewEScooter(id);
	}

	public JsonObject getEScooterInfo(String id) throws EScooterNotFoundException  {
		Optional<EScooter> escooter = this.logic.getEScooter(id);
		if (escooter.isPresent()) {
			return escooter.get().toJson();
		} else {
			throw new EScooterNotFoundException();
		}
	}

	public String startNewRide(String userId, String escooterId) throws RideNotPossibleException {
		Optional<User> user = this.logic.getUser(userId);
		Optional<EScooter> escooter = this.logic.getEScooter(escooterId); 
		if (user.isPresent() && escooter.isPresent()) {
			EScooter sc = escooter.get();
			if (sc.isAvailable()) {
				return this.logic.startNewRide(UUID.randomUUID().toString(), user.get(), escooter.get());
			} else {
				throw new RideNotPossibleException();
			}
		} else {
			throw new RideNotPossibleException();
		}
	}
	
	public JsonObject getRideInfo(String id) throws RideNotFoundException  {
		Optional<Ride> ride = this.logic.getRide(id);
		if (ride.isPresent()) {
			return ride.get().toJson();
		} else {
			throw new RideNotFoundException();
		}
	}

	public void endRide(String rideId) throws RideNotFoundException, RideAlreadyEndedException {
		Optional<Ride> ride = this.logic.getRide(rideId);
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

	// public void registerNewUser(RoutingContext context) {
	// 	logger.log(Level.INFO, "New registration user request - " + context.currentRoute().getPath());
	// 	JsonObject userInfo = context.body().asJsonObject();
	// 	logger.log(Level.INFO, "Body: " + userInfo.encodePrettily());
		
	// 	String id = userInfo.getString("id");
	// 	String name = userInfo.getString("name");
	// 	String surname = userInfo.getString("surname");
		
	// 	JsonObject reply = new JsonObject();
	// 	try {
	// 		serviceLayer.registerNewUser(id, name, surname);
	// 		reply.put("result", "ok");
	// 	} catch (UserIdAlreadyExistingException ex) {
	// 		reply.put("result", "user-id-already-existing");
	// 	}
	// 	sendReply(context, reply); 	
	// }
	
	// public  void getUserInfo(RoutingContext context) {
	// 	logger.log(Level.INFO, "New user info request: " + context.currentRoute().getPath());
	//     String userId = context.pathParam("userId");
	// 	JsonObject reply = new JsonObject();
	// 	try {
	// 		JsonObject info = serviceLayer.getUserInfo(userId);
	// 		reply.put("result", "ok");
	// 		reply.put("user", info);
	// 	} catch (UserNotFoundException ex) {
	// 		reply.put("result", "user-not-found");
	// 	}
	// 	sendReply(context, reply);
	// }

	// public  void registerNewEScooter(RoutingContext context) {
	// 	logger.log(Level.INFO, "new EScooter registration request: " + context.currentRoute().getPath());
	// 	JsonObject escooterInfo = context.body().asJsonObject();
	// 	logger.log(Level.INFO, "Body: " + escooterInfo.encodePrettily());
		
	// 	String id = escooterInfo.getString("id");
		
	// 	JsonObject reply = new JsonObject();
	// 	try {
	// 		serviceLayer.registerNewEScooter(id);
	// 		reply.put("result", "ok");
	// 	} catch (UserIdAlreadyExistingException ex) {
	// 		reply.put("result", "escooter-id-already-existing");
	// 	}
	// 	sendReply(context, reply);
	// }

	// public void getEScooterInfo(RoutingContext context) {
	// 	logger.log(Level.INFO, "New escooter info request: " + context.currentRoute().getPath());
	//     String escooterId = context.pathParam("escooterId");
	// 	JsonObject reply = new JsonObject();
	// 	try {
	// 		JsonObject info = serviceLayer.getEScooterInfo(escooterId);
	// 		reply.put("result", "ok");
	// 		reply.put("escooter", info);
	// 	} catch (EScooterNotFoundException ex) {
	// 		reply.put("result", "escooter-not-found");
	// 	}
	// 	sendReply(context, reply);
	// }
	
	// public  void startNewRide(RoutingContext context) {
	// 	logger.log(Level.INFO, "Start new ride request: " + context.currentRoute().getPath());
	// 	JsonObject rideInfo = context.body().asJsonObject();
	// 	logger.log(Level.INFO, "Body: " + rideInfo.encodePrettily());
		
	// 	String userId = rideInfo.getString("userId");
	// 	String escooterId = rideInfo.getString("escooterId");
		
	// 	JsonObject reply = new JsonObject();
	// 	try {
	// 		String rideId = serviceLayer.startNewRide(userId, escooterId);
	// 		reply.put("result", "ok");
	// 		reply.put("rideId", rideId);
	// 	} catch (Exception  ex) {
	// 		reply.put("result", "start-new-ride-failed");
	// 	}
	// 	sendReply(context, reply);
	// }
	
	// public  void getRideInfo(RoutingContext context) {
	// 	logger.log(Level.FINE, "New ride info request: " + context.currentRoute().getPath());
	//     String rideId = context.pathParam("rideId");
	// 	JsonObject reply = new JsonObject();
	// 	try {
	// 		JsonObject info = serviceLayer.getRideInfo(rideId);
	// 		reply.put("result", "ok");
	// 		reply.put("ride", info);
	// 	} catch (RideNotFoundException ex) {
	// 		reply.put("result", "ride-not-found");
	// 	}
	// 	sendReply(context, reply);
	// }

	// public  void endRide(RoutingContext context) {
	// 	logger.log(Level.INFO, "End ride request: " + context.currentRoute().getPath());
	//     String rideId = context.pathParam("rideId");
	// 	JsonObject reply = new JsonObject();
	// 	try {
	// 		serviceLayer.endRide(rideId);
	// 		reply.put("result", "ok");
	// 	} catch (RideNotFoundException ex) {
	// 		reply.put("result", "ride-not-found");
	// 	} catch (RideAlreadyEndedException ex) {
	// 		reply.put("result", "ride-already-ended");
	// 	}
	// 	sendReply(context, reply);
	// }
	
	// private void sendReply(RoutingContext request, JsonObject reply) {
	// 	HttpServerResponse response = request.response();
	// 	response.putHeader("content-type", "application/json");
	// 	response.end(reply.toString());
	// }
    
}
