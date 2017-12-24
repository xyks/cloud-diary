package cn.bossge.cloud_diary_common.enums;


public enum TokenMessage {
	EXPIRATION("Failed to vetify your identification, please re-login.");
	
	private String info;
	
	private TokenMessage(String info) {
		this.info = info;
	}
	
	public String info() {
		return this.info;
	}
}
