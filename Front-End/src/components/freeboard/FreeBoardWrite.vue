<template>
  <div style="min-width: 650px">
    <v-container fluid>
      <div class="title">
        <v-textarea v-model="subject" name="input-7-1" filled label="제목" value="" height="10px"></v-textarea>
      </div>
      <div class="content">
        <v-textarea
          v-model="content"
          counter
          filled
          label="내용"
          :rules="rules"
          :value="value"
          height="400px"
        ></v-textarea>
      </div>
    </v-container>

    <v-btn @click="insertFreeBoard" depressed color="#03c75a" style="color: white; margin-bottom: 10px"> 글쓰기 </v-btn>
    <v-btn @click="cancle" depressed color="red" style="color: white; margin: 0px 0px 10px 10px"> 취소 </v-btn>
  </div>
</template>

<script>
import { mapState, mapActions, mapMutations } from "vuex";
const freeBoardStore = "freeBoardStore";
const userStore = "userStore";

export default {
  name: "FreeBoardWrite",

  data() {
    return {
      rules: [(v) => v.length <= 500 || "500자 이내로 입력해주세요."],
      value: "",
      subject: "",
      content: "",
      userId: "",
    };
  },
  computed: {
    ...mapState(freeBoardStore, []),
    ...mapState(userStore, ["userInfo"]),
  },
  methods: {
    ...mapActions(freeBoardStore, ["instFreeBoard"]),
    ...mapMutations(freeBoardStore, []),
    insertFreeBoard() {
      if (this.title != "" && this.content != "") {
        let article = {
          subject: this.subject,
          content: this.content,
          userId: this.userInfo.userId,
          userName: this.userInfo.userName,
        };
        this.instFreeBoard(article);
        this.$router.push("list");
      } else {
        alert("제목과 내용을 입력하세요");
      }
    },
    cancle() {
      this.$router.push("list");
    },
  },
};
</script>

<style></style>
