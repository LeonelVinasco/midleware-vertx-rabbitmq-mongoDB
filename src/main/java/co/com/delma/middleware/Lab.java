
package co.com.delma.middleware;

import io.netty.handler.codec.http.HttpResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
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

/*En esta clase está implementado RabbitMq y el cliente de MongoDB que escribe los datos
 * Ya falta poco
 */


public class Lab extends AbstractVerticle{
	private final static String QUEUE_NAME = "Pila";
	private static final String QUEUE = "hello";
    private static RabbitMQClient client;
    private static JsonObject jas=null;
    
    public static void main(String[] argv) {
		Launcher.main(new String[]{"run", Lab.class.getName(),"-middleware"});
		
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
	        
	        JsonObject configJson = Vertx.currentContext().config();

	        String uri = configJson.getString("mongo_uri");
	        if (uri == null) {
	          uri = "mongodb://localhost:27017";
	        }
	        String db = configJson.getString("mongo_db");
	        if (db == null) {
	          db = "test";
	        }

	        JsonObject mongoconfig = new JsonObject()
	            .put("connection_string", uri)
	            .put("db_name", db);

	        MongoClient mongoClient = MongoClient.createShared(vertx, mongoconfig);
	        JsonObject product1 = new JsonObject().put("itemId", "12345").put("name", "Cooler").put("price", "100.0");
	        
	        
	        vertx.createHttpServer().requestHandler(req -> {
  		    client.start(r -> {
  			  vertx.setPeriodic(2000, id -> { //mandar dato cada 200ms
  		    	client.basicGet(QUEUE_NAME, true, getResult -> {
  		    	  if (getResult.succeeded()) {
  		    	    JsonObject msg = getResult.result();
  		    	  jas = msg;
    		    	
  		    	    System.out.println("Got message: " + msg.getString("body"));
  		    	    
  		    	  mongoClient.save("products", product1, id2 -> {
  		    	      System.out.println("Inserted id: " + id2.result());

  		    	      mongoClient.find("products", new JsonObject().put("itemId", "12345"), res -> {
  		    	        System.out.println("Name is " + res.result().get(0).getString("name"));

  		    	        mongoClient.removeDocuments("products", new JsonObject().put("itemId", "12345"), rs -> {
  		    	          if (rs.succeeded()) {
  		    	            System.out.println("Product removed ");
  		    	          }
  		    	        });

  		    	      });

  		    	    });
  		    	    
  		    	  } else {
  		    	    getResult.cause().printStackTrace();
  		    	  }
  		    	});
  		  
  		    });
  		  

  	  });
  		  req.response().end("<h1>Hello from my first "  + jas.getString("body")+
  	              "Vert.x 3 application</h1>");
	        }).listen(8080);
 
   }
  	  
    
    
    
    
    
    
    
    
    
  	  
    }
    
    
    
  
