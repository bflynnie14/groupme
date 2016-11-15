package com.flynn.groupme.ingest;

import com.google.gson.annotations.SerializedName;

public class Messages {
	
	private Response response;
	private Meta meta;
	
	protected Response getResponse() {
		return response;
	}

	protected void setResponse(Response response) {
		this.response = response;
	}
	
	protected Meta getMeta() {
		return meta;
	}

	protected void setMeta(Meta meta) {
		this.meta = meta;
	}
	
	protected class Response {
		private int count;
		@SerializedName("messages")
		private Message[] message;

		protected int getCount() {
			return count;
		}

		protected void setCount(int count) {
			this.count = count;
		}
		
		protected Message[] getMessage() {
			return message;
		}

		protected void setMessage(Message[] message) {
			this.message = message;
		}
		
	}
	
	protected class Meta {
		private int code;

		protected int getCode() {
			return code;
		}

		protected void setCode(int code) {
			this.code = code;
		}
	}
	
}
