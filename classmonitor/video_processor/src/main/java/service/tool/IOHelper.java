package service.tool;

import org.apache.commons.io.FileUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import service.util.PathConfig;

import java.awt.geom.Point2D;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elva on 2018/5/16.
 */
public class IOHelper {
    public static List<List<Point2D>> dealWithJson() throws Exception {
    	String pathStart = "E:\\百度云下载\\2018.5.4\\jsonOut\\IMG_1960\\IMG_1960_00000000";
//        String pathStart = "D:/workspace\\video\\jsonOut/IMG_1961/IMG_1961_00000000";
        DecimalFormat f = new DecimalFormat("0000");
        String pathEnd = "_keypoints.json";

        int start = 0;
        int end = 2700;

        List<List<Point2D>> result = new ArrayList<>();
        for (int j = start; j < end; j++) {
            String pathIn = f.format(j);
            String path = pathStart + pathIn + pathEnd;
            File file = new File(path);

            List<Point2D> current = new ArrayList<>();

            String content = FileUtils.readFileToString(file);
            JSONObject object = JSONObject.fromObject(content);
            JSONArray jsonArray = object.getJSONArray("people");

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject people = jsonArray.getJSONObject(i);
                JSONArray pose = people.getJSONArray("pose_keypoints_2d");
                Point2D.Double p = new Point2D.Double(pose.getDouble(3), pose.getDouble(4));
                current.add(p);
            }
            result.add(current);
        }
        return result;
    }

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
