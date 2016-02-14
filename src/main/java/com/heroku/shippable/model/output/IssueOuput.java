package com.heroku.shippable.model.output;

import com.heroku.shippable.constants.ErrorMessages;

public class IssueOuput extends ApiOutput {

	private int totalIssues;
	private int totalIssuesPastDay;
	private int totalIssuesPastWeek;

	public int getTotalIssues() {
		return totalIssues;
	}

	public void setTotalIssues(int totalIssues) {
		this.totalIssues = totalIssues;
	}

	public int getTotalIssuesPastDay() {
		return totalIssuesPastDay;
	}

	public void setTotalIssuesPastDay(int totalIssuesPastDay) {
		this.totalIssuesPastDay = totalIssuesPastDay;
	}

	public int getTotalIssuesPastWeek() {
		return totalIssuesPastWeek;
	}

	public void setTotalIssuesPastWeek(int totalIssuesPastWeek) {
		this.totalIssuesPastWeek = totalIssuesPastWeek;
	}

	IssueOuput(ErrorMessages errorMessage) {
		super(errorMessage);
	}

	/**
	 * @param errorMessage
	 * @param totalIssues
	 * @param totalIssuesPastDay
	 * @param totalIssuesPastWeek
	 */
	public IssueOuput( int totalIssues, int totalIssuesPastDay, int totalIssuesPastWeek) {
		super();
		this.totalIssues = totalIssues;
		this.totalIssuesPastDay = totalIssuesPastDay;
		this.totalIssuesPastWeek = totalIssuesPastWeek;
	}

	IssueOuput() {
		super();
	}

}
