import { apiInstance, fileApiInstance } from "./index.js";

const api = apiInstance();
const fileApi = fileApiInstance();

// 공지사항항 목록
function articleList(page, success, fail) {
  api.get(`/board/articles/${page}`).then(success).catch(fail);
}

// 공지사항 검색
function searchList(data, success, fail) {
  api.get(`/board/search/${data.searchScope}/${data.searchParam}`).then(success).catch(fail);
}

// 공지사항 상세
function articleDetail(articleNo, success, fail) {
  api.get(`/board/detail/${articleNo}`).then(success).catch(fail);
}

// 공지사항 삭제
function deleteArticle(articleNo, success, fail) {
  api.delete(`/board/delete/${articleNo}`).then(success).catch(fail);
}

// 공지사항 등록
function insertnotice(data, success, fail) {
  // let data = upload.upfile.append("request", JSON.stringify(upload.article));
  // console.log(data);
  fileApi.post(`/board/regist`, data).then(success).catch(fail);
}

// 공지사항 수정
function modifyArticle(data, success, fail) {
  fileApi.put(`/board/upload/${data.articleNo}`, data.formdata).then(success).catch(fail);
}



function getRecentArticle(success, fail) {
  api.get(`/board/recent`).then(success).catch(fail);
}

export {
  articleList,
  articleDetail,
  deleteArticle,
  insertnotice,
  searchList,
  modifyArticle,
  getRecentArticle
};
