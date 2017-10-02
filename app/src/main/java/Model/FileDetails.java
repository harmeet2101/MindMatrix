package Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileDetails implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("original_file_name")
    @Expose
    private String originalFileName;
    @SerializedName("saved_file_name")
    @Expose
    private String savedFileName;
    @SerializedName("path_to_file")
    @Expose
    private String pathToFile;
    @SerializedName("full_path_to_file")
    @Expose
    private String fullPathToFile;
    @SerializedName("entity_type_id")
    @Expose
    private String entityTypeId;
    @SerializedName("entity_type_name")
    @Expose
    private String entityTypeName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getSavedFileName() {
        return savedFileName;
    }

    public void setSavedFileName(String savedFileName) {
        this.savedFileName = savedFileName;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public String getFullPathToFile() {
        return fullPathToFile;
    }

    public void setFullPathToFile(String fullPathToFile) {
        this.fullPathToFile = fullPathToFile;
    }

    public String getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(String entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }

    public void setEntityTypeName(String entityTypeName) {
        this.entityTypeName = entityTypeName;
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


    public FileDetails(String id, String originalFileName, String userId, String savedFileName, String pathToFile, String fullPathToFile, String entityTypeId, String entityTypeName, String status, String createdAt) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.userId = userId;
        this.savedFileName = savedFileName;
        this.pathToFile = pathToFile;
        this.fullPathToFile = fullPathToFile;
        this.entityTypeId = entityTypeId;
        this.entityTypeName = entityTypeName;
        this.status = status;
        this.createdAt = createdAt;
    }

    protected FileDetails(Parcel in) {
        id = in.readString();
        userId = in.readString();
        originalFileName = in.readString();
        savedFileName = in.readString();
        pathToFile = in.readString();
        fullPathToFile = in.readString();
        entityTypeId = in.readString();
        entityTypeName = in.readString();
        status = in.readString();
        createdAt = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(originalFileName);
        dest.writeString(savedFileName);
        dest.writeString(pathToFile);
        dest.writeString(fullPathToFile);
        dest.writeString(entityTypeId);
        dest.writeString(entityTypeName);
        dest.writeString(status);
        dest.writeString(createdAt);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FileDetails> CREATOR = new Parcelable.Creator<FileDetails>() {
        @Override
        public FileDetails createFromParcel(Parcel in) {
            return new FileDetails(in);
        }

        @Override
        public FileDetails[] newArray(int size) {
            return new FileDetails[size];
        }
    };
}