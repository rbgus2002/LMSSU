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
    private static List<Subject> subjectList = new ArrayList<>();

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

        for(String url : answer){
            driver.get(url+"/external_tools/2");


            // frame 이동
            driver.switchTo().frame("tool_content");

            // 모든 주차 펴기
            element = new WebDriverWait(driver, Duration.ofSeconds(7))
                    .until(ExpectedConditions.elementToBeClickable(By.className("xncb-section-wrapper"))); // Explicit Waits
            driver.findElement(By.className("xncb-fold-toggle-button")).click();
            List<WebElement> weekList = driver.findElements(By.className("xncb-section-content-wrapper"));

            // lectureContentsPerWeeks 객체 생성
            int weeks = 1;
            Map<Integer, ArrayList<String>> lectureContentsPerWeeks = new HashMap<>();

            // 주차별 강의 콘텐츠 제목 가져오기
            for (WebElement week : weekList){
                lectureContentsPerWeeks.put(weeks, new ArrayList<>());
                List<WebElement> contentList = week.findElements(By.className("xncb-component-title"));

                // 해당 주차에 강의 콘텐츠 없는 경우 예외처리
                if(contentList.isEmpty())
                    break;

                // Map에서 Key에 해당하는 Value 값 업데이트 (weeks주차에 강의 콘텐츠 추가)
                for(WebElement content : contentList){
                    ArrayList<String> tmp = lectureContentsPerWeeks.get(weeks);
                    tmp.add(content.getText());
                    lectureContentsPerWeeks.put(weeks, tmp);
                }
                weeks += 1;
            }

            for(int i = 1; i < weeks; i++){
                System.out.println("------------------------" + i + "주차" + "------------------------");
                ArrayList<String> contents = lectureContentsPerWeeks.get(i);
                for(String content : contents){
                    System.out.println(content);
                }
            }
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
