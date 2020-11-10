package codeSnippets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//import java.time.DateTime;

public class DateFormat {

	public static void main(String[] args) {
		DateFormat s = new DateFormat();
		String ss = s.getDatum();
	//	java.time.DateTime dt= new DateTime();
		

	}
public String getDatum() {
	
	// 2005-12-22
	// 23:19:16
	// timestamp="2005-12-22
	// 23:19:16";

	
	
	 // (1) get today's date
    Date today = Calendar.getInstance().getTime();

    // (2) create a date "formatter" (the date format we want)
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    // (3) create a new String using the date format we want
    String folderName = formatter.format(today);
    
    // (4) this prints "Folder Name = 2009-09-06-08.23.23"
    System.out.println("Folder Name = " + folderName);
	

	String timestamp="";
	return timestamp;
}
}
