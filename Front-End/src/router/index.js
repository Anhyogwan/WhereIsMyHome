import Vue from "vue";
import VueRouter from "vue-router";
import AppMain from "@/views/AppMain";
import store from "@/store";
import axios from "axios";

// import UserPage from '@/components/user/UserPage'

Vue.use(VueRouter);

const onlyAuthUser = async (to, from, next) => {
  const checkUserInfo = store.getters["userStore/checkUserInfo"];
  const checkToken = store.getters["userStore/checkToken"];
  let token = sessionStorage.getItem("access-token");
  console.log("�α��� ó�� ��", checkUserInfo, token);

  if (checkUserInfo != null && token) {
    console.log("��ū ��ȿ�� üũ�Ϸ� ����!!!!");
    await store.dispatch("userStore/getUserInfo", token);
  }
  if (!checkToken || checkUserInfo === null) {
    alert("로그인이 필요한 페이지입니다");
    // next({ name: "login" });
    router.push({ name: "userlogin" });
  } else {
    console.log("�α��� �ߴ�!!!!!!!!!!!!!.");
    next();
  }
};

const routes = [
  {
    path: "/",
    name: "main",
    component: AppMain,
  },
  {
    path: "/map",
    name: "map",
    component: () => import("@/views/AppMap"),
    redirect: "/map/view",
    children: [
      {
        path: "view",
        name: "mapview",
        component: () => import("@/components/map/MapView"),
      },
    ],
  },

  {
    path: "/notice",
    name: "notice",
    component: () => import("@/views/AppNotice"),
    redirect: "/notice/list",
    children: [
      {
        path: "list",
        name: "noticelist",
        component: () => import("@/components/notice/NoticeList"),
      },
      {
        path: "write",
        name: "noticewrite",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/notice/NoticeWrite"),
      },
      {
        path: "view/:articleNo",
        name: "noticeview",
        component: () => import("@/components/notice/NoticeView"),
      },
      {
        path: "modify/:articleNo",
        name: "noticemodify",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/notice/NoticeModify"),
      },
      {
        path: "delete/:articleno",
        name: "noticedelete",
        // component: () => import("@/components/notice/NoticeDelete"),
      },
    ],
  },
  {
    path: "/freeboard",
    name: "freeboard",
    component: () => import("@/views/AppFreeBoard"),
    redirect: "/freeboard/list",
    children: [
      {
        path: "list",
        name: "freeboardlist",
        component: () => import("@/components/freeboard/FreeBoardList"),
      },
      {
        path: "write",
        name: "freeboardwrite",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/freeboard/FreeBoardWrite"),
      },
      {
        path: "view/:articleNo",
        name: "freeboardview",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/freeboard/FreeBoardView"),
      },
      {
        path: "update/:articleNo",
        name: "freeboardmodify",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/freeboard/FreeBoardModify"),
      },
      {
        path: "delete/:articleno",
        name: "freeboarddelete",
        // component: () => import("@/components/freeboard/FreeBoardDelete"),
      },
    ],
  },
  {
    path: "/user",
    name: "user",
    component: () => import("@/views/AppUser"),
    children: [
      {
        path: "list",
        name: "userlist",
        component: () => import("@/components/user/UserList"),
      },
      {
        path: "modify",
        name: "usermodify",
        component: () => import("@/components/user/UserModify"),
      },
      {
        path: "regist",
        name: "userregist",
        component: () => import('@/components/user/UserRegist'),
      },
      {
        path: "findpassword",
        name: "findpassword",
        component: () => import('@/components/user/FindPassword')
      },
      {
        path: "updatepassword",
        name: "updatepassword",
        component: () => import('@/components/user/UpdatePassword')
      },
      {
        path: "login",
        name: "userlogin",
        component: () => import("@/components/user/UserLogin"),
        children: [
          {
            path: "oauth2",
            name: "oauth2",
            children: [
              {
                path: "naver",
                name: "naver",
                beforeEnter() {
                  var callbackFuc = async () => {
                    console.dir(VueRouter);
                    let params = new URLSearchParams(location.search);
                    //���̹� �α��� ���� �ڵ�
                    await axios.get(`http://192.168.120.62/user/naverlogin/${params.get("code")}/${params.get("state")}`)
                      .then(data => {
                        if (data.data.message == "SUCCESS") {
                          console.log(data.userInfo);
                          console.log(data);
                          data.data.userInfo.userId = data.data.userInfo.emailId;
                          store.commit('userStore/SET_IS_LOGIN', true);
                          store.commit('userStore/SET_USER_INFO', data.data.userInfo)
                          store.commit("userStore/SET_IS_LOGIN_ERROR", false);
                          store.commit("userStore/SET_IS_VALID_TOKEN", true);
                          store.commit('userStore/SET_STATUS', data.data.status);
                          sessionStorage.setItem("access-token", data.data.userInfo.accessToken);
                          sessionStorage.setItem("refresh-token", data.data.userInfo.refreshToken);
                          location.href = "http://localhost:8080/"
                        } else {
                          store.commit("userStore/SET_IS_LOGIN", false);
                          store.commit("userStore/SET_IS_LOGIN_ERROR", true);
                          store.commit("userStore/SET_IS_VALID_TOKEN", false);
                        }
                      }).catch(data => {
                        console.log(data);
                        store.commit("userStore/SET_IS_LOGIN", false);
                        store.commit("userStore/SET_IS_LOGIN_ERROR", true);
                        store.commit("userStore/SET_IS_VALID_TOKEN", false);
                      })
                    //this.$router.push("/");
                  }
                  callbackFuc();
                },
                component: () => import("@/components/user/Oauth2/NaverLogin")
              },
              {
                path: "google",
                name: "google",

                component: () => import("@/components/user/Oauth2/GoogleLogin")
              },
              {
                path: "kakao",
                name: "kakao",
                beforeEnter() {
                  var callbackFuc = async () => {
                    console.dir(VueRouter);
                    let params = new URLSearchParams(location.search);
                    //īī�� �α��� ���� �ڵ�
                    await axios.get(`http://192.168.120.62/user/kakaologin/${params.get("code")}`)
                      .then(data => {
                        if (data.data.message == "SUCCESS") {
                          store.commit('userStore/SET_IS_LOGIN', true);
                          store.commit('userStore/SET_USER_INFO', data.data.userInfo)
                          location.href = "http://localhost:8080/"
                          store.commit("userStore/SET_IS_LOGIN_ERROR", false);
                          store.commit("userStore/SET_IS_VALID_TOKEN", true);
                          store.commit('userStore/SET_STATUS', data.data.status);
                          sessionStorage.setItem("access-token", data.data.userInfo.accessToken);
                          sessionStorage.setItem("refresh-token", data.data.userInfo.refreshToken);
                          location.href = "http://localhost:8080/"
                        } else {
                          store.commit("userStore/SET_IS_LOGIN", false);
                          store.commit("userStore/SET_IS_LOGIN_ERROR", true);
                          store.commit("userStore/SETIS_VALID_TOKEN", false);
                        }
                      }).catch(data => {
                        console.log(data);
                        store.commit("userStore/SET_IS_LOGIN", false);
                        store.commit("userStore/SET_IS_LOGIN_ERROR", true);
                        store.commit("userStore/SET_IS_VALID_TOKEN", false);
                      })
                    //this.$router.push("/");
                  }
                  callbackFuc();
                },
                component: () => import("@/components/user/Oauth2/KakaoLogin")
              }
            ]
          }
        ]
      },
    ],
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
_