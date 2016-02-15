package com.heroku.shippable.service;

import static com.heroku.shippable.constants.ShippableConstants.GIT_HUB;
import static com.heroku.shippable.constants.ShippableConstants.MILLI_SECONDS_IN_DAY;
import static com.heroku.shippable.constants.ShippableConstants.PAGE_SIZE;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heroku.shippable.constants.ErrorMessages;
import com.heroku.shippable.constants.ShippableConstants;
import com.heroku.shippable.exception.ShippableException;
import com.heroku.shippable.generators.UrlGenerator;
import com.heroku.shippable.model.Issue;
import com.heroku.shippable.model.RepositoryInfo;
import com.heroku.shippable.model.output.IssueOuput;

import scala.annotation.meta.getter;

/**
 * @author Amit
 * 
 *         Responsible for making rest call to github and calculating desired
 *         output as per problem statement.
 */
@Service
public class IssueService {

	@Autowired
	private GitRepoService gitRepoService;

	@Autowired
	private ObjectMapper jacksonObjectMapper;

	/**
	 * @param repoUrl
	 * @return
	 * @throws ShippableException
	 *             Builds IssueOutput as per problem statement
	 */
	public IssueOuput execute(String repoUrl) throws ShippableException {
		isValidUrl(repoUrl);
		Map<String, String> repoDetails = buildRequestMap(repoUrl);
		RepositoryInfo repositoryInfo = findRepoInfo(repoDetails);
		return findIssuesInPeriod(repoDetails, repositoryInfo);
	}

	/**
	 * @param repoDetails
	 * @param repositoryInfo
	 * @param timeinMilliSeconds
	 * @return
	 * @throws ShippableException
	 *             Finds issues raised in particular time period
	 */
	private IssueOuput findIssuesInPeriod(Map<String, String> repoDetails, RepositoryInfo repositoryInfo)
			throws ShippableException {
		IssueOuput issueOuput = new IssueOuput();
		int totalOpenIssue = Integer.valueOf(repositoryInfo.getOpen_issues());
		if (Integer.valueOf(repositoryInfo.getOpen_issues()) == 0) {
			return new IssueOuput();
		}

		// repoDetails.put(ShippableConstants.SINCE,
		// getTimeInISO8601Format(timeinMilliSeconds));
		// else calculate
		int totalHits = totalOpenIssue % PAGE_SIZE;
		for (int i = 0; i < totalHits; i++) {
			setPagination(repoDetails, i + 1);
			String generatedUrl = UrlGenerator.ISSUES_URL.generate(repoDetails);
			String listOfIssues = (String) gitRepoService.provisionService(generatedUrl, String.class);
			List<Issue> issues = null;
			try {
				issues = jacksonObjectMapper.readValue(listOfIssues, new TypeReference<List<Issue>>() {
				});
			} catch (Exception e) {
				e.printStackTrace();

			}
			issueOuput = processIssues(issues, issueOuput);
			if (issues.size() < PAGE_SIZE) {
				break;
			}
		}
		issueOuput.setTotalIssues(totalOpenIssue);
		return issueOuput;

	}

	/**
	 * @param repoUrl
	 * @throws ShippableException
	 *             Just to check if url is valid.Can be improved
	 */
	private void isValidUrl(String repoUrl) throws ShippableException {
		if (!StringUtils.hasText(repoUrl))
			throw new ShippableException(ErrorMessages.INVALID_REPO_URL);
		if (!repoUrl.contains(GIT_HUB))
			throw new ShippableException(ErrorMessages.INVALID_REPO_URL);

	}

	/**
	 * @param repoUrl
	 * @return
	 * @throws ShippableException
	 *             This will build a desired map which will be used to build git
	 *             repository URL's.
	 */
	private Map<String, String> buildRequestMap(String repoUrl) throws ShippableException {
		String[] url = repoUrl.split(ShippableConstants.GIT_HUB);
		String repoIdentifierPath = url[url.length - 1];
		String[] repoInfo = repoIdentifierPath.split("/");
		if (repoInfo == null) {
			throw new ShippableException(ErrorMessages.INVALID_REPO_URL);
		}
		// If length is less than 2 org/repository name is missing
		if (repoInfo.length < 2) {
			throw new ShippableException(ErrorMessages.INVALID_REPO_URL);
		}
		// Find org in Url
		return new HashMap<String, String>() {
			{
				put(ShippableConstants.ORG, repoInfo[0]);
				put(ShippableConstants.REPO, repoInfo[1]);
			}
		};

	}

	/**
	 * @param repoDetails
	 * @return
	 * @throws ShippableException
	 *             Used to total open issues for a repository
	 */
	private RepositoryInfo findRepoInfo(Map<String, String> repoDetails) throws ShippableException {
		String generatedUrl = UrlGenerator.REPO_INFO.generate(repoDetails);

		String listOfIssues = (String) gitRepoService.provisionService(generatedUrl, String.class);
		RepositoryInfo repositoryInfo = null;
		try {
			repositoryInfo = jacksonObjectMapper.readValue(listOfIssues, RepositoryInfo.class);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return repositoryInfo;

	}

	/**
	 * @param repoDetails
	 * @param pageCount
	 * @return Used to set pagination count as Git api supports max 100 per page
	 *         with default 30
	 */
	private Map<String, String> setPagination(Map<String, String> repoDetails, int pageCount) {
		repoDetails.put(ShippableConstants.PER_PAGE, "100");
		repoDetails.put(ShippableConstants.PAGE, String.valueOf(pageCount));
		return repoDetails;

	}

	/**
	 * @param timeinMilliSeconds
	 * @return Can be moved to utility class
	 */
	private String getTimeInISO8601Format(long timeinMilliSeconds) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(tz);
		return df.format(new Date(timeinMilliSeconds));
	}

	private IssueOuput processIssues(List<Issue> responseIssues, IssueOuput issueOuput) {
		Calendar cal = Calendar.getInstance();
		Date dc = cal.getTime();
		for (Issue item : responseIssues) {

			Date di = toDate(item.getCreated_at());
			int daysDiff = getDays(di, dc);
			if (daysDiff <= 0) {
				issueOuput.incrementTotalIssuesPastDay();
			} else if (daysDiff <= 7) {
				issueOuput.inrementTotalIssuesPastWeek();
			} else {
				issueOuput.inrementTotalIssuesMoreThanWeek();
			}
		}
		return issueOuput;
	}

	private int getDays(Date d1, Date d2) {
		return (int)Math.abs( (d2.getTime() - d1.getTime()) / ShippableConstants.MILLI_SECONDS_IN_DAY);
	}

	private Date toDate(Object source) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		try {
			return dateFormatter.parse(source.toString());
		} catch (ParseException e) {

		}
		return null;
	}

}
