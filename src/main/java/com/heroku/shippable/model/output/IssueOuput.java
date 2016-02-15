package com.heroku.shippable.model.output;

import com.heroku.shippable.constants.ErrorMessages;

public class IssueOuput extends ApiOutput {

	private int totalIssues;
	private int totalIssuesPastDay;
	private int totalIssuesPastWeek;
	private int totalIssuesMoreThanWeek;

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

	public IssueOuput() {
		super();
	}

	public int getTotalIssuesMoreThanWeek() {
		return totalIssuesMoreThanWeek;
	}

	public void setTotalIssuesMoreThanWeek(int totalIssuesMoreThanWeek) {
		this.totalIssuesMoreThanWeek = totalIssuesMoreThanWeek;
	}
	
	public void incrementTotalIssuesPastDay(){
		this.totalIssuesPastDay++;
	}
	
	public void inrementTotalIssuesPastWeek(){
		this.totalIssuesPastWeek++;
	}
	
	public void inrementTotalIssuesMoreThanWeek(){
		this.totalIssuesMoreThanWeek++;
	}
}
