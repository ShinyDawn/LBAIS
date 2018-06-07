package service.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import service.service.SubjectService;
import service.util.IOHelper;
import service.vo.SubjectVO;

@Controller
public class SubjectController {

	@Autowired
	private SubjectService subjectService;

	@RequestMapping(value = "/subject")
	@ResponseBody
	public List<SubjectVO> index(@RequestParam("cid") int cid) {
		return subjectService.getSubject(cid);
	}

	@RequestMapping(value = "/subject/upload")
	@ResponseBody
	public int upload(@RequestParam MultipartFile uploadFile, @RequestParam String date, @RequestParam int sid,
			@RequestParam int cid) {
		IOHelper ioHelper = new IOHelper();
		String fileName = ioHelper.upload(uploadFile);
		if (fileName != null) {
			return subjectService.upload(fileName, cid, sid, date);
		}
		return -1;
	}

	@RequestMapping(value = "/subject/download", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
		IOHelper ioHelper = new IOHelper();
		FileSystemResource file = ioHelper.getDownloadResource();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"",
				new String(file.getFilename().getBytes("gb2312"), "ISO8859-1")));
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok().headers(headers).contentLength(file.contentLength())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(new InputStreamResource(file.getInputStream()));
	}

	@RequestMapping(value = "/subject/add")
	@ResponseBody
	public int add(@RequestParam("id") int id, @RequestParam("cid") int cid, @RequestParam("name") String name,
			@RequestParam("tname") String tname) {
		return subjectService.add(cid, name, tname);
	}

	@RequestMapping(value = "/subject/update")
	@ResponseBody
	public int update(@RequestParam("id") int id, @RequestParam("cid") int cid, @RequestParam("name") String name,
			@RequestParam("tname") String tname) {
		return subjectService.update(id, cid, name, tname);
	}

	@RequestMapping(value = "/subject/delete")
	@ResponseBody
	public void delete(@RequestParam("id") int id) {
		subjectService.delete(id);
	}
}
