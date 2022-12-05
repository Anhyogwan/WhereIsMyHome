import jwtDecode from "jwt-decode";
import router from "@/router";
import {
  userList,
  delUser,
  updateUser,
  login,
  findById,
  tokenRegeneration,
  logout,
  sendEmail,
  insertNewUser,
  sendFindEmail,
  updatePass,
  deleteCode,
} from "@/components/api/user";

const userStore = {
  namespaced: true,
  state: {
    isLogin: false,
    isLoginError: false,
    userInfo: null,
    isVaildToken: false,
    isCookieCheck: false,
    status: null,
    users: [],
    userInterestArea: [],
  },
  getters: {
    checkUserInfo: function (state) {
      return state.userInfo;
    },
    checkToken: function (state) {
      return state.isVaildToken;
    },
    checkStatus: function (state) {
      return state.status;
    },
  },
  mutations: {
    SET_USER_LIST(state, users) {
      state.users = users;
    },

    SET_IS_LOGIN: (state, isLogin) => {
      state.isLogin = isLogin;
    },
    SET_IS_LOGIN_ERROR: (state, isLoginError) => {
      state.isLoginError = isLoginError;
    },
    SET_IS_VALID_TOKEN: (state, isVaildToken) => {
      state.isVaildToken = isVaildToken;
    },
    SET_USER_INFO: (state, userInfo) => {
      //state.isLogin = true;
      state.userInfo = userInfo;
    },
    SET_IS_COOKIE_CHECK: (state, isCookieCheck) => {
      state.isCookieCheck = isCookieCheck;
    },
    SET_STATUS: (state, status) => {
      state.status = status;
    },
    SET_USER_INTEREST_AREA: (state, instArea) => {
      state.userInterestArea = instArea;
    },
  },
  actions: {
    getUserList: ({ commit }) => {
      userList(
        ({ data }) => {
          commit("SET_USER_LIST", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    deleteUser: ({ commit }, userId) => {
      delUser(userId);
      console.log(commit);
    },
    modifyUser: ({ commit }, user) => {
      updateUser(user);
      console.log(commit);
    },
    async userConfirm({ commit }, user) {
      await login(
        user,
        ({ data }) => {
          if (data.message === "SUCCESS") {
            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];
            // console.log("login success token created!!!! >> ", accessToken, refreshToken);
            commit("SET_IS_LOGIN", true);
            commit("SET_IS_LOGIN_ERROR", false);
            commit("SET_IS_VALID_TOKEN", true);
            commit("SET_IS_COOKIE_CHECK", user.checkbox);
            commit("SET_STATUS", data.status);
            sessionStorage.setItem("access-token", accessToken);
            sessionStorage.setItem("refresh-token", refreshToken);
          } else {
            commit("SET_IS_LOGIN", false);
            commit("SET_IS_LOGIN_ERROR", true);
            commit("SET_IS_VALID_TOKEN", false);
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    async getUserInfo({ commit, dispatch, state }, token) {
      let decodeToken = "";
      if (state.status == "wimh") {
        decodeToken = jwtDecode(token).userid;
      } else {
        decodeToken = state.userInfo.userId;
      }
      // console.log("2. getUserInfo() decodeToken :: ", decodeToken);
      await findById(
        decodeToken,
        state.status,
        ({ data }) => {
          console.log(data);
          if (data.message === "SUCCESS") {
            if (state.status == "wimh") {
              commit("SET_USER_INFO", data.userInfo);
            }
            console.log("3. getUserInfo data >> ", data);
          } else {
            console.log("���� ���� ����!!!!");
          }
        },
        async (error) => {
          console.log("getUserInfo() error code [��ū ����Ǿ� ��� �Ұ���.] ::: ", error.response.status);
          commit("SET_IS_VALID_TOKEN", false);
          await dispatch("tokenRegeneration");
        }
      );
    },
    async tokenRegeneration({ commit, state }) {
      console.log("��ū ��߱� >> ���� ��ū ���� : {}", sessionStorage.getItem("access-token"));
      await tokenRegeneration(
        JSON.stringify(state.userInfo),
        state.status,
        ({ data }) => {
          if (data.message === "SUCCESS") {
            let accessToken = data["access-token"];
            console.log("��߱� �Ϸ� >> ���ο� ��ū : {}", accessToken);
            sessionStorage.setItem("access-token", accessToken);
            commit("SET_IS_VALID_TOKEN", true);
          }
        },
        async (error) => {
          // HttpStatus.UNAUTHORIZE(401) : RefreshToken �Ⱓ ���� >> �ٽ� �α���!!!!
          if (error.response.status === 401) {
            console.log("���� ����");
            // �ٽ� �α��� �� DB�� ����� RefreshToken ����.
            await logout(
              state.userInfo.userid,
              state.status,
              ({ data }) => {
                if (data.message === "SUCCESS") {
                  console.log("�������� ��ū ���� ����");
                } else {
                  console.log("�������� ��ū ���� ����");
                }
                alert("RefreshToken �Ⱓ ����!!! �ٽ� �α����� �ּ���.");
                commit("SET_IS_LOGIN", false);
                commit("SET_USER_INFO", null);
                commit("SET_IS_VALID_TOKEN", false);
                commit("SET_STATUS", null);
                sessionStorage.removeItem("access-token");
                sessionStorage.removeItem("refresh-token");
                router.push({ name: "login" });
              },
              (error) => {
                console.log(error);
                commit("SET_IS_LOGIN", false);
                commit("SET_USER_INFO", null);
                commit("SET_STATUS", null);
              }
            );
          }
        }
      );
    },
    async userLogout({ commit, state }, userid) {
      await logout(
        userid,
        state.status,
        ({ data }) => {
          if (data.message === "SUCCESS") {
            console.log("�α׾ƿ���");
            commit("SET_IS_LOGIN", false);
            commit("SET_USER_INFO", null);
            commit("SET_IS_VALID_TOKEN", false);
            commit("SET_STATUS", null);
            sessionStorage.removeItem("access-token");
            sessionStorage.removeItem("refresh-token");
          } else {
            console.log("���� ���� ����!!!!");
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    async sendMail({ state }, email) {
      state.status;
      console.log("이다음");
      console.dir(email);
      await sendEmail(email);
    },
    async insertUser({ state }, user) {
      state.status;
      console.log(user);
      await insertNewUser(user, ({ data }) => {
        console.log(data);
        alert("가입되었습니다");
        console.log(user.code);
        deleteCode(user.code);
        router.push({ name: "main" });
      });
    },
    async sendFindMail({ state }, user) {
      await sendFindEmail(user, ({ data }) => {
        state.status;
        console.log(data);
        //commit("SET_USER_INFO", user.userId);
        //router.push({ name: "updatepassword" });
        return true;
      }),
        (error) => {
          console.log(error);
          alert("일치하는 아이디와 이메일이 존재하지 않습니다");
        };
    },
    async updatePwd({ commit }, user) {
      await updatePass(user, ({ data }) => {
        commit("SET_USER_INFO", null);

        console.log(data);
        return true;
      });
    },
    async delCode({ state }, code) {
      state.status;
      console.log(code);
      await deleteCode(code);
    },

    setUserInstArea({ commit }, userInstList) {
      commit("SET_USER_INTEREST_AREA", userInstList);
    },
  },
};

export default userStore;
