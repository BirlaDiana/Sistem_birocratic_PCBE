package birou;
import java.util.concurrent.Semaphore;
import com.rabbitmq.client.*;

public class Office {
	
	//Numele cozii din care care consuma mesaje
	private final String queueName;
	
	//Numarul biroului
	private int deskNumber;
	
	//Numarul de angajati din birou(egal cu numarul de ghisee)
	private int numberOfEmp;
	
	//Stampila biroului
	private final Stamp stamp;
	
	//Primitiva semaphore
	private Semaphore sem ;
	
	public Office(String queueName, int deskNumber, int numberOfEmp ) {

		this.queueName = queueName;
		this.deskNumber = deskNumber;
		this.numberOfEmp=numberOfEmp;
		stamp=new Stamp(deskNumber);
		sem=new Semaphore(this.numberOfEmp);
	}

	public void solveRequest(ConnectionFactory factory) throws Exception {
		//Creare canal
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		//Declarare coada
		channel.queueDeclare(queueName, true, false, false,null);
		//Curatare content
		channel.queuePurge(queueName);
		channel.basicQos(1);

		System.out.println("[Birou " + deskNumber + "] " + "Asteptare clienti la " + queueName);

				DeliverCallback deliverCallback = (consumerTag, delivery) -> 
				{
					AMQP.BasicProperties replyProps = new AMQP.BasicProperties
	                        .Builder()
	                        .correlationId(delivery.getProperties().getCorrelationId())
	                        .build();	
	                		ThreadClass Thread=new ThreadClass(stamp,sem,channel,delivery,replyProps);
	                		Thread.start();
				};
		channel.basicConsume(queueName, true, deliverCallback, (consumerTag -> { }));
		}

	
}





	