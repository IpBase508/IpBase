package com.ygip.ipbase_android.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XQM on 2017/7/14.
 */

public class StringUtils {
    private static final String EMAIL_CHECK = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";


    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 顶级域名判断；如果要忽略大小写，可以直接在传入参数的时候toLowerCase()再做判断
     * @param str
     * @return
     */
    public static boolean isTopURL(String str){
        //转换为小写
        str = str.toLowerCase();
        String domainRules = "com.cn|net.cn|org.cn|gov.cn|com.hk|公司|中国|网络|com|net|org|int|edu|gov|mil|arpa|Asia|biz|info|name|pro|coop|aero|museum|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cf|cg|ch|ci|ck|cl|cm|cn|co|cq|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|eh|es|et|ev|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gh|gi|gl|gm|gn|gp|gr|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|in|io|iq|ir|is|it|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|ml|mm|mn|mo|mp|mq|mr|ms|mt|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nt|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|pt|pw|py|qa|re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|st|su|sy|sz|tc|td|tf|tg|th|tj|tk|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|va|vc|ve|vg|vn|vu|wf|ws|ye|yu|za|zm|zr|zw";
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "(([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]+\\.)?" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "("+domainRules+"))" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher isUrl = pattern.matcher(str);
        return isUrl.matches();
    }

    /**
     * 判断字符串是否包含中文
     * @param str
     * @return
     */
    public static boolean isContainsChinese(String str)
    {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()){
            flg = true;
        }
        return flg;
    }
    /**
     * 判断字符串是不是密码（符不符合密码要求）
     * @param str
     * @return
     */
    public static boolean isPassword(String str){
        if (TextUtils.isEmpty(str)) return false;
        String regEx = "^\\w{6,12}$";
        Pattern pattern = Pattern.compile(regEx);
        return pattern.matcher(str).matches();
    }


    /**
     * 字符串(以逗号为分割符)转List
     * @param s
     * @return
     */
    public static List<String> stringToList(String s){
        if (TextUtils.isEmpty(s)) return new ArrayList<>();
        String ss[] = s.split(",");
        return Arrays.asList(ss);
    }

    /**
     * 字符串(以任何为分割符)转List
     * @param s
     * @param separator
     * @return
     */
    public static List<String> stringToList(String s, String separator) {
        if (TextUtils.isEmpty(s)) return new ArrayList<>();
        String[] ss = s.split(separator);
        return Arrays.asList(ss);
    }

    /**
     * 判断手机号码
     * @param str
     * @return
     */
    public static boolean isPhone(String str) {
        if (TextUtils.isEmpty(str)) return false;
        Pattern pattern = Pattern.compile("[0-9]{11}");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 判断是不是邮箱
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        if (TextUtils.isEmpty(str)) return false;
        Pattern pattern = Pattern.compile(EMAIL_CHECK);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
