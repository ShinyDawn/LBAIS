package service.service;

import service.util.MessageInfo;
import service.vo.ApprovalVO;

import java.util.List;

/**
 * Created by elva on 2018/5/9.
 */
public interface ApprovalService {
    public List<ApprovalVO> getApprovalList(int cid, String startDate, String endDate);

    public MessageInfo addApproval(int cid, int sid, String dates,String tids, String type, String reason);

    public int deleteApproval(int id);

    public ApprovalVO showInfo(int id);

    public MessageInfo updateApproval(int id,int cid, int sid, String dates, String tids, String type, String reason);

}
