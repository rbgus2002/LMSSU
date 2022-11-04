
const { text } = require('express');
const {Builder, By, Key, until} = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');    
const { delayed } = require('selenium-webdriver/lib/promise');

module.exports = { async getLectures(driver, userid) {
  list = [];
  try {
      await driver.get('https://canvas.ssu.ac.kr/learningx/dashboard?user_login='+userid+'&locale=ko');
      
      await driver.wait(until.elementLocated(By.className('xnms-my-subject-title')), 10000);
      lectures = await driver.findElements(By.className("xn-student-course-container"));
      for(let i = 0; i < lectures.length; i++) {
        title = await lectures[i].findElement(By.className("xnscc-header-title")).getText();
        subInfo = await lectures[i].findElements(By.className("xnscc-header-sub-title"));
        term = await subInfo[0].getText();
        professor = await subInfo[1].getText();
        id = await lectures[i].findElement(By.className("xnscc-header-redirect-link")).getAttribute('href');
        list.push({"title" : title, "term" : term, "professor" : professor, "id" : id});
      }
      console.log(list);
  }
  catch(e){
    console.log(e);
    return false;
  }
  finally {
    return list;
  }
}};