package hexagonal.adapters.vertxdashboard;

import java.util.logging.Level;
import java.util.logging.Logger;

import hexagonal.ports.GUI.GUIPort;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;

public class EScooterDashboard extends AbstractVerticle {
    private int numericPort;
    private GUIPort port;
    static Logger logger = Logger.getLogger("[EScooter DashBoard]");

    public EScooterDashboard(int port, GUIPort guiPort) {
        this.numericPort = port;
        this.port = guiPort;
        logger.setLevel(Level.INFO);
    }

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();

        server.requestHandler(this::handleRequest);

        server.listen(numericPort, result -> {
            if (result.succeeded()) {
                System.out.println("Web page dashboard started on port " + numericPort);
            } else {
                System.err.println("Web page dashboard failed to start");
            }
        });
    }

    private void handleRequest(HttpServerRequest request) {
        int rides = this.port.getOnGoingRides().size() - 1;
        request.response()
                .putHeader("content-type", "text/html")
                // .end("<h1>Hello, Vert.x!</h1>");
                .end("""
                                            <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Dashboard EScooter</title>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    text-align: center;
                                    margin: 50px;
                                }
                                h1 {
                                    color: #333;
                                }
                                #counter {
                                    font-size: 2em;
                                    color: #1E90FF; /* Colore blu dodger */
                                }
                                button {
                                    padding: 10px 20px;
                                    font-size: 1em;
                                    margin: 10px;
                                    cursor: pointer;
                                }
                            </style>
                        </head>
                        <body>
                            <h1>Contatore Corse attuali</h1>
                            <div id="counter"> """ + rides + """
                                </div>
                            <script>
                                // Funzioni JavaScript per incrementare e decrementare il contatore
                                var counter = 0;

                                function updateCounter() {
                                    document.getElementById('counter').innerHTML = counter;
                                }

                                function incrementCounter() {
                                    counter++;
                                    updateCounter();
                                }

                                function decrementCounter() {
                                    counter--;
                                    updateCounter();
                                }
                            </script>
                        </body>
                        </html>

                                            """);
    }
}
