import {
  getApts,
  searchCode,
  getAptDeals,
  getAptDealByDate,
  instInterestArea,
  getInterestArea,
  delInterestArea,
  getRecentlyApt,
  getAptInfo,
} from "@/components/api/map";
// import mapView from "@/components/map/MapView";
// import router from "@/router";

const mapStore = {
  namespaced: true,
  state: {
    sidos: [],
    guguns: [],
    dongs: [],
    aptList: [],
    aptDeals: [],
    interestAreaList: [],
    recentlyAptList: [],
    year: "",
    month: "",
    sido: "",
    gugun: "",
    dong: "",
    near: [],
    aptInfo: "",
    aptlabels: [],
    aptdata:[]
  },
  getters: {},
  mutations: {
    SET_SIDO_CODE_LIST(state, data) {
      state.dongs = ["구군을 선택해주세요"];
      state.sidos = data;
    },
    SET_GUGUN_CODE_LIST(state, data) {
      state.guguns = ["시도를 선택해주세요"];
      state.dongs = ["구군을 선택해주세요"];
      state.guguns = data;
    },
    SET_DONG_CODE_LIST(state, data) {
      state.dongs = data;
    },
    SET_APT_LIST(state, data) {
      state.aptList = data;
    },
    SET_APT_DEALS(state, data) {
      state.aptDeals = data;
    },
    SET_INTEREST_AREA_LIST(state, data) {
      state.interestAreaList = data;
    },
    SET_RECENTLY_APT_LIST(state, data) {
      state.recentlyAptList = data;
    },
    SET_APT_INFO(state, data) {
      state.aptInfo = data;
    },
  },
  actions: {
    getCodeStore({ commit }, input) {
      searchCode(input).then(({ data }) => {
        if (input.type == "sido") {
          commit("SET_SIDO_CODE_LIST", data);
        } else if (input.type == "gugun") {
          commit("SET_GUGUN_CODE_LIST", data);
        } else if (input.type == "dong") {
          commit("SET_DONG_CODE_LIST", data);
        }
      });
    },

    getAptList({ commit }, code) {
      getApts(code).then(({ data }) => {
        // console.log(data);
        commit("SET_APT_LIST", data);
      });
    },

    getAptDealsByAptCode({ commit }, aptCode) {
      getAptDeals(aptCode).then(({ data }) => {
        // console.log(data);
        commit("SET_APT_DEALS", data);
      });
    },

    getFilterApt({ commit }, map) {
      getAptDealByDate(map).then(({ data }) => {
        commit("SET_APT_DEALS", data);
      });
    },

    async insertInterestArea({ commit }, data) {
      if (data.dongCode != "") {
        await instInterestArea(data);
        console.log(commit);
      }
    },

    async getInstArea({ commit }, user_id) {
      await getInterestArea(user_id).then(({ data }) => {
        commit("SET_INTEREST_AREA_LIST", data);
      });
    },

    deleteInstArea({ commit }, data) {
      delInterestArea(data);
      console.log(commit);
    },

    async getRecentlyAptList({ commit }, qty) {
      await getRecentlyApt(qty).then(({ data }) => {
        commit("SET_RECENTLY_APT_LIST", data);
      });
    },

    getAptInfoByAptCode({ commit }, aptCode) {
      getAptInfo(aptCode).then(({ data }) => {
        commit("SET_APT_INFO", data);
      });
    },
  },
};

export default mapStore;
