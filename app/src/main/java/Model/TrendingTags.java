package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrendingTags {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("count")
    @Expose
    private String count;

    private DiscussionTags discussionTag;

    public DiscussionTags getDiscussionTag() {
        return discussionTag;
    }

    public void setDiscussionTag(DiscussionTags discussionTag) {
        this.discussionTag = discussionTag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

}