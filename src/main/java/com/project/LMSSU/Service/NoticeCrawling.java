package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.NoticeDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoticeCrawling {
    // 학교 공지사항 크롤링
    public List<NoticeDTO> ssuNoticeCrawling() {
        List<NoticeDTO> list = new ArrayList<>();
        final String mainURL = "https://scatch.ssu.ac.kr/%EA%B3%B5%EC%A7%80%EC%82%AC%ED%95%AD/";

        try {
            // 1페이지 크롤링
            Connection conn = Jsoup.connect(mainURL).timeout(5000);
            Document document = conn.get();
            list = getSSUData(document);

            // 다음 페이지 url 가져와서
            String pageURL = document.getElementsByAttributeValue("class", "page-numbers").first().attr("href");

            // 해당 크롤링
            Connection next = Jsoup.connect(pageURL).timeout(5000);
            Document doc = next.get();
            list.addAll(getSSUData(doc));
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        return list;
    }
    // 학교 공지사항 크롤링 시에 원하는 데이터만 가져오기
    public List<NoticeDTO> getSSUData(Document document){
        List<NoticeDTO> list = new ArrayList<>();
        Elements notice = document.getElementsByAttributeValue("class", "row no-gutters align-items-center");
        for (Element element : notice) {
            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .date(element.firstElementChild().text())
                    .title(element.child(2).select("span[class=label d-inline-blcok border pl-3 pr-3 mr-2]").text() + ") " + element.child(2).select("span[class=d-inline-blcok m-pt-5]").text())
                    .url(element.child(2).child(0).attr("href"))
                    .build();
            list.add(noticeDTO);
        }
        return list;
    }
    // AI융합학부 공지사항 크롤링
    public List<NoticeDTO> aiNoticeCrawling() {
        List<NoticeDTO> list = new ArrayList<>();
        String pageURL = "http://aix.ssu.ac.kr/notice.html";
        try {
            // 1페이지 크롤링
            Connection conn = Jsoup.connect(pageURL).timeout(5000);
            Document document = conn.get();
            list = getAIData(document);

            // 2페이지 크롤링
            pageURL = "http://aix.ssu.ac.kr/notice.html?&page=2#none";
            Connection page = Jsoup.connect(pageURL).timeout(5000);
            Document doc = page.get();
            list.addAll(getAIData(doc));
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        return list;
    }
    // AI융합학부 공지사항 크롤링 시에 원하는 데이터만 가져오기
    public List<NoticeDTO> getAIData(Document document){
        List<NoticeDTO> list = new ArrayList<>();
        Elements notice = document.select("table.table tbody tr");
        for (int i=1; i<notice.size(); i++) {
            Element element = notice.get(i);
            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .date(element.child(2).text())
                    .title(element.child(0).text())
                    .url("http://aix.ssu.ac.kr/" + element.child(0).child(0).attr("href"))
                    .build();
            list.add(noticeDTO);
        }
        return list;
    }
    // 펀시스템 프로그램 크롤링
    public List<NoticeDTO> funProgramCrawling() {
        List<NoticeDTO> list = new ArrayList<>();
        String pageURL = "https://fun.ssu.ac.kr/ko/program/all/list/all/1?sort=date";
        try {
            // 1페이지 크롤링
            Connection conn = Jsoup.connect(pageURL).timeout(5000);
            Document document = conn.get();
            list = getFunData(document);
            // 다음 페이지 url 가져와서
            Elements elements = document.select("div.pagination div div ul li a");
            Element element = elements.get(2);
            pageURL = "https://fun.ssu.ac.kr" + element.attr("href");
            // 해당 페이지 크롤링
            Connection next = Jsoup.connect(pageURL).timeout(5000);
            Document doc = next.get();
            list.addAll(getFunData(doc));
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        return list;
    }
    // 펀시스템 프로그램 크롤링 시에 원하는 데이터만 가져오기
    public List<NoticeDTO> getFunData(Document document){
        List<NoticeDTO> list = new ArrayList<>();
        Elements program = document.select("ul.columns-4 li div a");
        for(Element element : program) {
            Element title = element.select("div.content").last();
            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .date(element.select("div.content small time").get(3).text().substring(0, 10))
                    .title(title.child(0).text() + ") "+ title.child(1).text())
                    .url("https://fun.ssu.ac.kr/" + element.attr("href"))
                    .build();
            list.add(noticeDTO);
        }
        return list;
    }
}
