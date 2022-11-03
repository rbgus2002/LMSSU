
const {Builder, By, Key, until} = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');    
const { delayed } = require('selenium-webdriver/lib/promise');

const loginDriver = async (driver) => {
  try {
      await driver.get('https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fclass.ssu.ac.kr%2Fxn-sso%2Fgw-cb.php');
      let userAgent = await driver.executeScript("return navigator.userAgent;")
      console.log('[UserAgent]', userAgent);

      await driver.wait(until.elementLocated(By.id('sLogin')), 10000);
      let resultElements  = await driver.findElements(By.className("inp placeholder"));

      console.log('[resultElements.length]', resultElements.length)
      resultElements[0].sendKeys(userid);
      resultElements[1].sendKeys(pwd);

      const loginBtn = await driver.findElement(By.className("btn_login"))
      await loginBtn.click();

      await driver.wait(until.elementLocated(By.className("xnp-manual-wrap xn-mypage")), 10000);
  }
  catch(e){
      console.log(e);
  }
  finally {
      return driver;
  }
}

const newDriver = async (showWindow = false) => {
  let driver = await new Builder();

  if(showWindow) {
    return await driver
      .forBrowser('chrome')
      .build();
  } else {
    return await driver
      .forBrowser('chrome')
      .setChromeOptions(new chrome.Options()
      .headless()
      .addArguments("--disable-gpu", "window-size=1920x1080", "lang=ko_KR"))
      .build();
  }
}

module.exports = { async getLogin(showWindow = false) {
  driver = await newDriver(showWindow);
  driver = await loginDriver(driver);
  return driver;
} };