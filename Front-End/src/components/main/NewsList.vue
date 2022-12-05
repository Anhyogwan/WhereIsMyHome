<template>
  <div class="NoticeDiv">
    <div>
      <!-- <h6 style="float: right; margin-right: 15px"><a @click="mvNoticeList">+</a></h6> -->
      <h4>부동산 관련뉴스</h4>
    </div>
    <v-divider></v-divider>
    <ul class="list_notice">
      <li v-for="item in news" :key="item.name">
        <a :href="item.link" target="_blank" rel="noreferrer" class="link_notice" :value="item.articleNo"
          ><span class="txt_notice">{{ item.title }}</span
          ><span class="txt_date" style="float: right">{{ item.date }}</span>
        </a>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      instance: "",
      news: [],
    };
  },
  created() {
    this.instance = axios.create({
      headers: {
        "Content-Type": "application/json;charset=utf-8",
        "X-Naver-Client-Id": "",
        "X-Naver-Client-Secret": "",
      },
    });
    this.getNews();
  },
  methods: {
    async getNews() {
      let query = "부동산 집값";
      let display = 5;
      let sort = "date";
      let url = "http://192.168.120.62/news/searchnews?query=" + query + "&display=" + display + "&sort=" + sort;

      let news = [];
      await this.instance
        .get(url)
        .then((response) => (news = response.data.items))
        .catch((error) => console.log("error:", error));

      console.log(news);

      for (let i = 0; i < news.length; i++) {
        let date = news[i].pubDate.split(" ");
        let day = date[1];
        let month = date[2];
        let year = date[3];
        let time = date[4];
        switch (month) {
          case "Jan":
            month = 1;
            break;
          case "Feb":
            month = 2;
            break;
          case "Mar":
            month = 3;
            break;
          case "Apr":
            month = 4;
            break;
          case "May":
            month = 5;
            break;
          case "Jun":
            month = 6;
            break;
          case "Jul":
            month = 7;
            break;
          case "Aug":
            month = 8;
            break;
          case "Sep":
            month = 9;
            break;
          case "Oct":
            month = 10;
            break;
          case "Nov":
            month = 11;
            break;
          case "Dec":
            month = 12;
            break;

          default:
            break;
        }

        console.log(year + "-" + month + "-" + day + " " + time);

        this.news = [
          ...this.news,
          {
            title: news[i].title
              .replace(/<\/?("[^"]*"|'[^']*'|[^>])*(>|$)/gi, "")
              .replaceAll("&quot;", "")
              .replaceAll("&apos;", "")
              .replaceAll("&gt;", "")
              .replaceAll("&lt;", ""),
            link: news[i].link,
            date: year + "-" + month + "-" + day + " " + time,
          },
        ];
      }
    },
  },
};
</script>

<style></style>
