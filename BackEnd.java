import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/*The following class represents all Back End manipulations */

public class BackEnd {
	
	/*The following creates a new event transaction file, it ensures all the files are unique
	 * and numbered
	 */
	public static void dailyEventTransaction(String eventTrans) {
		eventTrans += "00\r\n";
		int i = 1;
		String name = "EventTransaction-" + String.valueOf(i);
		File dailyTrans = new File("/Users/ericbolboa/Desktop/AllFiles/" + name);
		//if it exists, it increments the name
		while (dailyTrans.exists()) {
			i += 1;
			name = "EventTransaction-" + String.valueOf(i);
			dailyTrans = new File("/Users/ericbolboa/Desktop/AllFiles/" + name);
		}
		name = "EventTransaction-" + String.valueOf(i);
		dailyTrans = new File("/Users/ericbolboa/Desktop/AllFiles/" + name);
		try {
			dailyTrans.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(dailyTrans));
			out.write(eventTrans);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* The following creates a merged event transaction file. It reads every event transaction
	 * file line by line, and in order, and writes them to the new merged event transaction file
	 */
	public static void mergedEventTransaction() throws IOException {
		String targ_dir = "/Users/ericbolboa/Desktop/AllFiles";
		File dir = new File(targ_dir);
		File[] files = dir.listFiles();
		String fname = "";
		String content = "";
		
		for (File f : files) {
			fname = f.toString();
			if (fname.matches(".*EventTransaction.*")) {
				BufferedReader inputStream = null;

                try {
                    inputStream = new BufferedReader(
                                    new FileReader(f));
                    String line;
                    //reads every line in every file and writes them to a string
                    while ((line = inputStream.readLine()) != null) {
                        if (!line.matches("00")) {
                        	content += line + "\r\n";
                        }
                    }
                    
                }
                finally {
                	if (inputStream != null) {
                		inputStream.close();
                	}
                }
			}
		}
		content += "00";
		//the string is written to the file
		File merge = new File("MergedEventTransaction.txt");
		merge.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(merge));
		out.write(content);
		out.close();
	}
	
	//updates pre-existing events in the current events file
	public static void CurrentTicketUpdate(String eventName, String ticket) throws IOException {

		FileInputStream fil = new FileInputStream("CurrentEvents.txt");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fil));
		
		String line = null;
		String oldText = "";
		while ((line = bufferedReader.readLine()) != null) {
			if (line.matches("(?i).*"+eventName+".*")) {
				eventName = String.format("%-36s", eventName);
				oldText += eventName + " " + ticket + "\r\n";
			}
			else {
				oldText += line + "\r\n";
			}
		}
		bufferedReader.close();
		FileWriter fileWriter = new FileWriter("CurrentEvents.txt");
		fileWriter.write(oldText);
		fileWriter.close();
	}
	
	//deletes an event from the current events file
	public static void deleteCurrent(String eventName) throws IOException {
		FileInputStream fil = new FileInputStream("CurrentEvents.txt");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fil));
		
		String line = null;
		String oldText = "";
		while ((line = bufferedReader.readLine()) != null) {
			if (line.matches("(?i).*"+eventName+".*")) {
				oldText += "";
			}
			else {
				oldText += line + "\r\n";
			}
			
		}
		
		bufferedReader.close();
		FileWriter fileWriter = new FileWriter(CurrentEvents.txt");
		fileWriter.write(oldText);
		fileWriter.close();
	}
	//adds a new event to the current events file
	public static void addCurrent(String eventName, String tickets) throws IOException {
		eventName = String.format("%-36s", eventName);
		FileWriter fileWriter = new FileWriter("CurrentEvents.txt", true);
		fileWriter.write(eventName + " " + tickets +"\r\n");
		fileWriter.close();
	}
	
	/* Read the merged event transaction and updates the current events file using three methods 
	 * listed above */
	public static void CurrentTicket() throws IOException {
		String targ = "MergedEventTransaction.txt";
		File file = new File(targ);
		String eventName = "";
		String tickets = "";
					BufferedReader inputStream = null;
					try {
	                    inputStream = new BufferedReader(
	                                    new FileReader(file));
	                    String line;

	                    while ((line = inputStream.readLine()) != null) {
	                    	//if event is deleted, the file is updated
	                        if (line.matches("05.*")) {
	                        	eventName = line.substring(3, 40);
	                        	eventName = eventName.replaceAll("\\s+","");
	                        	deleteCurrent(eventName);
	                        }
	                        //if event is created, the file is updated
	                        if (line.matches("03.*")) {
	                    
	                        	eventName = line.substring(3, 40);
	                        	eventName = eventName.replaceAll("\\s+","");
	                        	tickets = line.substring(47);
	                        	tickets = tickets.replaceAll("\\s+","");
	                        	addCurrent(eventName, tickets);
	                        }
	                        //if end of file
	                        else if (line.matches("00")) {
	                        	break;
	                        }
	                        //if action is add, sell, or return, the file is updated
	                        else {
	                        	eventName = line.substring(3, 40);
	                        	eventName = eventName.replaceAll("\\s+","");
	                        	tickets = line.substring(47);
	                        	tickets = tickets.replaceAll("\\s+","");
	                        	CurrentTicketUpdate(eventName, tickets);
	                        }
	                    }
	                    
	                }
	                finally {
	                	if (inputStream != null) {
	                		inputStream.close();
	                	}
	                }
	}
	
	//deletes an event from the master events file
	public static void deleteMaster(String eventName) throws IOException {
		FileInputStream fil = new FileInputStream("MasterEventsFile.txt");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fil));
		String line = null;
		String oldText = "";
		//replaces line that contains event with a blank space
		while ((line = bufferedReader.readLine()) != null) {
			if (line.matches("(?i).*"+eventName+".*")) {
				oldText += "";
			}
			else {
				oldText += line + "\r\n";
			}
			
		}
		
		bufferedReader.close();
		FileWriter fileWriter = new FileWriter("MasterEventsFile.txt");
		fileWriter.write(oldText);
		fileWriter.close();
	}
	
	//The following updates the master events file by adding the new number of tickets
	public static void updateMaster(String eventName, String tickets) throws IOException {
		FileInputStream fil = new FileInputStream("MasterEventsFile.txt");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fil));
		String date = "";
		String line = null;
		String oldText = "";
		while ((line = bufferedReader.readLine()) != null) {
			if (line.toLowerCase().contains(eventName.toLowerCase())) {
				eventName = String.format("%-36s", eventName);
				date = line.substring(0,6);
				oldText += date + " " + tickets + " " + eventName + "\r\n";
			}
			else {
				oldText += line + "\r\n";
			}
			
		}
		
		bufferedReader.close();
		FileWriter fileWriter = new FileWriter("MasterEventsFile.txt");
		fileWriter.write(oldText);
		fileWriter.close();
	}
	
	/* The following adds a new event to the master events file and ensures that the event
	 * is placed in the right order. The events are ordered by date 
	 */
	public static void createMaster(String eventName, String tickets, String date) throws IOException {
		String line;
		String oldText = "";
		eventName = String.format("%-36s", eventName);
		File file = new File("MasterEventsFile.txt");
		//if file is empty, just add the event regardless of order
		if (!file.exists()) {
		    file.createNewFile();
			FileWriter fileWriter = new FileWriter("MasterEventsFile.txt", true);
			fileWriter.write(date + " " + tickets + " " + eventName +"\r\n");
			fileWriter.close();
		}
		//adds event in the correct order
		else {
			FileInputStream fil2 = new FileInputStream("MasterEventsFile.txt");
			BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(fil2));
			String checkDate;
			int originalDate;
			int existingDate;
			int stop = 0;
			while ((line = bufferedReader2.readLine()) != null) {
				checkDate = line.substring(0,6);
				existingDate = Integer.valueOf(checkDate);
				originalDate = Integer.valueOf(date);
				if (existingDate > originalDate && stop != 1) {
					oldText += date + " " + tickets + " " + eventName + "\r\n";
					oldText += line + "\r\n";
					stop = 1;
				}
				else {
					oldText += line + "\r\n";
				}
			}
			if (stop == 0) {
				oldText += date + " " + tickets + " " + eventName + "\r\n";
			}
			bufferedReader2.close();
			FileWriter fileWriter = new FileWriter("MasterEventsFile.txt");
			fileWriter.write(oldText);
			fileWriter.close();
		}
	}
	
	/* Read the merged event transaction and updates the master events file using three methods 
	 * listed above */
	public static void MasterEvents() throws IOException {
		String targ = "MergedEventTransaction.txt";
		File file = new File(targ);
		String eventName = "";
		String tickets = "";
		String date = "";
					BufferedReader inputStream = null;
					try {
	                    inputStream = new BufferedReader(
	                                    new FileReader(file));
	                    String line;

	                    while ((line = inputStream.readLine()) != null) {
	                    	//if event is deleted, the file is updated
	                        if (line.matches("05.*")) {
	                        	eventName = line.substring(3, 40);
	                        	eventName = eventName.replaceAll("\\s+","");
	                        	deleteMaster(eventName);
	                        }
	                        //if event is created, the file is updated
	                        if (line.matches("03.*")) {
	                        	eventName = line.substring(3, 40);
	                        	eventName = eventName.replaceAll("\\s+","");
	                        	tickets = line.substring(47);
	                        	tickets = tickets.replaceAll("\\s+","");
	                        	date = line.substring(40,46);
	                     
	                        	createMaster(eventName, tickets, date);
	                        }
	                        //if end of file
	                        else if (line.matches("00")) {
	                        	break;
	                        }
	                      //if action is add, sell, or return, the file is updated
	                        else {
	                        	eventName = line.substring(3, 40);
	                        	eventName = eventName.replaceAll("\\s+","");
	                        	tickets = line.substring(47);
	                        	tickets = tickets.replaceAll("\\s+","");
	                        	updateMaster(eventName, tickets);
	                        }
	                    }
	                    
	                }
	                finally {
	                	if (inputStream != null) {
	                		inputStream.close();
	                	}
	                }
	}
	
	
	/* The following deletes all event transaction file in the directory */
	public static void deleteAllEvents() {
		String targ_dir = "/Users/ericbolboa/Desktop/AllFiles";
		File dir = new File(targ_dir);
		File[] files = dir.listFiles();
		String fname = "";
		String content = "";
		
		for (File f : files) {
			fname = f.toString();
			if (fname.matches(".*EventTransaction.*")) {
				f.delete();
			}
	}
	}
}

