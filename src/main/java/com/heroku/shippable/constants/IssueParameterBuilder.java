package com.heroku.shippable.constants;

import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * @author Amit
 *  Builds any extra parameters needed for querying git repository.
 *  Only few are added extendible.
 *  Some of the redundant code can be removed
 */
public enum IssueParameterBuilder {
	// More parameters can be added later.According to problem I need only since
	// and Pagination

	SINCE_FROM(ShippableConstants.SINCE) {
		@Override
		public String build(Map<String, String> params, String issuesUrl) {
			if (params.containsKey(param) && StringUtils.hasText(params.get(param))) {
				return issuesUrl += param + "=" + params.get(param) + "&";
			}
			return issuesUrl;
		}
	},
	PAGE_NO(ShippableConstants.PAGE) {
		@Override
		public String build(Map<String, String> params, String issuesUrl) {
			if (params.containsKey(param) && StringUtils.hasText(params.get(param))) {
				return issuesUrl += param + "=" + params.get(param) + "&";
			}
			return issuesUrl;
		}
	},
	PAGE_PER(ShippableConstants.PER_PAGE) {
		@Override
		public String build(Map<String, String> params, String issuesUrl) {
			if (params.containsKey(param) && StringUtils.hasText(params.get(param))) {
				return issuesUrl += param + "=" + params.get(param) + "&";
			}
			return issuesUrl;
		}
	};
	/**
	 * @param param
	 */
	private IssueParameterBuilder(String param) {
		this.param = param;
	}

	String param;

	public abstract String build(Map<String, String> params, String issuesUrl);

	public static String buildExtraParams(Map<String, String> params, String issuesUrl) {

		for(IssueParameterBuilder builder:IssueParameterBuilder.values()){
			issuesUrl=builder.build(params, issuesUrl);
		}
		
		return  (issuesUrl.endsWith("&")) ? issuesUrl.substring(0,issuesUrl.lastIndexOf("&")) : issuesUrl;
	}

}
