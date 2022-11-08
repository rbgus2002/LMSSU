import com.beust.ah.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Subject {
    /*
    마이페이지
     */
    private String name;
    private String professor;
    private String homepageLink;


    /*
    과목 홈 - 강의콘텐츠
     */
    List<ContentPerWeek> contentPerWeekList = new ArrayList<>();

    /*
    과목 홈 - 공지
     */
    List<Notice> noticeList = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getHomepageLink() {
        return homepageLink;
    }

    public void setHomepageLink(String homepageLink) {
        this.homepageLink = homepageLink;
    }

    public List<ContentPerWeek> getContentPerWeekList() {
        return contentPerWeekList;
    }

    public void setContentPerWeekList(List<ContentPerWeek> contentPerWeekList) {
        this.contentPerWeekList = contentPerWeekList;
    }

    public List<Notice> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<Notice> noticeList) {
        this.noticeList = noticeList;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------" + this.name + "--------------").append("\n");
        sb.append("교수님 : ").append(this.professor).append("\n");
        sb.append("과목홈 : ").append(this.homepageLink).append("\n");
        sb.append("과목공지 : ").append(this.noticeList.toString()).append("\n");
        for(ContentPerWeek contentPerWeek : this.contentPerWeekList){
            sb.append(contentPerWeek.weeks).append(" : ").append(contentPerWeek.contentList.toString()).append("\n");
        }
        sb.append("------------------------------------------").append("\n");

        return sb.toString();
    }
}

class ContentPerWeek{
    int weeks;
    /*
    <icon>
    pdf, mp4, offline_attendance, file, text, assignment, video_conference, quiz
     */
    List<Content> contentList = new ArrayList<>(); // #으로 구분

    public ContentPerWeek(int weeks, List<Content> contentList) {
        this.weeks = weeks;
        this.contentList = contentList;
    }
}

class Content{
    /*
    None -> Data not exist
     */
    private String type;
    private String name;
    private String isDone;
    private String startDate;

    public Content(String type, String name, String isDone, String startDate) {
        this.type = type;
        this.name = name;
        this.isDone = isDone;
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return type + "  '" + name + "' / " + isDone + " / '" + startDate + "'";
    }
}


class Notice{
    String title;
    String date;
    String link;

    public Notice(String title, String date, String link) {
        this.title = title;
        this.date = date;
        this.link = link;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
/*
name
professor
homepageLink
contentPerWeekList (weeks, typeAndContentName, isDone)
noticeList (title, date)
 */