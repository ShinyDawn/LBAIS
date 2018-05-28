package service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.service.InsertDataService;
import service.service.PositivityService;

@Controller
public class PositivityController {

	@Autowired
	private PositivityService positivityService;
	//@Autowired
	//private InsertDataService insertDataService;
	
	@RequestMapping(value = "/data")
    @ResponseBody
    public int[][] index(@RequestParam("cid") int cid,@RequestParam("type") int type) {
        return positivityService.getPosivity(type, cid);
    }
	
	@RequestMapping(value = "/sub")
    @ResponseBody
    public String[] getSubjects(@RequestParam("cid") int cid) {
		//insertDataService.insert();
        return positivityService.getSubjects(cid);
    }
}
