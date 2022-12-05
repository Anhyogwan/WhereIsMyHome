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
    <div>
      <div style="margin: 0 auto; text-align: left"><v-icon> mdi-folder</v-icon>첨부파일</div>
      <v-file-input v-model="files" small-chips multiple label="첨부파일"></v-file-input>
    </div>

    <v-btn @click="insertNotice" depressed color="#03c75a" style="color: white; margin-bottom: 10px"> 글쓰기 </v-btn>
    <v-btn @click="cancle" depressed color="red" style="color: white; margin: 0px 0px 10px 10px"> 취소 </v-btn>
  </div>
</template>

<script>
import { mapState, mapActions, mapMutations } from "vuex";
const noticeStore = "noticeStore";

export default {
  name: "NoticeWrite",

  data() {
    return {
      rules: [(v) => v.length <= 500 || "500자 이내로 입력해주세요."],
      value: "",
      subject: "",
      content: "",
      userId: "admin",
      files: null,
    };
  },
  computed: {
    ...mapState(noticeStore, []),
  },
  methods: {
    ...mapActions(noticeStore, ["instNotice"]),
    ...mapMutations(noticeStore, []),
    async insertNotice() {
      console.log(this.files);
      if (this.title != "" && this.content != "") {
        let article = { subject: this.subject, content: this.content, userId: this.userId };
        let files = this.files;
        let uplaod = { article, files };

        // console.log(files);
        await this.instNotice(uplaod);
      } else {
        alert("제목과 내용을 입력하세요");
      }
    },
    // insertNotice() {
    //   if (this.title != "" && this.content != "") {
    //     let article = { subject: this.subject, content: this.content, userId: this.userId };
    //     this.instNotice(article);
    //     this.$router.push("list");
    //   } else {
    //     alert("제목과 내용을 입력하세요");
    //   }
    // },
    cancle() {
      this.$router.push("list");
    },
  },
};
</script>

<style></style>
