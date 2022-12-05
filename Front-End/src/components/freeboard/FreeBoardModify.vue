<template>
  <div style="min-width: 650px">
    <v-container fluid>
      <div class="title">
        <v-textarea
          v-model="subject"
          name="input-7-1"
          filled
          label="제목"
          value=""
          height="10px"
        ></v-textarea>
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

    <v-btn
      @click="updateFreeBoard"
      depressed
      color="#03c75a"
      style="color: white; margin-bottom: 10px"
    >
      수정
    </v-btn>
    <v-btn @click="cancle" depressed color="red" style="color: white; margin: 0px 0px 10px 10px">
      취소
    </v-btn>
  </div>
</template>

<script>
import { mapState, mapActions, mapMutations, mapGetters } from "vuex";
const freeBoardStore = "freeBoardStore";
// import http from "@/components/api/http";

export default {
  name: "FreeBoardModify",

  data() {
    return {
      rules: [(v) => v.length <= 500 || "500자 이내로 입력해주세요."],
      value: "",
      // articleNo: "",
      subject: "",
      content: "",
      userId: "ssafy",
    };
  },
  computed: {
    ...mapState(freeBoardStore, ["article"]),
    ...mapGetters(freeBoardStore, ["article", "articleNo"]),
  },
  methods: {
    ...mapActions(freeBoardStore, ["getArticleDetail", "updateArticle"]),
    ...mapMutations(freeBoardStore, ["SET_ARTICLE_DETAIL"]),
    updateFreeBoard() {
      if (this.title != "" && this.content != "") {
        let article = {
          articleNo: this.$route.params.articleNo,
          subject: this.subject,
          content: this.content,
          userId: this.userId,
        };
        this.updateArticle(article);
        this.$router.push("/freeboard");

        // http
        //   .put(`/freeboard/update/${this.articleNo}`, {
        //     articleNo: this.articleNo,
        //     subject: this.subject,
        //     content: this.content,
        //     userId: this.userId,
        //   })
        //   .then(() => {
        //     this.$router.push("/freeboard");
        //   });
      } else {
        alert("제목과 내용을 입력하세요");
      }
    },
    cancle() {
      this.$router.push("/freeboard");
    },
  },
  created() {
    this.getArticleDetail(this.$route.params.articleNo);
    this.subject = this.article.subject;
    this.content = this.article.content;
    this.userId = this.article.userId;
  },
};
</script>

<style></style>
