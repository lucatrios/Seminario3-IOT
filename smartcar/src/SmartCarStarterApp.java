import componentes.SmartCar;

public class SmartCarStarterApp {
    public static void main(String[] args) throws Exception {

		if ( args.length < 2 )
		{
			System.out.println("Usage: SmartCarStarterApp <smartCarID> <brokerURL>");
			System.exit(1);
		}

		String smartCarID = args[0];
		String brokerURL = args[1];

        SmartCar sc1 = new SmartCar(smartCarID, brokerURL);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

		sc1.getIntoRoad("R1s5", 0);  // indicamos que el SmartCar está en tal segmento
//		sc1.notifyIncident("");
		sc1.notifyRoad("VEHICLE_IN");
    }
}
