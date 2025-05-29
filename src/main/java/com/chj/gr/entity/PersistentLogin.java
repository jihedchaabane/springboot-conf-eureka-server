package com.chj.gr.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogin {
	@Id
	@Column(length = 64)
	private String series;

	@Column(length = 64, nullable = false)
	private String username;

	@Column(length = 64, nullable = false)
	private String token;

	@Column(nullable = false)
	private LocalDateTime lastUsed;

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(LocalDateTime lastUsed) {
		this.lastUsed = lastUsed;
	}
}
