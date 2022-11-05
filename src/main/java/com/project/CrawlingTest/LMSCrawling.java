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

    public static void main(String[] args) throws InterruptedException, IOException {
        // 경로 설정
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); // ??
        driver = new ChromeDriver(options);

        // 아이디 비밀번호 입력 (추후에 예외처리 해주기)
        Integer userId = 20182662;
        String pwd = "qwe@50584621";

        login(userId, pwd);


        /*
        마이페이지
         */
        setMySubjectNameAndProfessorAndHomeLink();

        /*
        과목 홈페이지 - 강의콘텐츠 (추후 비동기 처리)
         */
        for(Subject subject : subjectList){

        }





        /*
        과목 홈페이지 - 공지사항
         */








        for(Subject subject : subjectList){
            System.out.println(subject.toString());
        }


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
                .until(driver -> driver.findElement(By.className("xnscc-header-title")));

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
}



/*
name
professor
homepageLink
contentPerWeekList (weeks, typeAndContentName, isDone)
noticeList (title, date)
 */




