
import puppeteer from 'puppeteer'

export default async function handler(req, res) {

  if (req.method === "POST") {
    const { studentId, pwd } = req.body;
    console.log(req.body)

    const browser = await puppeteer.launch({headless: true});
    const page = await browser.newPage();
    const ndhs_id = studentId; // 추후 로그인 폼에서 각자의 아이디 비밀번호를 입력받게 할 예정
    const ndhs_pw = pwd;

    page.setDefaultNavigationTimeout(3000); 

    await page.goto('https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fclass.ssu.ac.kr%2Fxn-sso%2Fgw-cb.php');
    
    await page.evaluate((id, pw) => {
      document.querySelector('#userid').value = id;
      document.querySelector('#pwd').value = pw;
    }, ndhs_id, ndhs_pw);


    await page.click('#sLogin > div > div:nth-child(1) > form > div > div:nth-child(2) > a');

    await page.waitForNavigation().then(() => {
      console.log('success')
    }).catch((res) => {
        console.log('fails', res)
    })

    if(page.url() === 'https://class.ssu.ac.kr/'){
      res.status(200).json({ id: ndhs_id, pwd: ndhs_pw, status: "OK" })
    } else {
      res.status(200).json({ id: ndhs_id, pwd: ndhs_pw, status: "FAIL" })
    }
  }
}