<script>
// import axios from "axios";
// // import { mapState, mapMutations } from 'vuex';
// // //const userStore = "userStore";
// // //import { login, findById, tokenRegeneration, logout } from "@/components/api/user";
// alert("들어왔음");
// let url = new URL(window.location.href);
// let code = url.searchParams.get("code");
// let state = url.searchParams.get("state");
// axios.get(`http://localhost/user/naverlogin/${code}/${state}`).then((data) => {
//   console.dir(data);
//   sessionStorage.setItem("access-token", data.data.userInfo.accessToken);
//   sessionStorage.setItem("refresh-token", data.data.userInfo.refreshToken);
//   alert(data.data.message);
//   alert(this.user);
//   window.location.replace("/");
// });
export default {
  mounted() {
    alert("들어왓음");
  },
  created() {
    alert("들어왔음");
  },
};
</script>
