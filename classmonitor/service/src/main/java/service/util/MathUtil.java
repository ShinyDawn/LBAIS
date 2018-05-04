package service.util;

import service.vo.StudentDataVO;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by elva on 2018/5/5.
 */
public class MathUtil {

    public static int getSort(List<StudentDataVO> list, int sid) {
        //因为是排名,所以降序排序
        Collections.sort(list, new Comparator<StudentDataVO>() {
            @Override
            public int compare(StudentDataVO o1, StudentDataVO o2) {
                if (o1.getData() > o2.getData()) {
                    return -1;
                } else if (o1.getData() == o2.getData()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        int sort = 0;
        //因为list从0开始,所以要+1
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSid() == sid) {
                if (i != 0 && (list.get(i - 1).getData() == list.get(i).getData())) {
                    sid = list.get(i - 1).getSid();
                    //i=i-1;之后i++，故再-1
                    i -= 2;
                    continue;
                }
                sort = i + 1;
                break;
            }
        }
        return sort;
    }
}
