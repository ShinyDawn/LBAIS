package process.impl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import process.service.SourceService;

public class ClassBehaviorSource implements SourceService {

	private static String dir;
	private static String[] sources;
	private static List<String> current = new LinkedList<String>();
	private static int index = 0;

	@Override
	public void init(String direct, String target) {
		dir = direct;
		if (target.contains("+")) {
			sources = target.split("\\+");
		} else {
			sources = new String[1];
			sources[0] = target;
		}
		File file = new File(dir + "/" + sources[0]);
		String[] fileList = file.list();
		for (int i = 0; i < fileList.length; i++) {
			current.add(fileList[i]);
		}
	}

	@Override
	public String getNextSource() {
		if (current.size() == 0) {
			index++;
			File file = new File(dir + "/" + sources[index]);
			String[] fileList = file.list();
			for (int i = 0; i < fileList.length; i++) {
				current.add(fileList[i]);
			}
			if (index == sources.length)
				index = 0;
		}
		String currentPath = dir + "/" + sources[index];
		String filename = current.get(0);
		current.remove(0);
		System.out.println(currentPath + "/" + filename);

		return currentPath + "/" + filename;
	}
}
