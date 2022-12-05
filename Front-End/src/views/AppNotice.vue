<template>
  <div class="text-center" style="margin: 0 auto; width: 70%">
    <div style="margin: 20px 0px 50px 0px">
      <h2>공지사항</h2>
      
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
import { mapState } from "vuex";
const userStore = "userStore";
export default {
  data() {
    return {};
  },
  name: "AppNotice",
  computed: {
    ...mapState(userStore, ["userInfo"]),
  },
  methods: {
  },
};
</script>

<style></style>
