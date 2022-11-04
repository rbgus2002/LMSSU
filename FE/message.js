
const { text } = require('express');
const {Builder, By, Key, until} = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');    
const { delayed } = require('selenium-webdriver/lib/promise');

module.exports = { async getMessages(driver) {
  list = [];
  try {
      await driver.get('https://canvas.ssu.ac.kr/conversations#filter=type=inbox');
      //userAgent = await driver.executeScript("return navigator.userAgent;")
      //console.log('[UserAgent]', userAgent);

      // css selector로 가져온 element가 위치할때까지 최대 10초간 기다린다.
      await driver.wait(until.elementLocated(By.className('messages unstyled collectionViewItems')), 10000);
      before_length = 0;
      while(true) {
          notice  = await driver.findElements(By.className("read-state"));
          after_length = notice.length;
          console.log(after_length);
          notice[notice.length-1].click();
          notice[notice.length-1].click();

          if(before_length == after_length) {
              break;
          }
          before_length = after_length;
          await delayed(250);
      }

      messageContext = await driver.findElement(By.className("message-detail conversations__message-detail"));

      messages = await driver.findElements(By.className("message-left-column"));
      console.log("length", messages.length)
      for (var i = 0; i < messages.length; i++) {
          console.log(i + 'th Message :')
          messagesReadState = await messages[i].findElement(By.className("read-state"));
          //console.log(await messagesReadState.getAttribute('class'))
          messagesSelectCheckbox = await messages[i].findElement(By.className("select-checkbox"));
          messagesReadState.click();
          messagesSelectCheckbox.click();
          await driver.wait(until.elementLocated(By.className('message-content unstyled')), 10000);
          title = await messageContext.findElement(By.className("subject")).getText();
          username = await messageContext.findElement(By.className("username")).getText();
          context = await messageContext.findElement(By.className("context")).getText();
          date = await messageContext.findElement(By.className("date")).getText();
          content = await messageContext.findElement(By.xpath("//p")).getText();
          console.log('Title : ' + title);
          console.log('Name : ' + username + ' \tContext : ' + context + ' \tDate : ' + date);
          console.log('Text : \n' + content);
          messagesSelectCheckbox.click();
          list.push({
            "title" : title,
            "username" : username,
            "context" : context,
            "date" : date,
            "content" : content
          })
      }
  }
  catch(e){
    console.log(e);
  }
  finally {
      return list;
  }
}};