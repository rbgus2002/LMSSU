const {Builder, By, Key, until} = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');    
const { delayed } = require('selenium-webdriver/lib/promise');

const loginDriver = require('./getLogin')
const getMessages = require('./message')
const getLectures = require('./lectures')

let userid = '20182618';
let pwd = 'Hyunsu99!@03';

module.exports.getMessages = async () => {
  let driver = await loginDriver.getLogin(userid, pwd, true);
  let messageList = await getMessages.getMessages(driver);
  
  driver.quit();
  return messageList;
}

module.exports.getLectures = async () => {
  let driver = await loginDriver.getLogin(userid, pwd, false);
  let lectureList;
  while(true) {
    lectureList = await getLectures.getLectures(driver, userid);
    if(lectureList != false) break;
  }
  
  driver.quit();
  return lectureList;
}