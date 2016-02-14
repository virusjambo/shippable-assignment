package com.heroku.shippable.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.heroku.shippable.exception.ShippableException;
import com.heroku.shippable.model.output.IssueOuput;
import com.heroku.shippable.service.IssueService;

/**
 * @author Amit Jambotkar 
 * Controller mappings for rest service
 */
@Controller
@RequestMapping("/info")
public class DashBoardController {

	@Autowired
	private IssueService issueService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public IssueOuput home(@RequestParam String value) throws ShippableException, Exception {

		return issueService.execute(value);
	}

}
