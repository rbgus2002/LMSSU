import com.beust.ah.A;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LMSCrawling {
    private static WebDriver driver;
    private static WebElement element;
    private static String url;
    private static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    private static String WEB_DRIVER_PATH = "/Users/choegyuhyeon/Downloads/chromedriver";;
    private static List<Subject> subjectList = new ArrayList<>();

    /*
    1. login
    2. mypage
    3. 강의콘텐츠
    4. 공지
    순으로 Crawling
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        long time0 = System.currentTimeMillis();

        // 경로 설정
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
//        options.addArguments("--blink-settings=imagesEnabled=false");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); // ??
        driver = new ChromeDriver(options);

        // 아이디 비밀번호 입력 (추후에 예외처리 해주기)
        Integer userId = 20182662;
        String pwd = "qwe@50584621";

        /*
        로그인
         */
        login(userId, pwd);

        /*
        마이페이지
         */
        setMySubjectNameAndProfessorAndHomeLink();

        /*
        과목 홈페이지 - 강의콘텐츠
         */
        for(Subject subject : subjectList){
            setMySubjectContents(subject);
        }

        /*
        과목 홈페이지 - 공지사항
         */
        for(Subject subject : subjectList){
            setMySubjectNotice(subject);
        }

        // subjectList 출력
        for(Subject subject : subjectList){
            System.out.println(subject.toString());
        }
        long time1 = System.currentTimeMillis();


        System.out.println();
        System.out.println("총 : " + (time1 - time0) + " msec");
    }


    /*
    로그인 후 마이페이지로 이동
     */
    static void login(Integer userId, String pwd) throws InterruptedException {
        // LMS Login page
        url  = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fclass.ssu.ac.kr%2Fxn-sso%2Fgw-cb.php/";

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
        Thread.sleep(1000);
        driver.get("https://canvas.ssu.ac.kr/learningx/dashboard?user_login=" + userId.toString() + "&locale=ko");
    }


    /*
    수강하고 있는 과목 정보를 가져온다 (name, homepageLink, professor)
     */
    static void setMySubjectNameAndProfessorAndHomeLink() {
        // 특정 element Explicit Waits
        element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> driver.findElement(By.className("xnscc-header-sub-title")));

        List<WebElement> elements = driver.findElements(By.className("xn-student-course-container"));

        for(WebElement element : elements){
            Subject subject = new Subject();
            // 과목 이름 찾기
            String name = element.findElement(By.className("xnscc-header-title")).getText();
            // 과목 홈페이지 링크 찾기
            String homepageLink = element.findElement(By.className("xnscc-header-redirect-link")).getAttribute("href");
            // 교수 이름 찾기
            List<WebElement> professors = element.findElements(By.className("xnscc-header-sub-title"));
            String professor = professors.get(1).getText().trim();

            // Subject 객체 초기화 -> 추후 빌더 패턴으로 변경
            subject.setName(name);
            subject.setHomepageLink(homepageLink);
            subject.setProfessor(professor);

            subjectList.add(subject);
        }
    }

    /*
    특정 과목의 강의컨텐츠에서 컨텐츠 정보를 가져와 contentPerWeekList 객체를 초기화한다.
     */
    static void setMySubjectContents(Subject subject){
        // 강의콘텐츠로 이동
        driver.get(subject.getHomepageLink()+"/external_tools/2");

        // frame 이동
        driver.switchTo().frame("tool_content");


        // 모든 주차 펴기
        element = new WebDriverWait(driver, Duration.ofSeconds(7))
                .until(ExpectedConditions.elementToBeClickable(By.className("xncb-section-wrapper"))); // Explicit Waits
        element = driver.findElement(By.className("xncb-fold-toggle-button"));
        if(element.getText().contains("펴기")) {
            element.click();
        }

        // 주차별 강의목록을 가져온다
        List<WebElement> weekList = driver.findElements(By.className("xncb-section-content-wrapper"));

        // ContentPerWeek 객체 초기화하여 List에 add
        List<ContentPerWeek> contentPerWeekList = new ArrayList<>();
        int weekNum = 1;
        for(WebElement weekWebElement : weekList){
            List<Content> contentList = new ArrayList<>();

            // 특정 주차에 해당하는 강의콘텐츠 Element 리스트를 가져온다.
            List<WebElement> contentElementList = weekWebElement.findElements(By.className("xncb-unit-content-wrapper"));

            // 특정 주차에 강의콘텐츠 없는 경우 예외 처리
            if(contentElementList.isEmpty()){
                weekNum += 1;
                continue;
            }

            // 특정 주차에 해당하는 강의콘텐츠 이름을 contentList에 추가한다.
            for(WebElement contentElement : contentElementList){
                // Content 객체 생성
                // set type
                String type = contentElement.findElement(By.className("xncb-component-icon")).getAttribute("class");
                type = type.split(" ")[1];
                // set name
                String name = contentElement.findElement(By.className("xncb-component-title")).getText();
                // set isDone (존재하면 넣기 메소드 찾기)
                String isDone = "None";
                boolean isExistence = contentElement.findElement(By.className("xncb-component-attendance-state-wrapper")).isDisplayed();
                if(isExistence){
                    isDone = contentElement.findElement(By.className("xncb-component-attendance-state")).getText();
                }
//                // set startDate
                String startDate = "None";
                isExistence = contentElement.findElement(By.className("xncb-component-periods-wrapper")).isDisplayed();
                if(isExistence){
                    startDate = contentElement.findElement(By.className("xncb-component-periods-item-date")).getText();
                }
                Content content = new Content(type, name, isDone, startDate);

                contentList.add(content);
            }


            // ContentPerWeek 객체 생성 후 add
            ContentPerWeek contentPerWeek = new ContentPerWeek(weekNum, contentList); // 임시 true
            contentPerWeekList.add(contentPerWeek);

            weekNum += 1;
        }
        subject.setContentPerWeekList(contentPerWeekList);
    }

    static void setMySubjectNotice(Subject subject){
        // 강의콘텐츠로 이동
        driver.get(subject.getHomepageLink()+"/announcements");

        // 특정 element Explicit Waits
        element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> driver.findElement(By.className("ic-item-row")));

        // 공지사항 Element 가져오기
        List<WebElement> noticeElementList = driver.findElements(By.className("ic-item-row"));

        // Notice 객체 초기화 하여 add
        List<Notice> noticeList = new ArrayList<>();
        for(WebElement webElement : noticeElementList){
            // 제목
            String title = webElement.findElement(By.className("emyav_fAVi")).getText();
            // 날짜
            String date = webElement.findElement(By.className("cjUyb_bLsb")).getText();
            // 링크
            String link = webElement.findElement(By.className("ic-item-row__content-link")).getAttribute("href");

            Notice notice = new Notice(title, date, link);
            noticeList.add(notice);
        }
        subject.setNoticeList(noticeList);
    }
}



/*
name
professor
homepageLink
contentPerWeekList (weeks, typeAndContentName, isDone)
noticeList (title, date)
 */




