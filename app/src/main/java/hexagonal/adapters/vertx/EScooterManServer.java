package hexagonal.adapters.vertx;

import java.util.logging.Level;
import java.util.logging.Logger;

import hexagonal.businessLogic.exceptions.EScooterNotFoundException;
import hexagonal.businessLogic.exceptions.RideAlreadyEndedException;
import hexagonal.businessLogic.exceptions.RideNotFoundException;
import hexagonal.businessLogic.exceptions.UserIdAlreadyExistingException;
import hexagonal.businessLogic.exceptions.UserNotFoundException;
import hexagonal.ports.GUI.GUIPort;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.*;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class EScooterManServer extends AbstractVerticle {

	private int numericPort;
	private GUIPort port;
    static Logger logger = Logger.getLogger("[EScooter Server]");	

	public EScooterManServer(int port, GUIPort guiPort) {
		this.numericPort = port;
		this.port = guiPort;
		logger.setLevel(Level.INFO);
	}
	
	
	public void start() {
		logger.log(Level.INFO, "EScooterMan server initializing...");
		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);

		/* static files by default searched in "webroot" directory */
		router.route("/static/*").handler(StaticHandler.create().setCachingEnabled(false));
		router.route().handler(BodyHandler.create());
		
		router.route(HttpMethod.POST, "/api/users").handler(event -> {
			try {
				registerNewUser(event);
			} catch (UserIdAlreadyExistingException e) {
				e.printStackTrace();
			}
		});
		router.route(HttpMethod.GET, "/api/users/:userId").handler(event -> {
			try {
				getUserInfo(event);
			} catch (UserNotFoundException e) {
				e.printStackTrace();
			}
		});
		router.route(HttpMethod.POST, "/api/escooters").handler(event -> {
			try {
				registerNewEScooter(event);
			} catch (UserIdAlreadyExistingException e) {
				e.printStackTrace();
			}
		});
		router.route(HttpMethod.GET, "/api/escooters/:escooterId").handler(event -> {
			try {
				getEScooterInfo(event);
			} catch (EScooterNotFoundException e) {
				e.printStackTrace();
			}
		});
		router.route(HttpMethod.POST, "/api/rides").handler(this::startNewRide);
		router.route(HttpMethod.GET, "/api/rides/:rideId").handler(event -> {
			try {
				getRideInfo(event);
			} catch (RideNotFoundException e) {
				e.printStackTrace();
			}
		});
		router.route(HttpMethod.POST, "/api/rides/:rideId/end").handler(event -> {
			try {
				endRide(event);
			} catch (RideNotFoundException | RideAlreadyEndedException e) {
				e.printStackTrace();
			}
		});

		server
		.requestHandler(router)
		.listen(numericPort);
		
		logger.log(Level.INFO, "EScooterMan server ready - port: " + numericPort);
	}
	
	protected void registerNewUser(RoutingContext context) throws UserIdAlreadyExistingException {
		logger.log(Level.INFO, "New registration user request - " + context.currentRoute().getPath());
		JsonObject userInfo = context.body().asJsonObject();
		logger.log(Level.INFO, "Body: " + userInfo.encodePrettily());
		
		String id = userInfo.getString("id");
		String name = userInfo.getString("name");
		String surname = userInfo.getString("surname");
		
		JsonObject reply = new JsonObject();
		this.port.registerNewUser(id, name, surname);
		reply.put("result", "ok");
		sendReply(context, reply); 	
	}
	
	protected void getUserInfo(RoutingContext context) throws UserNotFoundException {
		logger.log(Level.INFO, "New user info request: " + context.currentRoute().getPath());
	    String userId = context.pathParam("userId");
		JsonObject reply = new JsonObject();
		JsonObject info = this.port.getUserInfo(userId);
		reply.put("result", "ok");
		reply.put("user", info);
		sendReply(context, reply);
	}

	protected void registerNewEScooter(RoutingContext context) throws UserIdAlreadyExistingException {
		logger.log(Level.INFO, "new EScooter registration request: " + context.currentRoute().getPath());
		JsonObject escooterInfo = context.body().asJsonObject();
		logger.log(Level.INFO, "Body: " + escooterInfo.encodePrettily());
		
		String id = escooterInfo.getString("id");
		
		JsonObject reply = new JsonObject();
		this.port.registerNewEScooter(id);
		reply.put("result", "ok");
		sendReply(context, reply);
	}

	protected void getEScooterInfo(RoutingContext context) throws EScooterNotFoundException {
		logger.log(Level.INFO, "New escooter info request: " + context.currentRoute().getPath());
	    String escooterId = context.pathParam("escooterId");
		JsonObject reply = new JsonObject();
		JsonObject info = this.port.getEScooterInfo(escooterId);
		reply.put("result", "ok");
		reply.put("escooter", info);
		sendReply(context, reply);
	}
	
	protected void startNewRide(RoutingContext context) {
		logger.log(Level.INFO, "Start new ride request: " + context.currentRoute().getPath());
		JsonObject rideInfo = context.body().asJsonObject();
		logger.log(Level.INFO, "Body: " + rideInfo.encodePrettily());
		
		String userId = rideInfo.getString("userId");
		String escooterId = rideInfo.getString("escooterId");
		
		JsonObject reply = new JsonObject();
		try {
			String rideId = this.port.startNewRide(userId, escooterId);
			reply.put("result", "ok");
			reply.put("rideId", rideId);
		} catch (Exception  ex) {
			reply.put("result", "start-new-ride-failed");
		}
		sendReply(context, reply);
	}
	
	protected void getRideInfo(RoutingContext context) throws RideNotFoundException {
		logger.log(Level.FINE, "New ride info request: " + context.currentRoute().getPath());
	    String rideId = context.pathParam("rideId");
		JsonObject reply = new JsonObject();
		JsonObject info = this.port.getRideInfo(rideId);
		reply.put("result", "ok");
		reply.put("ride", info);
		sendReply(context, reply);
	}

	protected void endRide(RoutingContext context) throws RideNotFoundException, RideAlreadyEndedException {
		logger.log(Level.INFO, "End ride request: " + context.currentRoute().getPath());
	    String rideId = context.pathParam("rideId");
		JsonObject reply = new JsonObject();
		this.port.endRide(rideId);
		reply.put("result", "ok");
		sendReply(context, reply);
	}
	
	private void sendReply(RoutingContext request, JsonObject reply) {
		HttpServerResponse response = request.response();
		response.putHeader("content-type", "application/json");
		response.end(reply.toString());
	}
	
	
}
