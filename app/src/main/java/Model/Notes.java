
package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Notes {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("course_id")
    @Expose
    private String courseId;
    @SerializedName("user_id")
    @Expose
    private List<UserDetails> userId = null;
    @SerializedName("chapter_id")
    @Expose
    private String chapterId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("announcement_msg")
    @Expose
    private String announcementMsg;
    @SerializedName("assignment_id")
    @Expose
    private String assignmentId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("file_id")
    @Expose
    private List<FileDetails> fileId = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public List<UserDetails> getUserId() {
        return userId;
    }

    public void setUserId(List<UserDetails> userId) {
        this.userId = userId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnnouncementMsg() {
        return announcementMsg;
    }

    public void setAnnouncementMsg(String announcementMsg) {
        this.announcementMsg = announcementMsg;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<FileDetails> getFileId() {
        return fileId;
    }

    public void setFileId(List<FileDetails> fileId) {
        this.fileId = fileId;
    }

}
