package com.heroku.shippable.service;

import static com.heroku.shippable.constants.ShippableConstants.GIT_HUB;
import static com.heroku.shippable.constants.ShippableConstants.MILLI_SECONDS_IN_DAY;
import static com.heroku.shippable.constants.ShippableConstants.PAGE_SIZE;

import java.text.DateFormat;
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

/**
 * @author Amit
 * 
 *  Responsible for making rest call to github and calculating
 *         desired output as per problem statement.
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
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("0"));
		int issuesPast24Hour = findIssuesInPeriod(repoDetails, repositoryInfo,
				c.getTimeInMillis() - MILLI_SECONDS_IN_DAY);
		int issuesPastWeek = findIssuesInPeriod(repoDetails, repositoryInfo,
				c.getTimeInMillis() - (MILLI_SECONDS_IN_DAY * 7));

		return new IssueOuput(Integer.valueOf(repositoryInfo.getOpen_issues()), issuesPast24Hour, issuesPastWeek);

	}

	/**
	 * @param repoDetails
	 * @param repositoryInfo
	 * @param timeinMilliSeconds
	 * @return
	 * @throws ShippableException
	 *             Finds issues raised in particular time period
	 */
	private int findIssuesInPeriod(Map<String, String> repoDetails, RepositoryInfo repositoryInfo,
			Long timeinMilliSeconds) throws ShippableException {
		int count = 0;
		int totalOpenIssue = Integer.valueOf(repositoryInfo.getOpen_issues());
		if (Integer.valueOf(repositoryInfo.getOpen_issues()) == 0) {
			return count;
		}

		repoDetails.put(ShippableConstants.SINCE, getTimeInISO8601Format(timeinMilliSeconds));
		// else calculate
		int totalHits = totalOpenIssue % PAGE_SIZE;
		for (int i = 0; i < totalHits; i++) {
			setPagination(repoDetails, i);
			String generatedUrl = UrlGenerator.ISSUES_URL.generate(repoDetails);
			String listOfIssues = (String) gitRepoService.provisionService(generatedUrl, String.class);
			List<Issue> data = null;
			try {
				data = jacksonObjectMapper.readValue(listOfIssues, new TypeReference<List<Issue>>() {
				});
			} catch (Exception e) {
				e.printStackTrace();

			}
			count += data.size();
			if (data.size() < PAGE_SIZE) {
				break;
			}
		}

		return count;

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
	 * @return
	 * Can be moved to utility class
	 */
	private String getTimeInISO8601Format(long timeinMilliSeconds) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(tz);
		return df.format(new Date(timeinMilliSeconds));
	}

}
