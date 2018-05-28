package service.util;

import service.vo.StudentDataVO;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by elva on 2018/5/5.
 */
public class MathUtil {

    //方差s^2=[(x1-x)^2 +...(xn-x)^2]/n
    public static double Variance(double[] x) {
        int m = x.length;
        double sum = 0;
        for (int i = 0; i < m; i++) {//求和
            sum += x[i];
        }
        double dAve = sum / m;//求平均值
        double dVar = 0;
        for (int i = 0; i < m; i++) {//求方差
            dVar += (x[i] - dAve) * (x[i] - dAve);
        }
        return dVar / m;
    }

    //标准差σ=sqrt(s^2)
    public static double StandardDiviation(double[] x) {
        int m = x.length;
        double sum = 0;
        for (int i = 0; i < m; i++) {//求和
            sum += x[i];
        }
        double dAve = sum / m;//求平均值
        double dVar = 0;
        for (int i = 0; i < m; i++) {//求方差
            dVar += (x[i] - dAve) * (x[i] - dAve);
        }
        return Math.sqrt(dVar / m);
    }

    //标准差σ=sqrt(s^2)
    public static double StandardDiv(List<Double> x) {
        int m = x.size();
        if (m == 0) {
            return 0;
        }
        double sum = 0;
        for (int i = 0; i < m; i++) {//求和
            sum += x.get(i);
        }
        double dAve = sum * 1.0 / m;//求平均值

        double dVar = 0;
        for (int i = 0; i < m; i++) {//求方差
            dVar += (x.get(i) - dAve) * (x.get(i) - dAve);
        }
        return Math.sqrt(dVar / m);
    }

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

    public static double estimate(List<Double> x, List<Double> y, double input)
    {
        double a = getA(x, y);
        double b = getB(x, y);
        System.out.println("线性回归系数a值：\t" + a + "\n" + "线性回归系数b值：\t" + b);
        return (a * input + b);
    }

    /*
     * 杜航 功能：返回x的系数a 公式：a = ( n sum( xy ) - sum( x ) sum( y ) ) / ( n sum( x^2 )
     * - sum(x) ^ 2 )
     */
    public static double getA(List<Double> x, List<Double> y)
    {
        int n = x.size();
        return (double) ((n * pSum(x, y) - sum(x) * sum(y)) / (n * sqSum(x) - Math
                .pow(sum(x), 2)));
    }

    /*
     * 功能：返回常量系数系数b 公式：b = sum( y ) / n - a sum( x ) / n
     */
    public static double getB(List<Double> x, List<Double> y)
    {
        int n = x.size();
        double a = getA(x, y);
        return sum(y) / n - a * sum(x) / n;
    }

    /*
     * 功能：求和
     */
    private static double sum(List<Double> ds)
    {
        double s = 0;
        for (double d : ds)
        {
            s = s + d;
        }
        return s;
    }

    /*
     * 功能：求平方和
     */
    private static double sqSum(List<Double> ds)
    {
        double s = 0;
        for (double d : ds)
        {
            s = (double) (s + Math.pow(d, 2));
        }
        return s;
    }

    /*
     * 功能：返回对应项相乘后的和
     */
    private static double pSum(List<Double> x, List<Double> y)
    {
        double s = 0;
        for (int i = 0; i < x.size(); i++)
        {
            s = s + x.get(i) * y.get(i);
        }
        return s;
    }


}
