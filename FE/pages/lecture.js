import Head from 'next/head'
import Image from 'next/image'
import lecturestyles from '../styles/Lecture.module.css'
import MARQUEE from "react-fast-marquee";
import React, { useEffect, useRef, useLayoutEffect, useState, useCallback } from "react";
import axios from 'axios'

let stdid, pwd;

const slideRef = React.createRef();

export default function Lecture() {

  let semesterBegin = new Date("2022-09-01")
  let nowWeeks = parseInt((new Date().getTime() - semesterBegin.getTime())/(1000*60*60*24*7)+1)

  const [selectedWeeks, setSelectedWeeks] = useState(nowWeeks);
  const [weeklySubjectList, setWeeklySubjectList] = useState([]);

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);

    for(const param of searchParams) {
      if(param[0] == 'stdid') {
        stdid = param[1]
      }
      if(param[0] == 'pwd') {
        pwd = param[1]
      }
    }

    getLectureItems();
  }, [])
  
  useEffect(() => {
    const slideHeight = slideRef && slideRef.current && slideRef.current.offsetHeight;
    console.log(slideHeight)
    window.parent.postMessage({ head: "changeHeight", body: {view: "Lecture", height: slideHeight } }, '*');
  })
  
  const getLectureSubjects = (item) => {
    let subjectList = weeklySubjectList[selectedWeeks].weeksSubjectListDTO.subjectDTO[item].subjectContentsTitle.map((subject, idx) => {
      let img_src = "images/"
      if(subject.contentsType == "assignment") img_src += "assignment.png"
      else if(subject.contentsType == "mp4") img_src += "play-button.png"
      else if(subject.contentsType == "pdf") img_src += "pdf.png"
      else if(subject.contentsType == "file") img_src += "file.png"
      else if(subject.contentsType == "offline_attendance") img_src += "lecture.png"
      else if(subject.contentsType == "text") img_src += "text.png"

      return (
        <div key={"lectureSubject"+idx} className={lecturestyles.lecture_subject}>
          <img src={img_src}/>
          <h5>{subject.title}</h5>
        </div>
      )
    });
    if(subjectList.length == 0) {
      subjectList = (
        <div className={lecturestyles.lecture_subject}>
          <h5>-</h5>
        </div>
      )
    }
  
    return <div className={lecturestyles.lecture_subjects}>{subjectList}</div>;
  }
  
  const getLectureTodoLists = (item) => {
    const names = ["김익수교수님 사랑합니다", "수업시간 필기 정리하기", "수업시간 필기 정리하기", "수업시간 필기 정리하기", "수업시간 필기 정리하기"];
    let todoList = weeklySubjectList[selectedWeeks].weeksSubjectListDTO.subjectDTO[item].toDoDTO.map((subject, idx) => 
      <div key={"lectureTodoList"+idx} className={lecturestyles.lecture_todolist_content}>
        <input className={lecturestyles.lecture_todolist_check} type="checkbox" name="check" id="GFG" value="1" defaultChecked />
        <div className={lecturestyles.lecture_todolist_title}>
          <h4>{subject}</h4>
          <hr/>
        </div>
      </div>
    );
    if(todoList.length == 0) {
      todoList = (
        <div className={lecturestyles.lecture_todolist_content}>
          <div className={lecturestyles.lecture_todolist_title}>
            <h4>-</h4>
            <hr/>
          </div>
        </div>
      )
    }
  
    return <div className={lecturestyles.lecture_todolist}>{todoList}</div>;
  }
  
  const getLectureNotice = () => {
    return (
      <MARQUEE>안녕하세요 LMSSU입니다. 즐거운 학교생활 되세요~</MARQUEE>
    )
  }

  const getLectureItems = () => {
    const getApi = async () => {
      console.log("asd")
      const REQ_URL = "http://localhost:3000/apis/list?week="+selectedWeeks
      let data
      let tmpList = weeklySubjectList.slice()
      console.log(stdid, pwd)
      axios.defaults.withCredentials = true;
      await axios.post(REQ_URL, {
        studentId: stdid,
        userId: 0,
        pwd: pwd
      }, {
        withCredentials: true
      }).then((response) => {
        data = response.data
        tmpList[selectedWeeks] = data
        weeklySubjectList[selectedWeeks] = tmpList[selectedWeeks]
        console.log(tmpList)
      }).catch((error) => {
        console.log(error.response)
      });

      setWeeklySubjectList(tmpList)
      
      console.log(weeklySubjectList)
    }
    getApi();
  }
  
  const getLectureItemsComponent = useCallback(() => {
    let lectureList;

    if(weeklySubjectList[selectedWeeks] !== undefined) {
      lectureList = weeklySubjectList[selectedWeeks].weeksSubjectListDTO.subjectDTO.map((lecture, idx) =>  {
        return (
          <div key={"lecture"+idx} className={lecturestyles.lecture_item}>
            <div className={lecturestyles.lecture_top_bar}>
              <h3>{lecture.title}</h3>
              <div className={lecturestyles.div_grow}></div>
              <img src="images/home-icon-silhouette.png"/>
            </div>
            <div className={lecturestyles.lecture_notice}>
              <img src="images/megaphone.png"/>
              {getLectureNotice()}
            </div>
            <div className={lecturestyles.lecture_content}>
              {getLectureSubjects(idx)}
              {getLectureTodoLists(idx)}
            </div>
          </div>
        )
      });

      return (
        <div className={lecturestyles.lecture_items}>{lectureList}</div>
      )
    } else {
      return (
        "asdasd"
      )
    }
  }, [selectedWeeks, weeklySubjectList])

  const changeWeekPrev = () => {
    if(selectedWeeks > 1) {
      setSelectedWeeks(selectedWeeks - 1);
    }
  };

  const changeWeekNext = () => {
    if(selectedWeeks < 16) {
      setSelectedWeeks(selectedWeeks + 1);
    }
  };
  
  const getLectureWeeks = useCallback(() => {
    let startWeek = new Date(semesterBegin)
    startWeek.setDate(startWeek.getDate()+(selectedWeeks-1)*7)
    let endWeek = new Date(startWeek)
    endWeek.setDate(endWeek.getDate() + 7)

    if(weeklySubjectList[selectedWeeks] === undefined) {
      getLectureItems();
    }

    return (
      <div className={lecturestyles.lecture_board}>
        <div className={lecturestyles.lecture_week}>
          <h1 onClick={changeWeekPrev}>{"<"}</h1>
          <div>
            <h1>{selectedWeeks+"주차"}</h1>
            <h5>{(startWeek.getMonth()+1)+"/"+startWeek.getDate()+" ~ "+(endWeek.getMonth()+1)+"/"+endWeek.getDate()}</h5>
          </div>
          <h1 onClick={changeWeekNext}>{">"}</h1>
        </div>
        {getLectureItemsComponent()}
      </div>
    )
  }, [selectedWeeks])

  return (
    <div ref={slideRef}>
      <Head>
        <title>LMSSU</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main>
        {getLectureWeeks()}
      </main>
    </div>
  )
}
