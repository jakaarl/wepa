package wepa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {
	
	private static final String INDEX_TEMPLATE = "index";
	
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return INDEX_TEMPLATE;
	}
}
