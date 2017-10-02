package Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QuizData implements Comparable<QuizData>, Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("course_id")
    @Expose
    private String courseId;
    @SerializedName("chapter_id")
    @Expose
    private String chapterId;
    @SerializedName("faculty_id")
    @Expose
    private List<UserDetails> facultyId = null;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("due_date")
    @Expose
    private String dueDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("total_marks")
    @Expose
    private String totalMarks;
    @SerializedName("liferay_quiz_id")
    @Expose
    private String liferayQuizId;

    private String score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public List<UserDetails> getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(List<UserDetails> facultyId) {
        this.facultyId = facultyId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getLiferayQuizId() {
        return liferayQuizId;
    }

    public void setLiferayQuizId(String liferayQuizId) {
        this.liferayQuizId = liferayQuizId;
    }

    public String getScore() {
        return score;
    }


    public QuizData(String id, String score) {
        this.score = score;
        this.id = id;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public int compareTo(QuizData o) {
        return o.getCreatedAt().compareTo(getCreatedAt());
    }

    protected QuizData(Parcel in) {
        id = in.readString();
        name = in.readString();
        courseId = in.readString();
        chapterId = in.readString();
        if (in.readByte() == 0x01) {
            facultyId = new ArrayList<UserDetails>();
            in.readList(facultyId, UserDetails.class.getClassLoader());
        } else {
            facultyId = null;
        }
        duration = in.readString();
        dueDate = in.readString();
        createdAt = in.readString();
        type = in.readString();
        sessionId = in.readString();
        totalMarks = in.readString();
        liferayQuizId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(courseId);
        dest.writeString(chapterId);
        if (facultyId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(facultyId);
        }
        dest.writeString(duration);
        dest.writeString(dueDate);
        dest.writeString(createdAt);
        dest.writeString(type);
        dest.writeString(sessionId);
        dest.writeString(totalMarks);
        dest.writeString(liferayQuizId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<QuizData> CREATOR = new Parcelable.Creator<QuizData>() {
        @Override
        public QuizData createFromParcel(Parcel in) {
            return new QuizData(in);
        }

        @Override
        public QuizData[] newArray(int size) {
            return new QuizData[size];
        }
    };
}