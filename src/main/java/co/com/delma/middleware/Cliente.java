package co.com.delma.middleware;


import io.vertx.amqpbridge.AmqpConstants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/*Esta clase usa la librería RabbitMQClient. aún no funciona por
 * que falta hacer correctamente la parte de client.start(EventHandler). 
 * Aprender sobre EventHandler Handler<AsyncResult<Void>>
 *  y funciones Lambda
 */


public class Cliente extends AbstractVerticle{
	private final static String QUEUE_NAME = "Pila";
	private static final String QUEUE = "hello";
	private static String TAG = Cliente.class.getSimpleName();
    private static RabbitMQClient client;
    
    
    public static void main(String[] argv) {
		Launcher.main(new String[]{"run", Cliente.class.getName(),"-middleware"});
		
	}
    
    @Override
    public void start() throws Exception{
  	 
    	 RabbitMQOptions config = new RabbitMQOptions();
	        // Each parameter is optional
	        // The default parameter with be used if the parameter is not set
	        //config.setUser("test");
	        //config.setPassword("test");
	        config.setHost("localhost"); // adresse IP du cluster rabbitMQ
	        config.setPort(5672);
	       
	        RabbitMQClient client = RabbitMQClient.create(vertx, config);
	        
  	  vertx.setPeriodic(2000, id -> { //mandar dato cada 200ms
  	 	  
  		 
  		    client.start(r -> {
  		    	client.basicGet(QUEUE_NAME, true, getResult -> {
  		    	  if (getResult.succeeded()) {
  		    	    JsonObject msg = getResult.result();
  		    	    System.out.println("Got message: " + msg.getString("body"));
  		    	  } else {
  		    	    getResult.cause().printStackTrace();
  		    	  }
  		    	});
  		    	
  		    });
  		  

  	  });
  	  
  	  
  	  
  	  
  	  
    }
    
    
    
  }