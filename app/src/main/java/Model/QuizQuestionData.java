package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizQuestionData {

@SerializedName("quiz_id")
@Expose
private String quizId;
@SerializedName("question")
@Expose
private String question;
@SerializedName("ques_option1")
@Expose
private String quesOption1;
@SerializedName("ques_option2")
@Expose
private String quesOption2;
@SerializedName("ques_option3")
@Expose
private String quesOption3;
@SerializedName("ques_option4")
@Expose
private String quesOption4;
@SerializedName("correct")
@Expose
private String correct;
@SerializedName("marks")
@Expose
private String marks;

public String getQuizId() {
return quizId;
}

public void setQuizId(String quizId) {
this.quizId = quizId;
}

public String getQuestion() {
return question;
}

public void setQuestion(String question) {
this.question = question;
}

public String getQuesOption1() {
return quesOption1;
}

public void setQuesOption1(String quesOption1) {
this.quesOption1 = quesOption1;
}

public String getQuesOption2() {
return quesOption2;
}

public void setQuesOption2(String quesOption2) {
this.quesOption2 = quesOption2;
}

public String getQuesOption3() {
return quesOption3;
}

public void setQuesOption3(String quesOption3) {
this.quesOption3 = quesOption3;
}

public String getQuesOption4() {
return quesOption4;
}

public void setQuesOption4(String quesOption4) {
this.quesOption4 = quesOption4;
}

public String getCorrect() {
return correct;
}

public void setCorrect(String correct) {
this.correct = correct;
}

public String getMarks() {
return marks;
}

public void setMarks(String marks) {
this.marks = marks;
}

}

