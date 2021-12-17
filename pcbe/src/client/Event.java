package client;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import configurare.ConfigurationDetails;
import configurare.JsonReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class Event implements AutoCloseable 
{
	private static ConnectionFactory  factory = new ConnectionFactory();
	private static Connection  conexiune;
    private static Channel canal;
    private static final JsonReader json=new JsonReader();
	private static final ConfigurationDetails configDetails=json.getConfigurare();
    private String act;
    private String[] stamps=null;
    LinkedList<String> list=new LinkedList<String>();

    public Event(String actDorit)  
    {
    	act=actDorit;
    	list.addAll(configDetails.getTrasee().get(act));
    	stamps=new String[configDetails.getTrasee().get(act).size()];
    	System.out.println(list);
        factory.setHost("localhost");
        try {
			conexiune = factory.newConnection();
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
        try {
			canal = conexiune.createChannel();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public String cerereStampila(String mesaj,String coadaCerere) throws IOException, InterruptedException 
    {
    	//Determinare ID unic
    	final String Id = UUID.randomUUID().toString();
    	String coadaRaspuns = canal.queueDeclare().getQueue();
	    AMQP.BasicProperties proprietati = new AMQP.BasicProperties
	             .Builder()
	             .correlationId(Id)
	             .replyTo(coadaRaspuns)
	             .build();
	    //Publocare mesaj
        canal.basicPublish("", coadaCerere, proprietati, mesaj.getBytes("UTF-8"));
        
        final BlockingQueue<String> raspuns = new ArrayBlockingQueue<>(1);
        //Consumare raspuns de la birou
        String ctag = canal.basicConsume(coadaRaspuns, true, (consumerTag, delivery) -> 
        {
          if (delivery.getProperties().getCorrelationId().equals(Id)) {
              raspuns.offer(new String(delivery.getBody(), "UTF-8"));
          }
        }, consumerTag -> {
        });
        //Extragere mesaj de raspuns
        String stampila = raspuns.take();
        canal.basicCancel(ctag);
        return stampila;
     }

    public void close() throws IOException {
        conexiune.close();
    }
    private static void parcurgereTraseu(Event event) throws IOException, InterruptedException  {
    	for(int i=0;i<event.list.size();i++)
        {
     	   System.out.println(" [Cerere]  Cerere stampilate de la Birou "+event.list.get(i));
     	   event.stamps[i]=event.cerereStampila("Cerere", event.list.get(i));
        }
    }
    private static void afisareListaStampile(Event event) {
    	 System.out.println("Stampilele:");
         for (int i=0;i<event.stamps.length;i++)
         {
      	   System.out.println(event.stamps[i]);
         }
		
	}

    public static void main(String[] argv) throws IOException, InterruptedException 
    {
       Event event = new Event(argv[0]);
       parcurgereTraseu(event);
       afisareListaStampile(event);
       event.close();
    }
}