package service.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.WebSocketServer;
import service.entity.study.Alarm;
import service.entity.study.Pattern;
import service.service.StudyService;

@Controller
public class StudyController {
	@Autowired
	private StudyService study;

	// 获得报警信息（输入时间区间和班级编号）
	@RequestMapping(value = "/study/getalarm")
	@ResponseBody
	public List<Alarm> getAlarm(@RequestParam("type") String type, @RequestParam("cid") int cid) {
		return study.getAlarm(type, cid);
	}

	// 获得报警信息
	@RequestMapping(value = "/study/get_s_alarm")
	@ResponseBody
	public Alarm getAlarmById(@RequestParam("alarm_id") int aid) {
		return study.getSingleAlarm(aid);
	}

	@RequestMapping(value = "/study/alarmhandle")
	@ResponseBody
	// 获得处理类型和结果
	public String handleAlarm(@RequestParam("alarm_id") int aid, @RequestParam("h_type") int isHandle,
			@RequestParam("handle") String handle) throws Exception {
		try {
			study.handleAlarm(aid, isHandle, handle);
		} catch (Exception e) {
			return "操作失败";
		}
		return "操作成功";
	}

	// 获得报警信息（输入时间区间和班级编号）
	@RequestMapping(value = "/study/getpattern")
	@ResponseBody
	public List<Pattern> getPattern(@RequestParam("type") String type, @RequestParam("cid") int cid) {
		return study.getPattern(type, cid);
	}

	// 获得报警信息
	@RequestMapping(value = "/study/get_s_pattern")
	@ResponseBody
	public Pattern getPatternById(@RequestParam("pattern_id") int pid) {
		return study.getSinglePattern(pid);
	}

	@RequestMapping(value = "/study/patternhandle")
	@ResponseBody
	// 获得处理类型和结果
	public String handlePattern(@RequestParam("pattern_id") int pid, @RequestParam("h_type") int isHandle,
			@RequestParam("handle") String handle) throws Exception {
		try {
			study.handleAlarm(pid, isHandle, handle);
		} catch (Exception e) {
			return "操作失败";
		}
		return "操作成功";
	}

	// 弹出报警信息
	@RequestMapping(value = "/study/alertAlarm")
	@ResponseBody
	public void alertAlarm(@RequestParam("alarm_id") int alarm_id) throws IOException {
		Alarm result=study.getSingleAlarm(alarm_id);
		System.out.println("警报");
		WebSocketServer.sendInfo("有警报！");
	}

	// websocket
	@RequestMapping(value = "/pushVideoListToWeb", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> pushVideoListToWeb(@RequestBody Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
//		try {
//			WebSocketServer.sendInfo("有新客户呼入,sltAccountId:");
//			result.put("operationResult", true);
//		} catch (IOException e) {
//			result.put("operationResult", true);
//		}
		result.put("operationResult", true);
		return result;
	}
}
