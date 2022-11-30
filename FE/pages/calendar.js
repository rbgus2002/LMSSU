import Head from 'next/head'
import Image from 'next/image'
import calendarstyles from '../styles/Calendar.module.css'
import React, { useEffect, useRef, useLayoutEffect, useState, useCallback } from "react";

if (typeof window !== "undefined") {
  window.onload = () => {
  }

  window.onclick = (e) => {
  }
}

const slideRef = React.createRef();
const SetHeight = () => {
  useEffect(() => {
    const slideHeight = slideRef && slideRef.current && slideRef.current.offsetHeight;
    console.log(slideHeight)
    window.parent.postMessage({ head: "changeHeight", body: {view: "Calendar", height: slideHeight } }, '*');
  }, []);
}

function checkLeapYear(year) {
    if (year%400 == 0) {
      return true;
    } else if (year%100 == 0) {
      return false;
    } else if (year%4 == 0) {
      return true;
    } else {
      return false;
    }
}
function getFirstDayOfWeek(year, month){
  let monthStr = ""+month
  if (month < 10) monthStr = "0" + month;
  return (new Date(year + "-" + monthStr + "-01")).getDay();
}

function getCalendarDateContext(year, month){
  let arrCalendar=[];
  let month_day=[31,28,31,30,31,30,31,31,30,31,30,31]
  if (month == 2){
    if (checkLeapYear(year))month_day[1] = 29;
  }
  let first_day_of_week= getFirstDayOfWeek(year, month);
  let arrCalendarItem=[];
  for(let i = 0; i < first_day_of_week; i++){
    arrCalendarItem.push("");
    if(arrCalendarItem.length == 7) {
      arrCalendar.push(arrCalendarItem.slice());
      arrCalendarItem.splice(0);
    }
  }

  for(let i = 1; i <= month_day[month-1]; i++){
    arrCalendarItem.push(String(i));
    if(arrCalendarItem.length == 7) {
      arrCalendar.push(arrCalendarItem.slice());
      arrCalendarItem.splice(0);
    }
  }

  let remain_day = 7 - (arrCalendarItem.length%7);
  if(remain_day < 7){
    for(let i = 0; i < remain_day; i++){
      arrCalendarItem.push("");
      if(arrCalendarItem.length == 7) {
        arrCalendar.push(arrCalendarItem.slice());
        arrCalendarItem.splice(0);
      }
    }
  }

  return arrCalendar;
}

const getCalendarDay = () => {
  const names = ["일", "월", "화", "수", "목", "금", "토"];
  const calendarDay = names.map((day, idx) => {
    if(idx == 0) return <h5 className={calendarstyles.sun}>{day}</h5>
    else if(idx == 6) return <h5 className={calendarstyles.sat}>{day}</h5>
    else return <h5>{day}</h5>
  });

  return (
    <div className={calendarstyles.calendar_day}>
      {calendarDay}
    </div>
  )
}

const getCalendarDate = (year, month) => {
  let calendarDate = getCalendarDateContext(year, month);

  const celendarDateComponent = calendarDate.map((dateLine) => {
    const calendarDateLine = dateLine.map((date, idx) => {
      console.log(idx);
      if(idx == 0) return <td className={calendarstyles.sun}>{date}</td>
      else if(idx == 6) return <td className={calendarstyles.sat}>{date}</td>
      else return <td>{date}</td>
    })

    return <tr>{calendarDateLine}</tr>
  });

  return (
    <table className={calendarstyles.calendar_date}>
      <tbody>
        {celendarDateComponent}
      </tbody>
    </table>
  )
}

const getMonthSelect = (month) => {
  let monthArr = [];
  for(let i = 1; i <= 12; i++) {
    monthArr.push(i);
  }

  const monthOption = monthArr.map((m) => {
    if(m == month) return <option value={m} selected>{m+"월"}</option>
    else return <option value={m}>{m+"월"}</option>
  })

  return (
    <select onChange={handleSelect} name="month" id="monthSelect">
      {monthOption}
    </select>
  )
}

const handleSelect = (e) => {
  calendarDate = getCalendarDate(year, e.target.select);
  console.log("asd");
};
let year, month;

export default function Calendar() {

  const calendarDay = getCalendarDay();

  let now = new Date();
  const [selectedYear, setSelectedYear] = useState(now.getFullYear()); //현재 선택된 연도
  const [selectedMonth, setSelectedMonth] = useState(now.getMonth()+1); //현재 선택된 달
  const dateTotalCount = new Date(selectedYear, selectedMonth, 0).getDate(); //선택된 연도, 달의 마지막 날짜

  const changeYearPrev = useCallback(() => {
    setSelectedYear(selectedYear - 1);
  }, [selectedYear]);

  const changeYearNext = useCallback(() => {
    setSelectedYear(selectedYear + 1);
  }, [selectedYear]);

  const yearControl = useCallback(() => {
    //연도 선택박스에서 고르기
    let yearArr = [];
    const startYear = today.year - 10; //현재 년도부터 10년전 까지만
    const endYear = today.year + 10; //현재 년도부터 10년후 까지만
    for (let i = startYear; i < endYear + 1; i++) {
      yearArr.push(
        <option key={i} value={i}>
          {i}년
        </option>
      );
    }
    return (
      <select
        // className="yearSelect"
        onChange={changeSelectYear}
        value={selectedYear}
      >
        {yearArr}
      </select>
    );
  }, [selectedYear]);

  const calendarMonth = useCallback(() => {
    //달 선택박스에서 고르기
    let monthOption = [];
    for(let i = 1; i <= 12; i++) {
      monthOption.push(
        <option value={i}>{i+"월"}</option>
      );
    }

    return (
      <select onChange={changeSelectMonth} name="month" id="monthSelect" value={selectedMonth}>
        {monthOption}
      </select>
    )
  }, [selectedMonth]);

  const changeSelectMonth = (e) => {
    setSelectedMonth(Number(e.target.value));
  };

  const calendarDate = useCallback(() => {
    let calendarDate = getCalendarDateContext(selectedYear, selectedMonth);
  
    const celendarDateComponent = calendarDate.map((dateLine) => {
      const calendarDateLine = dateLine.map((date, idx) => {
        console.log(idx);
        if(idx == 0) return <td className={calendarstyles.sun}>{date}</td>
        else if(idx == 6) return <td className={calendarstyles.sat}>{date}</td>
        else return <td>{date}</td>
      });
  
      return <tr>{calendarDateLine}</tr>
    });
  
    return (
      <table className={calendarstyles.calendar_date}>
        <tbody>
          {celendarDateComponent}
        </tbody>
      </table>
    )
  }, [selectedYear, selectedMonth, dateTotalCount]);

  return (
    <div ref={slideRef}>
      <Head>
        <title>LMSSU</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main>
        <div className={calendarstyles.calendar_board}>
          <div className={calendarstyles.calendar_title}>
            <div className={calendarstyles.calendar_title_year}>
              <h5 onClick={changeYearPrev}>{"<"}</h5>
              <div>
                <h3>{selectedYear}</h3>
                <h5>학년도</h5>
              </div>
              <h5 onClick={changeYearNext}>{">"}</h5>
            </div>
            <div className={calendarstyles.calendar_title_contour}></div>
            <div className={calendarstyles.calendar_title_month}>
              {calendarMonth()}
            </div>
            <div className={calendarstyles.calendar_title_contour}></div>
            <div className={calendarstyles.calendar_title_schedule}>
              <div className={calendarstyles.calendar_title_schedule_btn}>
                <h5>시험일정 추가</h5>
              </div>
            </div>
          </div>
          <div className={calendarstyles.calendar_board_contour}></div>
          <div className={calendarstyles.calendar_context}>
            {calendarDay}
            {calendarDate()}
          </div>
        </div>
      </main>
      
      <SetHeight/>
    </div>
  )
}
