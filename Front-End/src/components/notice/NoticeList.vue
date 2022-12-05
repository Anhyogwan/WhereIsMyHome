<template>
  <div class="text-center" style="margin: 0 auto; width: 80%; min-width: 900px">
    <v-btn
      depressed
      style="color: white; margin-top: -50px; background-color: #03c75a; float: right"
      @click="moveNoticeWrite"
      v-if="userInfo == null ? false : userInfo.userId == 'admin'"
    >
      글쓰기
    </v-btn>

    <div class="NoticeDiv" style="height: auto">
      <v-simple-table style="width: 100%; background-color: white; color: hsla(0, 76%, 3%, 0.6)">
        <thead>
          <tr>
            <th style="text-align: center">글 번호</th>
            <th style="text-align: center">제목</th>
            <th style="text-align: center">작성자</th>
            <th style="text-align: center">작성일</th>
            <th style="text-align: center">조회</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in articles" :key="item.name">
            <td>{{ item.articleNo }}</td>
            <td>
              <router-link :to="{ name: 'noticeview', params: { articleNo: item.articleNo } }">
                {{ item.subject }}
              </router-link>
            </td>
            <td>{{ item.userId }}</td>
            <td>{{ item.registerTime }}</td>
            <td>{{ item.hit }}</td>
          </tr>
        </tbody>
      </v-simple-table>
    </div>

    <!-- pagination -->
    <div>
      <button @click="updateList(pages.prevClickPage)" v-show="pages.prevCheck">&lt;&lt;</button>
      <button
        @click="updateList(pages.startButton + index - 1)"
        v-for="index in pages.endButton - pages.startButton + 1"
        :key="index"
        style="padding: 10px"
      >
        <span v-if="pages.startButton + index - 1 == pages.nowPage" style="font-weight: bold">
          {{ pages.startButton + index - 1 }}
        </span>
        <span v-else>
          {{ pages.startButton + index - 1 }}
        </span>
      </button>
      <button @click="updateList(pages.nextClickPage)" v-show="pages.nextCheck">>></button>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions, mapMutations } from "vuex";
const noticeStore = "noticeStore";
const userStore = "userStore";
export default {
  name: "noticeList",
  data() {
    return {
      // search
      searchScope: "제목",
      searchScopes: [
        { text: "제목", value: "subject" },
        { text: "작성자", value: "userId" },
        { text: "내용", value: "content" },
      ],

      searchParam: "",
    };
  },
  computed: {
    ...mapState(noticeStore, ["articles", "pages", "page"]),
    ...mapState(userStore, ["userInfo"]),
  },
  created() {
    this.getArticleList(this.page);
  },
  mounted() {
    this.getArticleList(this.page);
  },
  methods: {
    ...mapActions(noticeStore, ["getArticleList", "searchArticleList"]),
    ...mapMutations(noticeStore, ["SET_ARTICLE_LIST"]),
    updateList(page) {
      this.getArticleList(page);
    },
    searchArticle() {
      let searchScope = this.searchScope;
      let searchParam = this.searchParam;
      this.searchArticleList({ searchScope, searchParam });
    },
    moveNoticeWrite() {
      this.$router.push("write");
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
<!-- 
<script>
var naver_id_login = new naver_id_login("", "http://localhost:8080/login/oauth2/code/naver");
var state = naver_id_login.getUniqState();
naver_id_login.setButton("white", 2, 40);
naver_id_login.setDomain("http://localhost:8080/login");
naver_id_login.setState(state);
naver_id_login.setPopup();
naver_id_login.init_naver_id_login();
</script> -->
