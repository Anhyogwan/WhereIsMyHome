<template>
  <div style="margin: 0 auto; width: 70%">
    <carousel-jo></carousel-jo>
    <h2 style="margin: 20px 0px 10px 10px">최근 거래 매물</h2>
    <v-row no-gutters style="margin-bottom: 20px; margin-top: 20px">
      <v-col class="d-flex flex-row" cols="12" style="overflow-y: hidden; overflow-x: auto; white-space: nowrap">
        <recently-trade class="recently-trade"></recently-trade>
      </v-col>
      <v-col cols="12" lg="6">
        <year-chart />
      </v-col>
    </v-row>
    <notice-list></notice-list>
    <news-list></news-list>
  </div>
</template>
<script>
import RecentlyTrade from "@/components/main/RecentlyTrade";
import CarouselJo from "@/components/main/CarouselJo";
import NoticeList from "@/components/main/NoticeList";
import NewsList from "@/components/main/NewsList";
import YearChart from "@/components/common/YearChart";

export default {
  name: "AppMain",
  components: {
    RecentlyTrade,
    CarouselJo,
    NoticeList,
    NewsList,
    YearChart,
  },
};
</script>

<style></style>
