package Retrofit_Network_Utils;


import java.util.Map;

import Constants.Const;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiInterface {

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "X-Requested-With: XMLHttpRequest"
    })
    @FormUrlEncoded
    @POST("api/validateLogin")
    Call<ResponseBody> login(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("api/validateLogin")
    Call<ResponseBody> forgotPassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("api/changePassword")
    Call<ResponseBody> changeUserPassword(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("loginlogapi/createLoginLog")
    Call<ResponseBody> createLoginLog(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("api/getCourseBySpecialization")
    Call<ResponseBody> getCourseBySpecialization(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("quizapi/getQuizWithinSemester")
    Call<ResponseBody> getQuiz(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("quizapi/getQuizResult")
    Call<ResponseBody> getQuizResult(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("questionapi/getQuestionAndOptions")
    Call<ResponseBody> getQuizQuestion(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("quizapi/createQuizResult")
    Call<ResponseBody> sendQuizResult(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("assignmentapi/getAllAssignmentsWithinSemester")
    Call<ResponseBody> getAssignments(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("assignmentapi/getAllAnswers")
    Call<ResponseBody> getAssignmentsAnswer(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("assignmentapi/getAssignmentMarksOnAssignentLevel")
    Call<ResponseBody> getAssignmentsResult(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("uploadapi/getAllFiles")
    Call<ResponseBody> getAllFiles(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("assignmentapi/createAssignmentAnswer")
    Call<ResponseBody> createAssignmentAnswer(@FieldMap Map<String, String> params);

    @Multipart
    @POST("uploadapi/uploadFile")
    Call<ResponseBody> uploadFile(@Part(Const.Params.USER_ID) int user_id,
                                  @Part(Const.Params.ENTITY_TYPE_ID) int entity_id,
                                  @Part(Const.Params.ENTITY_TYPE_NAME) String entity_name,
                                  @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("scormapi/getScorm")
    Call<ResponseBody> getScorm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("announcementapi/getAnnouncements")
    Call<ResponseBody> getAnnouncements(@FieldMap Map<String, String> params);

    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Url String url);

    @FormUrlEncoded
    @POST("announcementapi/getAnnouncementsWithoutUniversity")
    Call<ResponseBody> getAnnouncementsWithoutUniversity(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("likeapi/getTopTrendingTags")
    Call<ResponseBody> getTopTrendingTags(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("announcementapi/getAnnouncementsOfTAG")
    Call<ResponseBody> getAnnouncementsOfTAG(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("commentapi/getComments")
    Call<ResponseBody> getComments(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("commentapi/createComment")
    Call<ResponseBody> createComments(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("likeapi/createLike")
    Call<ResponseBody> createLike(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("announcementapi/createAnnouncement")
    Call<ResponseBody> createDiscussion(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("announcementapi/editAnnouncement")
    Call<ResponseBody> updateDiscussion(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("teamapi/getListOfScenarioByOptionsStudent")
    Call<ResponseBody> getListOfScenarioByOptions(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("teamapi/createTeamByStep")
    Call<ResponseBody> createTeamByStep(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("teamapi/editTeamByStep")
    Call<ResponseBody> editTeamByStep(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("teamapi/getListOfTeamByOptionsStudent")
    Call<ResponseBody> getListOfTeamByOptions(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("teamapi/getListOfRequestForTeamByOptions")
    Call<ResponseBody> getListOfRequestForTeamByOptions(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("teamapi/getListOfStudentForInvite")
    Call<ResponseBody> getListOfStudentForInvite(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("teamapi/sendRequestToTeamMembersForTeamCreation")
    Call<ResponseBody> sendRequestToTeamMembersForTeamCreation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("teamapi/getListOfRequestForTeamByOptions")
    Call<ResponseBody> getListOfRequestForTeamByOption(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("teamapi/approveRequestByTeamMember")
    Call<ResponseBody> approveRequestByTeamMember(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("teamapi/rejectRequestByTeamMember")
    Call<ResponseBody> rejectRequestByTeamMember(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("teamapi/createTeamMilestone")
    Call<ResponseBody> createTeamMilestone(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("userapi/editUser")
    Call<ResponseBody> editUser(@FieldMap Map<String, String> params);


}