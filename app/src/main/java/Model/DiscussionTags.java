package Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DiscussionTags implements Parcelable {

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
    private List<User> userId = null;
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
    private List<Object> fileId = null;

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

    public List<User> getUserId() {
        return userId;
    }

    public void setUserId(List<User> userId) {
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

    public List<Object> getFileId() {
        return fileId;
    }

    public void setFileId(List<Object> fileId) {
        this.fileId = fileId;
    }


    protected DiscussionTags(Parcel in) {
        id = in.readString();
        version = in.readString();
        courseId = in.readString();
        if (in.readByte() == 0x01) {
            userId = new ArrayList<User>();
            in.readList(userId, User.class.getClassLoader());
        } else {
            userId = null;
        }
        chapterId = in.readString();
        title = in.readString();
        announcementMsg = in.readString();
        assignmentId = in.readString();
        type = in.readString();
        status = in.readString();
        createdAt = in.readString();
        if (in.readByte() == 0x01) {
            fileId = new ArrayList<Object>();
            in.readList(fileId, Object.class.getClassLoader());
        } else {
            fileId = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(version);
        dest.writeString(courseId);
        if (userId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(userId);
        }
        dest.writeString(chapterId);
        dest.writeString(title);
        dest.writeString(announcementMsg);
        dest.writeString(assignmentId);
        dest.writeString(type);
        dest.writeString(status);
        dest.writeString(createdAt);
        if (fileId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(fileId);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DiscussionTags> CREATOR = new Parcelable.Creator<DiscussionTags>() {
        @Override
        public DiscussionTags createFromParcel(Parcel in) {
            return new DiscussionTags(in);
        }

        @Override
        public DiscussionTags[] newArray(int size) {
            return new DiscussionTags[size];
        }
    };
}