<template>
  <div class="text-center" style="margin: 0 auto; width: 80%; min-width: 900px">
    <div style="margin: 20px 0px 50px 0px">
      <h2>사용자 목록</h2>
    </div>
    <div class="NoticeDiv" style="height: auto">
      <v-simple-table style="width: 100%; background-color: white; color: hsla(0, 76%, 3%, 0.6)">
        <thead>
          <tr>
            <th style="text-align: center">아아디</th>
            <th style="text-align: center">이름</th>
            <th style="text-align: center">이메일</th>
            <th style="text-align: center">가입날짜</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in users" :key="item.name">
            <td>{{ item.userId }}</td>
            <td>{{ item.userName }}</td>
            <td>{{ item.emailId }}@{{ item.emailDomain }}</td>
            <td>{{ item.joinDate }}</td>
          </tr>
        </tbody>
      </v-simple-table>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions, mapMutations } from "vuex";
const userStore = "userStore";

export default {
  name: "userList",
  data() {
    return {};
  },
  computed: {
    ...mapState(userStore, ["users"]),
  },
  created() {
    this.getUserList();
  },
  methods: {
    ...mapActions(userStore, ["getUserList"]),
    ...mapMutations(userStore, ["SET_USER_LIST"]),
  },
};
</script>

<style scope>
.NoticeDiv {
  margin: 0 auto;
  background-color: white;
  width: 100%;
  /* height: 300px; */
  font-size: 25px;
  padding: 10px;
  margin-bottom: 40px;
}

.tdClass {
  width: 50px;
  text-align: center;
}
.tdSubject {
  width: 300px;
  text-align: left;
}
a {
  text-decoration: none;
}
a:link {
  color: black;
}
a:link {
  color: black;
}
a:visited {
  color: black;
}
a:hover {
  color: black;
}
a:active {
  color: black;
}
</style>
