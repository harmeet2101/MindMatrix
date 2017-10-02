package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Blog {

    @SerializedName("numFound")
    @Expose
    private Integer numFound;
    @SerializedName("data")
    @Expose
    private List<BlogData> data = null;

    public Integer getNumFound() {
        return numFound;
    }

    public void setNumFound(Integer numFound) {
        this.numFound = numFound;
    }

    public List<BlogData> getData() {
        return data;
    }

    public void setData(List<BlogData> data) {
        this.data = data;
    }

}