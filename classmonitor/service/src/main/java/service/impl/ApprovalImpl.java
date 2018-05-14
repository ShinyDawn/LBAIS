package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.entity.Absentee;
import service.repository.AbsenteeRepository;
import service.repository.CurriculumRepository;
import service.repository.StudentRepository;
import service.service.ApprovalService;
import service.util.DateUtil;
import service.util.MessageInfo;
import service.vo.ApprovalVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elva on 2018/5/11.
 */
@Service
public class ApprovalImpl implements ApprovalService {
    @Autowired
    private AbsenteeRepository absenteeRepository;
    @Autowired
    private CurriculumRepository curriculumRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<ApprovalVO> getApprovalList(int cid, int period) {
        List<ApprovalVO> approvalVOS = new ArrayList<>();

//        String start="2018-05-07";
        String start = DateUtil.getDate();
        String end = DateUtil.getFutureDate(period, start);

        List<Absentee> absentees = absenteeRepository.findAll(cid,start,end);

        if (absentees ==null || absentees.isEmpty()){
            return approvalVOS;
        }
        for(Absentee absentee:absentees){
            String courseName =curriculumRepository.getCourseName(cid,absentee.getTid(),absentee.getDate());
            ApprovalVO approvalVO = new ApprovalVO();
            approvalVO.setCourse(courseName);
            approvalVO.setCourseTime(absentee.getTid());
            approvalVO.setDate(absentee.getDate());
            approvalVO.setId(absentee.getId());
            approvalVO.setName(studentRepository.getName(cid,absentee.getSid()));
            approvalVO.setReason(absentee.getReason());
            approvalVO.setType(absentee.getType());
            approvalVO.setSid(absentee.getSid());
            approvalVOS.add(approvalVO);
        }
        return approvalVOS;
    }

    @Override
    public MessageInfo addApproval(int cid, int sid, String date, String tids_String, String type, String reason) {

        String[] tids = tids_String.split(",");
        if (tids.length ==0){;
            return new MessageInfo(false,"请假时间段不可为空");
        }
        for (int i =0;i<tids.length;i++){
            int tid =Integer.parseInt(tids[i]);
            if (absenteeRepository.isApproved(cid,sid,date,tid)==1){
                return new MessageInfo(false,date+" "+tid+" 该时段已请假，不可重复添加");
            }
            if(curriculumRepository.isOnSchool(cid,tid,date)==0){
                return new MessageInfo(false,date+" "+tid+" 该时段没有课程，不需请假");
            }
        }
        for (int i =0;i<tids.length;i++){
            int tid =Integer.parseInt(tids[i]);
            Absentee absentee = new Absentee();
            absentee.setCid(cid);
            absentee.setDate(date);
            absentee.setSid(sid);
            absentee.setTid(tid);
            absentee.setType(type);
            absentee.setReason(reason);
            absenteeRepository.save(absentee);
        }
        return new MessageInfo(true,"新增请假说明成功");
    }


    @Override
    public int deleteApproval(int id) {
        absenteeRepository.deleteById(id);
        return 1;
    }

    public ApprovalVO showInfo(int id){
        /**
         * 这两个可以保障一定有
         */
        Absentee absentee = absenteeRepository.findOne(id);
        String courseName =curriculumRepository.getCourseName(absentee.getCid(),absentee.getTid(),absentee.getDate());

        ApprovalVO approvalVO = new ApprovalVO();
        approvalVO.setCourse(courseName);
        approvalVO.setCourseTime(absentee.getTid());
        approvalVO.setDate(absentee.getDate());
        approvalVO.setId(absentee.getId());
        approvalVO.setName(studentRepository.getName(absentee.getCid(),absentee.getSid()));
        approvalVO.setReason(absentee.getReason());
        approvalVO.setType(absentee.getType());
        approvalVO.setSid(absentee.getSid());

        return approvalVO;
    }

    @Override
    public MessageInfo updateApproval(int id,int cid, int sid, String date, String tids_String, String type, String reason) {

        String[] tids = tids_String.split(",");
        if (tids.length ==0){;
            return new MessageInfo(false,"请假时间段不可为空");
        }

        for (int i =0;i<tids.length;i++){
            int tid =Integer.parseInt(tids[i]);
            if (absenteeRepository.isApproved(cid,sid,date,tid)==1){
                return new MessageInfo(false,date+" 第"+tid+"节 该时段已请假，不可重复添加");
            }
            if(curriculumRepository.isOnSchool(cid,tid,date)==0){
                return new MessageInfo(false,date+" 第"+tid+"第 该时段没有课程，不需请假");
            }
        }
        for (int i =0;i<tids.length;i++){
            int tid =Integer.parseInt(tids[i]);
            absenteeRepository.updateById(id,cid,sid,date,tid,reason,type);
        }

        return null;
    }

}
