package com.heroku.shippable.generators;

import static com.heroku.shippable.constants.ShippableConstants.templateValidPattern;

import java.util.Map;

import com.heroku.shippable.constants.IssueParameterBuilder;
import com.heroku.shippable.constants.UrlFormatters;
import com.heroku.shippable.exception.ShippableException;

/**
 * @author Amit
 * Generates URL as per selected rule.
 */
public enum UrlGenerator {

	ISSUES_URL(UrlFormatters.REPO_ISSUES) {
		@Override
		public String generate(Map<String, String> changes) throws ShippableException {
			String url = expand_template(this.getUrlFormatters(), changes, true);
			return IssueParameterBuilder.buildExtraParams(changes, url);
		}
	},
	REPO_INFO(UrlFormatters.REPO_INFO) {
		@Override
		public String generate(Map<String, String> changes) throws ShippableException {
			String url = expand_template(this.getUrlFormatters(), changes, true);
			return IssueParameterBuilder.buildExtraParams(changes, url);
		}
	};

	public UrlFormatters getUrlFormatters() {
		return urlFormatters;
	}

	public void setUrlFormatters(UrlFormatters urlFormatters) {
		this.urlFormatters = urlFormatters;
	}

	private UrlFormatters urlFormatters;

	/**
	 * @param urlFormatters
	 */
	private UrlGenerator(UrlFormatters urlFormatters) {
		this.urlFormatters = urlFormatters;
	}

	/**
	 * Looks for ${} in the text and replaces with matching key values from the
	 * datamap
	 * 
	 * @param text
	 * @param changes
	 * @param exceptionOnMissingFields
	 *            if true, if the string has ${} remaining, throws an exception
	 * @return
	 * @throws Exception
	 */
	private static String expand_template(UrlFormatters formatter, Map<String, String> changes,
			boolean exceptionOnMissingFields) throws ShippableException {
		if (changes == null)
			return formatter.getFormat();
		String ntext = formatter.getFormat();
		for (Map.Entry<String, String> e : changes.entrySet()) {
			ntext = ntext.replaceAll("\\$\\{" + e.getKey() + "\\}", e.getValue());
		}
		if (exceptionOnMissingFields && templateValidPattern.matcher(ntext).find()) {
			throw new ShippableException();
		}
		return ntext;
	}

	public abstract String generate(Map<String, String> changes) throws ShippableException;

}
