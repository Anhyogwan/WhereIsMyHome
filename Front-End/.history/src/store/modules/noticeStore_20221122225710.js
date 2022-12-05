import {
  articleList,
  articleDetail,
  deleteArticle,
  insertnotice,
  searchList,
  modifyArticle,
  getRecentArticle,
} from "@/components/api/notice";
import router from "@/router";

const noticeStore = {
  namespaced: true,
  state: {
    // list
    articles: [],
    pages: [],
    page: 1,
    files: [],
    //view
    article: [],
    recents: [],
  },
  getters: { article: (state) => state.article },
  mutations: {
    SET_RECENTS_LIST(state, recent) {
      state.recents = recent;
    },
    SET_ARTICLE_LIST(state, articles) {
      state.articles = articles.list;
      state.pages = articles.page;
    },
    SET_ARTICLE_DETAIL(state, article) {
      state.article = article;
    },
    SET_FILES(state, files) {
      state.files = files;
    },
  },
  actions: {
    getArticleList: async ({ commit }, page) => {
      await articleList(
        page,
        ({ data }) => {
          commit("SET_ARTICLE_LIST", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },

    async searchArticleList({ commit }, param) {
      await searchList(
        param,
        ({ data }) => {
          commit("SET_ARTICLE_LIST", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },

    getArticleDetail: async ({ commit }, articleNo) => {
      await articleDetail(
        articleNo,
        ({ data }) => {
          // console.log(data);
          commit("SET_ARTICLE_DETAIL", data);
          let tempfiles = [];
          if (data.fileInfos != null) {
            data.fileInfos.forEach((e) => {
              let fileinfo = {
                name: e.originalFile,
                sfolder: e.saveFolder,
                sfile: e.saveFile,
                ofile: e.originalFile,
                downurl: `http://localhost/board/download?sfolder=${e.saveFolder}&ofile=${e.originalFile}&sfile=${e.saveFile}`,
              };
              tempfiles.push(fileinfo);
            });
            commit("SET_FILES", tempfiles);
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },

    async delArticle({ commit }, articleNo) {
      await deleteArticle(articleNo, ({ data }) => {
        console.log(data);
        router.push("/notice");
      });
      console.log(commit);
    },

    async instNotice({ commit }, data) {
      // let article = {
      //   subject: data.article.subject,
      //   content: data.article.content,
      //   userId: data.article.userId,
      // };
      const formData = new FormData();

      if (data.files != null) {
        for (let i = 0; i < data.files.length; i++) {
          formData.append("upfile", data.files[i]);
          console.log(data.files[i]);
        }
      }
      formData.append("subject", data.article.subject);
      formData.append("content", data.article.content);
      formData.append("userId", data.article.userId);
      await insertnotice(formData, ({ data }) => {
        console.log(data);
        router.push("/notice");
      });
      console.log(commit);
    },
    // instNotice({ commit }, data) {
    //   let article = { subject: data.subject, content: data.content, userId: data.userId };
    //   insertnotice(article);
    //   console.log(commit);
    // },

    async updateArticle({ commit }, data) {
      const formData = new FormData();
      console.log(123);
      console.log(data.files);
      if (data.files != null) {
        for (let i = 0; i < data.files.length; i++) {
          formData.append("upfile", data.files[i]);
          console.log(data.files[i]);
        }
      }
      formData.append("subject", data.article.subject);
      formData.append("content", data.article.content);
      formData.append("userId", data.article.userId);
      await modifyArticle({ articleNo: data.article.articleNo, formdata: formData }, ({ data }) => {
        console.log(data);
        router.push("/notice");
      });
      console.log(commit);
    },
    async getRecNotice({ commit }) {
      await getRecentArticle(({ data }) => {
        commit("SET_RECENTS_LIST", [...data]);
        return true;
      }),
        (error) => {
          console.log(error);
          return false;
        };
    },
  },
};

export default noticeStore;
