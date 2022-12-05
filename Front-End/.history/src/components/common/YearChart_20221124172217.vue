<template>
  <v-container fluid>
    <v-select
      :items="sido"
      item-text="name"
      item-value="code"
      v-model="c"
      @change="getData(c)"
      label="서울특별시"
      solo
    ></v-select>
    <LineChartGenerator
      :chart-options="chartOptions"
      :chart-data="chartData"
      :chart-id="chartId"
      :dataset-id-key="datasetIdKey"
      :plugins="plugins"
      :css-classes="cssClasses"
      :styles="styles"
      :width="width"
      :height="height"
    />
  </v-container>
</template>

<script>
import { Line as LineChartGenerator } from "vue-chartjs/legacy";
import axios from "axios";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  LinearScale,
  CategoryScale,
  PointElement,
} from "chart.js";

ChartJS.register(Title, Tooltip, Legend, LineElement, LinearScale, CategoryScale, PointElement);

export default {
  name: "LineChart",
  components: {
    LineChartGenerator,
  },
  props: {
    chartId: {
      type: String,
      default: "line-chart",
    },
    datasetIdKey: {
      type: String,
      default: "label",
    },
    width: {
      type: Number,
      default: 400,
    },
    height: {
      type: Number,
      default: 300,
    },
    cssClasses: {
      default: "",
      type: String,
    },
    styles: {
      type: Object,
      default: () => {},
    },
    plugins: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      c: "",
      sido: [
        { name: "서울특별시", code: ["서울특별시", 11] },
        { name: "부산광역시", code: ["부산광역시", 26] },
        { name: "대구광역시", code: ["대구광역시", 27] },
        { name: "인천광역시", code: ["인천광역시", 28] },
        { name: "광주광역시", code: ["광주광역시", 29] },
        { name: "대전광역시", code: ["대전광역시", 30] },
        { name: "울산광역시", code: ["울산광역시", 31] },
        { name: "경기도", code: ["경기도", 41] },
        { name: "강원도", code: ["강원도", 42] },
        { name: "충청북도", code: ["충청북도", 43] },
        { name: "충청남도", code: ["충청남도", 44] },
        { name: "전라북도", code: ["전라북도", 45] },
        { name: "전라남도", code: ["전라남도", 46] },
        { name: "경상북도", code: ["경상북도", 47] },
        { name: "경상남도", code: ["경상남도", 48] },
        { name: "제주특별시", code: ["제주특별시", 50] },
      ],
      chartData: {
        labels: [],
        datasets: [
          {
            label: ``,
            backgroundColor: "azure",
            pointBackgroundColor: "white",
            borderWidth: 5,
            pointBorderColor: "#249EBF",
            //Data to be represented on y-axis
            data: [],
          },
        ],
      },
      chartOptions: {
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true,
              },
              gridLines: {
                display: true,
              },
            },
          ],
          xAxes: [
            {
              gridLines: {
                display: false,
              },
            },
          ],
        },
        legend: {
          display: true,
        },
        responsive: true,
        maintainAspectRatio: false,
      },
    };
  },
  mounted() {},
  async created() {
    await axios.get(`http://192.168.120.62/apt/mainchart/11`).then((data) => {
      this.chartData.labels = [];
      this.chartData.datasets[0].data = [];
      var lab = this.chartData.labels;
      var da = this.chartData.datasets[0].data;
      this.chartData.datasets[0].label = `서울특별시 년도별 아파트 거래량`;
      data.data.forEach((element) => {
        lab.push(element.dealYear);
        da.push(element.count);
      });
    });
  },
  methods: {
    getData(c) {
      axios.get(`http://192.168.120.62/apt/mainchart/${c[1]}`).then((data) => {
        this.chartData.labels = [];
        this.chartData.datasets[0].data = [];
        var lab = this.chartData.labels;
        var da = this.chartData.datasets[0].data;
        this.chartData.datasets[0].label = `${c[0]} 년도별 아파트 거래량`;
        data.data.forEach((element) => {
          lab.push(element.dealYear);
          da.push(element.count);
        });
      });
    },
  },
};
</script>
