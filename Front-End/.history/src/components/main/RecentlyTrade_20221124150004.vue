<template>
  <v-sheet>
    <v-slide-group style="display: flex" class="pa-4" actice-class="success" show-arrows>
      <v-slide-item style="margin-bottom: 30px" v-for="item in recentlyList" :key="item">
        <v-card :loading="loading" class="mx-2" max-width="250" height="400px">
          <v-img height="140" src="https://image.ajunews.com/content/image/2021/07/14/20210714153438141154.jpg"></v-img>

          <v-card-title style="margin-bottom: -30px">{{ item.aptName }}</v-card-title>

          <v-card-text>
            <div class="my-4 text-subtitle-1">
              <div style="font-size: 14px">{{ item.dongName }} {{ item.jibun }}</div>
              거래일시 : {{ item.dealDate }} <br />
              건축년도 : {{ item.buildYear }}년 <br />
              거래금액 : {{ item.dealAmount }} 만원 <br />
              거래층 : {{ item.floor }} 층 <br />
              공급면적 : {{ item.area }} <em>m <sup>2</sup></em> <br /><br />
            </div>
            <div></div>
          </v-card-text>
        </v-card>
      </v-slide-item>
    </v-slide-group>
  </v-sheet>
</template>

<script>
import { mapState, mapActions } from "vuex";
const mapStore = "mapStore";

export default {
  data: () => ({
    loading: false,
    selection: 1,
    qty: 10,
    recentlyList: [],
  }),

  computed: {
    ...mapState(mapStore, ["recentlyAptList", "aptInfo"]),
  },

  created() {
    this.getRecentInfo();
  },

  methods: {
    ...mapActions(mapStore, ["getRecentlyAptList", "getAptInfoByAptCode"]),

    reserve() {
      this.loading = true;

      setTimeout(() => (this.loading = false), 2000);
    },

    async getRecentInfo() {
      console.log(123);
      await this.getRecentlyAptList(this.qty);

      console.log(this.recentlyAptList);

      this.recentlyList = [];
      for (let i = 0; i < this.recentlyAptList.length; i++) {
        console.log(this.recentlyAptList[i].aptCode);
        this.getAptInfoByAptCode(this.recentlyAptList[i].aptCode);
        let url =
          "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=" + this.aptInfo.dongCode;
        let dongName = "";
        await fetch(url)
          .then((res) => res.json())
          .then((data) => {
            dongName = data.regcodes[0].name;
          });

        this.recentlyList = [
          ...this.recentlyList,
          {
            dongName: dongName,
            aptName: this.aptInfo.apartmentName,
            buildYear: this.aptInfo.buildYear,
            jibun: this.aptInfo.jibun,
            dealDate:
              this.recentlyAptList[i].dealYear +
              "년 " +
              this.recentlyAptList[i].dealMonth +
              "월 " +
              this.recentlyAptList[i].dealDay +
              "일",
            area: this.recentlyAptList[i].area,
            dealAmount: this.recentlyAptList[i].dealAmount,
            floor: this.recentlyAptList[i].floor,
          },
        ];
        console.log(this.recentlyList);
      }
      // this.setUserInstArea(this.instAreaList);
    },
  },
};
</script>

<style></style>
