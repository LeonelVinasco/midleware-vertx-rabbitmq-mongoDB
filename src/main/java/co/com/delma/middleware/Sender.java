package co.com.delma.middleware;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/*En esta clase ya funciona el envío de mensajes a RabbitMQ
 * sin utilizar vertx
 */

public class Sender extends AbstractVerticle {
	private final static String QUEUE_NAME = "Pila";
    private int count = 1;

  // Convenience method so you can run it in your IDE
  public static void main(String[] argv) throws Exception {
	  Launcher.main(new String[]{"run", Sender.class.getName(), "-Sender"});
	 
      
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
     // 
    
      
	  vertx.setPeriodic(200, id -> { //mandar dato cada 200ms
	 	  
		 
		 
			// This handler will be call
		    client.start(r -> {
		    	count++;
		    	client.queueDeclare(QUEUE_NAME, true, false, false, null);
		    	JsonObject message = new JsonObject().put("body", "Hello RabbitMQ "+count + ", from Vert.x !");
		    	client.basicPublish("",  QUEUE_NAME, message, pubResult -> {
		    	  if (pubResult.succeeded()) {
		    	    System.out.println("Message published !"+ message+"my.queue");
		    	  } else {
		    	    pubResult.cause().printStackTrace();
		    	  }
		    	});
		    	
		    });
		    
		   
		  
		  

		  
			/*
			 * ConnectionFactory factory = new ConnectionFactory();
			 * //factory.setUsername("test"); //factory.setPassword("test");
			 * factory.setHost("localhost"); try (Connection connection =
			 * factory.newConnection(); Channel channel = connection.createChannel()) {
			 * channel.queueDeclare(QUEUE_NAME, false, false, false, null); String message =
			 * "Hello World!"; channel.basicPublish("", QUEUE_NAME, null,
			 * message.getBytes("UTF-8")); System.out.println(" [x] Sent '" + message +
			 * "'"); //channel.close(); // connection.close();
			 * //System.out.println(" Conexión cerrada"); } catch (IOException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } catch (TimeoutException e)
			 * { // TODO Auto-generated catch block e.printStackTrace(); }
			 */
		  
		  
		  
		  
		  
	  });
	  
	  
	  
	  
	  
  }
  
  
  
}