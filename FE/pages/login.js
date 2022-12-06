import Head from 'next/head'
import Image from 'next/image'
import loginstyles from '../styles/Login.module.css'
import React, { useEffect, useRef, useLayoutEffect, useState, useCallback } from "react";
import axios from 'axios'

export default function Login() {

  const slideRef = React.createRef();

  const [loginSubmit, setLoginSubmit] = useState(0)
  const [signUpSubmit, setSignUpSubmit] = useState(0)
  const [signUpMode, setSignUpMode] = useState(0)

  const LoginForm = useCallback(() => {
    const [values, setValues] = useState({
      stdid: "",
      pwd: "",
      name: "",
      major: "",
    })
  
    const [errors, setErrors] = useState({
      stdid: "",
      pwd: "",
      name: "",
      major: "",
    })
  
    // 필드 방문 상태를 관리한다
    const [touched, setTouched] = useState({
      stdid: false,
      pwd: false,
      name: false,
      major: false,
    })
  
    const handleChange = e => {
      setValues({
        ...values,
        [e.target.name]: e.target.value,
      })
    }
  
    // blur 이벤트가 발생하면 touched 상태를 true로 바꾼다
    const handleBlur = e => {
      setTouched({
        ...touched,
        [e.target.name]: true,
      })
    }
    
    const handleLoginSubmit = async e => {
      let status = 0

      e.preventDefault()
  
      // 모든 필드에 방문했다고 표시한다.
      setTouched({
        stdid: true,
        pwd: true,
        name: true,
        major: true,
      })
  
      // 필드 검사 후 잘못된 값이면 제출 처리를 중단한다.
      const errors = validate()
      // 오류 메세지 상태를 갱신한다
      setErrors(errors)
      // 잘못된 값이면 제출 처리를 중단한다.
      if (Object.values(errors).some(v => v)) {
        return
      }
      console.log("로그인중..")

      setLoginSubmit(1)
      
      console.log("로그인중..")
      await axios.post(process.env.FRONT_BASE_URL + "/api/crawl", {
        studentId: values.stdid,
        pwd: values.pwd
      }, {
        withCredentials: true
      }).then((response) => {
        console.log(response)
        if(response.data.status == 'FAIL') {
          alert("잘못된 학번 혹은 비밀번호입니다")
          setLoginSubmit(0)
          return
        }
        status = 1
      }).catch((error) => {
        console.log(error.response)
        alert("ERROR!")
        setLoginSubmit(0)
        return
      });

      if(status != 1) return

      await axios.post(process.env.FRONT_BASE_URL+"/apis/student/sign-in", {
        studentId: values.stdid,
        userId: values.stdid,
        pwd: values.pwd
      }, {
        withCredentials: true
      }).then((response) => {
        console.log(response)
        if(response.data.student == 'new') {
          status = 2
          let retval = confirm("미등록된 사용자입니다.\n회원가입을 진행하시겠습니까?")
          if(retval) {
            setSignUpMode(1)
          } else {
            setLoginSubmit(0)
          }
        }
      }).catch((error) => {
        console.log(error.response)
        alert("ERROR!")
        setLoginSubmit(0)
        return
      });
      
      if(status == 1) {
        window.location.href=process.env.FRONT_BASE_URL+'/?stdid='+values.stdid+'&pwd='+values.pwd;
      }
    }
  
    const handleSignUpSubmit = async e => {

      setSignUpSubmit(1)

      e.preventDefault()
  
      // 모든 필드에 방문했다고 표시한다.
      setTouched({
        stdid: true,
        pwd: true,
        name: true,
        major: true,
      })
  
      // 필드 검사 후 잘못된 값이면 제출 처리를 중단한다.
      const errors = validate()
      // 오류 메세지 상태를 갱신한다
      setErrors(errors)
      // 잘못된 값이면 제출 처리를 중단한다.
      if (Object.values(errors).some(v => v)) {
        return
      }

      let retval = confirm("해당 정보로 회원가입을 진행합니다")
      if(!retval) {
        setSignUpMode(0)
        return
      }
      
      await axios.post(process.env.FRONT_BASE_URL+"/apis/student/sign-up", {
        studentId: values.stdid,
        major: values.major,
        studentName: values.name
      }, {
        withCredentials: true
      }).then((response) => {
        console.log(response)
      }).catch((error) => {
        console.log(error.response)
        alert("ERROR!")
        setSignUpSubmit(0)
        return
      });

      window.location.href=process.env.FRONT_BASE_URL+'/?stdid='+values.stdid+'&pwd='+values.pwd;
    }
  
    // 필드값을 검증한다.
    const validate = useCallback(() => {
      const errors = {
        stdid: "",
        pwd: "",
        name: "",
        major: "",
      }
  
      if (!values.stdid) {
        errors.stdid = "이메일을 입력하세요"
      }
      if (!values.pwd) {
        errors.pwd = "비밀번호를 입력하세요"
      }
      if (signUpMode && !values.name) {
        errors.name = "이름을 입력하세요"
      }
      if (signUpMode && !values.major) {
        errors.major = "학과/학부를 입력하세요"
      }

      return errors
    }, [values])
  
    // 입력값이 변경될때 마다 검증한다.
    useEffect(() => {
      validate()
    }, [validate])
    
    return (
      <form onSubmit={signUpMode==0?handleLoginSubmit:handleSignUpSubmit} className={loginstyles.login_form}>
        <div>
          <h4>아이디</h4>
          <input
            type="text"
            name="stdid"
            placeholder="학번을 입력하세요"
            value={values.stdid}
            onChange={handleChange}
            onBlur={handleBlur}
            disabled={loginSubmit==1?"disabled":""}
          />
          {touched.stdid && errors.stdid && <h6>{errors.stdid}</h6>}
        </div>
        <div>
          <h4>비밀번호</h4>
          <input
            type="password"
            name="pwd"
            placeholder="비밀번호를 입력하세요"
            value={values.pwd}
            onChange={handleChange}
            onBlur={handleBlur}
            disabled={loginSubmit==1?"disabled":""}
          />
          {touched.pwd && errors.pwd && <h6>{errors.pwd}</h6>}
        </div>

        {signUpMode==1?(<hr/>):""}
        {signUpMode==1?(
          <div>
            <h4>이름</h4>
            <input
              type="text"
              name="name"
              placeholder="이름을 입력하세요"
              value={values.name}
              onChange={handleChange}
              onBlur={handleBlur}
              disabled={signUpSubmit==1?"disabled":""}
            />
            {touched.name && errors.name && <h6>{errors.name}</h6>}
          </div>
        ):""}
        {signUpMode==1?(
          <div>
            <h4>학과/학부</h4>
            <input
              type="text"
              name="major"
              placeholder="비밀번호를 입력하세요"
              value={values.major}
              onChange={handleChange}
              onBlur={handleBlur}
              disabled={signUpSubmit==1?"disabled":""}
            />
            {touched.major && errors.major && <h6>{errors.major}</h6>}
          </div>
        ):""}
        <button type="submit">{signUpMode==0?"로그인":"회원가입"}</button>
      </form>
    )
  }, [loginSubmit, signUpSubmit, signUpMode])

  return (
    <div ref={slideRef}>
      <Head>
        <title>LMSSU 로그인</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main>
        <div className={loginstyles.login_board}>
          <h2>LMSSU:로그인</h2>
          {LoginForm()}
        </div>
      </main>
    </div>
  )
}
