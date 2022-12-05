<template>
  <div class="NoticeDiv">
    <div>
      <h6 style="float: right; margin-right: 15px"><a @click="mvNoticeList">+</a></h6>
      <h4>공지사항</h4>
    </div>
    <v-divider></v-divider>
    <ul class="list_notice">
      <li v-for="item in recents" :key="item.name">
        <a
          @click="mvNotice(item.articleNo)"
          target="_blank"
          rel="noreferrer"
          class="link_notice"
          :value="item.articleNo"
          ><span class="txt_notice">[공지] {{ item.subject }}</span
          ><span class="txt_date" style="float: right">{{ item.registerTime }}</span>
        </a>
      </li>
      <!-- <li>
        <a href="#" target="_blank" rel="noreferrer" class="link_notice"
          ><span class="txt_notice">[공지] 서비스 장애 및 정상화 안내 </span><span class="txt_date">2022.10.24</span></a
        >
      </li>
      <li>
        <a href="#" target="_blank" rel="noreferrer" class="link_notice"
          ><span class="txt_notice">[공지] 2022/10/15 장애 공유 </span><span class="txt_date">2022.10.24</span></a
        >
      </li>
      <li>
        <a href="#" target="_blank" rel="noreferrer" class="link_notice"
          ><span class="txt_notice">[공지] JavaScript SDK v1 다운로드 링크 변경 </span
          ><span class="txt_date">2022.10.19</span></a
        >
      </li> -->
    </ul>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
const noticeStore = "noticeStore";
export default {
  data() {
    return {
      recent: [],
    };
  },
  computed: {
    ...mapState(noticeStore, ["recents"]),
  },
  created() {
    if (this.getRecNotice()) {
      this.recent = [...this.recents];
    }
  },
  methods: {
    ...mapActions(noticeStore, ["getRecNotice"]),
    mvNotice(page) {
      this.$router.push({ path: `/notice/view/${page}` });
    },
    mvNoticeList() {
      this.$router.push({ name: "noticelist" });
    },
  },
};
</script>

<style>
.NoticeDiv {
  width: 100%;
  /* height: 300px; */
  font-size: 25px;
  padding: 10px;
  margin-bottom: 40px;
}

.NoticeMargin {
  margin-bottom: 10px !important;
}

.list_notice .txt_notice {
  position: relative;
  display: table-cell;
  padding: 5px 0 0 5px;
  font-size: 18px;
  line-height: 32px;
  color: hsla(0, 76%, 3%, 0.6);
  font-family: NotoSans Medium, Malgun Gothic, 맑은 고딕, Apple SD Gothic Neo, 돋움, dotum, sans-serif;
  letter-spacing: -0.2px;
  vertical-align: middle;
}

.link_notice .txt_date {
  display: table-cell;
  padding: 5px 0 0 32px;
  font-size: 14px;
  line-height: 24px;
  color: hsla(0, 76%, 3%, 0.6);
  letter-spacing: -0.2px;
  vertical-align: middle;
}

.link_notice {
  text-decoration: none;
}

.list_notice > li {
  list-style: none;
  padding-left: 0px;
}
</style>
