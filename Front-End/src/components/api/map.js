import { apiInstance } from "./index.js";

const api = apiInstance();

// 시군구 코드 검색
function searchCode(data, success, fail) {
  return api.get(`/apt/search/${data.type}/${data.code}`).then(success).catch(fail);
}

// 아파트 리스트 검색
function getApts(code, success, fail) {
  return api.get(`/apt/houseInfoList/${code}`).then(success).catch(fail);
}

// 아파트 거래정보 검색
function getAptDeals(aptCode, success, fail) {
  return api.get(`/apt/houseDealList/${aptCode}`).then(success).catch(fail);
}

function getAptDealByDate(map, success, fail) {
  return api
    .get(`/apt/houseDealList?aptCode=${map.aptCode + ""}&start=${map.start}&end=${map.end}`)
    .then(success)
    .catch(fail);
}

// 관심지역 삽입
function instInterestArea(dto, success, fail) {
  api
    .post(`/apt/interestarea?dongCode=${dto.dongCode + ""}&user_id=${dto.user_id}`)
    .then(success)
    .catch(fail);
}

// 관심지역 검색
function getInterestArea(user_id, success, fail) {
  return api.get(`/apt/interestarea/${user_id}`).then(success).catch(fail);
}

// 관심지역 삭제
function delInterestArea(dto, success, fail) {
  api
    .delete(`/apt/interestarea?dongCode=${dto.dongCode + ""}&user_id=${dto.user_id}`)
    .then(success)
    .catch(fail);
}

// 최근 N개 매물 검색
function getRecentlyApt(qty, success, fail) {
  return api.get(`/apt/recently/${qty}`).then(success).catch(fail);
}

// 아파트 정보 검색
function getAptInfo(aptCode, success, fail) {
  return api.get(`/apt/houseinfo/${aptCode}`).then(success).catch(fail);
}

export {
  searchCode,
  getApts,
  getAptDeals,
  getAptDealByDate,
  instInterestArea,
  getInterestArea,
  delInterestArea,
  getRecentlyApt,
  getAptInfo,
};
