package Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommentsData implements Parcelable,Comparable<CommentsData> {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("announcement_id")
    @Expose
    private String announcementId;
    @SerializedName("user_id")
    @Expose
    private List<UserDetails> userId = null;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(String announcementId) {
        this.announcementId = announcementId;
    }

    public List<UserDetails> getUserId() {
        return userId;
    }

    public void setUserId(List<UserDetails> userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public CommentsData(String id, String announcementId, List<UserDetails> userId, String content, String createdAt) {
        this.id = id;
        this.announcementId = announcementId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    protected CommentsData(Parcel in) {
        id = in.readString();
        announcementId = in.readString();
        if (in.readByte() == 0x01) {
            userId = new ArrayList<UserDetails>();
            in.readList(userId, UserDetails.class.getClassLoader());
        } else {
            userId = null;
        }
        content = in.readString();
        createdAt = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(announcementId);
        if (userId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(userId);
        }
        dest.writeString(content);
        dest.writeString(createdAt);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CommentsData> CREATOR = new Parcelable.Creator<CommentsData>() {
        @Override
        public CommentsData createFromParcel(Parcel in) {
            return new CommentsData(in);
        }

        @Override
        public CommentsData[] newArray(int size) {
            return new CommentsData[size];
        }
    };

    @Override
    public int compareTo(CommentsData o) {
        return getCreatedAt().compareTo(o.getCreatedAt());
    }
}