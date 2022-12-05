package com.ssafy.myhome.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.myhome.dto.Comment;
import com.ssafy.myhome.dto.FreeBoard;
import com.ssafy.myhome.dto.Pagenation;
import com.ssafy.myhome.dto.SearchParam;

@Mapper
public interface FreeBoardDao {
	// 등록, 수정, 삭제, 조회
	int regist(FreeBoard dto) throws Exception;
	int modify(FreeBoard dto) throws Exception;
	int delete(int article_no) throws Exception;
	List list(Pagenation pagenation) throws Exception;
	FreeBoard detail(int article_no) throws Exception;
	int hit(int article_no) throws Exception;
	List<FreeBoard> search(SearchParam searchparam) throws Exception;
	int totalCount(SearchParam search) throws Exception;
	FreeBoard select(int articleNo) throws Exception;
	int insertComment(Comment cmt) throws Exception;
	int updateComment(Comment cmt) throws Exception;
	int updateOrder(Comment cmt) throws Exception;
	int deleteComment(int commentNo) throws Exception;
	int deleteOrder(int commentNo) throws Exception;
	Comment haveChild(int commentNo) throws Exception;
	int deleteArticleComment(int article_no) throws Exception;
}
