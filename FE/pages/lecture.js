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
  }, [])
  
  useEffect(() => {
    const slideHeight = slideRef && slideRef.current && slideRef.current.offsetHeight;
    console.log(slideHeight)
    window.parent.postMessage({ head: "changeHeight", body: {view: "Lecture", height: slideHeight } }, '*');
  })
  
  const getLectureSubjects = () => {
    const names = [
      {title: "4주차 강의 영상", src: "images/play-button.png"}, 
      {title: "4주차 2차시 00:00", src: "images/lecture.png"}, 
      {title: "4주차 강의 자료", src: "images/pdf.png"}
    ];
    const subjectList = names.map((subject, idx) => 
      <div key={"lectureSubject"+idx} className={lecturestyles.lecture_subject}>
        <img src={subject.src}/>
        <h4>{subject.title}</h4>
      </div>
    );
  
    return <div className={lecturestyles.lecture_subjects}>{subjectList}</div>;
  }
  
  const getLectureTodoLists = () => {
    const names = ["김익수교수님 사랑합니다", "수업시간 필기 정리하기", "수업시간 필기 정리하기", "수업시간 필기 정리하기", "수업시간 필기 정리하기"];
    const todoList = names.map((subject, idx) => 
      <div key={"lectureTodoList"+idx} className={lecturestyles.lecture_todolist_content}>
        <input className={lecturestyles.lecture_todolist_check} type="checkbox" name="check" id="GFG" value="1" defaultChecked />
        <div className={lecturestyles.lecture_todolist_title}>
          <h4>{subject}</h4>
          <hr/>
        </div>
      </div>
    );
  
    return <div className={lecturestyles.lecture_todolist}>{todoList}</div>;
  }
  
  const getLectureNotice = () => {
    return (
      <MARQUEE>안녕하세요 LMSSU입니다. 즐거운 학교생활 되세요~</MARQUEE>
    )
  }
  
  const getLectureItems = useCallback(() => {
    const lectureSubjects = getLectureSubjects();
    const lectureTodoLists = getLectureTodoLists();
  
    const names = ["[오픈소스기반기초설계]", "[과목명]", "[과목명]"];
    const lectureNotice = getLectureNotice();
  
    const lectureList = names.map((lecture, idx) => 
      <div key={"lecture"+idx} className={lecturestyles.lecture_item}>
        <div className={lecturestyles.lecture_top_bar}>
          <h3>{lecture}</h3>
          <div className={lecturestyles.div_grow}></div>
          <img src="images/home-icon-silhouette.png"/>
        </div>
        <div className={lecturestyles.lecture_notice}>
          <img src="images/megaphone.png"/>
          {lectureNotice}
        </div>
        <div className={lecturestyles.lecture_content}>
          {lectureSubjects}
          {lectureTodoLists}
        </div>
      </div>
    );

    if(weeklySubjectList[selectedWeeks] === undefined) {
      const getApi = async () => {
        console.log("asd")
        const REQ_URL = "./api/proxy/list?week="+selectedWeeks
  
        var REQ_PARAM = {
          studentId: stdid,
          userId: 0,
          pwd: pwd
        };

        console.log(REQ_PARAM)

        await axios.post(REQ_URL, 
          {
            studentId: stdid,
            userId: 0,
            pwd: pwd
          }).then((response) => {
            const data = response.data
            console.log(data)
          }).catch((error) => {
            console.log(error.response)
          });
/* 
        axios({
          method: "post",
          url: REQ_URL,
          data: REQ_PARAM,
          headers: REQ_HEADER,
          timeout: 10000
        })
        .then(function(response) {
            console.log("");
            console.log("RESPONSE : " + JSON.stringify(response.data));
            console.log("");
        })
        .catch(function(error) {
            console.log("");
            console.log("ERROR : " + JSON.stringify(error));
            console.log("");
        }); */
      }
      getApi();
    }

    if(weeklySubjectList[selectedWeeks] === undefined) return (
      "asdasd"
    )
    else return (
      <div className={lecturestyles.lecture_items}>{lectureList}</div>
    )
  }, [selectedWeeks, weeklySubjectList])

  const changeWeekPrev = () => {
    if(selectedWeeks > 1)
      setSelectedWeeks(selectedWeeks - 1);
  };

  const changeWeekNext = () => {
    if(selectedWeeks < 16)
      setSelectedWeeks(selectedWeeks + 1);
  };
  
  const getLectureWeeks = useCallback(() => {
    let startWeek = new Date(semesterBegin)
    startWeek.setDate(startWeek.getDate()+(selectedWeeks-1)*7)
    let endWeek = new Date(startWeek)
    endWeek.setDate(endWeek.getDate() + 7)

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
        {getLectureItems()}
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
