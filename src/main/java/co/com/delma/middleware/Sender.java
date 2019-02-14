package co.com.delma.middleware;

import io.vertx.amqpbridge.AmqpBridge;
import io.vertx.amqpbridge.AmqpConstants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import co.com.delma.middleware.Runner;

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
      }
	  
	  Runner.runExample(Sender.class);
  }

  @Override
  public void start() throws Exception {
    AmqpBridge bridge = AmqpBridge.create(vertx);

    // Start the bridge, then use the event loop thread to process things thereafter.
    bridge.start("localhost", 5672, res -> {
      if(!res.succeeded()) {
        System.out.println("Bridge startup failed: " + res.cause());
        return;
      }

      // Set up a producer using the bridge, send a message with it.
      MessageProducer<JsonObject> producer = bridge.createProducer("myAmqpAddress");

      // Schedule sending of a message every second
      System.out.println("Producer created, scheduling sends.");
      vertx.setPeriodic(1000, v -> {
        JsonObject amqpMsgPayload = new JsonObject();
        amqpMsgPayload.put(AmqpConstants.BODY, "myStringContent" + count);

        producer.send(amqpMsgPayload);

        System.out.println("Sent message: " + count++);
      });
    });
  }


}