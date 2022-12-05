<template>
  <div>
    <div style="margin: 50px 0px 50px 0px; text-align: center; font-size: 40px">비밀번호 재설정</div>
    <div style="margin: 0 auto; display: flex; justify-content: center">
      <div style="margin: 0px 0px 40px 0px">
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-container fluid>
            <v-text-field dense label="아이디" v-model="userId" :rules="idRules" required></v-text-field>
            <v-text-field
              v-model="userEmail"
              name="input-10-1"
              :rules="emailRules"
              label="등록된 이메일"
              required
            ></v-text-field>
            <v-text-field
              v-model="code"
              :rules="authRules"
              :error-messages="sign"
              label="인증번호"
              required
            ></v-text-field>
            <span v-if="isShowTime">남은시간 : {{ TimeStr }}</span>
          </v-container>
        </v-form>
      </div>
      <div>
        <v-btn
          @click="sendAuthmail"
          depressed
          style="color: white; margin-top: 140px; background-color: #03c75a; height: 40px"
        >
          인증번호 발송
        </v-btn>
      </div>
    </div>
    <div style="margin: 0 auto; display: flex; justify-content: center">
      <div style="margin: -40px 160px 50px 0px">
        <v-btn style="margin-right: 10px" @click="checkcode">재설정</v-btn>
        <v-btn style="background-color: red" dark @click="cancle">돌아가기</v-btn>
      </div>
    </div>
  </div>
</template>
<script>
import { mapState, mapActions, mapMutations } from "vuex";
import axios from "axios";
const userStore = "userStore";
export default {
  data() {
    return {
      userId: "",
      userEmail: "",
      isShowTime: false,
      Timer: null,
      TimeCounter: 180,
      TimeStr: "03:00",
      code: "",
      sign: [],
      idRules: [
        (v) => !!v || "필수입력 항목입니다.",
        (v) => /^[a-zA-Z0-9]*$/.test(v) || "아이디는 영문+숫자만 입력 가능합니다.",
        (v) => !(v && v.length >= 18 && v.length <= 6) || "아이디는 6자 이상 18자 이하만 입력 가능합니다",
      ],
      emailRules: [
        (v) => !!v || "필수입력 항목입니다.",
        (v) => /.+@.+\..+/.test(v) || "올바르지 않은 이메일주소 입니다.",
      ],
      authRules: [(v) => !!v || "필수입력 항목입니다."],
    };
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
  },
  methods: {
    ...mapMutations(userStore, ["SET_USER_INFO"]),
    ...mapActions(userStore, ["sendFindMail", "delCode"]),
    sendAuthmail() {
      if (this.userId == "" || this.userEmail == "") {
        alert("아이디와 이메일을 모두 입력해 주세요");
      } else {
        // 아이디와 이메일이 매칭 되는 결과값이 있다면 인증메일 발송 후 200 리턴, 아니면 401 리턴 하고 alert창 띄우기
        if (
          this.sendFindMail({
            userId: this.userId,
            userEmail: this.userEmail,
            emailId: this.userEmail.split("@")[0],
            emailDomain: this.userEmail.split("@")[1],
          })
        ) {
          this.timerStart();
          this.isShowTime = true;
        }
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
    checkcode() {
      if (this.code != "") {
        axios
          .post(`http://192.168.120.62/user/checkmail`, { code: this.code })
          .then(() => {
            this.sign = [];
            this.$store.state.userInfo = this.userId;
            console.log(this.$store.state.userInfo);
            this.delCode(this.code);
            this.$router.push("updatepassword");
          })
          .catch(() => {
            this.sign = ["인증번호가 틀립니다"];
          });
      }
    },
    cancle() {
      this.$router.push("/user/login");
    },
  },
};
</script>
