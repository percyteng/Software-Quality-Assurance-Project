import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/* The following class is a helper class for searching if events exist inside the directory
 * containing all the files */
public class Helper {
	
	//checks the event transaction files to see if the event exists
	public static boolean checkEventExist() {
		String targ_dir = "AllFiles";
		File dir = new File(targ_dir);
		File[] files = dir.listFiles();
		String fname = "";
		String content = "";
		boolean check = false;
		
		for (File f : files) {
			fname = f.toString();
			if (fname.matches(".*EventTransaction.*")) {
				check = true;
			}
	}
		return check;
	}
	
	//checks the current transaction to see if the file was deleted or not
	public static boolean checkEventTransDelete(String eventName, String eventTrans) {
		
		Scanner scanner = new Scanner(eventTrans);
		String sub = "";
		boolean check = true;
		
		while (scanner.hasNextLine()) {
			
			sub = scanner.nextLine();
			if (sub.matches("03 " + eventName + ".*")) {
				check = true;
			}
			else if (sub.matches("05 " + eventName + ".*")) {
				check = false;
			}
		}
		
		return check;
	}
	
	//checks all the event transaction files line by line to see if the event was deleted or not
	public static boolean checkAllFilesDelete(String eventName) throws IOException {
		String targ_dir = "AllFiles";
		File dir = new File(targ_dir);
		File[] files = dir.listFiles();
		String fname = "";
		boolean check = true;
		int larg = returnLargest(files);
		String eventIndex = String.valueOf(larg);
		String cont = "";

		if (larg == 0) {
			return check;
		}
		
		//checks every file starting from the last one that was created
		else {
		
			while (larg != 0) {
				eventIndex = String.valueOf(larg);
				for (File f : files) {
					fname = f.toString();
					
					if (fname.equals("EventTransaction-" + eventIndex)) {
						BufferedReader inputStream = null;
		
		                try {
		                    inputStream = new BufferedReader(
		                                    new FileReader(f));
		                    String line;
		
		                    while ((line = inputStream.readLine()) != null) {
		                        if (line.matches("03 " + eventName + ".*")) {
		                        	check = true;
		                        	cont = line;
		                        }
		                        else if (line.matches("05 "+ eventName + ".*")) {
		                        	check = false;
		                        	cont = line;
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
				if (!cont.equals("")) {
					break;
				}
				larg -= 1;
			}
		}
		
		return check;
	}
	//returns the name of the last created event transaction file
	public static int returnLargest(File[] files) {
		int largest = 0;
		String fname = "";
		String num = "";
		int temp = 0;
		for (File f : files) {
			fname = f.toString();
			if (fname.matches(".*EventTransaction.*")) {
				num = fname.substring(52);
				temp = Integer.parseInt(num);
				if (temp > largest) {
					largest = temp;
				}
			}
		}
		return largest;
	}
	
	//checks all the event transaction files line by line to see if the event exists
	public static String checkAllFiles(String eventName) throws IOException {
		String targ_dir = "AllFiles";
		File dir = new File(targ_dir);
		File[] files = dir.listFiles();
		String fname = "";
		int index = 0;
		String sub = "";
		int larg = returnLargest(files);
		String eventIndex = String.valueOf(larg);
		String cont = "";
		if (larg == 0) {
			return sub;
		}
		
		//checks every file starting from the last one that was created
		else {
			while (cont.equals("")) {
				eventIndex = String.valueOf(larg);
				for (File f : files) {
					fname = f.toString();
					if (fname.equals("EventTransaction-" + eventIndex)) {
					
						BufferedReader inputStream = null;
		
						try {
							inputStream = new BufferedReader(
		            			new FileReader(f));
							String line;
		
							while ((line = inputStream.readLine()) != null) {
		                        if (line.matches(".*" + eventName + ".*")) {
		                        	for (int i = -1; (i = line.indexOf(eventName, i + 1)) != -1; ) {
		                    		    index = i;
		                    		    cont = line;
		                    		    
		                    		}
		                        }
		                    }
		                if (!cont.equals("")){
		          
			                int len = eventName.length();
			                index += len;
			                int j = index;
			                char c = cont.charAt(j);
			        		while (c == ' ') {
			        			j += 1;
			        			c = cont.charAt(j);
			        		}
			        		sub = cont.substring(j+7,j+12);
		                }
		                
		                }
		                finally {
		                	if (inputStream != null) {
		                		try {
									inputStream.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		                	}
		                }
					}
				}
				if (larg == 1) {
					break;
				}
				else {
					larg -= 1;
				}
			}
		}
		return sub;
	}
	
	//checks the current transaction to see if the event exists or not
	public static String checkEventTrans(String eventName, String eventTrans) {
		int index = -1;
		for (int i = -1; (i = eventTrans.indexOf(eventName, i + 1)) != -1; ) {
		    index = i;
		}
		int len = 0;
		if (index != -1) {
			len = eventName.length();
		}
		else {
			return "-1";
		}
		index += len;
		int j = index;
		char c = eventTrans.charAt(j);
		while (c == ' ') {
			j += 1;
			c = eventTrans.charAt(j);
		}
		String sub = eventTrans.substring(j+7,j+12);
		return sub;
	}

}
