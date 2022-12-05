<template>
  <div class="NoticeDiv">
    <ul class="list_notice">
      <li>
        <a href="https://devtalk.kakao.com/t/topic/125592" target="_blank" rel="noreferrer" class="link_notice"
          ><span class="txt_notice">[공지] 서비스 장애 및 정상화 안내 </span><span class="txt_date">2022.10.24</span></a
        >
      </li>
      <li>
        <a href="https://devtalk.kakao.com/t/topic/125543" target="_blank" rel="noreferrer" class="link_notice"
          ><span class="txt_notice">[공지] 2022/10/15 장애 공유 </span><span class="txt_date">2022.10.24</span></a
        >
      </li>
      <li>
        <a href="https://devtalk.kakao.com/t/topic/125629" target="_blank" rel="noreferrer" class="link_notice"
          ><span class="txt_notice">[공지] JavaScript SDK v1 다운로드 링크 변경 </span
          ><span class="txt_date">2022.10.19</span></a
        >
      </li>
    </ul>
  </div>
</template>

<script>
export default {};
</script>

<style>
.NoticeDiv {
  background-color: gray;
  width: 100%;
  /* height: 300px; */
  font-size: 25px;
  padding: 10px;
  margin-bottom: 40px;
}

.NoticeMargin {
  margin-bottom: 10px !important;
}

.list_notice .txt_notice {
  position: relative;
  display: table-cell;
  padding: 5px 0 0 5px;
  font-size: 18px;
  line-height: 32px;
  color: hsla(0, 76%, 3%, 0.6);
  font-family: NotoSans Medium, Malgun Gothic, 맑은 고딕, Apple SD Gothic Neo, 돋움, dotum, sans-serif;
  letter-spacing: -0.2px;
  vertical-align: middle;
}

.link_notice .txt_date {
  display: table-cell;
  padding: 5px 0 0 32px;
  font-size: 14px;
  line-height: 24px;
  color: hsla(0, 76%, 3%, 0.6);
  letter-spacing: -0.2px;
  vertical-align: middle;
}

.link_notice {
  text-decoration: none;
}

.list_notice > li {
  list-style: none;
  padding-left: 0px;
}
