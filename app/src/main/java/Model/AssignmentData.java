package Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignmentData implements Comparable<AssignmentData>, Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("faculty_id")
    @Expose
    private String facultyId;
    @SerializedName("course_id")
    @Expose
    private String courseId;
    @SerializedName("assignment_name")
    @Expose
    private String assignmentName;
    @SerializedName("completion_date")
    @Expose
    private String completionDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("total_marks")
    @Expose
    private String totalMarks;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("liferay_assignment_id")
    @Expose
    private String liferayAssignmentId;

    private String assignmentText;
    private String assignmentFile;

    public String getAssignmentText() {
        return assignmentText;
    }

    public void setAssignmentText(String assignmentText) {
        this.assignmentText = assignmentText;
    }

    public String getAssignmentFile() {
        return assignmentFile;
    }

    public void setAssignmentFile(String assignmentFile) {
        this.assignmentFile = assignmentFile;
    }

    public AssignmentData(String id, String assignmentText, String assignmentFile) {
        this.id = id;
        this.assignmentText = assignmentText;
        this.assignmentFile = assignmentFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLiferayAssignmentId() {
        return liferayAssignmentId;
    }

    public void setLiferayAssignmentId(String liferayAssignmentId) {
        this.liferayAssignmentId = liferayAssignmentId;
    }

    @Override
    public int compareTo(AssignmentData o) {

        return o.getCreatedAt().compareTo(getCreatedAt());
    }


    protected AssignmentData(Parcel in) {
        id = in.readString();
        facultyId = in.readString();
        courseId = in.readString();
        assignmentName = in.readString();
        completionDate = in.readString();
        status = in.readString();
        createdAt = in.readString();
        sessionId = in.readString();
        totalMarks = in.readString();
        type = in.readString();
        liferayAssignmentId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(facultyId);
        dest.writeString(courseId);
        dest.writeString(assignmentName);
        dest.writeString(completionDate);
        dest.writeString(status);
        dest.writeString(createdAt);
        dest.writeString(sessionId);
        dest.writeString(totalMarks);
        dest.writeString(type);
        dest.writeString(liferayAssignmentId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AssignmentData> CREATOR = new Parcelable.Creator<AssignmentData>() {
        @Override
        public AssignmentData createFromParcel(Parcel in) {
            return new AssignmentData(in);
        }

        @Override
        public AssignmentData[] newArray(int size) {
            return new AssignmentData[size];
        }
    };
}