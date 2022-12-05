<template>
  <div>
    <div style="margin: 50px 0px 50px 0px; text-align: center; font-size: 40px">이 집만큼은 안된다 이놈아!!</div>
    <div style="margin: 0 auto; display: flex; justify-content: center">
      <div style="margin: 0px 0px 40px 0px">
        <v-form>
          <v-container fluid>
            <v-text-field
              dense
              label="아이디"
              v-model="user.userId"
              @keyup.enter="confirm"
              :rules="rules"
            ></v-text-field>
            <v-text-field
              v-model="user.userPassword"
              :type="'password'"
              name="input-10-1"
              label="비밀번호"
              @keyup.enter="confirm"
              @click:append="show1 = !show1"
              :rules="rules"
            ></v-text-field>
            <v-checkbox style="margin-top: -5px" v-model="user.checkbox" label="아이디저장"></v-checkbox>
          </v-container>
        </v-form>
        <div style="margin-top: -15px; padding-left: 10px">
          <a @click="loginClick" style="color: black"> 회원가입 </a> &nbsp; | &nbsp;
          <a @click="findPassword" style="color: black"> 비밀번호 찾기 </a>
        </div>
        <div style="display: flex; margin-top: 5px">
          <a @click="naverLoginAuth()" style="margin: 1px 10px 0px 0px">
            <img src="@/assets/NaverLogin/naverLogin.png" style="width: 135px" />
          </a>
          <div
            id="g_id_onload"
            data-client_id="412823083793-evivmdrm0m5dp2koukthrm0eel5hmgb3.apps.googleusercontent.com"
            :data-callback="handleCredentialResponse"
          ></div>
          <div class="g_id_signin" data-type="standard"></div>
          <!-- <a @click="googleLoginAuth()" style="margin-right: 10px">
            <img src="@/assets/GoogleLogin/google_normal.png" style="width: 150px" />
          </a> -->
          <a @click="kakaoLoginAuth()">
            <img src="@/assets/KakaoLogin/kakao_login.png" style="width: 150px" />
          </a>
        </div>
      </div>
      <div>
        <v-btn
          @click="confirm"
          depressed
          style="color: white; margin-top: 10px; background-color: #03c75a; height: 100px"
          :loading="loginBtn"
          :disabled="loginBtn"
        >
          로그인
        </v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
const userStore = "userStore";
const mapStore = "mapStore";

export default {
  data() {
    return {
      show1: false,
      user: {
        userId: this.$cookies.get("saveId"),
        userPassword: "",
        checkbox: this.$cookies.get("checkbox"),
      },
      naverLogin: "https://nid.naver.com/oauth2.0/authorize?response_type=code",
      rules: [(v) => !!v || "필수입력 항목입니다."],
      //
      instAreaList: [],
      loader: null,
      loginBtn: false,
    };
  },
  watch: {
    loader() {
      const l = this.loader;
      this[l] = !this[l];

      setTimeout(() => (this[l] = false), 3000);

      this.loader = null;
    },
  },
  computed: {
    ...mapState(userStore, ["isLogin", "isLoginError", "userInfo", "isCookieCheck"]),
    ...mapState(mapStore, ["interestAreaList"]),
  },
  methods: {
    ...mapActions(userStore, ["userConfirm", "getUserInfo", "setUserInstArea"]),
    ...mapActions(mapStore, ["getInstArea"]),
    async confirm() {
      await this.userConfirm(this.user);
      let token = sessionStorage.getItem("access-token");
      console.log(token);
      if (this.isLogin) {
        this.loader = "loginBtn";
        await this.getUserInfo(token);
        if (this.user.checkbox) {
          this.$cookies.set("saveId", this.user.userId);
          this.$cookies.set("checkbox", true);
        } else {
          this.$cookies.remove("saveId");
          this.$cookies.remove("checkbox");
        }

        await this.getInstArea(this.userInfo.userId);
        this.instAreaList = [];
        for (let i = 0; i < this.interestAreaList.length; i++) {
          let url =
            "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=" +
            this.interestAreaList[i].dongCode;
          let name = "";
          await fetch(url)
            .then((res) => res.json())
            .then((data) => {
              name = data.regcodes[0].name;
            });

          this.instAreaList = [...this.instAreaList, { dongCode: this.interestAreaList[i].dongCode, dongName: name }];
        }
        this.setUserInstArea(this.instAreaList);

        this.$router.push({ name: "main" });
      } else {
        alert("일치하는 정보가 없습니다 ?");
      }
    },
    async naverLoginAuth() {
      window.location.replace(this.naverLogin);
    },
    async kakaoLoginAuth() {
      const url =
        "https://kauth.kakao.com/oauth/authorize?client_id=" +
        "" +
        "&redirect_uri=" +
        "http://localhost:8080/user/login/oauth2/kakao" +
        "&response_type=code&" +
        "scope=profile_nickname account_email profile_image";

      window.location.replace(url);
    },
    loginClick() {
      this.$router.push({ name: "userregist" });
    },
    findPassword() {
      this.$router.push({ name: "findpassword" });
    },
  },
  created() {
    this.naverLogin += "&client_id=" + "";
    this.naverLogin += "&redirect_uri=" + "http://localhost:8080/user/login/oauth2/naver";
    this.naverLogin += "&state=" + "123";
  },
};
</script>

<style>
.custom-loader {
  animation: loader 1s infinite;
  display: flex;
}
@-moz-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@-webkit-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@-o-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
