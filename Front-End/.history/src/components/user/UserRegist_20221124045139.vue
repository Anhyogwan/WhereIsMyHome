<template>
  <div class="text-center" style="margin: 0 auto; width: 600px">
    <div style="margin: 20px 0px 50px 0px">
      <h2>회원가입</h2>
    </div>
    <div class="NoticeDiv" style="height: auto">
      <v-form ref="form" v-model="valid" lazy-validation>
        <v-text-field v-model="userId" :rules="idRules" label="아이디" required></v-text-field>
        <v-text-field
          type="password"
          v-model="userPassword"
          :rules="passwordRules"
          label="비밀번호"
          required
        ></v-text-field>
        <v-text-field
          type="password"
          v-model="authPassword"
          :rules="authPasswordRules"
          label="비밀번호 확인"
          required
        ></v-text-field>

        <v-text-field v-model="userName" :rules="nameRules" label="이름" required></v-text-field>

        <v-text-field v-model="email" :rules="emailRules" label="이메일" required></v-text-field>
        <v-btn @click="sendAuthCode()" :disabled="isShowTime">인증번호 발송 </v-btn>
        <span v-if="isShowTime">남은시간 : {{ TimeStr }}</span>
        <v-text-field
          v-model="code"
          :rules="authRules"
          :error-messages="sign"
          label="인증번호"
          required
        ></v-text-field>
        <v-btn :disabled="!valid" color="success" class="mr-4" @click="regist"> 회원가입 </v-btn>

        <v-btn color="error" class="mr-4" @click="cancle"> 돌아가기 </v-btn>
      </v-form>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import axios from "axios";
const userStore = "userStore";

export default {
  name: "userRegist",
  data() {
    return {
      isShowTime: false,
      Timer: null,
      TimeCounter: 180,
      TimeStr: "03:00",
      valid: true,
      userId: "",
      userPassword: "",
      authPassword: "",
      userName: "",
      email: "",
      code: "",
      sign: [],
      idRules: [
        (v) => !!v || "필수입력 항목입니다.",
        (v) => /^[a-zA-Z0-9]*$/.test(v) || "아이디는 영문+숫자만 입력 가능합니다.",
        (v) =>
          !(v && v.length >= 18 && v.length <= 6) ||
          "아이디는 6자 이상 18자 이하만 입력 가능합니다",
      ],
      passwordRules: [
        (v) => !!v || "필수입력 항목입니다.",
        (v) =>
          (v && v.length >= 8 && v.length <= 20) ||
          "비밀번호는 8자 이상 20자 이하만 입력 가능합니다",
        (v) => /[~!@#$%^&*()_+|<>?:{}]/.test(v) || "반드시 특수문자가 한 개 이상 포함되어야 합니다",
      ],
      authPasswordRules: [
        (v) => !!v || "필수입력 항목입니다.",
        (v) => v == this.userPassword || "비밀번호와 일치하지 않습니다",
      ],
      nameRules: [
        (v) => !!v || "필수입력 항목입니다.",
        (v) => !(v && v.length >= 10) || "이름은 10자 이상 입력할 수 없습니다.",
        (v) => !/[~!@#$%^&*()_+|<>?:{}]/.test(v) || "이름에는 특수문자를 사용할 수 없습니다.",
      ],
      emailRules: [
        (v) => !!v || "필수입력 항목입니다.",
        (v) => /.+@.+\..+/.test(v) || "올바르지 않은 이메일주소 입니다.",
      ],
      authRules: [(v) => !!v || "필수입력 항목입니다.", (v) => this.checkcode(v)],
    };
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
  },
  created() {},
  methods: {
    ...mapActions(userStore, ["sendMail", "insertUser"]),
    cancle() {
      this.$router.push("/");
    },
    sendAuthCode() {
      if (this.email != "" && /.+@.+\..+/.test(this.email)) {
        console.log(this.email);
        this.sendMail(this.email);
        this.isShowTime = true;
        this.timerStart();
      }
    },
    timerStop(Timer) {
      clearInterval(Timer);
      this.TimeCounter = 0;
    },
    timerStart() {
      this.TimeCounter = 180;
      this.Timer = setInterval(() => {
        this.TimeCounter--;
        this.TimeStr = this.prettyTime();
        if (this.TimeCounter <= 0) {
          this.timerStop(this.Timer);
          this.isShowTime = false;
        }
      }, 1000);
    },
    prettyTime() {
      let time = this.TimeCounter / 60;
      let minutes = parseInt(time);
      let secondes = Math.round((time - minutes) * 60);
      return minutes.toString().padStart(2, "0") + ":" + secondes.toString().padStart(2, "0");
    },
    checkcode(v) {
      axios
        .post(`http://localhost/user/checkmail`, { code: v })
        .then(() => {
          this.sign = [];
        })
        .catch(() => {
          this.sign = ["인증번호가 틀립니다"];
        });
    },
    regist() {
      if (
        this.userId == "" ||
        this.userPassword == "" ||
        this.userName == "" ||
        this.authPassword == "" ||
        this.email == "" ||
        this.code == ""
      ) {
        alert("모든 데이터를 입력해 주세요!");
      } else {
        this.insertUser({
          userId: this.userId,
          userName: this.userName,
          userPassword: this.userPassword,
          emailId: this.email.split("@")[0],
          emailDomain: this.email.split("@")[1],
          code: this.code,
        });
      }
    },
  },
};
</script>

<style scope>
.NoticeDiv {
  margin: 0 auto;
  background-color: white;
  width: 100%;
  /* height: 300px; */
  font-size: 25px;
  padding: 10px;
  margin-bottom: 40px;
}

.tdClass {
  width: 50px;
  text-align: center;
}
.tdSubject {
  width: 300px;
  text-align: left;
}
a {
  text-decoration: none;
}
a:link {
  color: black;
}
a:link {
  color: black;
}
a:visited {
  color: black;
}
a:hover {
  color: black;
}
a:active {
  color: black;
}
</style>
