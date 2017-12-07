package com.ygip.ipbase_android.mvp.universalModel;

/**
 * Created by LockyLuo on 2017/9/20.
 * api接口，注意部分假删除是放在put类里
 */

public class ApiUrl {
    private static final String HTTP_URL = "http://182.254.222.51:8888/base/api/";


    public class Get {
        public static final String GET_ATTEND_URL = HTTP_URL + "attend?";
        public static final String GET_PROJECT_URL = HTTP_URL + "project?";
        public static final String GET_DYNAMIC_URL = HTTP_URL + "dynamic?";
        public static final String GET_NOTICE_URL = HTTP_URL + "notice?";
        public static final String GET_USER_URL = HTTP_URL + "user?";
        public static final String GET_RECYCLE_URL = HTTP_URL + "recycle?";
    }

    public class Post{
        public static final String LOGIN_USER_URL = HTTP_URL + "login";
        public static final String POST_USER_URL = HTTP_URL + "user";
        public static final String POST_USER_BATCH_IMPORT_URL = HTTP_URL + "user/excel";
        public static final String POST_ATTEND_URL = HTTP_URL + "attend";
        public static final String POST_ATTENDANCE_CHECK = HTTP_URL + "check";
        public static final String POST_PROJECT_URL = HTTP_URL + "project";
        public static final String POST_DYNAMIC_URL = HTTP_URL + "dynamic";
        public static final String POST_NOTICE_URL = HTTP_URL + "notice";
        public static final String POST_FILE_URL = HTTP_URL + "qiniu/info";
    }

    public class Put{
        public static final String DEL_DYNAMIC_FAKE_URL = HTTP_URL + "dynamic/batchDelete";
        public static final String DEL_PROJECT_FAKE_URL = HTTP_URL + "project/batchDelete";
        public static final String PUT_NOTICE_URL = HTTP_URL + "notice";
        public static final String PUT_USER_URL = HTTP_URL + "user";
        public static final String FIND_USER_URL = HTTP_URL + "user/getMessage";
        public static final String FORGET_PASSWORD_URL = HTTP_URL + "user/forgetPassword";
        public static final String PUT_USER_ADMIN_URL = HTTP_URL + "user/updatePower";
        public static final String PUT_ATTEND_URL = HTTP_URL + "attend";

    }

    public class Del{
        public static final String DEL_ATTEND_URL = HTTP_URL + "attend/";
        public static final String DEL_NOTICE_URL = HTTP_URL + "notice/";
        public static final String DEL_USER_ADMIN_URL = HTTP_URL + "user/";
    }
}
