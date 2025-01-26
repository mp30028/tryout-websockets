package com.zonesoft.person.ui.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/")
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
            
    @GetMapping(path = "/greeting")
    @ResponseBody
    public String greeting(){
    	LOGGER.debug("FROM HomeController: request received by /person/ui/greeting ");
		StringBuilder htmlResponse = new StringBuilder();		
			htmlResponse.append("<h3>");
				htmlResponse.append("Greetings World");
			htmlResponse.append("</h3>");
			htmlResponse.append("<br/><br/>");
		return htmlResponse.toString();
    }

   
}