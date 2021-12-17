package main;

import com.rabbitmq.client.ConnectionFactory;

import birou.Office;

public class MainApp {
	

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        int deskNumber =Integer.parseInt(args[1]);
        int numberOfEmp =Integer.parseInt(args[2]);
		Office server1 = new Office(args[0],deskNumber,numberOfEmp);
		server1.solveRequest(factory);

	}

}
