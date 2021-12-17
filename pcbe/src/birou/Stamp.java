package birou;

public class Stamp {
	private int deskNumber;
	
	public Stamp(int deskNumber) {
		this.deskNumber=deskNumber;
	}
	
	public String stamping() throws InterruptedException {
		
		System.out.println("Se stampileaza");
		for(int i =0;i<=5;i++)
		{
			System.out.println("Prelucrare date..");
			Thread.sleep(1000);
		}
        String stamp="Stamp"+this.deskNumber;
        return stamp;
        
	}

}
