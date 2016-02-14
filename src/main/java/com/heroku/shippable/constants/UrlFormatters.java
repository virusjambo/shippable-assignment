package com.heroku.shippable.constants;

/**
 * @author Amit
 *  Defines URL templates.
 *  
 */
public enum UrlFormatters {

	REPO_ISSUES("https://api.github.com/repos/${org}/${repo}/issues?"),
	REPO_INFO("https://api.github.com/repos/${org}/${repo}");

	private String format;

	/**
	 * @param format
	 */
	private UrlFormatters(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
}
