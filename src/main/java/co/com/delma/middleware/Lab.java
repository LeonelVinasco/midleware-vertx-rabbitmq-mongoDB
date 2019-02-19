package co.com.delma.middleware;

import io.vertx.amqpbridge.AmqpConstants;
import io.vertx.core.Launcher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;


/*Esta clase usa la librería RabbitMQClient. aún no funciona por
 * que falta hacer correctamente la parte de client.start(EventHandler). 
 * Aprender sobre EventHandler Handler<AsyncResult<Void>>
 *  y funciones Lambda
 */


public class Lab extends AbstractVerticle{
	private final static String QUEUE_NAME = "Pila";
	private static final String QUEUE = "hello";
 
    
    public static void main(String[] argv) {
  	 
    	
    	
    	Launcher.main(new String[]{"run", Lab.class.getName(),"-middleware"});
    	
    	
	}
    
    @Override
    public void start(Future<Void> fut) throws Exception{
    	
    	String QUEUE_NAME = "Pila";

        // PARAMETRE DE CONNEXION RABBITMQ
        RabbitMQOptions config = new RabbitMQOptions();
        // Each parameter is optional
        // The default parameter with be used if the parameter is not set
        config.setUser("test");
        config.setPassword("test");
        config.setHost("192.168.1.40"); // adresse IP du cluster rabbitMQ
        config.setPort(5672);
        config.setConnectionTimeout(6000); // in milliseconds
        config.setRequestedHeartbeat(60); // in seconds
        config.setHandshakeTimeout(6000); // in milliseconds
        config.setRequestedChannelMax(5);
        config.setNetworkRecoveryInterval(500); // in milliseconds
        config.setAutomaticRecoveryEnabled(true);
        RabbitMQClient client = RabbitMQClient.create(vertx, config);

        //vertx.createHttpServer().requestHandler(req -> {
            client.start(v-> {
                vertx.setPeriodic(100, id -> { // Toutes les 100 miliisecondes
                    client.basicGet(QUEUE_NAME, true, getResult -> {
                        if (getResult.succeeded()) {
                            JsonObject msg = getResult.result();
                            String response = msg.getString("body");
                            System.out.println("Got Message : "+ response);
                           // req.response().end(response);
                        } else {
                            getResult.cause().printStackTrace();
                        }
                    });
                });
            });
       // }).listen(8080);
    	
    }
  	  
  	  
  	  
    }