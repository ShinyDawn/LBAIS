package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.entity.Absentee;
import service.repository.AbsenteeRepository;
import service.repository.BehaviorRepository;
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
    @Autowired
    private BehaviorRepository behaviorRepository;

    @Override
    public List<ApprovalVO> getApprovalList(int cid, String startDate,String endDate) {
        List<ApprovalVO> approvalVOS = new ArrayList<>();


        List<Absentee> absentees = absenteeRepository.findAll(cid, startDate, endDate);

        if (absentees == null || absentees.isEmpty()) {
            return approvalVOS;
        }
        for (Absentee absentee : absentees) {
            String courseName = curriculumRepository.findByTidAndCidAndDate(absentee.getTid(), cid,absentee.getDate()).getCourse();
            ApprovalVO approvalVO = new ApprovalVO();
            approvalVO.setCourse(courseName);
            approvalVO.setCourseTime(absentee.getTid());
            approvalVO.setDate(absentee.getDate());
            approvalVO.setId(absentee.getId());
            approvalVO.setName(studentRepository.findByCidAndSid(cid, absentee.getSid()).getName());
            approvalVO.setReason(absentee.getReason());
            approvalVO.setType(absentee.getType());
            approvalVO.setSid(absentee.getSid());
            approvalVOS.add(approvalVO);
        }
        return approvalVOS;
    }

    @Override
    public MessageInfo addApproval(int cid, int sid, String date_String, String tids_String, String type, String reason) {

        String[] tids = tids_String.split(",");
        if (tids.length == 0) {
            ;
            return new MessageInfo(false, "请假时间段不可为空");
        }
        String[] dates = date_String.split(",");

        if (dates.length == 0) {
            ;
            return new MessageInfo(false, "请假时间段不可为空");
        }
        for (String date : dates) {
            for (int i = 0; i < tids.length; i++) {
                if (tids[i].equals("")) {
                    continue;
                }
                int tid = Integer.parseInt(tids[i]);
                if (absenteeRepository.countByCidAndSidAndDateAndTid(cid, sid, date, tid) == 1) {
                    return new MessageInfo(false, date + " " + tid + " 该时段已请假，不可重复添加");
                }
                if (curriculumRepository.countByCidAndTidAndDate(cid, tid, date) == 0) {
                    return new MessageInfo(false, date + " " + tid + " 该时段没有课程，不需请假");
                }
            }
            for (int i = 0; i < tids.length; i++) {
                if (tids[i].equals("")) {
                    continue;
                }
                int tid = Integer.parseInt(tids[i]);
                Absentee absentee = new Absentee();
                absentee.setCid(cid);
                absentee.setDate(date);
                absentee.setSid(sid);
                absentee.setTid(tid);
                absentee.setType(type);
                absentee.setReason(reason);
                absenteeRepository.save(absentee);
                behaviorRepository.updateStatus(cid,sid,date,tid,"已请假");
            }
        }


        return new MessageInfo(true, "新增请假说明成功");
    }


    @Override
    public int deleteApproval(int id) {
        absenteeRepository.deleteById(id);
        return 1;
    }

    public ApprovalVO showInfo(int id) {
        /**
         * 这两个可以保障一定有
         */
        Absentee absentee = absenteeRepository.findOne(id);
        String courseName = curriculumRepository.findByTidAndCidAndDate( absentee.getTid(),absentee.getCid(), absentee.getDate()).getCourse();

        ApprovalVO approvalVO = new ApprovalVO();
        approvalVO.setCourse(courseName);
        approvalVO.setCourseTime(absentee.getTid());
        approvalVO.setDate(absentee.getDate());
        approvalVO.setId(absentee.getId());
        approvalVO.setName(studentRepository.findByCidAndSid(absentee.getCid(), absentee.getSid()).getName());
        approvalVO.setReason(absentee.getReason());
        approvalVO.setType(absentee.getType());
        approvalVO.setSid(absentee.getSid());

        return approvalVO;
    }

    @Override
    public MessageInfo updateApproval(int id, int cid, int sid, String date, String tids_String, String type, String reason) {

        String[] tids = tids_String.split(",");
        if (tids.length == 0) {
            ;
            return new MessageInfo(false, "请假时间段不可为空");
        }

        for (int i = 0; i < tids.length; i++) {
            int tid = Integer.parseInt(tids[i]);
            if (absenteeRepository.countByCidAndSidAndDateAndTid(cid, sid, date, tid) == 1) {
                return new MessageInfo(false, date + " 第" + tid + "节 该时段已请假，不可重复添加");
            }
            if (curriculumRepository.countByCidAndTidAndDate(cid, tid, date) == 0) {
                return new MessageInfo(false, date + " 第" + tid + "第 该时段没有课程，不需请假");
            }
        }
        for (int i = 0; i < tids.length; i++) {
            int tid = Integer.parseInt(tids[i]);
            absenteeRepository.updateById(id, cid, sid, date, tid, reason, type);
        }

        return null;
    }

}
