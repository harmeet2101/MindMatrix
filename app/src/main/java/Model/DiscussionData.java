package Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DiscussionData implements Parcelable {

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
    @SerializedName("like")
    @Expose
    private List<Like> like = null;
    @SerializedName("file_id")
    @Expose
    private List<Object> fileId = null;


    private int likeCount;
    private String tags;

    private Boolean isIVoted = false;
    private Boolean isIFlagged = false;

    public Boolean getIVoted() {
        return isIVoted;
    }

    public void setIVoted(Boolean IVoted) {
        isIVoted = IVoted;
    }

    public Boolean getIFlagged() {
        return isIFlagged;
    }

    public void setIFlagged(Boolean IFlagged) {
        isIFlagged = IFlagged;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

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

    public List<Like> getLike() {
        return like;
    }

    public void setLike(List<Like> like) {
        this.like = like;
    }

    public List<Object> getFileId() {
        return fileId;
    }

    public void setFileId(List<Object> fileId) {
        this.fileId = fileId;
    }


    protected DiscussionData(Parcel in) {
        id = in.readString();
        version = in.readString();
        courseId = in.readString();
        if (in.readByte() == 0x01) {
            userId = new ArrayList<UserDetails>();
            in.readList(userId, UserDetails.class.getClassLoader());
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
            like = new ArrayList<Like>();
            in.readList(like, Like.class.getClassLoader());
        } else {
            like = null;
        }
        if (in.readByte() == 0x01) {
            fileId = new ArrayList<Object>();
            in.readList(fileId, Object.class.getClassLoader());
        } else {
            fileId = null;
        }
        likeCount = in.readInt();
        tags = in.readString();
        byte isIVotedVal = in.readByte();
        isIVoted = isIVotedVal == 0x02 ? null : isIVotedVal != 0x00;
        byte isIFlaggedVal = in.readByte();
        isIFlagged = isIFlaggedVal == 0x02 ? null : isIFlaggedVal != 0x00;
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
        if (like == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(like);
        }
        if (fileId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(fileId);
        }
        dest.writeInt(likeCount);
        dest.writeString(tags);
        if (isIVoted == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isIVoted ? 0x01 : 0x00));
        }
        if (isIFlagged == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isIFlagged ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DiscussionData> CREATOR = new Parcelable.Creator<DiscussionData>() {
        @Override
        public DiscussionData createFromParcel(Parcel in) {
            return new DiscussionData(in);
        }

        @Override
        public DiscussionData[] newArray(int size) {
            return new DiscussionData[size];
        }
    };

    public DiscussionData() {
    }
}