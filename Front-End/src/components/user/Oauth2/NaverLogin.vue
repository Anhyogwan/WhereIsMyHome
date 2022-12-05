<script>
 import axios from 'axios';
// import { mapState, mapMutations } from 'vuex';
// //const userStore = "userStore";
// //import { login, findById, tokenRegeneration, logout } from "@/components/api/user";
        
        // let url =new URL(window.location.href);
        // let code = url.searchParams.get("code");
        // let state = url.searchParams.get("state");
        // axios.get(`http://localhost/user/naverlogin/${code}/${state}` )
        // .then(data => {
        //     console.dir(data);
        //     sessionStorage.setItem("access-token", data.data.userInfo.accessToken);
        //     sessionStorage.setItem("refresh-token", data.data.userInfo.refreshToken);
        //     alert(data.data.message);
        //     alert(this.user);
        //     window.location.replace("/");
        // });

import { mapState, mapActions } from 'vuex';
const userStore = "userStore";
export default {
    name:"naverLogin",
     data() {
    return {
      show1: false,
      user: {
        userId: this.$cookies.get("saveId"),
        userPassword: "",
        checkbox:this.$cookies.get('checkbox')
      },
      naverLogin: 'https://nid.naver.com/oauth2.0/authorize?response_type=code',
      rules:{},
    };
  },
  computed: {
    ...mapState(userStore,["isLogin","isLoginError","userInfo","isCookieCheck"]),
  },
  methods: {
    ...mapActions(userStore,["userConfirm","getUserInfo"]),
    async confirm() {
      await this.userConfirm(this.user);
      let token = sessionStorage.getItem("access-token");
      console.log(token);
      if(this.isLogin){
         await this.getUserInfo(token); 
        if(this.user.checkbox){
         this.$cookies.set('saveId',this.user.userId);
         this.$cookies.set('checkbox',true);
        }else{
          this.$cookies.remove('saveId');
          this.$cookies.remove("checkbox");
        }
        //this.$router.push({name:"main"});
      }
    },
  },
mounted(){
      var callbackFuc = async () =>{
        alert(this.$route.query.code);
        //네이버 로그인 인증 코드 (nodejs api)
        await axios.get(`http://192.168.120.62/user/naverlogin/${this.$route.query.code}/${this.$route.query.state}`)
        .then(data=>{
          this.$store.state.isLogin=true;
          this.$store.state.userInfo=data.userInfo;
           // this.$router.push("/");
        })
        alert("들어옴");
        //this.$router.push("/");
      }
      callbackFuc();
    },
created(){
      var callbackFuc = async () =>{
        alert(this.$route.query.code);
        //네이버 로그인 인증 코드 (nodejs api)
        await axios.get(`http://192.168.120.62/user/naverlogin/${this.$route.query.code}/${this.$route.query.state}`)
        .then(data=>{
          this.$store.state.isLogin=true;
          this.$store.state.userInfo=data.userInfo;
           // this.$router.push("/");
        })
        alert("들어옴");
        //this.$router.push("/");
      }
      callbackFuc();
},
beforeCreate() {
        var callbackFuc = async () =>{
        alert(this.$route.query.code);
        //네이버 로그인 인증 코드 (nodejs api)
        await axios.get(`http://192.168.120.62/user/naverlogin/${this.$route.query.code}/${this.$route.query.state}`)
        .then(data=>{
          this.$store.state.isLogin=true;
          this.$store.state.userInfo=data.userInfo;
           // this.$router.push("/");
        })
        alert("들어옴");
        //this.$router.push("/");
      }
      callbackFuc();
},
beforeMount() {
          var callbackFuc = async () =>{
        alert(this.$route.query.code);
        //네이버 로그인 인증 코드 (nodejs api)
        await axios.get(`http://localhost/user/naverlogin/${this.$route.query.code}/${this.$route.query.state}`)
        .then(data=>{
          this.$store.state.isLogin=true;
          this.$store.state.userInfo=data.userInfo;
           // this.$router.push("/");
        })
        alert("들어옴");
        //this.$router.push("/");
      }
      callbackFuc();
},
beforeRouteEnter () {
          var callbackFuc = async () =>{
        alert(this.$route.query.code);
        //네이버 로그인 인증 코드 (nodejs api)
        await axios.get(`http://localhost/user/naverlogin/${this.$route.query.code}/${this.$route.query.state}`)
        .then(data=>{
          this.$store.state.isLogin=true;
          this.$store.state.userInfo=data.userInfo;
           // this.$router.push("/");
        })
        alert("들어옴");
        //this.$router.push("/");
      }
      callbackFuc();
}
}
</script>
