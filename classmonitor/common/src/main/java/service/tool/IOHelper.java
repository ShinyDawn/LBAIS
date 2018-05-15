package service.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import service.tool.PathConfig;

public class IOHelper {

	public BufferedReader read(String tar) {
		String pathname = tar;
		BufferedReader br = null;
		try {
			File filename = new File(pathname);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
			br = new BufferedReader(reader);
		} catch (Exception e) {
		}
		return br;
	}

	public FileSystemResource getDownloadResource() {
		FileSystemResource file = new FileSystemResource(PathConfig.download);
		return file;
	}

	public String upload(MultipartFile uploadFile) {
		String fileName = PathConfig.upload + uploadFile.getOriginalFilename();
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
		
		try {
			byte[] bytes = uploadFile.getBytes();
			Path path = Paths.get(fileName);
			Files.write(path, bytes);
			return uploadFile.getOriginalFilename();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
