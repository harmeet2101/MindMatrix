package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Discussion {

@SerializedName("numFound")
@Expose
private Integer numFound;
@SerializedName("data")
@Expose
private List<DiscussionData> data = null;

public Integer getNumFound() {
return numFound;
}

public void setNumFound(Integer numFound) {
this.numFound = numFound;
}

public List<DiscussionData> getData() {
return data;
}

public void setData(List<DiscussionData> data) {
this.data = data;
}

}