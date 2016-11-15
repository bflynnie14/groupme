package com.flynn.groupme.ingest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FetchDataTest {

	public static void main(String[] args) {
		FetchData fd = new FetchData();
		try {
			String dateFormat = "yyyy-MM-dd HH:mm:ss zzz";
			String startDate = "2016-11-15 00:00:00 EST";
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date date = sdf.parse(startDate);
			
			ArrayList<Message> messageList = fd.getChatByRange(date.getTime());
			
			System.out.println("\n********************");
			System.out.println("Messages fetched: " + messageList.size());
			System.out.println("********************");
			
			System.out.println("\n********************");
			System.out.println("Message Output");
			System.out.println("********************");
			for(Message message : messageList){
				System.out.println(message.getMessageDetails(dateFormat));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
