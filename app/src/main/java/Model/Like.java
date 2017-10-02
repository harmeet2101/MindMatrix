package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Like {

@SerializedName("id")
@Expose
private String id;
@SerializedName("user_id")
@Expose
private String userId;
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

}