<template>
  <div>
    <div style="margin: 50px 0px 50px 0px; text-align: center; font-size: 40px">비밀번호 재설정</div>
    <div style="margin: 0 auto; display: flex; justify-content: center">
      <div style="margin: 0px 0px 40px 0px">
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-container fluid>
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
          </v-container>
        </v-form>
        <div style="margin-left: 30px" @click="updatepwd">
          <v-btn :disabled="!valid">비밀번호 재설정</v-btn>
        </div>
      </div>
      <div></div>
    </div>
  </div>
</template>
<script>
import { mapState, mapMutations, mapActions } from "vuex";
const userStore = "userStore";
export default {
  data() {
    return {
      userPassword: "",
      authPassword: "",
      passwordRules: [
        (v) => !!v || "필수입력 항목입니다.",
        (v) => (v && v.length >= 8 && v.length <= 20) || "반드시 8 ~ 20자 이내 여야 합니다",
        (v) => /[~!@#$%^&*()_+|<script>?:{}]/.test(v) || "반드시 특수문자가 한 개 이상 포함되어야 합니다",
      ],
      authPasswordRules: [
        (v) => !!v || "필수입력 항목입니다.",
        (v) => v == this.userPassword || "비밀번호와 일치하지 않습니다",
      ],
      valid: false,
    };
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
    ...mapMutations(userStore, ["SET_USER_INFO"]),
  },
  methods: {
    ...mapActions(userStore, ["updatePwd"]),
    updatepwd() {
      if (this.userPassword != "") {
        if (
          this.updatePwd({
            userId: this.$store.state.userInfo,
            userPassword: this.userPassword,
          })
        ) {
          alert("비밀번호가 재설정 되었습니다");
          this.$router.push({ name: "main" });
        }
      } else {
        alert("비밀번호를 입력해주세요.");
      }
    },
  },
};
</script>
