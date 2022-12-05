import { apiInstance } from "./index.js";

const api = apiInstance();

async function login(user, success, fail) {
    await api.post(`/user/login`, JSON.stringify(user)).then(success).catch(fail);
}

async function findById(userid, status, success, fail) {
    api.defaults.headers["access-token"] = sessionStorage.getItem("access-token");
    await api.get(`/user/info/${userid}/${status}`).then(success).catch(fail);
}

async function tokenRegeneration(user, status, success, fail) {
    api.defaults.headers["refresh-token"] = sessionStorage.getItem("refresh-token"); //axios header에 refresh-token 셋팅
    await api.post(`/user/refresh/${status}`, user).then(success).catch(fail);
}

async function logout(userid, status, success, fail) {
    api.defaults.headers["access-token"] = sessionStorage.getItem("access-token");
    await api.post(`/user/logout/${userid}/${status}`).then(success).catch(fail);
}

// 사용자 목록
function userList(success, fail) {
  api.get(`/user/users`).then(success).catch(fail);
}

// 사용자 삭제
function delUser(userId, success, fail) {
  api.delete(`/user/delete/${userId}`).then(success).catch(fail);
}

// 사용자 등록
function insertUser(user, success, fail) {
  console.log(user);
  api.post(`/user/join`, user).then(success).catch(fail);
}

// 사용자 수정
function updateUser(user, success, fail) {
  api.put(`/user/update`, user).then(success).catch(fail);
}

function sendEmail(email, success, fail) {
  console.log(email);
  api.post(`/user/sendmail`, { Email: email }).then(success).catch(fail);
}

function insertNewUser(user, success, fail) {
  api.post(`/user/join`, user).then(success).catch(fail);
}
function sendFindEmail(info, success, fail) {
  api.post(`/user/findmail`, info).then(success).catch(fail);
}
function updatePass(user, success, fail) {
  api.put(`/user/updatepwd`, user).then(success).catch(fail);
}
function deleteCode(code, success, fail) {
  api.delete(`/user/deletecode/${code}`).then(success).catch(fail);
}

export { userList, delUser, insertUser, updateUser, login, findById, tokenRegeneration, logout, sendEmail, insertNewUser, sendFindEmail, updatePass,deleteCode };
