const {Builder, By, Key, until} = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');    
const { delayed } = require('selenium-webdriver/lib/promise');

const loginDriver = require('./getLogin')
const getMessages = require('./message')

module.exports.getMessages = async () => {
  let driver = await loginDriver.getLogin(false);
  let messageList = await getMessages.getMessages(driver);
  
  driver.quit();
  return messageList;
}