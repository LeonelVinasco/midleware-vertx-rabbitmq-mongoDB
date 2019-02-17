package co.com.delma.middleware;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;


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
	  
	  ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");
      try (Connection connection = factory.newConnection();
           Channel channel = connection.createChannel()) {
          channel.queueDeclare(QUEUE_NAME, false, false, false, null);
          String message = "Hello World!";
          channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
          System.out.println(" [x] Sent '" + message + "'");
          //channel.close();
         // connection.close();
          //System.out.println(" Conexión cerrada");
      }
	  
	 
  }

  
  
  
  
}