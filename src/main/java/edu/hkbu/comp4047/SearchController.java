package edu.hkbu.comp4047;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
	@RequestMapping("/keyword")
	public String SearchResult(@RequestParam String keywords, Model model) {
		List<String> URLs = new ArrayList<String>();
		URLs.add("http://www.google.com/");
		URLs.add("http://www.yahoo.com/");
		model.addAttribute("URLs", URLs);
		model.addAttribute("query", keywords);
		return "result";
	}
	public String SearchForKeyword(String keyword) {
		
		
		
		return "result";
	} 
	
	
}
