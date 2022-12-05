import { apiInstance } from "./index.js";

const api = apiInstance();

function articleList(page, success, fail) {
  console.log(page.searchScope);
  console.log(page.searchParam);
  api.get(`/freeboard/articles/${page.page}?searchScope=${page.searchScope}&searchParam=${page.searchParam}`, [page.searchScope, page.searcParam]).then(success).catch(fail);
}
function searchList(data, success, fail) {
  api.get(`/freeboard/search/${data.searchScope}/${data.searchParam}`).then(success).catch(fail);
}

function articleDetail(articleNo, success, fail) {
  api.get(`/freeboard/detail/${articleNo}`).then(success).catch(fail);
}

function newComment(comment, success, fail) {
  api.post(`/freeboard/comment`, comment).then(success).catch(fail);
}

function newreComment(recomment, success, fail) {
  api.post(`/freeboard/comment`, recomment).then(success).catch(fail);
}
function modifyCommnet(comment, success, fail) {
  api.put(`/freeboard/comment`, comment).then(success).catch(fail);
}
function deleteComment(comment, success, fail) {
  api
    .delete(`/freeboard/comment/${comment.articleNo}/${comment.commentNo}`)
    .then(success)
    .catch(fail);
}
function deleteArticle(articleNo, success, fail) {
  api.delete(`/freeboard/delete/${articleNo}`).then(success).catch(fail);
}
function insertFreeBoard(article, success, fail) {
  console.log(article);
  api.post(`/freeboard/upload`, article).then(success).catch(fail);
}
function modifyArticle(article, success, fail) {
  api.put(`/freeboard/update/${article.articleNo}`, article).then(success).catch(fail);
}

export {
  articleList,
  articleDetail,
  newComment,
  newreComment,
  modifyCommnet,
  deleteComment,
  deleteArticle,
  insertFreeBoard,
  searchList,
  modifyArticle,
};
