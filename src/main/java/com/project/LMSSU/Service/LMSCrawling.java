package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.LMSCrawlingRequestDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LMSCrawling {
    private static WebDriver driver;
    private static WebElement element;
    private static String url;
    private static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    private static String WEB_DRIVER_PATH = "/Users/choegyuhyeon/Downloads/chromedriver";
    private static Integer userId;
    private static String pwd;
    private static Long studentId;

    private static List<SubjectContentsInfo> subjectList = new ArrayList<>();

    public LMSCrawling(LMSCrawlingRequestDTO lmsCrawlingRequestDTO) throws InterruptedException {
        this.userId = lmsCrawlingRequestDTO.getUserId();
        this.pwd = lmsCrawlingRequestDTO.getPwd();
        this.studentId = lmsCrawlingRequestDTO.getStudentId();

        initCrawling();
        login(userId, pwd);
    }

    private void initCrawling() {
        // 경로 설정
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--blink-settings=imagesEnabled=false");
//        options.addArguments("headless");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); // ??

        // 크롬 드라이버 객체 생성
        driver = new ChromeDriver(options);
    }

    /*
    로그인
     */
    private void login(Integer userId, String pwd) throws InterruptedException {
        // LMS Login page
        url = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fclass.ssu.ac.kr%2Fxn-sso%2Fgw-cb.php/";

        // 페이지 로딩
        driver.get(url);
        element = new WebDriverWait(driver, Duration.ofSeconds(4))
                .until(ExpectedConditions.elementToBeClickable(By.className("btn_login"))); // 로그인 버튼 누를 수 있을 때까지만 기다림

        // 아이디, 비밀번호 입력
        driver.findElement(By.id("userid")).sendKeys(userId.toString());
        driver.findElement(By.id("pwd")).sendKeys(pwd);

        // 로그인
        driver.findElement(By.className("btn_login")).click();

        // 웹페이지 이동
        Thread.sleep(2000);
    }

    /*
    수강중인 과목의 id List 가져오기
     */
    public List<Long> getSubjectId() {
        List<Long> answer = new ArrayList<>();

        // 마이페이지 이동
        driver.get("https://canvas.ssu.ac.kr/learningx/dashboard?user_login=" + userId.toString() + "&locale=ko");

        // 특정 element Explicit Waits
        element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> driver.findElement(By.className("xnscc-header-sub-title")));

        List<WebElement> elements = driver.findElements(By.className("xn-student-course-container"));
        for (WebElement element : elements) {
            // 과목 이름 찾기
            String name = element.findElement(By.className("xnscc-header-title")).getText();

            // subjectId 추출
            Pattern p = Pattern.compile("\\((\\d+)\\)");
            Matcher matcher = p.matcher(name);

            if (matcher.find()) {
                answer.add(Long.parseLong(matcher.group(1)));
            }
        }

        return answer;
    }


    /*
    subjectId에 해당하는 과목의 링크, 교수이름, 과목이름 가져오기
     */
    public Map<Object, String> getSubjectInfo(String subjectId) {
        Map<Object, String> answer = new HashMap();
        List<WebElement> elements = driver.findElements(By.className("xn-student-course-container"));

        for (WebElement element : elements) {
            // 과목 이름 찾기
            String name = element.findElement(By.className("xnscc-header-title")).getText();

            if (name.contains(subjectId)) {
                // homepageAddress
                String homepageAddress = element.findElement(By.className("xnscc-header-redirect-link")).getAttribute("href");
                System.out.println("homepageAddress : " + homepageAddress);

                // professorName
                List<WebElement> professors = element.findElements(By.className("xnscc-header-sub-title"));
                String professorName = professors.get(1).getText().trim();
                System.out.println("homepageLink : " + professorName);

                // Map에 담기
                answer.put("homepageAddress", homepageAddress);
                answer.put("professorName", professorName);
                answer.put("subjectName", name);

                return answer;
            }
        }

        return null;
    }

    /*
    subjectId에 해당하는 과목의 강의 컨텐츠, 공지사항 가져오기
     */
    public SubjectContentsInfo getSubjectContentsInfo(String homepageAddress) {
        SubjectContentsInfo answer = new SubjectContentsInfo();

        // 과목 콘텐츠 이동
        driver.get(homepageAddress + "/external_tools/2");

        // frame 이동
        driver.switchTo().frame("tool_content");

        // 모든 주차 펴기
        element = new WebDriverWait(driver, Duration.ofSeconds(7))
                .until(ExpectedConditions.elementToBeClickable(By.className("xncb-section-wrapper"))); // Explicit Waits
        element = driver.findElement(By.className("xncb-fold-toggle-button"));
        if (element.getText().contains("펴기")) {
            element.click();
        }

        // 주차별 강의목록을 가져온다
        List<WebElement> weekList = driver.findElements(By.className("xncb-section-content-wrapper"));

        // ContentPerWeek 객체 초기화하여 List에 add
        List<ContentPerWeek> contentPerWeekList = new ArrayList<>();
        int weekNum = 1;

        for (WebElement weekWebElement : weekList) {
            List<Content> contentList = new ArrayList<>();

            // 특정 주차에 해당하는 강의콘텐츠 Element 리스트를 가져온다.
            List<WebElement> contentElementList = weekWebElement.findElements(By.className("xncb-unit-content-wrapper"));

            // 특정 주차에 강의콘텐츠 없는 경우 예외 처리
            if (contentElementList.isEmpty()) {
                weekNum += 1;
                continue;
            }

            // 특정 주차에 해당하는 강의콘텐츠 이름을 contentList에 추가한다.
            for (WebElement contentElement : contentElementList) {
                // Content 객체 생성
                // set type
                String type = contentElement.findElement(By.className("xncb-component-icon")).getAttribute("class");
                type = type.split(" ")[1];
                // set name
                String name = contentElement.findElement(By.className("xncb-component-title")).getText();
                // set isDone (존재하면 넣기 메소드 찾기)
                String isDone = "None";
                boolean isExistence = contentElement.findElement(By.className("xncb-component-attendance-state-wrapper")).isDisplayed();
                if (isExistence) {
                    isDone = contentElement.findElement(By.className("xncb-component-attendance-state")).getText();
                }

                // set endDate (존재하면 넣기 메소드 찾기)
                String endDate = "None";
                element = contentElement.findElement(By.className("xncb-component-periods-wrapper"));
                isExistence = element.isDisplayed();
                if (isExistence) {
                    element = element.findElement(By.className("xncb-component-periods-date"));
                    if (element.isDisplayed()) {
                        endDate = element.findElement(By.className("xncb-component-periods-item-date")).getText();
                    }
                }

                Content content = new Content(type, name, isDone, endDate);

                contentList.add(content);
            }
            // ContentPerWeek 객체 생성 후 add
            ContentPerWeek contentPerWeek = new ContentPerWeek(weekNum, contentList); // 임시 true
            contentPerWeekList.add(contentPerWeek);

            weekNum += 1;
        }
        answer.setContentPerWeekList(contentPerWeekList);

        // 과목공지 이동
        driver.get(homepageAddress + "/announcements");

        // 특정 과목의 공지를 가져온다

        // 특정 element Explicit Waits
        element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> driver.findElement(By.className("ic-item-row")));

        // 공지사항 Element 가져오기
        List<WebElement> noticeElementList = driver.findElements(By.className("ic-item-row"));

        // Notice 객체 초기화 하여 add
        List<Notice> noticeList = new ArrayList<>();
        for (WebElement webElement : noticeElementList) {
            // 제목
            String title = webElement.findElement(By.className("emyav_fAVi")).getText();
            // 날짜
            String date = webElement.findElement(By.className("cjUyb_bLsb")).getText();
            // 링크
            String link = webElement.findElement(By.className("ic-item-row__content-link")).getAttribute("href");
            // 공지사항 Id
            Long noticeId = Long.parseLong(link.substring(link.length()-5, link.length()));

            Notice notice = new Notice(noticeId, title, date, link);
            noticeList.add(notice);
        }
        answer.setNoticeList(noticeList);

        return answer;
    }


}

//    public List<CrawlingSubject> run(Integer userId, String pwd) throws InterruptedException, IOException {
//        long time0 = System.currentTimeMillis();
//
//        /*
//        로그인 (예외처리 필요)
//         */
//        login(userId, pwd);
//
//        /*
//        마이페이지
//         */
//        setMySubjectNameAndProfessorAndHomeLink();
//
//        /*
//        과목 홈페이지 - 강의콘텐츠
//         */
//        for (CrawlingSubject subject : subjectList) {
//            setMySubjectContents(subject);
//        }
//
//        /*
//        과목 홈페이지 - 공지사항
//         */
//        for (CrawlingSubject subject : subjectList) {
//            setMySubjectNotice(subject);
//        }
//
//        // subjectList 출력
//        for (CrawlingSubject subject : subjectList) {
//            System.out.println(subject.toString());
//        }
//        long time1 = System.currentTimeMillis();
//
//
//        System.out.println();
//        System.out.println("총 : " + (time1 - time0) + " msec");
//
//        return subjectList;
//    }


/*
    1. login
    2. mypage
    3. 강의콘텐츠
    4. 공지
    순서로 Crawling
     */