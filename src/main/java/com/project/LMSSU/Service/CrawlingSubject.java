package com.project.LMSSU.Service;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CrawlingSubject {
    /*
    마이페이지
     */
    private String name;
    private String professor;
    private String homepageLink;


    /*
    과목 홈 - 강의콘텐츠
     */
    private List<ContentPerWeek> contentPerWeekList = new ArrayList<>();

    /*
    과목 홈 - 공지
     */
    private List<Notice> noticeList = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------" + this.name + "--------------").append("\n");
        sb.append("교수님 : ").append(this.professor).append("\n");
        sb.append("과목홈 : ").append(this.homepageLink).append("\n");
        sb.append("과목공지 : ").append(this.noticeList.toString()).append("\n");
        for(ContentPerWeek contentPerWeek : this.contentPerWeekList){
            sb.append(contentPerWeek.getWeeks()).append(" : ").append(contentPerWeek.getContentList().toString()).append("\n");
        }
        sb.append("------------------------------------------").append("\n");

        return sb.toString();
    }
}

@Data
class ContentPerWeek{
    private int weeks;
    /*
    <icon>
    pdf, mp4, offline_attendance, file, text, assignment, video_conference, quiz
     */
    private List<Content> contentList = new ArrayList<>(); // #으로 구분

    public ContentPerWeek(int weeks, List<Content> contentList) {
        this.weeks = weeks;
        this.contentList = contentList;
    }
}

@Data
class Content{
    /*
    None -> Data not exist
     */
    private String type;
    private String name;
    private String isDone;
    private String endDate;

    public Content(String type, String name, String isDone, String endDate) {
        this.type = type;
        this.name = name;
        this.isDone = isDone;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return type + "  '" + name + "' / " + isDone + " / '" + endDate + "'";
    }
}

@Data
class Notice{
    private String title;
    private String date;
    private String link;

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
