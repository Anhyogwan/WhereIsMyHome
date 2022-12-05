import axios from "axios";

// local vue api axios instance
function apiInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// local vue api axios instance
function fileApiInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL,
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return instance;
}

// house deal API axios instance
function houseInstance() {
    const instance = axios.create({
        baseURL: "http://192.168.120.62/",
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
    });
    return instance;
}

export { apiInstance, houseInstance, fileApiInstance };
