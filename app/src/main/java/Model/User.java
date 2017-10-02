package Model;

/**
 * Created by ganesha on 17/3/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("role_id")
    @Expose
    private String roleId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("semester")
    @Expose
    private String semester;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("specializationId")
    @Expose
    private String specializationId;
    @SerializedName("universityId")
    @Expose
    private String universityId;
    @SerializedName("departmentId")
    @Expose
    private String departmentId;
    @SerializedName("profile_url")
    @Expose
    private String profileUrl;
    @SerializedName("liferay_id")
    @Expose
    private String liferayId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("admission_year")
    @Expose
    private String admissionYear;
    @SerializedName("isMentor")
    @Expose
    private String isMentor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(String specializationId) {
        this.specializationId = specializationId;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getLiferayId() {
        return liferayId;
    }

    public void setLiferayId(String liferayId) {
        this.liferayId = liferayId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(String admissionYear) {
        this.admissionYear = admissionYear;
    }

    public String getIsMentor() {
        return isMentor;
    }

    public void setIsMentor(String isMentor) {
        this.isMentor = isMentor;
    }


    protected User(Parcel in) {
        id = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        email = in.readString();
        password = in.readString();
        roleId = in.readString();
        status = in.readString();
        createdAt = in.readString();
        semester = in.readString();
        year = in.readString();
        specializationId = in.readString();
        universityId = in.readString();
        departmentId = in.readString();
        profileUrl = in.readString();
        liferayId = in.readString();
        updatedAt = in.readString();
        admissionYear = in.readString();
        isMentor = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(roleId);
        dest.writeString(status);
        dest.writeString(createdAt);
        dest.writeString(semester);
        dest.writeString(year);
        dest.writeString(specializationId);
        dest.writeString(universityId);
        dest.writeString(departmentId);
        dest.writeString(profileUrl);
        dest.writeString(liferayId);
        dest.writeString(updatedAt);
        dest.writeString(admissionYear);
        dest.writeString(isMentor);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}