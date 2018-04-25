package service.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import service.dao.Score;
import service.dao.Subject;
import service.dao.Test;
import service.repository.ScoreRepository;
import service.repository.SubjectRepository;
import service.repository.TestRepository;
import service.service.SubjectService;
import service.tool.PathConfig;
import service.vo.SubjectVO;

@Service
public class SubjectImpl implements SubjectService {
	@Autowired
	private SubjectRepository subjectRepo;
	@Autowired
	private TestRepository testRepo;
	@Autowired
	private ScoreRepository ScoreRepo;

	@Override
	public List<SubjectVO> getSubject(int cid) {
		List<SubjectVO> list = new ArrayList<SubjectVO>();
		List<Subject> sList = subjectRepo.findByCid(cid);
		if (sList == null)
			return list;
		for (Subject s : sList) {
			int sid = s.getId();
			Double avgScore = ScoreRepo.average(cid, sid);
			double score = 0;
			if (avgScore != null) {
				score = (double) avgScore;
			}
			Integer num = testRepo.getTestNum(cid, sid);
			int number = 0;
			if (num != null) {
				number = (int) num;
			}
			SubjectVO sv = new SubjectVO();
			sv.setId(sid);
			sv.setName(s.getName());
			sv.setTname(s.getTname());
			sv.setAverage(score);
			sv.setNum(number);
			list.add(sv);
		}
		return list;
	}

	@Override
	public int upload(String fileName, int cid, int sid, String date) {
		int testId = -1;
		try {
			// 创建输入流，读取Excel
			InputStream is = new FileInputStream(PathConfig.upload + fileName);

			Workbook wb = new XSSFWorkbook(is);
			Sheet sheet = wb.getSheetAt(0);
			int firstRowIndex = sheet.getFirstRowNum();
			int lastRowIndex = sheet.getLastRowNum();

			Test test = new Test();
			test.setCid(cid);
			test.setSubjectid(sid);
			test.setDate(date);
			Test current = testRepo.save(test);
			testId = current.getId();

			for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
				Row row = sheet.getRow(rIndex);

				if (row != null) {
					int firstCellIndex = row.getFirstCellNum();
					int stuid = (int) Double.parseDouble(row.getCell(firstCellIndex).toString());
					double mark = Double.parseDouble(row.getCell(firstCellIndex + 1).toString());

					Score score = new Score();
					score.setCid(cid);
					score.setSid(stuid);
					score.setTestid(testId);
					score.setScore(mark);
					score.setSubjectid(sid);

					ScoreRepo.save(score);

				}
			}
			return 0;
		} catch (Exception e) {
			testRepo.delete(testId);
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int add(int cid, String name, String tname) {
		Subject subject = subjectRepo.findByCidAndName(cid, name);
		if (subject == null) {
			Subject s = new Subject();
			s.setCid(cid);
			s.setName(name);
			s.setTname(tname);
			Subject current = subjectRepo.save(s);
			return current.getId();
		}
		return -1;
	}

	@Override
	public int update(int id, int cid, String name, String tname) {
		Subject subject = subjectRepo.findByIdAndCidAndName(id, cid, name);
		if (subject == null) {
			subjectRepo.update(id, cid, name, tname);
			return 0;
		}
		return -1;
	}

	@Override
	public void delete(int id) {
		subjectRepo.delete(id);
	}
}
