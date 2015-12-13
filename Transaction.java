import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

/* The following class represents all the methods needed to carry out the necessary transaction actions
 * such as create, add, sell return, delete 
 */

public class Transaction {
	public boolean ifLogin;
	public enum role{
		ADMIN,SALES,NONE,
	}
	public role user;
	public Transaction() {
		ifLogin = false;
		user = role.NONE;
	}
	

	/* The following checks if an event already exists or not. It checks not only the Current Events
	 * file and the pre-existing Event Transaction files, but it also checks the Event Transaction file
	 * of the current transaction and of the Event Transaction files created in the current day 
	 */
	public static boolean checkEventName(String eventName, String eventTrans) throws IOException {
		eventName = String.format("%-36s", eventName);
		FileInputStream fil;
		String found = "";
	    
		//checks if event was deleted in the current transaction
		if (!Helper.checkEventTransDelete(eventName, eventTrans)) {
			return false;
		}
		//checks if event was deleted in the current day
		else if (!Helper.checkAllFilesDelete(eventName)) {
			return false;
			}
		//checks if event exists in the current transaction
		else if (eventTrans.toLowerCase().contains(eventName.toLowerCase())) {
			return true;
		}
		//checks if event exists in the current day
		else if (!Helper.checkAllFiles(eventName).equals("")) {
			return true;
		}
		//check Current Events File to see if the event exists
		else {
			try {
				fil = new FileInputStream("CurrentEvents.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(fil));
				String line = null;
				while ((line = br.readLine()) != null) {
					if (line.matches("(?i).*"+eventName+".*")) {
						found = eventName;
						break;
					}
				}
				br.close(); 
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				System.err.println("Unable to read the file");
			}
		}
		if (found.isEmpty()) {
			return false;
		}
				
		return true;
	}
	
	//The following checks if the event name is valid 
	public static boolean writeEventName(String eventName, String eventTrans) throws IOException {
		if (!Helper.checkEventExist()) {
			return true;
		}
		else if (checkEventName(eventName, eventTrans)) { 
			System.out.println("This Event Already Exists!");
			return false;
		}
		else if (eventName.length() > 36) {
			System.out.println("Event Name Too Long!");
			return false;
		}
		else {
			return true;
		}
	}
	//The following checks if the date provided is valid
	public static boolean setDate(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date today = new Date();
		String tod = dateFormat.format(today);
		StringBuilder sb = new StringBuilder(tod);
		sb.deleteCharAt(0);
		sb.deleteCharAt(0);
		String curr = sb.substring(0, Math.min(sb.length(), 2));
		String input = date.substring(0, Math.min(date.length(), 2));
		int currdate = Integer.parseInt(curr);
		int inputdate = Integer.parseInt(input);
		int mid = Integer.parseInt(date.substring(2, Math.min(date.length(), 4)));
		int last = Integer.parseInt(date.substring(4));
		if ((inputdate - currdate) > 2 || (inputdate - currdate) < 0) {
			System.out.println("Invalid date");
			return false;
		}
		else if (mid > 12 || last > 31) {
			System.out.println("Invalid Date Format");
			return false;
		}
		else {
			return true;
		}
	
	}
	/* The following checks if the number of tickets provided is valid.
	 * It then adds the number of tickets, date, and event name to eventTrans.
	 */
	public static String allocateTickets(String eventName, String numTick, String date, String eventTrans) {
		String info = "";
		eventName = String.format("%-36s", eventName);
		if (Integer.parseInt(numTick) <= 0) {
			System.out.println("Number of tickets must be greater than Zero");
			return eventTrans;
			}
		else if (Integer.parseInt(numTick) > 99999) {
			System.out.println("Cannot allocate that many tickets");
			return eventTrans;
		}
		else{
			numTick = String.format("%5s", numTick).replace(' ', '0');
			info = "03 " + eventName + " " + date + " " + numTick + "\r\n";
			eventTrans += info;
			return eventTrans;
		}
	}
	
	/*The following checks if the number of tickets provided is valid, it then calls
	 * addMoreTickets() to add tickets to the event selected by the user.
	 */
	public static String eventTicketUpdate(String eventName, String numTick, String eventTrans) {
		if (Integer.parseInt(numTick) <= 0) {
			System.out.println("Number of tickets must be greater than Zero");
			return eventTrans;
		}
		else {
			numTick = String.format("%5s", numTick).replace(' ', '0');
			try {
				eventTrans = addMoreTickets(eventName, numTick, eventTrans);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return eventTrans;
		}
	
	}
	
	/*The following adds the tickets to the event and saves the information to the string eventTrans
	 */
	public static String addMoreTickets(String event, String tickets, String eventTrans) throws IOException {
		
		FileInputStream fil = new FileInputStream("CurrentEvents.txt");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fil));
		event = String.format("%-36s", event);
		int totNum = 0;
		String totalNum = null;
		String line = null;
		String numToRep = null;
		String check = eventTrans;
		String ticks = "";
		//checks if event exists in the current transaction or the current day
		if (check.equals(eventTrans)) {
			ticks = Helper.checkEventTrans(event, eventTrans);
			if (ticks.equals("-1")){
				ticks = Helper.checkAllFiles(event);
				
			}
			if (ticks.equals("")) {
				//checks if event exists in the current events file
				while ((line = bufferedReader.readLine()) != null) {
					if (line.matches("(?i).*"+event+".*")) {
						numToRep = line.substring(Math.max(line.length() - 5, 0));
						numToRep = Integer.valueOf(numToRep).toString();
						tickets = Integer.valueOf(tickets).toString();
						if (Integer.parseInt(numToRep) + Integer.parseInt(tickets) > 99999){
							System.out.println("Cannot add that many tickets");
							break;
						}
						else {
							totNum = Integer.parseInt(numToRep) + Integer.parseInt(tickets);
							totalNum = Integer.toString(totNum);
							totalNum = String.format("%5s", totalNum).replace(' ', '0');
							eventTrans += "04 " + event + " 000000";
							eventTrans += " " + totalNum + "\r\n";
							break;
							}
						}
					}
				bufferedReader.close();
			}
			else if (Integer.parseInt(ticks) + Integer.parseInt(tickets) > 99999){
				System.out.println("Cannot add that many tickets");
			}
			else {
				totNum = Integer.parseInt(ticks) + Integer.parseInt(tickets);
				totalNum = Integer.toString(totNum);
				totalNum = String.format("%5s", totalNum).replace(' ', '0');
				eventTrans += "04 " + event + " 000000";
				eventTrans += " " + totalNum + "\r\n";
			}
		}
		
		return eventTrans;
	}
	
	//Deletes event from the eventTrans string
	public static String deleteEvent(String event, String eventTrans) throws IOException {
		event = String.format("%-36s", event);
		FileInputStream fil = new FileInputStream("CurrentEvents.txt");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fil));
		String line = null;
		String tickets = "";
		String cont = "";
			//checks if event exists in the current transaction
			if (!Helper.checkEventTrans(event, eventTrans).equals("-1")) {
				tickets = Helper.checkEventTrans(event, eventTrans);
				eventTrans += "05 " + event + " " + "000000" + " " + tickets + "\r\n";
			}
			//check if event exists in the current day
			else if (!Helper.checkAllFiles(event).equals("")) {
				tickets = Helper.checkAllFiles(event);
				eventTrans += "05 " + event + " " + "000000" + " " + tickets + "\r\n";
			}
			//checks if event exists in the current events file
			else {
				while ((line = bufferedReader.readLine()) != null) {
					if (line.matches("(?i).*"+event+".*")) {
						tickets = line.substring(Math.max(line.length() - 5, 0));
						eventTrans += "05 " + event + " " + "000000" + " " + tickets + "\r\n";
						cont += line + "\r\n";
					}
				}
				bufferedReader.close();
			}
			
		return eventTrans;
		
	}
	
	/*The following checks if the number of tickets provided is valid, it then calls
	 * sellTickets() to sell tickets from the event selected by the user.
	 */
	public static String eventTicketSell(String eventName, String numTicket, String eventTrans) {
		
		if (Integer.parseInt(numTicket) <= 0) {
			System.out.println("Number of tickets must be greater than Zero");
		}
		else if (Integer.parseInt(numTicket) > 8) {
			System.out.println("Number of Tickets Exceeds 8");
		}
		else {
			
			try {
				eventTrans = sellTickets(eventName, numTicket, eventTrans);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return eventTrans;
	}
	
	/*The following sells tickets from the event and saves the information to the string eventTrans
	 */
	public static String sellTickets(String event, String tickets, String eventTrans) throws IOException {
		FileInputStream fil = new FileInputStream("CurrentEvents.txt");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fil));
		
		event = String.format("%-36s", event);
		String line = null;
		String numToRep = null;
		int totNum = 0;
		String totalNum = null;
		String check = eventTrans;
		String ticks = ""; 
			//check if event exists in the current day or the current transaction
			ticks = Helper.checkEventTrans(event, eventTrans);
			if (ticks.equals("-1")){
				ticks = Helper.checkAllFiles(event);
			}
			//checks if event exist in the current events file
			if (ticks.equals("")) {
				while ((line = bufferedReader.readLine()) != null) {
					if (line.matches("(?i).*"+event+".*")) {
						numToRep = line.substring(Math.max(line.length() - 5, 0));
						numToRep = Integer.valueOf(numToRep).toString();
						tickets = Integer.valueOf(tickets).toString();
						if (Integer.parseInt(tickets) > Integer.parseInt(numToRep)) {
							System.out.println("Cannot sell tickets, not enough");
							
						}
						else {
							totNum = Integer.parseInt(numToRep) - Integer.parseInt(tickets);
							totalNum = Integer.toString(totNum);
							totalNum = String.format("%5s", totalNum).replace(' ', '0');
							eventTrans += "01 " + event;
							eventTrans += " " + "000000 " + totalNum + "\r\n";
						}
					}
				}
				bufferedReader.close();
			}
			else if (Integer.parseInt(tickets) > Integer.parseInt(ticks)){
				System.out.println("Cannot sell tickets, not enough");
			}
			else {
				totNum = Integer.parseInt(ticks) - Integer.parseInt(tickets);
				totalNum = Integer.toString(totNum);
				totalNum = String.format("%5s", totalNum).replace(' ', '0');
				eventTrans += "01 " + event + " 000000";
				eventTrans += " " + totalNum + "\r\n";
			}
		return eventTrans;
	}
	
	/*The following checks if the number of tickets provided is valid, it then calls
	 * ReturnTickets() to return tickets to the event selected by the user.
	 */
	public static String eventTicketReturn(String eventName, String numTicket, String eventTrans, int num) {
		
		if (Integer.parseInt(numTicket) <= 0) {
			System.out.println("Number of tickets must be greater than Zero");
		}
		else if (Integer.parseInt(numTicket) > 8 && num == 1) {
			System.out.println("Cannot Return More Than 8 tickets");
		}
		else {
			try {
				eventTrans = ReturnTickets(eventName, numTicket, eventTrans);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return eventTrans;
	}
	
	/*The following returns tickets to the event and saves the information to the string eventTrans
	 */
	public static String ReturnTickets(String event, String tickets, String eventTrans) throws IOException {
		
		FileInputStream fil = new FileInputStream("CurrentEvents.txt");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fil));
		String totalNum = null;
		String line = null;
		String numToRep = null;
		int totNum = 0;
		String check = eventTrans;
		String ticks = "";
		event = String.format("%-36s", event);
			//check if event exists in the current day or the current transaction
			ticks = Helper.checkEventTrans(event, eventTrans);
			if (ticks.equals("-1")){
				ticks = Helper.checkAllFiles(event);
			}
			//checks if event exists in the current events file
			if (ticks.equals("")) {
				while ((line = bufferedReader.readLine()) != null) {
					if (line.matches("(?i).*"+event+".*")) {
						numToRep = line.substring(Math.max(line.length() - 5, 0));
						numToRep = Integer.valueOf(numToRep).toString();
						tickets = Integer.valueOf(tickets).toString();
						if (Integer.parseInt(tickets) + Integer.parseInt(numToRep) > 99999) {
							System.out.println("Cannot return tickets, not enough tickets");
						}
						else {
							totNum = Integer.parseInt(numToRep) + Integer.parseInt(tickets);
							totalNum = Integer.toString(totNum);
							totalNum = String.format("%5s", totalNum).replace(' ', '0');
							eventTrans += "02 " + event;
							eventTrans += " " + "000000 " + totalNum + "\r\n";
						}
					}
				}
				
				bufferedReader.close();
				
			}
			else if (Integer.parseInt(tickets) + Integer.parseInt(ticks) > 99999){
				System.out.println("Cannot return tickets, not enough");
			}
			else {
				totNum = Integer.parseInt(ticks) + Integer.parseInt(tickets);
				totalNum = Integer.toString(totNum);
				totalNum = String.format("%5s", totalNum).replace(' ', '0');
				eventTrans += "02 " + event + " 000000";
				eventTrans += " " + totalNum + "\r\n";
			}
		return eventTrans;
	}

}
