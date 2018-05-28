package service.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.service.ApprovalService;
import service.util.DateUtil;
import service.util.MessageInfo;
import service.vo.ApprovalVO;

import java.util.List;

/**
 * Created by elva on 2018/5/11.
 */
@Controller
public class ApprovalController {
    @Autowired
    private ApprovalService approvalService;

    @RequestMapping(value = "/approval")
    @ResponseBody
    public List<ApprovalVO> getApprovalList(@RequestParam("cid") int cid,@RequestParam("period") int period ){
        String start = DateUtil.getDate();
        String end = DateUtil.getFutureDate(period, start);
        return approvalService.getApprovalList(cid,start,end);
    }

    @RequestMapping(value = "/approval/add")
    @ResponseBody
    public MessageInfo add(@RequestParam("cid") int cid, @RequestParam("sid") int sid,@RequestParam("dates") String dates, @RequestParam("tids") String tids,
                           @RequestParam("type") String type, @RequestParam("reason") String reason) {
        return approvalService.addApproval(cid,sid,dates,tids,type,reason);
    }

    @RequestMapping(value = "/approval/show")
    @ResponseBody
    public ApprovalVO showInfo(@RequestParam("id") int id){
        return approvalService.showInfo(id);
    }

    @RequestMapping(value = "/approval/del")
    @ResponseBody
    public  int del(@RequestParam("id") int id){
        return approvalService.deleteApproval(id);
    }


}
