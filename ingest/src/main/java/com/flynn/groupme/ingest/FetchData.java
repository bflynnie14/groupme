package com.flynn.groupme.ingest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

public class FetchData {
	private final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss zzz";
	private String 	groupMeApiKey = "",
					groupMeGroupId = "";
	
	public FetchData() {
		Properties prop = new Properties();
		InputStream input = null;
		String 	propertiesFilename = "ingest.properties",
				apiKeyPropFieldname = "groupme.apikey",
				groupIdPropFieldname = "groupme.groupid";
		input = FetchData.class.getClassLoader().getResourceAsStream(propertiesFilename);
		if (input == null) {
			System.out.println("Sorry, unable to find " + propertiesFilename);
			System.out.println("Exiting...");
			System.exit(0);
		} else {
			try {
				prop.load(input);
			} catch (IOException e) {
				System.out.println("Exception accessing " + propertiesFilename);
				System.out.println("Exiting...");
				System.exit(0);
			}
			groupMeApiKey = prop.getProperty(apiKeyPropFieldname);
			groupMeGroupId = prop.getProperty(groupIdPropFieldname);
			if(groupMeApiKey == null || groupMeApiKey.equalsIgnoreCase("")) {
				System.out.println("Sorry, missing required property key of " + apiKeyPropFieldname + " in " + propertiesFilename);
				System.out.println("Exiting...");
				System.exit(0);
			} else if ( groupMeGroupId == null || groupMeGroupId.equalsIgnoreCase("")){
				System.out.println("Sorry, missing required property key of " + groupIdPropFieldname + " in " + propertiesFilename);
				System.out.println("Exiting...");
				System.exit(0);
			} else {
				System.out.println("Using GroupMe Key: " + groupMeApiKey);
				System.out.println("Using GroupMe Group ID: " + groupMeGroupId);
			}
		}
	}

	public ArrayList<Message> getChatByRange(long stopTime) {

		System.out.println("Fetch all messages since " + dateStringFromEpoch(stopTime, DEFAULT_DATE_FORMAT));
		ArrayList<Message> messageList = null;
		try {
			messageList = getLatestMessages();
			if (isLastMessageCreatedBeforeTime(messageList, stopTime)) {
				return trimMessagesPriorToDate(messageList, stopTime);
			} else {
				boolean lastMessageFound = false;
				while (!lastMessageFound) {
					ArrayList<Message> previousMessageList = getMessagesBeforeMessageId(getLastMessageId(messageList));
					if (isLastMessageCreatedBeforeTime(previousMessageList, stopTime)) {
						previousMessageList = trimMessagesPriorToDate(previousMessageList, stopTime);
						lastMessageFound = true;
					}
					messageList.addAll(previousMessageList);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return messageList;
	}

	public ArrayList<Message> getMessagesAfterMessageId(String afterId) throws IOException {
		return getMessages(null, afterId);
	}

	public ArrayList<Message> getMessagesBeforeMessageId(String beforeId) throws IOException {
		return getMessages(beforeId, null);
	}

	public ArrayList<Message> getLatestMessages() throws IOException {
		return getMessages(null, null);
	}

	private ArrayList<Message> getMessages(String beforeId, String afterId) throws IOException {
		String apiUrlString = "https://api.groupme.com/v3/groups/" + groupMeGroupId + "/messages?token=" + groupMeApiKey;
		if (beforeId != null && !beforeId.equalsIgnoreCase("")) {
			apiUrlString += "&before_id=" + beforeId;
		} else if (afterId != null && !afterId.equalsIgnoreCase("")) {
			apiUrlString += "&after_id=" + afterId;
		}
		System.out.println("Requesting " + apiUrlString);
		InputStream in = new URL(apiUrlString).openStream();
		String output = IOUtils.toString(in, "UTF-8");
		IOUtils.closeQuietly(in);
		Gson gson = new Gson();
		return createMessageList(gson.fromJson(output, Messages.class));
	}

	private ArrayList<Message> createMessageList(Messages messages) {
		ArrayList<Message> messageList = new ArrayList<Message>();
		messageList.addAll(Arrays.asList(messages.getResponse().getMessage()));
		return messageList;
	}

	private ArrayList<Message> trimMessagesPriorToDate(ArrayList<Message> messageList, long time) {
		if (isLastMessageCreatedBeforeTime(messageList, time)) {
			for (int i = (messageList.size() - 1); (i >= 0
					&& isMessageCreatedBeforeTime(messageList.get(i), time)); i--) {
				messageList.remove(i);
			}
		}
		return messageList;
	}

	private String getLastMessageId(ArrayList<Message> messageList) {
		if (messageList != null && messageList.size() > 0) {
			return messageList.get(messageList.size() - 1).getId();
		} else {
			return null;
		}
	}

	private boolean isLastMessageCreatedBeforeTime(ArrayList<Message> messageList, long time) {
		if (messageList != null && messageList.size() > 0) {
			Message lastMessage = messageList.get(messageList.size() - 1);
			return isMessageCreatedBeforeTime(lastMessage, time);
		} else {
			return false;
		}
	}

	private boolean isMessageCreatedBeforeTime(Message message, long time) {
		return (message.getCreatedAt() * 1000) < time;
	}

	private String dateStringFromEpoch(long time, String dateFormat) {
		Date printDate = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(printDate);
	}
}
