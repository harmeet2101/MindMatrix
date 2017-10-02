package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamMember {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("team_lead_id")
    @Expose
    private List<UserDetails> teamLeadId = null;
    @SerializedName("team_member_user_id")
    @Expose
    private List<UserDetails> teamMemberUserId = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("scenario_id")
    @Expose
    private String scenarioId;
    @SerializedName("module")
    @Expose
    private String module;


    public TeamMember(String id, List<UserDetails> teamLeadId,
                      List<UserDetails> teamMemberUserId,
                      String status, String createdAt,
                      String scenarioId, String module) {
        this.id = id;
        this.teamLeadId = teamLeadId;
        this.teamMemberUserId = teamMemberUserId;
        this.status = status;
        this.createdAt = createdAt;
        this.scenarioId = scenarioId;
        this.module = module;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<UserDetails> getTeamLeadId() {
        return teamLeadId;
    }

    public void setTeamLeadId(List<UserDetails> teamLeadId) {
        this.teamLeadId = teamLeadId;
    }

    public List<UserDetails> getTeamMemberUserId() {
        return teamMemberUserId;
    }

    public void setTeamMemberUserId(List<UserDetails> teamMemberUserId) {
        this.teamMemberUserId = teamMemberUserId;
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

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

}