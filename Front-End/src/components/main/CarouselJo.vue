<template>
  <v-carousel cycle height="400" hide-delimiter-background show-arrows-on-hover>
    <v-carousel-item v-for="(item, i) in items" :key="i" :src="item.src">
      <!-- <v-sheet height="100%"> -->
      <!-- <v-row class="fill-height" align="center" justify="center"> -->
      <!-- <div class="text-h2">{{ slide }}</div> -->
      <!-- </v-row> -->
      <!-- </v-sheet> -->
    </v-carousel-item>
  </v-carousel>
</template>

<script>
export default {
  data() {
    return {
      items: [
        {
          src: "https://cdn.vuetifyjs.com/images/carousel/squirrel.jpg",
        },
        {
          src: require("@/assets/HouseImg/img2.jpg"),
        },
        {
          src: require("@/assets/HouseImg/img3.jpg"),
        },
        {
          src: require("@/assets/HouseImg/img4.jpg"),
        },
        {
          src: require("@/assets/HouseImg/img5.jpg"),
        },
      ],
      slides: ["내 집 마련의 꿈", "Second", "Third", "Fourth", "Fifth"],
    };
  },
};
</script>

<style></style>
