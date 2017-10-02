package Constants;


import com.cli.knowledgebase.R;

/**
 * Created by user on 6/29/2015.
 */
public class Const {

    // prefname
    public static String PREF_NAME = "VOYAGEUP_EVENTS";


    public static final String EXTRA_MESSAGE = "message";
    public static final String RESULT = "result";
    public static final String REGID = "regid";

    public static String imagePAth = "";

    // for pagination
    public static int API_LIMIT = 10;


    public final static String DEFAULT_NETWORK_ID = "-1";
    public final static int MAXIMUM_MESSAGE_RESENDING_ATTEMPTS = 3;
    public static final String CONSTANT_OS_TYPE = "android";

    public static String current_ChatUser = "";

    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


    public static final int menu_bg[] = {R.mipmap.bg,
            R.mipmap.bg_1,
            R.mipmap.bg_2,
            R.mipmap.bg_4,
            R.mipmap.bg_5, R.mipmap.bg_6};


    public class ServiceType {
//        public static final String BASE_URL = "http://54.209.36.116/noplex0/rest_server/index.php/";
        public static final String BASE_URL = "http://13.228.49.46/noplex0/rest_server/index.php/";

        public static final String BASE_URL_LOCAL = "http://192.168.18.192/cli/index.php/";

        public static final String TOPICS_URL = "http://54.209.36.116/noplex0/rest_server/scormFiles/";

    }


    public class Params {

        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String SPECIALIZATION_ID = "specialization_id";

        public static final String COURSE_ID = "course_id";
        public static final String SEM_ID = "sem_id";
        public static final String SEMESTER = "semester";
        public static final String YEAR = "year";
        public static final String TYPE = "type";

        public static final String SPECIALIZATIONID = "specializationId";
        public static final String UNIVERSITY_ID = "universityId";
        public static final String MOBILE = "mobile";
        public static final String ADMISSION_YEAR = "admission_year";

        public static final String QUIZ_ID = "quiz_id";
        public static final String STUDENT_ID = "student_id";
        public static final String SCORE = "score";
        public static final String ENTITY_TYPE_ID = "entity_type_id";
        public static final String ENTITY_TYPE_NAME = "entity_type_name";
        public static final String USER_FILE = "userfile";
        public static final String USER_ID = "user_id";
        public static final String ANSWER = "answer";
        public static final String QUESTION_ID = "question_id";
        public static final String FILE_ID = "file_id";

        public static final String START = "start";
        public static final String LIMIT = "limit";
        public static final String STATUS = "status";
        public static final String TAG = "tag";
        public static final String ANNOUNCEMENT_ID = "announcement_id";
        public static final String CONTENT = "content";

        public static final String TITLE = "title";
        public static final String ANNOUNCEMENT_MSG = "announcement_msg";
        public static final String ID = "id";

        public static final String UNIVERSITYID = "university_id";

        public static final String TEAM_LEAD_ID = "team_lead_id";
        public static final String TEAM_NAME = "team_name";
        public static final String DEPT_ID = "dept_id";
        public static final String STAGE = "stage";
        public static final String SCENARIO_ID = "scenario_id";
        public static final String SCENARIO_STATUS = "scenario_status";
        public static final String MODULE = "module";
        public static final String TEAM_ID = "team_id";
        public static final String TEAM_SPECILIZATION_ID = "specilization_id";

        public static final String TEAM_MEMBER_USER_ID = "team_member_user_id";
        public static final String MILESTONE_NAME = "milestone_name";

        public static final String PROFILE_URL = "profile_url";

        public static final String CURRENT_PASSWORD = "currentPassword";
        public static final String NEW_PASSWORD = "newPassword";


    }


    public class IntentParams {

        public static final String BROADCAST_BUNDLE = "bundle";
        public static final String INTENT_BUNDLE = "intent_bundle";
        public static final String SPECIALIZATION_DATA = "specialization_data";
        public static final String QUIZ_DATA = "quiz_data";
        public static final String ASSIGNMENT_DATA = "assignment_data";
        public static final String FILE_DETAILS_DATA = "file_details_data";
        public static final String PATH = "path";


        public static final String ENTITY_TYPE_ID = "entity_type_id";
        public static final String STATUS = "status";
        public static final String USER_ID = "user_id";

        public static final String DISCUSSION_TAGS = "discussion_tags";
        public static final String BLOG_DATA = "blog_data";

        public static final String COMPETITION_DATA = "competition_data";

        public static final java.lang.String URL = "url";
    }


    public class IntentFilter {

        public static final String SELECT_PATH = "select_path";
        public static final String UPDATE_BLOG = "updateBlog";

    }

    public class FileNames {

        public static final String QUIZ = "quiz_";
        public static final String ASSIGNMENT = "assignment_";
        public static final String FILE_EXTENSION = ".txt";


    }

    public class FRAGMENT_TAG {

        public static final String FRAGMENT_SUBJECT = "frag_subject";
        public static final String FRAGMENT_PROJECT = "frag_project";
        public static final String FRAGMENT_DISCUSSION = "frag_discussion";
        public static final String FRAGMENT_BLOG = "frag_blog";
        public static final String FRAGMENT_COMPETITION = "frag_competition";

    }

    public class Json_Parms {

        public static final String CODE = "code";
        public static final String TOKEN = "token";
        public static final String USER = "user";
        public static final String ID = "id";
        public static final String COURSE = "Course";

        public static final String CATEGORY = "category";
        public static final String FULL_NAME = "fullname";


        public static final String SCORE = "score";
        public static final String QUIZ_ID = "quiz_id";
        public static final String QUESTION_ID = "question_id";
        public static final String ANSWER = "answer";
        public static final String FILE_ID = "file_id";
        public static final String MARKS = "marks";
        public static final String FULL_PATH_TO_FILE = "full_path_to_file";
        public static final String LAST_INSERT_ID = "lastInsertId";
    }


    public class ResponseCode {

        public static final int SUCCESS = 1001;
        public static final int FAILURE = 1000;

    }


    public class ServiceCode {

        public static final int LOGIN = 1;

        public static final int GET_COURSE_SPECIALIZATION = 2;
        public static final int GET_QUIZ_DATA = 3;
        public static final int GET_QUIZ_QUESTIONS = 4;
        public static final int GET_QUIZ_Result = 5;
        public static final int SEND_QUIZ_SCORE = 6;
        public static final int GET_ASSIGNMENT_DATA = 7;
        public static final int GET_ALL_FILES = 8;
        public static final int UPLOAD_FILES = 9;
        public static final int SUBMIT_ASSIGNMENT_ANSWER = 10;
        public static final int GET_ASSIGNMENT_ANSWER = 11;
        public static final int GET_ASSIGNMENT_RESULT = 12;
        public static final int CREATE_LOGIN_LOG = 13;
        public static final int GET_SCORM = 14;
        public static final int GET_SESSION_PLAN = 15;
        public static final int GET_NOTES = 16;
        public static final int GET_TAGS = 17;
        public static final int GET_DISCUSSION = 18;
        public static final int GET_TOP_TREADING_TAGS = 19;
        public static final int GET_COMMENTS = 20;
        public static final int CREATE_COMMENTS = 21;
        public static final int CREATE_LIKE = 22;
        public static final int CREATE_DISCUSSION = 23;
        public static final int GET_BLOG = 24;
        public static final int CREATE_BLOG = 25;
        public static final int UPDATE_BLOG = 26;
        public static final int GET_MY_BLOG = 27;
        public static final int GET_COMPETITION = 28;
        public static final int CREATE_TEAM = 29;
        public static final int UPDATE_TEAM = 30;
        public static final int GET_TEAM_DETAILS = 31;
        public static final int GET_ALL_TEAMMATE = 32;
        public static final int GET_ALL_STUDENTS = 33;
        public static final int SEND_INVITE = 34;
        public static final int GET_MY_INVITES = 35;
        public static final int APPROVE_REQUEST = 36;
        public static final int REJECT_REQUEST = 37;
        public static final int CREATE_MILESTONE = 38;
        public static final int UPDATE_PROFILE_PIC = 39;
        public static final int CHANGE_PASSWORD = 40;
        public static final int FORGET_PASSWORD = 41;

    }


    public class Headers_Params {


    }


    public class RequestCode {

        public static final int OPEN_GALLERY = 101;
        public static final int OPEN_CAMERA = 102;
        public static final int GET_LOCATION_PERMISSION = 103;
        public static final int GET_CAMERA_PERMISSION = 104;
        public static final int GET_STORAGE_PERMISSION = 105;
        public static final int GET_PHONE_PERMISSION = 106;
        public static final int OPEN_FILES = 107;
        public static final int CROPPED_IMAGE = 203;
    }


    public class MenuType {


        public static final int PROFILE = 0;
        public static final int LIST = 1;
    }

    public class STATUS {


        public static final String SUBMITTED = "1";
        public static final String UPDATED = "2";
    }
}
