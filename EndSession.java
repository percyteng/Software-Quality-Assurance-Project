import java.io.File;
import java.io.IOException;

/* The following class ends the current transaction and begins the Back End process */
public class EndSession {
	/*The following ends the current transaction and writes to a new event transaction file */
	public static void logOut(Transaction instance, String answer, String eventTrans) throws IOException{
		if (answer.equals("y")){
			instance.ifLogin = false;
			instance.user = instance.user.NONE;
			//writes a new event transaction file
			BackEnd.dailyEventTransaction(eventTrans);
			Main.start();
		}
		else
			Main.selectExecution(instance, eventTrans);
	}

}
