<template>
  <div style="z-index: 3">
    <!-- search bar -->
    <v-col class="d-flex" style="justify-content: center">
      <div class="selectBoxs">
        <v-select
          v-model="sido"
          :items="sidos"
          label="시도"
          item-text="name"
          item-value="code"
          @change="getCode('gugun', sido)"
          class="col-xs-7 col-sm-6 col-lg-8"
        ></v-select>
      </div>
      <div class="selectBoxs">
        <v-select
          v-model="gugun"
          :items="guguns"
          label="구군"
          item-text="name"
          item-value="code"
          @change="getCode('dong', gugun)"
          no-data-text="시도를 선택해주세요"
          class="col-xs-7 col-sm-6 col-lg-8"
        ></v-select>
      </div>
      <div class="selectBoxs">
        <v-select
          v-model="dong"
          :items="dongs"
          label="동"
          item-text="name"
          item-value="code"
          no-data-text="구군을 선택해주세요"
          class="col-xs-7 col-sm-6 col-lg-8"
        ></v-select>
      </div>

      <div style="padding-top: 5px">
        <v-btn class="ma-2" color="#03c75a" dark @click="searchClick">
          검색
          <v-icon dark right> mdi-checkbox-marked-circle </v-icon>
        </v-btn>
      </div>

      <div style="padding-top: 5px; margin-left: 50px">
        <v-btn class="ma-2" color="#03c75a" dark @click="addInterestArea"> + 관심지역 </v-btn>
      </div>

      <div class="text-center" style="padding-top: 13px">
        <v-menu v-model="interestAreaMenu" :close-on-content-click="false" :nudge-width="200" offset-x>
          <template v-slot:activator="{ on, attrs }">
            <v-btn color="indigo" dark v-bind="attrs" v-on="on"> 관심지역 목록 </v-btn>
          </template>

          <v-card style="background-color: beige">
            <v-list style="background-color: beige">
              <v-list-item>
                <v-list-item-content style="width: 150px; text-decoration: none">
                  <p style="color: black" v-if="instAreaList == ''">관심지역을 추가해보세요!</p>
                  <v-list-item-title v-for="(area, idx) in instAreaList" :key="idx"
                    ><a @click="moveInterestArea(area.dongCode)" style="color: black">{{ area.dongName }} </a>

                    <a @click="delInstArea(area.dongCode)" style="color: black" v-if="area.dongCode != 9999"> / 삭제</a>
                    <v-divider></v-divider>
                  </v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list>

            <v-divider></v-divider>

            <v-card-actions>
              <v-spacer></v-spacer>

              <v-btn text @click="interestAreaMenu = false"> 닫기 </v-btn>
            </v-card-actions>
          </v-card>
        </v-menu>
      </div>
    </v-col>
  </div>
</template>

<script>
// import http from "@/components/api/http";
import { mapState, mapActions, mapMutations } from "vuex";
const userStore = "userStore";
const mapStore = "mapStore";

export default {
  name: "HouseSearchBar",
  data() {
    return {
      // select box
      // sidos: [],
      // guguns: [],
      // dongs: [],
      // year: "",
      // month: "",
      sido: "",
      gugun: "",
      dong: "",
      interestAreaMenu: false,
      instAreaList: [{ dongCode: 9999, dongName: "로그인 후 이용해 주세요." }],
    };
  },
  computed: {
    ...mapState(mapStore, ["sidos", "guguns", "dongs", "year", "month", "aptList", "interestAreaList"]),
    ...mapState(userStore, ["userInfo"]),
  },
  methods: {
    ...mapActions(mapStore, ["getCodeStore", "getAptList", "insertInterestArea", "getInstArea", "deleteInstArea"]),
    ...mapActions(userStore, ["setUserInstArea"]),
    ...mapMutations(mapStore, []),
    getCode(type = "sido", code = "") {
      this.getCodeStore({ type, code });
    },

    searchClick() {
      let code = this.dong;
      this.getAptList(code);
      // console.log(this.aptList);
    },

    async addInterestArea() {
      if (this.dong != "" && this.dong != "구군을 선택해주세요" && this.userInfo != null) {
        console.log("들어옴");
        await this.insertInterestArea({ dongCode: this.dong, user_id: this.userInfo.userId });

        let url = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=" + this.dong;
        let name = "";
        await fetch(url)
          .then((res) => res.json())
          .then((data) => {
            name = data.regcodes[0].name;
          });

        let alreayIn = false;
        this.instAreaList.forEach((item) => {
          if (item.dongCode == this.dong) {
            alreayIn = true;
          }
        });

        if (!alreayIn) {
          this.instAreaList = [...this.instAreaList, { dongCode: this.dong, dongName: name }];
        }
      }
    },

    async getInterestArea() {
      await this.getInstArea(this.userInfo.userId);
      this.instAreaList = [];
      for (let i = 0; i < this.interestAreaList.length; i++) {
        let url =
          "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=" +
          this.interestAreaList[i].dongCode;
        let name = "";
        await fetch(url)
          .then((res) => res.json())
          .then((data) => {
            name = data.regcodes[0].name;
          });

        this.instAreaList = [...this.instAreaList, { dongCode: this.interestAreaList[i].dongCode, dongName: name }];
      }
      this.setUserInstArea(this.instAreaList);
    },

    moveInterestArea(dongCode) {
      console.log(dongCode);
      if (dongCode == "9999") {
        this.$router.push("/user/login");
      } else {
        this.sido = dongCode.substr(0, 2);
        this.getCode(dongCode.substr(0, 2));
        this.getCode("gugun", dongCode.substr(0, 2));
        this.gugun = dongCode.substr(0, 5);
        this.getCode(dongCode.substr(0, 5));
        this.getCode("dong", dongCode.substr(0, 5));
        this.dong = dongCode;
        this.searchClick();
      }
    },

    async delInstArea(dongCode) {
      await this.deleteInstArea({ dongCode: dongCode, user_id: this.userInfo.userId });
      this.instAreaList.forEach((item, index) => {
        if (item.dongCode == dongCode) {
          this.instAreaList.splice(index, 1);
        }
      });
    },
  },
  created() {
    this.getCodeStore({ type: "sido" });
    this.getInterestArea();
  },
};
</script>

<style>
.selectBox {
  border: 2px solid;
  height: 100px;
}
.selectBoxs {
  margin: 0px 0px 0px 10px;
  z-index: 9999;
  /* width: 30%; */
}
</style>
