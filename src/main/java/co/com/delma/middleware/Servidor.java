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

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;


/*En esta clase solo se implementa un servidor HTTP
 * 
 */


public class Servidor extends AbstractVerticle{
	private final static String QUEUE_NAME = "Pila";
	private static final String QUEUE = "hello";
 
    
    public static void main(String[] argv) {
  	 
    	
    	
    	Launcher.main(new String[]{"run", Servidor.class.getName(),"-middleware"});
    	
    	
	}
    
    @Override
    public void start(Future<Void> fut) {
    	
    	
    	vertx
        .createHttpServer()
        .requestHandler(r -> {
          r.response().end("<h1>Hello from my first " +
              "Vert.x 3 application</h1>");
          
        })
        .listen(8080, result -> {
          if (result.succeeded()) {
            fut.complete();
          } else {
            fut.fail(result.cause());
          }
        });
      
    
    	 vertx.setTimer(30000, r->{ 
    		 vertx.close();
    	     System.out.println("Server stoped");
    	     }
    	 );
    	
    }
  	  
  	  
  	  
    }