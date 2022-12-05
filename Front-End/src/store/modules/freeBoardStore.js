import {
  articleList,
  articleDetail,
  newComment,
  newreComment,
  modifyCommnet,
  deleteComment,
  deleteArticle,
  insertFreeBoard,
  searchList,
  modifyArticle,
} from "@/components/api/freeBoard";

const freeBoardStore = {
  namespaced: true,
  state: {
    // list
    articles: [],
    pages: [],
    comments: [],
    page: 1,

    //view
    article: [],
    isShow: [],
    currentBox: "",
    isButton: true,
  },
  getters: { article: (state) => state.article },
  mutations: {
    SET_ARTICLE_LIST(state, articles) {
      state.articles = articles.list;
      state.pages = articles.page;
    },
    SET_ARTICLE_DETAIL(state, article) {
      state.article = article;
      state.article.comments.forEach((e) => {
        state.isShow[e.commentNo] = false;
      });
    },
    SET_NEW_COMMENT(state, article) {
      state.article = article;
    },
    SET_NEW_RECOMMENT(state, article) {
      state.article = article.data;
      // state.recomment = "";
      state.isShow[article.comment.commentNo] = false;
    },
    SET_RECOMMENT_BOX(state, commentNo) {
      state.isShow[state.currentBox] = false;
      state.isShow[commentNo] = true;
      state.currentBox = commentNo;
      state.isShow = [...state.isShow];
      state.isButton = true;
    },
    SET_UPDATE_NEW_COMMENT(state, article) {
      state.isShow[state.currentBox] = false;
      state.currentBox = "";
      state.article = article;
    },
    SET_CHANGE_COMMENT_BOX(state) {
      state.isButton = false;
    },
    DEL_COMMENT(state, article) {
      state.article = article;
    },
  },
  actions: {
    getArticleList: ({ commit }, page) => {
      articleList(
        page,
        ({ data }) => {
          commit("SET_ARTICLE_LIST", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },

    searchArticleList({ commit }, param) {
      searchList(
        param,
        ({ data }) => {
          commit("SET_ARTICLE_LIST", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },

    getArticleDetail: ({ commit }, articleNo) => {
      articleDetail(
        articleNo,
        ({ data }) => {
          commit("SET_ARTICLE_DETAIL", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },

    insertNewComment(context, Info) {
      newComment(
        {
          articleNo: context.state.article.articleNo,
          commentGroupNo:
            context.state.article.comments.length == 0
              ? 1
              : context.state.article.comments[context.state.article.comments.length - 1]
                  .commentGroupNo + 1,
          content: Info.comment,
          cuserId: Info.userId,
          commentNo: 0,
          position: 0,
          depth: 0,
          parentCommentNo: 0,
        },
        ({ data }) => {
          context.commit("SET_NEW_COMMENT", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },

    insertNewreComment(context, comments) {
      let comment = comments.comment;
      newreComment(
        {
          articleNo: context.state.article.articleNo,
          commentGroupNo: comments.comment.commentGroupNo,
          content: comments.recomment,
          cuserId: comments.userId,
          commentNo: comments.comment.parentCommentNo,
          position: comments.comment.position,
          depth: comments.comment.depth + 1,
          parentCommentNo: comments.comment.commentNo,
        },
        ({ data }) => {
          context.commit("SET_NEW_RECOMMENT", { data, comment });
        }
      );
    },

    reCommentBox({ commit }, commentNo) {
      commit("SET_RECOMMENT_BOX", commentNo);
    },

    updateNewComment({ commit }, comment) {
      modifyCommnet(
        {
          articleNo: comment.articleNo,
          content: comment.content,
          commentNo: comment.commentNo,
        },
        ({ data }) => {
          commit("SET_UPDATE_NEW_COMMENT", data);
        }
      );
    },
    changeCommentBox({ commit }) {
      commit("SET_CHANGE_COMMENT_BOX");
    },
    delComment: ({ commit }, comment) => {
      deleteComment(comment);
      articleDetail(
        comment.articleNo,
        ({ data }) => {
          commit("DEL_COMMENT", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },

   async delArticle({ commit }, articleNo) {
      deleteArticle(articleNo);
      console.log(commit);
    },

    instFreeBoard({ commit }, data) {
      insertFreeBoard(data);
      console.log(commit);
    },

    updateArticle({ commit }, article) {
      modifyArticle(article);
      console.log(commit);
    },
  },
};

export default freeBoardStore;
