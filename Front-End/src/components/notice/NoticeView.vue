<template>
  <div style="min-width: 650px">
    <v-btn depressed style="color: white; margin-top: -50px; background-color: #03c75a; float: left" @click="movePrev">
      이전
    </v-btn>

    <div class="ArticleTitle">
      <h3>{{ article.subject }}</h3>
    </div>

    <div style="display: flex; justify-content: space-between">
      <div style="display: flex">
        <v-avatar color="indigo" style="margin: 10px 0px 10px 0px">
          <!-- <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" /> -->
          <v-icon dark> mdi-account-circle </v-icon>
        </v-avatar>
        <div style="margin: 20px 0px 0px 20px">{{ article.userId }}</div>
      </div>
      <div style="margin-top: 30px; white-space: nowrap; font-size: 12px; color: gray">
        <div>
          {{ article.registerTime }}
        </div>
        <div style="float: right">조회 {{ article.hit }}</div>
      </div>
    </div>

    <v-divider style="display: relative"></v-divider>

    <div class="ArticleContent">
      {{ article.content }}
    </div>

    <div style="float: right" v-if="userInfo == null ? false : userInfo.userId == 'admin'">
      <v-btn @click="modifyClick" style="margin-right: 10px"> 수정</v-btn>

      <v-btn @click="deleteArticle"> 삭제</v-btn>
    </div>

    <div></div>

    <v-divider style="margin: 50px 0px 10px 0px"></v-divider>
    <!-- Attachments -->
    <div class="Attachment" v-if="this.files.length == 0 ? false : true" style="text-align: left">
      <h3>첨부파일</h3>
    </div>
    <div id="filedoc" class="filedoc">
      <tr v-for="item in this.files" :key="item.name">
        <!-- <div style="background-color: #fffdeb; display: flex"> -->
        <div style="display: flex">
          &nbsp;&nbsp;
          <v-icon style="margin-top: -4px; margin-bottom: 4px">folder_open</v-icon>
          <td>&nbsp;&nbsp; {{ item.name }} &nbsp;&nbsp;&nbsp;</td>
          <td>
            <a :href="item.downurl" style="color: black; text-decoration: none"> [다운로드] </a>
            <!-- <a href="#" @click="downlaodFile(item.sfolder, item.ofile, item.sfile)">[다운로드]</a> -->
          </td>
        </div>
      </tr>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions, mapMutations } from "vuex";

const noticeStore = "noticeStore";
const userStore = "userStore";

export default {
  name: "NoticeDetailView",
  data() {
    return {
      comment: "",
      recomment: "",
      //files: [],
    };
  },
  computed: {
    ...mapState(noticeStore, ["article", "files"]),
    ...mapState(userStore, ["userInfo"]),
  },
  created() {
    this.getArticleDetail(this.$route.params.articleNo);
    console.log(this.files);
    // this.fileList();
  },
  methods: {
    ...mapActions(noticeStore, ["getArticleDetail", "delArticle", "downFile"]),
    ...mapMutations(noticeStore, ["SET_ARTICLE_DETAIL"]),

    async deleteArticle() {
      if (confirm(`해당 게시글을 지우시겠습니까?`)) {
        await this.delArticle(this.article.articleNo);

        //this.$router.push("/notice");
      } else {
        alert("취소하였습니다");
      }
    },
    downlaodFile(sfolder, ofile, sfile) {
      let data = `?sfolder=${sfolder}&ofile=${ofile}&sfile=${sfile}`;
      this.downFile(data);
    },

    modifyClick() {
      this.$router.push({ name: "noticemodify", params: { name: this.article.articleNo } });
    },

    movePrev() {
      this.$router.push("/notice/list");
    },
  },
};
</script>

<style>
.ArticleTitle {
  text-align: left;
  font-size: 24px;
}
.ArticleContent {
  padding: 20px 0px 20px 0px;
  text-align: left;
}

.filedoc {
  /* background-color: #fffdeb; */
  width: 70%;
  margin: 10px 0px 10px 0px;

  /* border: 2px rgba(0, 0, 0, 0.2) solid; */
  border-radius: 6px;

  font-size: 12px;
  /* 그림자 부분 */
  /* box-shadow: 5px 5px 5px gray; */
}
</style>
