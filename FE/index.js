const express = require('express');
const router = express.Router();
const { timeout } = require('nodemon/lib/config');
const app = express();
const getApp = require('./app.js')

app.set("view engine", "ejs");

app.listen(8080, function() {
  console.log('listening on 8080');
});

app.get('/test', function(요청, 응답) {
  응답.sendFile(__dirname + '/index.html');
})

const {Builder, By, Key, until, WebDriver} = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');    
const { delayed } = require('selenium-webdriver/lib/promise');

let userid = '20182618';
let pwd = 'Hyunsu99!@03';

const mainPageDriver = async (driver) => {

    try {
        await driver.get('https://canvas.ssu.ac.kr/learningx/dashboard?user_login='+userid+'&locale=ko');
        userAgent = await driver.executeScript("return navigator.userAgent;")
        console.log('[UserAgent]', userAgent);

        // css selector로 가져온 element가 위치할때까지 최대 10초간 기다린다.
        await driver.wait(until.elementLocated(By.className('xnhts-top')), 5000);
        titles  = await driver.findElements(By.className("xnscc-header-title"));
        console.log('asdasdsd', titles.length)
        urls  = await driver.findElements(By.className("xnscc-header-redirect-link"));
        console.log('[resultElements.length]', titles.length)
        for (var i = 0; i < titles.length; i++) {
            console.log('- ' + await titles[i].getText())
            console.log('- ' + await urls[i].getAttribute("href"))
        }
    }
    catch(e){
        console.log(e);
    }
    finally {
        return driver;
    }
}

const getMessages = async (driver) => {
    try {
        await driver.get('https://canvas.ssu.ac.kr/conversations#filter=type=inbox');
        userAgent = await driver.executeScript("return navigator.userAgent;")
        console.log('[UserAgent]', userAgent);

        // css selector로 가져온 element가 위치할때까지 최대 10초간 기다린다.
        await driver.wait(until.elementLocated(By.className('messages unstyled collectionViewItems')), 10000);
        before_length = 0;
        while(true) {
            notice  = await driver.findElements(By.className("read-state"));
            after_length = notice.length;
            console.log(after_length);
            notice[notice.length-1].click();
            notice[notice.length-1].click();

            if(before_length == after_length || after_length % 10) {
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
            await delayed(50);
            await driver.wait(until.elementLocated(By.className('username')), 10000);
            title = await messageContext.findElement(By.className("subject"))
            username = await messageContext.findElement(By.className("username"));
            context = await messageContext.findElement(By.className("context"));
            date = await messageContext.findElement(By.className("date"));
            text = await messageContext.findElement(By.xpath("//p"));
            console.log('\tTitle : ' + await title.getText());
            console.log('\tName : ' + await username.getText());
            console.log('\tContext : ' + await context.getText());
            console.log('\tDate : ' + await date.getText());
            console.log('\tText : \n' + await text.getText());
            messagesSelectCheckbox.click();
        }
        return true;
    }
    catch(e){
        console.log(e);
    }
    finally {
        return false;
    }
}

const getLectureList = async (driver) => {
    let list = [];
    try {
        await driver.get('https://canvas.ssu.ac.kr/');
        console.log('Function Load : getLecture()');

        lectures  = await driver.findElements(By.className("ic-DashboardCard"));
        console.log('asdasdsd', lectures.length);
        for (var i = 0; i < lectures.length; i++) {
            lectureURL = await lectures[i].findElement(By.className('ic-DashboardCard__link')).getAttribute("href");
            lectureName = await lectures[i].findElement(By.className('screenreader-only')).getText();
            list.push({'lectureName' : lectureName, 'lectureURL' : lectureURL});
            console.log(i + ' ' + list[i]);
        }
    }
    catch(e){
        console.log(e);
    }
    finally {
        return list;
    }
}

const getLectureInfo = async (driver, URL) => {
    let list = [];
    try {
        await driver.get(URL);
        console.log('Function Load : getLectureInfo()');

        await driver.switchTo().frame('tool_content')
        await driver.wait(until.elementLocated(By.className('xncb-section-wrapper ')), 3000)
        foldBtn = await driver.findElement(By.className("xncb-fold-toggle-button"))
        foldBool = await foldBtn.getText();
        if(foldBool.includes('펴기')) {
            console.log('펴짐')
            foldBtn.click()
        }
        await delayed(250)
        lectureUnit = await driver.findElements(By.className("xncb-component-wrapper  "));
        console.log(lectureUnit.length)
        for (var i = 0; i < lectureUnit.length; i++) {
            lectureTypeDiv = await lectureUnit[i].findElement(By.className("xncb-component-icon"))
            lectureTypeAttr = await lectureTypeDiv.getAttribute('class')
            lectureType = 'etc'
            if(lectureTypeAttr.includes('mp4')) {
                lectureType = 'mp4'
            } else if(lectureTypeAttr.includes('everlec')) {
                lectureType = 'everlec'
            } else if(lectureTypeAttr.includes('offline_attendance')) {
                lectureType = 'offline_attendance'
            } else if(lectureTypeAttr.includes('offline_attendance')) {
                lectureType = 'offline_attendance'
            } else if(lectureTypeAttr.includes('file')) {
                lectureType = 'file'
            } else if(lectureTypeAttr.includes('pdf')) {
                lectureType = 'pdf'
            } else if(lectureTypeAttr.includes('video_conference')) {
                lectureType = 'video_conference'
            } else if(lectureTypeAttr.includes('text')) {
                lectureType = 'text'
            } else if(lectureTypeAttr.includes('assignment')) {
                lectureType = 'assignment'
            }
            lectureComplete = false
            if(lectureTypeAttr.includes('complete')) {
                lectureComplete = true
            }
            lectureTitle = await lectureUnit[i].findElement(By.className("xncb-component-title")).getText()

            list.push({"lectureTitle" : lectureTitle, "lectureType" : lectureType, "lectureComplete" : lectureComplete})
        }
    }
    catch(e){
        console.log(e);
    }
    finally {
        return list;
    }
}

const run = async () => {
    /*
    let mainPageLoad = false;
    while(!mainPageLoad) {
        mainPageLoad = await mainPageDriver(driver);
    }
    */
    //await getMessages(driver);
    /*
    let lectureList = await getLectureList(driver);
    let lectureInfoList = []
    console.log(lectureList.length)
    for(var i = 0; i < lectureList.length; i++) {
        lectureInfoList.push({"lectureName" : lectureList[i].lectureName, "lectureInfo" : await getLectureInfo(driver, lectureList[i].lectureURL + '/external_tools/2')})
    }
    console.log(lectureInfoList)
    */
    let messageList = getApp.getMessages();
    
    return messageList
}

app.get('/hello', async function(req, res) {
    res.render('crawl', {"messageList" : [], "testfunc" : getApp.getMessages})
    //messageList = await run();
    //res.render('crawl', {"messageList" : [], "testfunc" : getApp.getMessages()})
})