package com.heroku.shippable.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryInfo {

	private String full_name;
	private String open_issues;

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getOpen_issues() {
		return open_issues;
	}

	public void setOpen_issues(String open_issues) {
		this.open_issues = open_issues;
	}

}
