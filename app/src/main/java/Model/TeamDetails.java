package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamDetails {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("team_name")
    @Expose
    private String teamName;
    @SerializedName("team_lead_id")
    @Expose
    private List<UserDetails> teamLeadId = null;
    @SerializedName("faculty_user_id")
    @Expose
    private String facultyUserId;
    @SerializedName("scenario_id")
    @Expose
    private String scenarioId;
    @SerializedName("dept_id")
    @Expose
    private String deptId;
    @SerializedName("specilization_id")
    @Expose
    private String specilizationId;
    @SerializedName("sem_id")
    @Expose
    private String semId;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("stage")
    @Expose
    private String stage;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("scenario_status")
    @Expose
    private String scenarioStatus;
    @SerializedName("liferay_team_id")
    @Expose
    private String liferayTeamId;
    @SerializedName("module")
    @Expose
    private String module;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<UserDetails> getTeamLeadId() {
        return teamLeadId;
    }

    public void setTeamLeadId(List<UserDetails> teamLeadId) {
        this.teamLeadId = teamLeadId;
    }

    public String getFacultyUserId() {
        return facultyUserId;
    }

    public void setFacultyUserId(String facultyUserId) {
        this.facultyUserId = facultyUserId;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getSpecilizationId() {
        return specilizationId;
    }

    public void setSpecilizationId(String specilizationId) {
        this.specilizationId = specilizationId;
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

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getScenarioStatus() {
        return scenarioStatus;
    }

    public void setScenarioStatus(String scenarioStatus) {
        this.scenarioStatus = scenarioStatus;
    }

    public String getLiferayTeamId() {
        return liferayTeamId;
    }

    public void setLiferayTeamId(String liferayTeamId) {
        this.liferayTeamId = liferayTeamId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

}