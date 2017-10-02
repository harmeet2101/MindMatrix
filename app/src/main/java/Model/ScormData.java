package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScormData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("course_id")
    @Expose
    private String courseId;
    @SerializedName("chapter_title")
    @Expose
    private String chapterTitle;
    @SerializedName("file_id")
    @Expose
    private String fileId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("chapter_id")
    @Expose
    private String chapterId;
    @SerializedName("lesson_title")
    @Expose
    private String lessonTitle;
    @SerializedName("lession_href")
    @Expose
    private String lessionHref;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public String getLessionHref() {
        return lessionHref;
    }

    public void setLessionHref(String lessionHref) {
        this.lessionHref = lessionHref;
    }

}