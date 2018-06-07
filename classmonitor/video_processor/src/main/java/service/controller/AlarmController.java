package service.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.impl.SelfstudyImpl;
import service.service.SelfstudyService;

@Controller
public class AlarmController {
	
	@Autowired
	private SelfstudyService selfstudy;

	@RequestMapping(value = "/selfstudy")
    public String selfstudy(){
		return "selfstudy";
    }
	
	@RequestMapping(value = "/pattern")
    public String index() throws Exception {
		try {
		int cid=Integer.parseInt(getConfig("classroom"));
		selfstudy.checkPattern(2);
		} catch (Exception e) {
			return "运行失败";
		}
		return "运行完成";
    }
	
	@RequestMapping(value = "/takeClass")
	@ResponseBody
	public String takeClass( @RequestParam("num") int num){
		System.out.println("TTTTTTTTTTTTTTT"+num);
		try {
			int cid=Integer.parseInt(getConfig("classroom"));
			String target=getConfig("json_location");
			
	        target=target+"\\IMG_"+num;
	        String video="../video/alarm"+(num-1959)+".mp4";
	        System.out.println(target+"  "+video);
			selfstudy.takeClass(target, cid, video);
		} catch (Exception e) {
			return "运行失败";
		}
		return "运行完成";
	}
	
	public static String getConfig(String key) throws Exception{
		File file = ResourceUtils.getFile("classpath:config.conf");
		String target="";
        if(file.exists()){
        	FileReader fre=new FileReader(file);
    		BufferedReader bre=new BufferedReader(fre);
    		String str="";
    		while((str=bre.readLine())!=null)
    		{
    			if(str.contains(key)){
    				target=str.substring(str.indexOf("=")+1).trim();
    			}
    		}
    		bre.close();
            fre.close();
    	}
        return target;
	}
	
}
