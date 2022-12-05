<template>
  <div class="text-center" style="margin: 0 auto; width: 50%; min-width: 520px; max-width: 650px">
    <div style="margin: 20px 0px 50px 0px">
      <h2>마이페이지</h2>
    </div>
    <div style="display: flex">
      <div style="width: 200px; height: 200px">
        <img :src="userInfo.profileImg != null ? userInfo.profileImg : alter" width="100%" height="100%" />
      </div>
      <div class="NoticeDiv" style="height: auto">
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-text-field v-model="userInfo.userId" :rules="nameRules" label="아이디" required disabled></v-text-field>

          <v-text-field
            v-model="userInfo.userName"
            :rules="nameRules"
            label="이름"
            required
            :disabled="status == 'naver' || status == 'kakao'"
          ></v-text-field>

          <v-text-field
            v-model="email"
            :rules="emailRules"
            label="이메일"
            required
            :disabled="status == 'naver' || status == 'kakao'"
          ></v-text-field>

          <v-btn :disabled="!valid" color="success" class="mr-4" @click="modify">
            <b>수정</b>
          </v-btn>

          <v-btn color="error" class="mr-4" @click="cancle"> <b>취소</b> </v-btn>

          <v-btn style="margin-top: 2px; float: right" color="orange" class="mr-4" @click="deleteClick" dark>
            <b>회원탈퇴</b>
          </v-btn>
        </v-form>
      </div>
    </div>
    <div>
      <h3 style="text-align: left">나의 관심지역 목록</h3>
      <!-- <v-list class="interestBox" style="border: 1px solid; margin-bottom: 50px; width: 400px"> -->
      <div class="interestBox" style="border: 1px solid; margin-bottom: 50px; width: 400px">
        <v-list-item>
          <v-list-item-content style="width: 150px; text-decoration: none; text-align: left">
            <p v-if="userInterestArea == ''">관심지역이 없습니다.</p>
            <v-list-item-title
              style="font-size: 18px; margin-bottom: 10px"
              v-for="(area, idx) in userInterestArea"
              :key="idx"
              >{{ area.dongName }}<v-divider style="width: 380px"></v-divider
            ></v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </div>
      <!-- </v-list> -->
    </div>
  </div>
</template>

<script>
import { mapState, mapActions, mapMutations } from "vuex";
const userStore = "userStore";

export default {
  name: "userModify",
  data() {
    return {
      valid: true,
      nameRules: [(v) => !!v || "필수입력 항목입니다."],
      email: "",
      emailRules: [
        (v) => !!v || "필수입력 항목입니다.",
        (v) => /.+@.+\..+/.test(v) || "올바르지 않은 이메일주소 입니다.",
      ],
      alter: "",
    };
  },

  computed: {
    ...mapState(userStore, ["userInfo", "status", "userInterestArea"]),
  },
  created() {
    this.getUserList();
    this.email = this.userInfo.emailId + "@" + this.userInfo.emailDomain;
    console.log(this.status);
  },
  methods: {
    ...mapActions(userStore, ["getUserList", "modifyUser", "deleteUser", "userLogout"]),
    ...mapMutations(userStore, []),
    modify() {
      this.modifyUser(this.userInfo);
      this.$router.push("/");
    },
    cancle() {
      this.$router.push("/");
    },
    async deleteClick() {
      if (confirm(`정말 탈퇴 하시겠습니까??`)) {
        let userid = this.userInfo.userId;
        this.userLogout(this.userInfo.userId);
        await this.deleteUser(userid);

        await this.$router.push("/");
      } else {
        alert("취소하였습니다");
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

.interestBox {
  background-color: white;

  border: 2px rgba(0, 0, 0, 0.2) solid;
  border-radius: 10px;

  /* 그림자 부분 */
  box-shadow: 5px 5px 5px gray;

  background-color: beige;
}
</style>
