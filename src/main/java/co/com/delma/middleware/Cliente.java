
package co.com.delma.middleware;


import io.netty.handler.codec.http.HttpResponse;
import io.vertx.amqpbridge.AmqpConstants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
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
    private static JsonObject jas=null;
    
    public static void main(String[] argv) {
		Launcher.main(new String[]{"run", Cliente.class.getName(),"-middleware"});
		
	}
    
    @Override
    public void start(Future<Void> fut) throws Exception{
  	 
    	 RabbitMQOptions config = new RabbitMQOptions();
	        // Each parameter is optional
	        // The default parameter with be used if the parameter is not set
	        //config.setUser("test");
	        //config.setPassword("test");
	        config.setHost("localhost"); // adresse IP du cluster rabbitMQ
	        config.setPort(5672);
	       
	        RabbitMQClient client = RabbitMQClient.create(vertx, config);
	        
  
  	 	  
	        vertx.createHttpServer().requestHandler(req -> {
  		    client.start(r -> {
  			  vertx.setPeriodic(2000, id -> { //mandar dato cada 200ms
  		    	client.basicGet(QUEUE_NAME, true, getResult -> {
  		    	  if (getResult.succeeded()) {
  		    	    JsonObject msg = getResult.result();
  		    	  jas = msg;
    		    	
  		    	    System.out.println("Got message: " + msg.getString("body"));
  		    	  } else {
  		    	    getResult.cause().printStackTrace();
  		    	  }
  		    	});
  		  
  		    });
  		  

  	  });
  		  req.response().end("<h1>Hello from my first "  + jas.getString("body")+
  	              "Vert.x 3 application</h1>");
	        }).listen(8080);
  /*	  
  	Vertx vertx = Vertx.vertx();
    System.out.println("===================Test start===================");
    HttpClient client1 = vertx.createHttpClient();
//    client1.get(8080, "localhost", "/api/prod/dfsad", response -> {
//        System.out.println("Received response with status code " + response.statusCode());
//    }).end();
  	
    client1.post(8080, "localhost", "/images", response -> {
        System.out.println("ahí vamos " + response.statusCode());
    }).end();
 /*/
   }
  	  
    
    
  	  
    }
    
    
    
  
