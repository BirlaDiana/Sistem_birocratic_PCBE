package birou;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

public class ThreadClass extends Thread{
	Semaphore sem;
    Stamp stamp;
    Channel channel;
    Delivery delivery;
    BasicProperties replyProps;
	
    public ThreadClass(Stamp stamp, Semaphore sem, Channel channel, Delivery delivery, BasicProperties replyProps) 
    {
		this.sem=sem;
		this.stamp=stamp;
		this.channel=channel;
		this.delivery=delivery;
		this.replyProps=replyProps;
	}
    @Override
    public void run()
    {
    	String stampName="";
        try 
        {
        	System.out.println(Thread.currentThread() + "Clientul asteapta la coada");
            sem.acquire();
            System.out.println(Thread.currentThread() + "Clientul a fost primit la ghiseu");
            // Accesare resursa comuna 
            // Daca nu are permisiune,thread-ul va astepta pana alt thread va elibera un loc
            stampName=stamp.stamping();
         } 
         catch (InterruptedException exc) 
         {
        	 System.out.println(exc);
         }
          // Trimitere raspuns si eliberare loc la ghiseu
         System.out.println(Thread.currentThread() + " Clientul paraseste ghiseul");
         try {
        	 channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, stampName.getBytes("UTF-8"));
		}
         catch (IOException e) {
        	 e.printStackTrace();
		}
        sem.release();
    }
 }