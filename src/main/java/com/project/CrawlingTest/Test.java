import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class Test {
    private static WebDriver driver;
    private static WebElement element;
    private static String url;
    private static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    private static String WEB_DRIVER_PATH = "/Users/choegyuhyeon/Downloads/chromedriver";;

    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    /*
    LMS 로그인 → 주차별 강의목록
     */
    private static void test1() throws InterruptedException {
        long time0 = System.currentTimeMillis();
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); // ??
        driver = new ChromeDriver(options);

        // 아이디 비밀번호 입력 (추후에 예외처리 해주기)
        Integer userId = 20182662;
        String pwd = "qwe@50584621";

        // LMS Login page //tmp
        url  = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fclass.ssu.ac.kr%2Fxn-sso%2Fgw-cb.php/";

        // 페이지 로딩 //tmp
        driver.get(url);
        long time1 = System.currentTimeMillis();

        login(userId, pwd);

        // ------------------------------------------------------------
        List<String> answer = new ArrayList<>();

        // 특정 element Explicit Waits
        element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> driver.findElement(By.className("xnscc-header-redirect-link")));

        // 크롤링
        List<WebElement> elements = driver.findElements(By.className("xnscc-header-redirect-link"));

        for (WebElement tmp : elements) {
            answer.add(tmp.getAttribute("href"));
        }
        //------------------------------------------------------------









        //------------------------------------------------------------
//        for(String url : answer){
//        }
        url = "https://canvas.ssu.ac.kr/courses/17027";
        driver.get(url+"/announcements");

        // 특정 element Explicit Waits
        element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> driver.findElement(By.className("ic-item-row")));

        // 공지사항 Element 가져오기
        List<WebElement> noticeList = driver.findElements(By.className("ic-item-row"));
        for(WebElement webElement : noticeList){
            // 제목
            System.out.println(webElement.findElement(By.className("emyav_fAVi")).getText());
            // 링크
            System.out.println(webElement.findElement(By.className("ic-item-row__content-link")).getAttribute("href"));
            // 날짜
            System.out.println(webElement.findElement(By.className("cjUyb_bLsb")).getText());

        }






        long time2 = System.currentTimeMillis();

        System.out.println();
        System.out.println("시작 ~ 로그인 창 : " + (time1 - time0) + " msec");
        System.out.println("로그인 창 ~ 완료 : " + (time2 - time1) + " msec");
        System.out.println("총 : " + (time2 - time0) + " msec");
    }

    static void login(Integer userId, String pwd) throws InterruptedException {
//        // LMS Login page
//        url  = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fclass.ssu.ac.kr%2Fxn-sso%2Fgw-cb.php/";
//
//        // 페이지 로딩
//        driver.get(url);
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
        Thread.sleep(1000);
    }

}