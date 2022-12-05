<template>
  <v-card class="mx-auto text-center" color="green" dark style="margin-bottom: 30px">
    <v-card-text>
      <v-sheet color="rgba(0, 0, 0, .12)">
        <v-sparkline
          :value="value"
          :labels="year"
          color="rgba(255, 255, 255, .7)"
          height="100"
          padding="24"
          stroke-linecap="round"
          smooth
        >
          <template v-slot:label="item"> {{ item.value }} </template>
        </v-sparkline>
      </v-sheet>
    </v-card-text>

    <v-card-text>
      <div class="text-h4 font-weight-thin">월별 아파트 거래량</div>
    </v-card-text>

    <!-- <v-divider></v-divider>

    <v-card-actions class="justify-center">
      <v-btn block text> Go to Report </v-btn>
    </v-card-actions> -->
  </v-card>
</template>
<script>
export default {
  data: () => ({
    value: [423, 446, 675, 510, 590, 610, 760],
    year: ["2022/04", "2022/05", "2022/06", "2022/07", "2022/08", "2022/09", "2022/10"],
  }),
};
</script>
