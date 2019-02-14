package co.com.delma.middleware;
import com.rabbitmq.client.*;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.*;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import io.vertx.amqpbridge.AmqpBridge;
import io.vertx.amqpbridge.AmqpConstants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.impl.*;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.*;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import java.util.concurrent.TimeoutException;


public class Cliente extends AbstractVerticle{
	private static final String QUEUE = "hello";
	
	private static String TAG = Cliente.class.getSimpleName();
    private static RabbitMQClient client;
    
    
    public static void main(String[] argv) throws java.io.IOException, TimeoutException {
		Vertx vertx = null;
		RabbitMQOptions config = new RabbitMQOptions();
		// Each parameter is optional
		// The default parameter with be used if the parameter is not set
		config.setUser("leo");
		config.setPassword("clave");
		config.setHost("localhost");
		config.setPort(5672);
		config.setVirtualHost("vhost1");
		config.setConnectionTimeout(6000); // in milliseconds
		config.setRequestedHeartbeat(60); // in seconds
		config.setHandshakeTimeout(6000); // in milliseconds
		config.setRequestedChannelMax(5);
		config.setNetworkRecoveryInterval(500); // in milliseconds
		config.setAutomaticRecoveryEnabled(true);

		RabbitMQClient client = RabbitMQClient.create(vertx, config);

		JsonObject message = new JsonObject().put("body", "Hello RabbitMQ, from Vert.x !");
		Handler<AsyncResult<Void>> resultHandler = reply -> {
		    if (reply.succeeded()) {
		        
		      } else {
		        
		      }
		    };
		   
		
		    ConnectionFactory factory = new ConnectionFactory();

	        factory.setHost("localhost");

	        factory.setPort(5672);

	        factory.setUsername("guest");

	        factory.setPassword("guest");

	        

	        

	        Connection conn = factory.newConnection();

	        Channel ch = conn.createChannel();
	
		
	         client.basicPublish("", "my.queue", message, pubResult -> {
	          if (pubResult.succeeded()) {
	            System.out.println("Message published !");
	          } else {
	            pubResult.cause().printStackTrace();
	          }
	        });
		
	}
    
}
	
   