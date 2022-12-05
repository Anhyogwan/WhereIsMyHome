<template>
  <div class="topEl">
    <div class="detail" v-if="detailShow">
      <div class="detailSelectBoxs">
        <v-col>
          <v-menu
            ref="startMenu"
            v-model="startMenu"
            :close-on-content-click="false"
            :return-value.sync="startDate"
            transition="scale-transition"
            offset-y
            max-width="290px"
            min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="startDate"
                label="시작월"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker v-model="startDate" type="month" no-title scrollable>
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="startMenu = false"> Cancel </v-btn>
              <v-btn text color="primary" @click="setStartDate"> OK </v-btn>
            </v-date-picker>
          </v-menu>
        </v-col>

        <v-col>
          <v-menu
            ref="endMenu"
            v-model="endMenu"
            :close-on-content-click="false"
            :return-value.sync="endDate"
            transition="scale-transition"
            offset-y
            max-width="290px"
            min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="endDate"
                label="종료월"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker v-model="endDate" type="month" no-title scrollable>
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="endMenu = false"> Cancel </v-btn>
              <v-btn text color="primary" @click="setEndDate"> OK </v-btn>
            </v-date-picker>
          </v-menu>
        </v-col>

        <div style="margin: 12px 10px 0px 5px; width: 120px">
          <v-select
            v-model="commercial"
            :items="commercials"
            label="주변 상권"
            item-text="name"
            item-value="code"
            @change="getMaker(commercial[0], commercial[1], markers2)"
            style="width: 190px"
          >
          </v-select>
        </div>
      </div>

      <v-card class="mx-auto" max-width="90%">
        <div style="width: 100%; height: 150px">
          <img :src="aptImg" style="width: 100%; height: 100%" />
        </div>
        <div style="display: flex; justify-content: space-between">
          <div>
            <v-card-title>[아파트] {{ selectedApt.apartmentName }} </v-card-title>

            <v-card-subtitle>
              {{ selectedApt.dong }} {{ selectedApt.jibun }} <br />
              건축년도 : {{ selectedApt.buildYear }}년</v-card-subtitle
            >
          </div>
        </div>
        <div>
          <apt-chart refs="aptchart" />
        </div>
        <v-card-actions>
          <v-btn color="orange lighten-2" text> 더보기 </v-btn>

          <v-spacer></v-spacer>
          <div @click="loadInfo(selectedApt.lat, selectedApt.lng, 'roadview')">로드뷰</div>
          <v-btn icon @click="loadInfo(selectedApt.lat, selectedApt.lng, 'roadview')">
            <v-icon>{{ aptDetailShow ? "mdi-chevron-up" : "mdi-chevron-down" }}</v-icon>
          </v-btn>
        </v-card-actions>

        <v-expand-transition>
          <div v-show="aptDetailShow">
            <v-divider></v-divider>

            <v-card-text> 로드뷰 </v-card-text>
            <div id="roadview" style="width: 100%; height: 300px"></div>
          </div>
        </v-expand-transition>
      </v-card>
      <v-divider style="margin-top: 15px"></v-divider>

      <!-- <div style="margin-top: 10px"> -->
      <div style="margin-top: 10px" v-for="(apt, idx) in aptDeals" :key="idx">
        <v-card class="mx-auto" max-width="90%">
          <v-card-title>거래일시 : {{ apt.dealYear }}년 {{ apt.dealMonth }}월 {{ apt.dealDay }}일 <br /> </v-card-title>
          <v-card-subtitle>
            공급면적 : {{ apt.area }} <em>m <sup>2</sup></em> <br />
            거래금액 : {{ apt.dealAmount }}만원 <br />
            거래 층 : {{ apt.floor }} 층
          </v-card-subtitle>
        </v-card>
      </div>
    </div>
    <div id="map"></div>
  </div>
</template>

<script
  type="text/javascript"
  src="//dapi.kakao.com/v2/maps/sdk.js?appkey=47ff0d312559435134555834b5cc86b3&libraries=services"
></script>
<script>
import { mapState, mapActions, mapMutations } from "vuex";
import axios from "axios";
import AptChart from "@/components/common/AptChart";
const mapStore = "mapStore";

export default {
  name: "MapView",
  data() {
    return {
      // 아파트 거래목록
      commercial: "",
      commercials: [
        { name: "은행", code: ["BK9", "0"] },
        { name: "마트", code: ["MT1", "1"] },
        { name: "약국", code: ["PM9", "2"] },
        { name: "주유소", code: ["OL7", "3"] },
        { name: "카페", code: ["CE7", "4"] },
        { name: "편의점", code: ["CS2", "5"] },
        { name: "음식점", code: ["FD6", "6"] },
        { name: "병원", code: ["HP8", "7"] },
      ],
      aptDetailShow: false,
      aptImg:
        "https://img.lovepik.com/photo/20211123/small/lovepik-modern-building-apartment-house-picture_500842455.jpg",
      fav: true,
      years: [
        { name: 2022, value: 2022 },
        { name: 2021, value: 2021 },
        { name: 2020, value: 2020 },
        { name: 2019, value: 2019 },
        { name: 2018, value: 2018 },
        { name: 2017, value: 2017 },
        { name: 2016, value: 2016 },
        { name: 2015, value: 2015 },
        { name: 2014, value: 2014 },
        { name: 2013, value: 2013 },
        { name: 2012, value: 2012 },
        { name: 2011, value: 2011 },
        { name: 2010, value: 2010 },
      ],
      months: [
        { name: 12, value: 12 },
        { name: 11, value: 11 },
        { name: 10, value: 10 },
        { name: 9, value: 9 },
        { name: 8, value: 8 },
        { name: 7, value: 7 },
        { name: 6, value: 6 },
        { name: 5, value: 5 },
        { name: 4, value: 4 },
        { name: 3, value: 3 },
        { name: 2, value: 2 },
        { name: 1, value: 1 },
      ],
      year: "",
      month: "",

      // kakao map
      markerPositions1: [
        [33.452278, 126.567803],
        [33.452671, 126.574792],
        [33.451744, 126.572441],
      ],

      // date

      startDate: "",
      startMenu: false,
      startModal: false,

      endDate: "",
      endMenu: false,
      endModal: false,

      // map
      markers: [],
      infowindow: null,
      aptMarkerPosition: [],
      detailShow: false,
      aptListCenter: [],
      nowApt: "",
      selectedApt: [],
      searchMarkers: [],
      markers2: [],
      map2: null,
      ps2: null,
      placeOverlay: null,
    };
  },
  created() {},
  computed: { ...mapState(mapStore, ["aptList", "sido", "gugun", "dong", "aptDeals", "near", "aptlabels", "aptdata"]) },
  components: {
    AptChart,
  },
  watch: {
    aptList: {
      handler(val) {
        this.AptLocation();
      },
      deep: true,
    },
  },

  mounted() {
    if (window.kakao && window.kakao.maps) {
      this.initMap();
      this.ps2 = new kakao.maps.services.Places(this.map);
    } else {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src = "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=915cffed372954b7b44804ed422b9cf0";
      document.head.appendChild(script);
      this.ps2 = new kakao.maps.services.Places(this.map);
    }
  },
  methods: {
    ...mapActions(mapStore, ["getAptDealsByAptCode", "getFilterApt"]),
    ...mapMutations(mapStore, []),
    initMap() {
      const container = document.getElementById("map");
      const options = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 5,
      };

      //지도 객체를 등록합니다.
      //지도 객체는 반응형 관리 대상이 아니므로 initMap에서 선언합니다.
      this.map = new kakao.maps.Map(container, options);
    },
    changeSize(size) {
      const container = document.getElementById("map");
      container.style.width = `${size}%`;
      // container.style.height = `${size}px`;
      this.map.relayout();
    },

    // 검색된 아파트를 지도에 마커로 표시한다
    AptLocation() {
      this.changeSize(100);
      this.detailShow = false;
      // console.log(this.aptList);
      let loc = [];
      if (this.aptList.length != 0) {
        for (let i = 0; i < this.aptList.length; i++) {
          loc.push({
            title:
              "[아파트]" +
              this.aptList[i].apartmentName +
              "<br>" +
              this.aptList[i].dong +
              this.aptList[i].jibun +
              "<br />" +
              "건축년도 : " +
              this.aptList[i].buildYear +
              "년",
            latlng: new kakao.maps.LatLng(this.aptList[i].lat, this.aptList[i].lng),
            idx: i,
          });
        }

        this.displayMarker(loc);
      } else {
        alert("검색된 매물이 없습니다.");
      }
    },

    // 지도에 마커를 표시한다
    displayMarker(positions) {
      // console.log(markerPositions);

      // 마커 초기화
      if (this.markers.length > 0) {
        this.markers.forEach((marker) => marker.setMap(null));
      }

      // let idx = 0;
      // 마커 표시하기
      positions.forEach((position) => {
        // 인포 윈도우 생성
        // idx++;

        const infowindow = new kakao.maps.InfoWindow({
          removable: false,
          content: `<div class="infoWin"><img src="${this.aptImg}" style="width: 30%; height: 100%; margin-right: 10px" />${position.title}</div>`,
        });
        this.aptListCenter = positions;

        // 마커 이미지 변경
        const imageSrc = require("@/assets/map/apartment.png"), // 마커이미지의 주소입니다
          imageSize = new kakao.maps.Size(35, 40), // 마커이미지의 크기입니다
          imageOption = { offset: new kakao.maps.Point(20, 40) }; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

        // 마커 생성
        const marker = new kakao.maps.Marker({
          map: this.map,
          position: position.latlng,
          image: markerImage, // 마커이미지 설정
        });

        // 이벤트 등록
        kakao.maps.event.addListener(marker, "mouseover", () => {
          infowindow.open(this.map, marker);
        });
        kakao.maps.event.addListener(marker, "mouseout", () => {
          infowindow.close(this.map, marker);
        });
        // 마커 클릭시 발생할 이벤트
        kakao.maps.event.addListener(marker, "click", () => {
          this.startDate = "";
          this.endDate = "";
          this.aptDetailShow = false;
          if (this.nowApt != infowindow.cc || this.detailShow == false) {
            this.changeSize(80);
            this.detailShow = true;
            // 여기다가 axios해서 데이터 받아와야 할듯
            this.getData(this.aptList[position.idx].aptCode);
          } else {
            this.changeSize(100);
            this.detailShow = false;
          }

          let select = this.detailShow;
          this.nowApt = infowindow.cc;
          this.selectedApt = this.aptList[position.idx];
          this.getAptDealsByAptCode(this.aptList[position.idx].aptCode);

          this.year = "";
          this.month = "";

          // this.selectedApt = this.aptList[idx];
          // console.log(this.selectedApt);
          // 클릭한 마커를 중심으로
          this.viewCenter([
            {
              selectOne: select,
              latlng: new kakao.maps.LatLng(marker.getPosition().getLat(), marker.getPosition().getLng()),
            },
          ]);
        });

        this.markers.push(marker);
      });

      // 검색된 리스트를 지도 중심으로
      this.viewCenter(positions);
    },

    viewCenter(positions) {
      let bounds = "";
      if (positions[0].selectOne != null) {
        if (positions[0].selectOne) {
          bounds = positions.reduce(
            (bounds, position) => bounds.extend(position.latlng),
            new kakao.maps.LatLngBounds()
          );
        } else {
          bounds = this.aptListCenter.reduce(
            (bounds, position) => bounds.extend(position.latlng),
            new kakao.maps.LatLngBounds()
          );
        }
      } else {
        bounds = positions.reduce((bounds, position) => bounds.extend(position.latlng), new kakao.maps.LatLngBounds());
      }
      this.map.setBounds(bounds);
      this.map.setLevel(4);
    },

    // 년월로 필터한 결과를 출력
    getFilterAptList() {
      // console.log(this.startDate);
      // console.log(this.endDate);
      // console.log(this.selectedApt.aptCode);
      // this.aptDetailShow = false;
      if (this.startDate != "" && this.endDate != "") {
        this.getFilterApt({
          start: this.startDate,
          end: this.endDate,
          aptCode: this.selectedApt.aptCode,
        });
        
      }
    },
    loadInfo(lat, lng, id) {
      if (id == "roadview") {
        this.aptDetailShow = !this.aptDetailShow;
      }
      var roadviewContainer = document.getElementById(id); //로드뷰를 표시할 div
      console.log(roadviewContainer);
      var roadview = new kakao.maps.Roadview(roadviewContainer); //로드뷰 객체
      var roadviewClient = new kakao.maps.RoadviewClient(); //좌표로부터 로드뷰 파노ID를 가져올 로드뷰 helper객체
      console.log("포지션 입니다");
      var loadviewposition = new kakao.maps.LatLng(lat, lng);

      // 특정 위치의 좌표와 가까운 로드뷰의 panoId를 추출하여 로드뷰를 띄운다.
      roadviewClient.getNearestPanoId(loadviewposition, 50, function (panoId) {
        roadview.setPanoId(panoId, loadviewposition); //panoId와 중심좌표를 통해 로드뷰 실행
      });
    },
    getMaker(commercial, dataOrder) {
      // 마커를 클릭했을 때 해당 장소의 상세정보를 보여줄 커스텀오버레이입니다
      var currCategory = commercial; // 현재 선택된 카테고리를 가지고 있을 변수입니다
      console.log(currCategory);
      // 장소 검색 객체를 생성합니다
      var map2 = this.map;
      var ps = this.ps2;
      var markers = this.markers2;
      // 지도에 idle 이벤트를 등록합니다
      if (this.placeOverlay != null) {
        this.placeOverlay.setMap(null);
      }

      var plcaOverlay2 = new kakao.maps.CustomOverlay({ zIndex: 1 }),
        contentNode = document.createElement("div"), // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
        markers = this.markers2, // 마커를 담을 배열입니다
        currCategory = commercial; // 현재 선택된 카테고리를 가지고 있을 변수입니다
      this.placeOverlay = plcaOverlay2;
      console.log(map2);

      // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
      contentNode.className = "placeinfo_wrap";

      // 커스텀 오버레이의 컨텐츠 노드에 mousedown, touchstart 이벤트가 발생했을때
      // 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록합니다
      addEventHandle(contentNode, "mousedown", kakao.maps.event.preventMap);
      addEventHandle(contentNode, "touchstart", kakao.maps.event.preventMap);

      // 커스텀 오버레이 컨텐츠를 설정합니다
      plcaOverlay2.setContent(contentNode);

      // 엘리먼트에 이벤트 핸들러를 등록하는 함수입니다
      function addEventHandle(target, type, callback) {
        if (target.addEventListener) {
          target.addEventListener(type, callback);
        } else {
          target.attachEvent("on" + type, callback);
        }
      }
      kakao.maps.event.addListener(map2,"click",this.removeOverlay());
      kakao.maps.event.addListener(map2, "idle", searchPlaces);
      // 카테고리 검색을 요청하는 함수입니다
      function searchPlaces() {
        if (!currCategory) {
          return;
        }
        // 지도에 표시되고 있는 마커를 제거합니다
        console.log("여기임?");
        console.log(currCategory);
        removeMarker();
        ps.categorySearch(currCategory, placesSearchCB, { useMapBounds: true });
      }

      // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
      function placesSearchCB(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {
          // 정상적으로 검색이 완료됐으면 지도에 마커를 표출합니다

          displayPlaces(data);
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
          // 검색결과가 없는경우 해야할 처리가 있다면 이곳에 작성해 주세요
        } else if (status === kakao.maps.services.Status.ERROR) {
          // 에러로 인해 검색결과가 나오지 않은 경우 해야할 처리가 있다면 이곳에 작성해 주세요
        }
      }
      var roadv = this.loadInfo;
      // 지도에 마커를 표출하는 함수입니다
      function displayPlaces(places) {
        for (var i = 0; i < places.length; i++) {
          // 마커를 생성하고 지도에 표시합니다
          var m = addMarker(new kakao.maps.LatLng(places[i].y, places[i].x), dataOrder);

          // 마커와 검색결과 항목을 클릭 했을 때
          // 장소정보를 표출하도록 클릭 이벤트를 등록합니다
          (function (m, place) {
            kakao.maps.event.addListener(m, "click", function () {
              displayPlaceInfo(place);
              roadv(place.y, place.x, place.id);
            });
          })(m, places[i]);
        }
      }

      // 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
      function displayPlaceInfo(place) {
        console.log(place);
        var content =
          '<div class="placeinfo">' +
          '   <a class="title" href="' +
          place.place_url +
          '" target="_blank" title="' +
          place.place_name +
          '">' +
          place.place_name +
          "</a>";
        content += `<div id=${place.id} style="width:100%;height:300px"></div>`;
        if (place.road_address_name) {
          content +=
            '    <span title="' +
            place.road_address_name +
            '">' +
            place.road_address_name +
            "</span>" +
            '  <span class="jibun" title="' +
            place.address_name +
            '">(지번 : ' +
            place.address_name +
            ")</span>";
        } else {
          content += '    <span title="' + place.address_name + '">' + place.address_name + "</span>";
        }

        content += '    <span class="tel">' + place.phone + "</span>" + "</div>" + '<div class="after"></div>';

        contentNode.innerHTML = content;
        plcaOverlay2.setPosition(new kakao.maps.LatLng(place.y, place.x));
        plcaOverlay2.setMap(map2);
      }

      // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
      function addMarker(position, order) {
        // var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png", // 마커 이미지 url, 스프라이트 이미지를 씁니다
        //   imageSize = new kakao.maps.Size(27, 28), // 마커 이미지의 크기
        //   imgOptions = {
        //     spriteSize: new kakao.maps.Size(72, 208), // 스프라이트 이미지의 크기
        //     spriteOrigin: new kakao.maps.Point(46, order * 36), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
        //     offset: new kakao.maps.Point(11, 28), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        //   },
        var imageSrc = require(`@/assets/map/${currCategory}.png`), // 마커이미지의 주소입니다
          imageSize = new kakao.maps.Size(35, 40), // 마커이미지의 크기입니다
          imageOption = { offset: new kakao.maps.Point(20, 40) }, // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
          markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption),
          marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage,
          });
        marker.setMap(map2); // 지도 위에 마커를 표출합니다
        markers.push(marker);
        return marker;
      }
      // 지도 위에 표시되고 있는 마커를 모두 제거합니다
      function removeMarker() {
        for (var i = 0; i < markers.length; i++) {
          markers[i].setMap(null);
        }
      }

      // 지도 위에 표시되고 있는 마커를 모두 제거합니다
      map2.setLevel(map2.getLevel() == 3 ? 4 : 3);
      map2.setLevel(4);
      kakao.maps.event.removeListener(map2, "idle", searchPlaces);
    },
    setStartDate() {
      this.getFilterAptList();
      this.$refs.startMenu.save(this.startDate);
    },

    setEndDate() {
      this.getFilterAptList();
      this.$refs.endMenu.save(this.endDate);
    },
    async getData(aptCode, startDate = "", endDate = "") {
      await axios
        .get(`http://localhost/apt/aptchart?aptCode=${aptCode}&startDate=${startDate}&endDate=${endDate}`)
        .then((data) => {
          this.aptlabels = [];
          this.aptdata = [];
          var lab = this.aptlabels;
          var da = this.aptdata;
          lab.splice(0, lab.length);
          da.splice(0, da.length);
          console.log(lab);
          console.log(da);

          data.data.forEach((e) => {
            lab.push(e.dealYear + "/" + e.dealMonth);
            da.push(e.dealAmount);
          });
          console.log(this.aptlabels);
          console.log(this.aptdata);
        });
    },
    
    removeOverlay(){
        
    }
  },
};
</script>

<style>
.topEl {
  margin: 0 auto;
  width: 100%;
  display: flex;
}

#map {
  width: 100%;
  height: 75vh;
  position: relative;
  margin: 0 0 0 auto;
}
.button-group {
  margin: 10px 0px;
}

.detail {
  background-color: white;
  position: absolute;
  z-index: 100;
  width: 400px;
  height: 75vh;
  overflow: scroll;
}

.detail::-webkit-scrollbar {
  display: none;
}

.detailSelectBoxs {
  display: flex;
  position: sticky;
  top: 0;
  z-index: 101;
  background-color: white;
  border-bottom: 1px solid #1215;
  border-top: 1px solid #1215;
}

.infoWin {
  width: 270x;
  height: 100px;
  padding: 5px 5px 5px 5px;
  margin: 0px 0px 0px 0px;
  background-color: beige;
  display: flex;
  /* justify-content: space-between; */
}

/*----------------커스텀 오버레이-----------------*/
.placeinfo_wrap {
  position: absolute;
  bottom: 28px;
  left: -150px;
  width: 300px;
}
.placeinfo {
  position: relative;
  width: 100%;
  border-radius: 6px;
  border: 1px solid #ccc;
  border-bottom: 2px solid #ddd;
  padding-bottom: 10px;
  background: #fff;
}
.placeinfo:nth-of-type(n) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}
.placeinfo_wrap .after {
  content: "";
  position: relative;
  margin-left: -12px;
  left: 50%;
  width: 22px;
  height: 12px;
  background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png");
}
.placeinfo a,
.placeinfo a:hover,
.placeinfo a:active {
  color: #fff;
  text-decoration: none;
}
.placeinfo a,
.placeinfo span {
  display: block;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
.placeinfo span {
  margin: 5px 5px 0 5px;
  cursor: default;
  font-size: 13px;
}
.placeinfo .title {
  font-weight: bold;
  font-size: 14px;
  border-radius: 6px 6px 0 0;
  margin: -1px -1px 0 -1px;
  padding: 10px;
  color: #fff;
  background: #03c75a;
  background: #03c75a url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/arrow_white.png) no-repeat right 14px
    center;
}
.placeinfo .tel {
  color: #0f7833;
}
.placeinfo .jibun {
  color: #999;
  font-size: 11px;
  margin-top: 0;
}
</style>
