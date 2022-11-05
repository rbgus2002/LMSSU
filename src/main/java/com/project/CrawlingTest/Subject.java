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
    class ContentPerWeek{
        int weeks;
        List<String> typeAndContentNameList = new ArrayList<>(); // #으로 구분
        boolean isDone;

        public ContentPerWeek(int weeks, List<String> typeAndContentNameList, boolean isDone) {
            this.weeks = weeks;
            this.typeAndContentNameList = typeAndContentNameList;
            this.isDone = isDone;
        }
    }
    List<ContentPerWeek> contentPerWeekList = new ArrayList<>();

    /*
    과목 홈 - 공지
     */
    class Notice{
        String title;
        Date date;

        public Notice(String title, Date date) {
            this.title = title;
            this.date = date;
        }
    }
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
        return "Subject{" +
                "name='" + name + '\'' +
                ", professor='" + professor + '\'' +
                ", homepageLink='" + homepageLink + '\'' +
                ", contentPerWeekList=" + contentPerWeekList +
                ", noticeList=" + noticeList +
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