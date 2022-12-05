<template>
  <div class="text-center" style="margin: 0 auto; width: 80%; min-width: 900px">
    <v-btn
      depressed
      style="color: white; margin-top: -50px; float: right; background-color: #03c75a"
      @click="moveFreeBoardWrite"
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
              <router-link :to="{ name: 'freeboardview', params: { articleNo: item.articleNo } }">
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

    <!-- search bar -->
    <v-col class="d-flex" cols="12" sm="6" style="margin: 0 auto">
      <v-select v-model="searchScope" :items="searchScopes" label="검색" style="width: 100px; flex-grow: 0"></v-select>
      <v-text-field v-model="searchParam" @keyup.enter="searchArticle(searchParam, searchScope)"></v-text-field>
      <v-btn
        @click="searchArticle(searchParam, searchScope)"
        depressed
        style="margin-top: 13px; margin-left: 20px; background-color: #03c75a; color: white"
      >
        검색
      </v-btn>
    </v-col>

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
const freeBoardStore = "freeBoardStore";

export default {
  name: "FreeBoardList",
  data() {
    return {
      fields: [
        { key: "articleNo", label: "글번호", tdClass: "tdClass" },
        { key: "subject", label: "제목", tdClass: "tdSubject" },
        { key: "userId", label: "작성자", tdClass: "tdClass" },
        { key: "registerTime", label: "작성일", tdClass: "tdClass" },
        { key: "hit", label: "조회수", tdClass: "tdClass" },
        { key: "nowPage", label: "현재페이지", tdClass: "tdClass" },
      ],

      // search
      searchScope: "",
      searchScopes: [
        { text: "제목", value: "subject" },
        { text: "작성자", value: "userId" },
      ],

      searchParam: "",
      param: "",
      scope: "",
    };
  },
  computed: {
    ...mapState(freeBoardStore, ["articles", "pages", "comments", "page"]),
  },
  created() {
    this.getArticleList({ page: this.page });
    this.param = "";
    this.scope = "";
  },
  mounted() {
    this.getArticleList({ page: this.page });
  },
  methods: {
    ...mapActions(freeBoardStore, ["getArticleList", "searchArticleList"]),
    ...mapMutations(freeBoardStore, ["SET_ARTICLE_LIST"]),
    updateList(page) {
      this.getArticleList({
        page: page,
        searchScope: this.scope,
        searchParam: this.param,
      });
    },
    searchArticle(searchParam, searchScope) {
      if (searchScope != "") {
        this.param = searchParam;
        this.scope = searchScope;
        this.getArticleList({ page: 1, searchScope: this.scope, searchParam: this.param });
      } else {
        alert("검색 범위를 설정해주세요.");
      }
    },
    moveFreeBoardWrite() {
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
