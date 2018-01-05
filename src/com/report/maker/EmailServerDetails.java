package com.report.maker;

public class EmailServerDetails {

	private String host;
	private String username;
	private String password;
	private String emailFrom;
	private String smtpPort;
	private String tlsEnable;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getTlsEnable() {
		return tlsEnable;
	}

	public void setTlsEnable(String tlsEnable) {
		this.tlsEnable = tlsEnable;
	}

}
