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

    <div style="float: right" v-if="userInfo.userId == article.userId ? true : userInfo.userId == 'admin'">
      <v-btn @click="modifyClick" style="margin-right: 10px"> 수정</v-btn>

      <v-btn @click="deleteArticle"> 삭제</v-btn>
    </div>

    <div></div>

    <v-divider style="margin: 50px 0px 10px 0px"></v-divider>

    <div>
      <!-- Comment -->
      <div class="CommentTitle">
        <v-icon large color="gray"> mdi-message-text </v-icon>댓글
        <div class="CommentContent">
          <v-textarea
            style="width: 100%; margin-right: 10px"
            height="100"
            solo
            name="input-7-4"
            label="댓글 입력"
            v-model="comment"
          ></v-textarea>
          <v-btn @click="insertComment()" depressed height="100" color="#03c75a" style="color: white"> 등록 </v-btn>
        </div>
        <v-divider style="margin: 0px 0px 20px 0px"></v-divider>
        <!-- 갯수 줄이기 -->

        <v-simple-table style="width: 100%; background-color: white; color: hsla(0, 76%, 3%, 0.6)">
          <tr v-for="(item,index) in article.comments" :key="item.commentNo">
            <!-- <td v-for="index in item.depth" :key="index"></td>-->
            <div v-bind:style="{ 'text-indent': item.depth * 30 + 'px' }">
              <div style="display: flex; justify-content: space-between; white-space: nowrap">
                <div>
                  {{ item.cuserId }}
                  &nbsp;&nbsp;&nbsp;&nbsp;
                  {{ item.commentDate }}
                  &nbsp;
                  <v-btn @click="updateCommentBox(item)"> 수정</v-btn>
                  <v-btn @click="deleteComment(item)"> 삭제</v-btn>
                </div>
                <div>
                  <a @click="changeReCommentBox(item.commentNo)"> 답글</a>
                </div>
              </div>
              <div style="display: flex; margin-top: 20px; margin-bottom: 10px; white-space: nowrap">
                {{ item.content }} 
              </div>
              <div v-show="isShow[item.commentNo]" style="display: flex" :id="item.commentNo" class="reCommentBox">
                <v-textarea
                  style="width: 80%; margin-right: 10px"
                  height="100"
                  solo
                  name="input-7-4"
                  label="답글 입력"
                  v-model="recomment"
                ></v-textarea>
                <v-btn
                  v-show="isButton"
                  @click="insertreComment(item)"
                  depressed
                  height="100"
                  color="#03c75a"
                  style="color: white"
                >
                  등록
                </v-btn>
                <v-btn
                  v-show="!isButton"
                  @click="updateComment(item)"
                  depressed
                  height="100"
                  color="#03c75a"
                  style="color: white"
                >
                  수정
                </v-btn>
              </div>
              <v-divider style="margin: 0px 0px 20px 0px"></v-divider>
            </div>
          </tr>
        </v-simple-table>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions, mapMutations } from "vuex";

const freeBoardStore = "freeBoardStore";
const userStore = "userStore";

export default {
  name: "FreeBoardView",
  data() {
    return {
      comment: "",
      recomment: "",
    };
  },
  computed: {
    ...mapState(freeBoardStore, ["article", "isShow", "currentBox", "isButton"]),
    ...mapState(userStore, ["userInfo"]),
  },
  created() {
    this.getArticleDetail(this.$route.params.articleNo);
  },
  methods: {
    ...mapActions(freeBoardStore, [
      "getArticleDetail",
      "insertNewComment",
      "insertNewreComment",
      "reCommentBox",
      "updateNewComment",
      "changeCommentBox",
      "delComment",
      "delArticle",
    ]),
    ...mapMutations(freeBoardStore, [
      "SET_ARTICLE_DETAIL",
      "SET_NEW_COMMENT",
      "SET_NEW_RECOMMENT",
      "SET_RECOMMENT_BOX",
      "SET_UPDATE_NEW_COMMENT",
      "SET_CHANGE_COMMENT_BOX",
    ]),
    async insertComment() {
      if (this.comment != "") {
        //        this.insertNewComment(this.comment);
        await this.insertNewComment({ comment: this.comment, userId: this.userInfo.userId });
        this.comment = "";
      } else {
        alert("내용을 입력하세요");
      }
    },
    async insertreComment(comment) {
      if (this.recomment != "") {
        // userinfo 추가
        let recomment = this.recomment;
        await this.insertNewreComment({
          comment: comment,
          recomment: recomment,
          userId: this.userInfo.userId,
        });
        this.recomment = "";
      } else {
        alert("내용을 입력하세요");
      }
    },
    async changeReCommentBox(commentNo) {
      this.recomment = "";
      await this.reCommentBox(commentNo);
    },
    async updateCommentBox(comment) {
      await this.changeReCommentBox(comment.commentNo);
      this.recomment = comment.content;
      await this.changeCommentBox();
    },

    async updateComment(comment) {
      comment.content = this.recomment;

      await this.updateNewComment(comment);
      this.recomment = "";
    },
    async deleteComment(comment) {
      if (confirm(`해당 댓글를 지우시겠습니까?`)) {
        await this.delComment(comment);
      } else {
        alert("취소하였습니다");
      }
    },
    async deleteArticle() {
      if (confirm(`해당 게시글을 지우시겠습니까?`)) {
        await this.delArticle(this.article.articleNo);
        this.$router.push("/freeboard/list");
      } else {
        alert("취소하였습니다");
      }
    },
    modifyClick() {
      this.$router.push({ name: "freeboardmodify", params: { name: this.article.articleNo } });
    },

    movePrev() {
      this.$router.push("/freeboard/list");
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
.CommentTitle {
  text-align: left;
}
.CommentContent {
  display: flex;
  margin: 10px 0px 40px 0px;
}
.reCommentContent {
  display: none;
}
</style>
