package co.com.delma.middleware;


import io.vertx.core.AbstractVerticle;
import io.vertx.amqpbridge.AmqpBridge;
import io.vertx.amqpbridge.AmqpConstants;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import co.com.delma.middleware.Runner;
/**
 * Hello world!
 *
 */
public class Receptor extends AbstractVerticle
{
    public static void main( String[] args )
    {
    	Runner.runExample(Receptor.class);
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
     // Set up a consumer using the bridge, register a handler for it.
        MessageConsumer<JsonObject> consumer = bridge.createConsumer("myAmqpAddress");
        consumer.handler(vertxMsg -> {
          JsonObject amqpMsgPayload = vertxMsg.body();
          Object amqpBody = amqpMsgPayload.getValue(AmqpConstants.BODY);

          // Print body of received AMQP message
          System.out.println("Received a message with body: " + amqpBody);
        });
      });
    }
  }