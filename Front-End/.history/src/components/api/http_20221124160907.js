import axios from "axios";

// axios 객체 생성
export default axios.create({
  baseURL: "http://192.168.120.62/",
  // baseURL: "http://192.168.120.62/",

  headers: {
    "Content-Type": "application/json;charset=utf-8",
  },
});
