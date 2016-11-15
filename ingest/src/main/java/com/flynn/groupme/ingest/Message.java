package com.flynn.groupme.ingest;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

//@TODO - move this class out to allow ArrayList of Message objects (not Messages objects)
public class Message {
	@SerializedName("avatar_url")
	private String avatarUrl;
	@SerializedName("created_at")
	private long createdAt;
	@SerializedName("group_id")
	private String groupId;
	private String id;
	private String name;
	@SerializedName("sender_id")
	private String senderId;
	@SerializedName("sender_type")
	private String senderType;
	@SerializedName("source_guid")
	private String sourceGuid;
	private boolean system;
	private String text;
	@SerializedName("user_id")
	private String userId;

	protected String getAvatarUrl() {
		return avatarUrl;
	}

	protected void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	protected long getCreatedAt() {
		return createdAt;
	}
	
	protected String getCreatedAtAsFormattedDate(String dateFormat){
		Date printDate = new Date(this.getCreatedAt() * 1000);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(printDate);
	}

	protected void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	protected String getGroupId() {
		return groupId;
	}

	protected void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	protected String getId() {
		return id;
	}

	protected void setId(String id) {
		this.id = id;
	}

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected String getSenderId() {
		return senderId;
	}

	protected void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	protected String getSenderType() {
		return senderType;
	}

	protected void setSenderType(String senderType) {
		this.senderType = senderType;
	}

	protected String getSourceGuid() {
		return sourceGuid;
	}

	protected void setSourceGuid(String sourceGuid) {
		this.sourceGuid = sourceGuid;
	}

	protected boolean isSystem() {
		return system;
	}

	protected void setSystem(boolean system) {
		this.system = system;
	}

	protected String getText() {
		return text;
	}

	protected void setText(String text) {
		this.text = text;
	}

	protected String getUserId() {
		return userId;
	}

	protected void setUserId(String userId) {
		this.userId = userId;
	}
	
	protected String getMessageDetails(String dateFormat) {
		String messageDetails = "";
		messageDetails += "id: " + this.getId() + "\n";
		messageDetails += "name: " + this.getName() + "\n";
		messageDetails += "text: " + this.getText() + "\n";
		messageDetails += "createdAt: " + this.getCreatedAt() + "\n";
		messageDetails += "createdAt (Formatted): " + this.getCreatedAtAsFormattedDate(dateFormat) + "\n";
		messageDetails += "avatarUrl: " + this.getAvatarUrl() + "\n";
		messageDetails += "senderId: " + this.getSenderId() + "\n";
		messageDetails += "senderType: " + this.getSenderType() + "\n";
		messageDetails += "userId: " + this.getUserId() + "\n";
		messageDetails += "sourceGuid: " + this.getSourceGuid() + "\n";
		messageDetails += "groupId: " + this.getGroupId() + "\n";
		
		return messageDetails;
	}

}