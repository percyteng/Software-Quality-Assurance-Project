import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/* Author: Eric Bolboa 10113035
 *         Percy Teng 10122592
 */

public class Main {
	public static Scanner scanner = new Scanner(System.in);
	
	public static void selectExecution(Transaction instance, String eventTrans) throws IOException{
	
		if (instance.user == instance.user.ADMIN)
			System.out.println("Please enter a valid type of execution: create/add/delete/sell/return/logout/exit");
		else
			System.out.println("Please enter a valid type of execution: sell/return/logout/exit");
		String execution = scanner.next();
		
		
		if(execution.equals("create")){
			if (instance.user == instance.user.SALES){
				System.out.print("Sorry, you can not create an event as sales. ");
				selectExecution(instance, eventTrans);
			}
			else{
				System.out.println("Enter Name of event: ");
				String eventName = scanner.next();
				boolean checkBool = Transaction.writeEventName(eventName, eventTrans);
				if (checkBool) {
					System.out.println("Enter Date: ");
					String date = scanner.next();
					boolean checkBool2 = Transaction.setDate(date);
					if (checkBool2 == true){
						System.out.println("Please enter number of tickets to allocate: ");
						String numTick = scanner.next();
						eventTrans = Transaction.allocateTickets(eventName, numTick, date, eventTrans);
						selectExecution(instance, eventTrans);
					}
				}
			}
		}
		else if(execution.equals("add")){
			if (instance.user == instance.user.SALES){
				System.out.print("Sorry, you can not add tickets to events as sales. ");
				selectExecution(instance, eventTrans);
			}
			else{
				System.out.println("Enter Event To Which To Add Tickets: ");
				String eventName = scanner.next();
				if (Transaction.checkEventName(eventName, eventTrans)){
					System.out.println("How Many Tickets Would You Like To Add: ");
					String numTick = scanner.next();
					eventTrans = Transaction.eventTicketUpdate(eventName, numTick, eventTrans);
					selectExecution(instance, eventTrans);
				}
				else {
					System.out.println("Event Does Not Exist!");
					selectExecution(instance, eventTrans);
				}
			}
		}
		else if(execution.equals("delete")){
			if (instance.user == instance.user.SALES){
				System.out.print
				("Sorry, you can not delete an event as sales. ");
				selectExecution(instance, eventTrans);
			}
			else{
				System.out.println("Enter Event To Delete: ");
				String eventName = scanner.next();
				if (Transaction.checkEventName(eventName, eventTrans)){
					try {
						eventTrans = Transaction.deleteEvent(eventName, eventTrans);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					selectExecution(instance, eventTrans);
				}
				else {
					System.out.println("Event Does Not Exist!");
					selectExecution(instance, eventTrans);
				}
			}
		}
		else if(execution.equals("sell")){
			System.out.println("Enter Event To Sell Tickets: ");
			String eventName = scanner.next();
			if (Transaction.checkEventName(eventName, eventTrans)) {
				System.out.println("Enter Number of Tickets to Sell: ");
				String numTick = scanner.next();
				eventTrans = Transaction.eventTicketSell(eventName, numTick, eventTrans);
				selectExecution(instance, eventTrans);
			}
			else {
				System.out.println("Event Does Not Exist!");
				selectExecution(instance, eventTrans);
			}
		}
		else if(execution.equals("return")){
			if (instance.user == instance.user.ADMIN) {
				System.out.println("Enter Event to Return Tickets: ");
				String eventName = scanner.next();
				if (Transaction.checkEventName(eventName, eventTrans)){
					System.out.println("Enter Number of Tickets to Return: ");
					String numTick = scanner.next();
					eventTrans = Transaction.eventTicketReturn(eventName, numTick, eventTrans, 0);
					selectExecution(instance, eventTrans);
				}
				else {
					System.out.println("Event Does Not Exist!");
					selectExecution(instance, eventTrans);
				}
			}
			else {
				System.out.println("Enter Event to Return Tickets: ");
				String eventName = scanner.next();
				if (Transaction.checkEventName(eventName, eventTrans)){
					System.out.println("Enter Number of Tickets to Return: ");
					String numTick = scanner.next();
					eventTrans = Transaction.eventTicketReturn(eventName, numTick, eventTrans, 1);
					selectExecution(instance, eventTrans);
				}
				else {
					System.out.println("Event Does Not Exist!");
					selectExecution(instance, eventTrans);
				}
			}
		}
		else if(execution.equals("exit"))
			System.exit(0);
		
		else if(execution.equals("logout")){
			System.out.println("Are you sure you want to log out? (y/n)");
			String answer = scanner.next();
			EndSession.logOut(instance, answer, eventTrans);
		}
		else{
			selectExecution(instance, eventTrans);
		}		
	}
	
	
	public static void checkLoginStatus(Transaction instance){
		System.out.println("Login as admin or sales?\n");
		String status = scanner.next();
		if (status.equals("admin"))
			instance.user = instance.user.ADMIN;
		else if (status.equals("sales"))
			instance.user = instance.user.SALES;
		else
			checkLoginStatus(instance);
	}
	
	public static void start() throws IOException {
		String eventTrans = "";
		Transaction instance = new Transaction();
		System.out.println("System Starts \n");
		String log = scanner.next();
		while (!log.equals("login")){
			System.out.println("Please login first");
			if (!scanner.hasNext()){
				System.exit(0);
			}
			log = scanner.next();
		}
		//check for if logged in already.
		instance.ifLogin = true;
		checkLoginStatus(instance);
		selectExecution(instance, eventTrans);
	}
	
	
	public static void main(String args[]) throws IOException{
		start();
        scanner.close();
	}
}
