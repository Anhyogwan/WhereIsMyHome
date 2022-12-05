<template>
  <div>
    <house-search-bar></house-search-bar>
    <router-view></router-view>
  </div>
</template>

<script>
import HouseSearchBar from "@/components/map/HouseSearchBar.vue";

export default {
  name: "AppMap",
  components: { HouseSearchBar },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style></style>
