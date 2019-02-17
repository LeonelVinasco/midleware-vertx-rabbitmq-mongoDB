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
	private final static String QUEUE_NAME = "Pila2";
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
		

		RabbitMQClient client = RabbitMQClient.create(vertx, config);

		client.start(v-> {
			vertx.setPeriodic(100, id ->{
				client.basicGet(QUEUE_NAME, true, getResult ->{
					if (getResult.succeeded()) {
						JsonObject msg =getResult.result();
						String response = msg.getString("body");
						System.out.println("Se obtuvo el mensaje: "+response);
					}else {
						getResult.cause().printStackTrace();
					}
					
				});
				
				
				
				
			});
			
			
		});
		
		
        	
     
		

		
		
    	
    	
    }
    
    
    
}
    

	
   