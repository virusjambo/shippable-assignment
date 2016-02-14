package com.heroku.shippable.constants;

import java.util.regex.Pattern;

/**
 * @author Amit
 *  Defines Constants
 */
public class ShippableConstants {
	public static final String REPOS = "repos";
	public static final String ISSUES = "issues";
	public static final String REPO = "repo";
	public static final String GIT_HUB = "github.com/";
	public static final String ORG = "org";

	public static final Pattern templateValidPattern = Pattern.compile("\\$\\{.*?\\}");

	/// Git Issue extra url params
	public static final String MILESTONE = "milestone";
	public static final String STATE = "state";
	public static final String ASSIGNEE = "assignee";
	public static final String CREATOR = "creator";
	public static final String MENTIONED = "mentioned";
	public static final String LABELS = "labels";
	public static final String SORT = "sort";
	public static final String SINCE = "since";

	// Git pagination param
	public static final String PAGE = "page";
	public static final String PER_PAGE = "per_page";
	public static final long MILLI_SECONDS_IN_DAY=86400000;
	public static final int PAGE_SIZE=100;


}
