import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";
// module import
import freeBoardStore from "@/store/modules/freeBoardStore";
import mapStore from "@/store/modules/mapStore";
import userStore from "@/store/modules/userStore";
import noticeStore from "@/store/modules/noticeStore";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    freeBoardStore,
    mapStore,
    userStore,
    noticeStore,
  },
  plugins: [
    createPersistedState({
      // 브라우저 종료시 제거하기 위해 localStorage가 아닌 sessionStorage로 변경. (default: localStorage)
      storage: sessionStorage,
    }),
  ],
});
