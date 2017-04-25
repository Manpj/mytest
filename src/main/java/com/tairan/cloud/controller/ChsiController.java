package com.tairan.cloud.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("chsi")
public class ChsiController {

	private final static Logger logger = LoggerFactory.getLogger(ChsiController.class);


	@RequestMapping(value = "test.do", method = RequestMethod.GET)
	public String test() {
		return "test";

	}
	
	
}
