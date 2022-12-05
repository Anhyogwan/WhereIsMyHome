<template>
  <v-app id="app">
    <header-bar></header-bar>
    <router-view></router-view>
    <footer-bar></footer-bar>
  </v-app>
</template>

<script>
import HeaderBar from "@/components/common/HeaderBar";
import FooterBar from "@/components/common/FooterBar";
import 'material-design-icons-iconfont/dist/material-design-icons.css' // Ensure you are using css-loader

export default {
  name: "App",
  components: {
    HeaderBar,
    FooterBar,
  },
};
</script>

<style></style>
