package Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitionData implements Comparable<CompetitionData>, Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("filepath")
    @Expose
    private String filepath;
    @SerializedName("dept_id")
    @Expose
    private String deptId;
    @SerializedName("specialization_id")
    @Expose
    private String specializationId;
    @SerializedName("sem_id")
    @Expose
    private String semId;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("due_date")
    @Expose
    private String dueDate;
    @SerializedName("liferay_scenario_id")
    @Expose
    private String liferayScenarioId;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(String specializationId) {
        this.specializationId = specializationId;
    }

    public String getSemId() {
        return semId;
    }

    public void setSemId(String semId) {
        this.semId = semId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getLiferayScenarioId() {
        return liferayScenarioId;
    }

    public void setLiferayScenarioId(String liferayScenarioId) {
        this.liferayScenarioId = liferayScenarioId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int compareTo(CompetitionData o) {
        return o.getCreatedAt().compareTo(getCreatedAt());
    }

    protected CompetitionData(Parcel in) {
        id = in.readString();
        title = in.readString();
        comment = in.readString();
        filepath = in.readString();
        deptId = in.readString();
        specializationId = in.readString();
        semId = in.readString();
        year = in.readString();
        status = in.readString();
        createdAt = in.readString();
        dueDate = in.readString();
        liferayScenarioId = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(comment);
        dest.writeString(filepath);
        dest.writeString(deptId);
        dest.writeString(specializationId);
        dest.writeString(semId);
        dest.writeString(year);
        dest.writeString(status);
        dest.writeString(createdAt);
        dest.writeString(dueDate);
        dest.writeString(liferayScenarioId);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CompetitionData> CREATOR = new Parcelable.Creator<CompetitionData>() {
        @Override
        public CompetitionData createFromParcel(Parcel in) {
            return new CompetitionData(in);
        }

        @Override
        public CompetitionData[] newArray(int size) {
            return new CompetitionData[size];
        }
    };
}