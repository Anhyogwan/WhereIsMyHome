<template>
  <div style="margin: 0 auto; width: 70%">
    <v-card class="no-border">
      <v-tabs background-color="" center-active>
        <div
          style="width: 40px; margin-right: 50px; margin-left: 0px; padding-top: 3px"
          @click="homeClick"
        >
          <v-tab>
            <router-link :to="{ name: 'main' }">
              <v-img
                alt="Vuetify Logo"
                class="shrink mr-2"
                contain
                src="@/assets/logo.png"
                transition="scale-transition"
                width="40px"
              />
            </router-link>
          </v-tab>
        </div>

        <v-spacer></v-spacer>

        <v-tab @click="noticeClick">NOTICE</v-tab>
        <v-tab @click="mapClick"> MAP</v-tab>
        <v-tab @click="freeboardClick">FREE BOARD</v-tab>
        <v-tab @click="userListClick" v-if="userInfo == null ? false : userInfo.userId == 'admin'"
          >USER INFO</v-tab
        >

        <!-- after login -->
        <v-row justify="end" v-if="userInfo">
          <v-app id="inspire">
            <div class="text-center" style="margin-top: 8px">
              <v-menu v-model="menu" :close-on-content-click="false" :nudge-width="80" offset-x>
                <template v-slot:activator="{ on, attrs }">
                  <v-list-item-avatar color="indigo" dark v-bind="attrs" v-on="on">
                    <!-- <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" /> -->
                    <!-- <v-icon dark> mdi-account-circle </v-icon> -->
                    <img
                      :src="userInfo.profileImg != null ? userInfo.profileImg : alter"
                      alt="profile"
                    />
                  </v-list-item-avatar>
                </template>

                <v-card>
                  <v-list>
                    <v-list-item>
                      <v-list-item-avatar color="indigo">
                        <!-- <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" /> -->
                        <!-- <v-icon dark> mdi-account-circle </v-icon> -->
                        <img
                          :src="userInfo.profileImg != null ? userInfo.profileImg : alter"
                          alt="profile"
                        />
                      </v-list-item-avatar>

                      <v-list-item-content>
                        <v-list-item-title>{{ userInfo.userName }}</v-list-item-title>
                        <v-list-item-subtitle
                          >{{ userInfo.emailId }}@{{ userInfo.emailDomain }}</v-list-item-subtitle
                        >
                      </v-list-item-content>

                      <!-- <v-list-item-action>
                        <v-btn :class="fav ? 'red--text' : ''" icon @click="fav = !fav">
                          <v-icon>mdi-heart</v-icon>
                        </v-btn>
                      </v-list-item-action> -->
                    </v-list-item>
                  </v-list>

                  <v-divider></v-divider>

                  <v-list>
                    <v-list-item>
                      <v-list-item-action>
                        {{ userInfo.userName }}님 환영합니다.
                      </v-list-item-action>
                    </v-list-item>
                  </v-list>

                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text @click="userModifyClick"> 마이페이지 </v-btn>
                    <v-btn text @click="Logout"> LOG OUT </v-btn>
                  </v-card-actions>
                </v-card>
              </v-menu>
            </div>
          </v-app>
        </v-row>
        <!-- before login -->

        <v-row style="justify-content: end; margin-right: 12px" v-else>
          <div class="text-center" style="margin-top: 20px">
            <div style="margin-top: -10px">
              <v-btn class="ma-2" color="#03c75a" dark @click="loginClick"> 로그인 </v-btn>
            </div>
            <!-- <a @click="loginClick"> 로그인 </a> -->
          </div>
        </v-row>
      </v-tabs>
    </v-card>
    <v-divider style="margin: 0px 0px 10px 0px"></v-divider>
  </div>
</template>

<script>
import { mapState, mapGetters, mapActions } from "vuex";
const userStore = "userStore";

export default {
  name: "HeaderBar",

  data: () => ({
    fav: true,
    menu: false,
    message: false,
    hints: true,
    alter: "",
  }),
  computed: {
    ...mapState(userStore, ["isLogin", "userInfo", "userInfo"]),
    ...mapGetters([]),
  },

  methods: {
    ...mapActions(userStore, ["userLogout"]),
    userListClick() {
      this.$router.push("/user/list");
    },
    homeClick() {
      this.$router.push("/");
    },
    noticeClick() {
      this.$router.push("/notice");
    },
    freeboardClick() {
      this.$router.push("/freeboard");
    },
    mapClick() {
      this.$router.push("/map");
    },
    loginClick() {
      this.$router.push("/user/login");
    },
    userModifyClick() {
      this.$router.push("/user/modify");
      this.menu = false;
    },
    Logout() {
      this.userLogout(this.userInfo.userId);
      //sessionStorage.removeItem("access-token"); //저장된 토큰 없애기
      //sessionStorage.removeItem("refresh-token"); //저장된 토큰 없애기
      if (this.$route.path != "/") this.$router.push({ name: "main" });
    },
  },
};
</script>
<style scoped>
.no-border {
  box-shadow: none !important;
}

v-tab * {
  text-decoration: none;
}
</style>
