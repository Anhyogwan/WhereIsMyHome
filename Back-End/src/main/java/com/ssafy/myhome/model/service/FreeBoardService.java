package com.ssafy.myhome.model.service;

import java.util.List;

import com.ssafy.myhome.dto.Comment;
import com.ssafy.myhome.dto.FreeBoard;
import com.ssafy.myhome.dto.Pagenation;
import com.ssafy.myhome.dto.SearchParam;

public interface FreeBoardService {
	int regist(FreeBoard dto) throws Exception;
	int modify(FreeBoard dto) throws Exception;
	int delete(int article_no) throws Exception;
	List list(Pagenation pagenation) throws Exception;
	FreeBoard detail(int article_no) throws Exception;
	List<FreeBoard> search(SearchParam searchparam) throws Exception;
	int totalCount(SearchParam search) throws Exception;
	FreeBoard select(String articleNo) throws Exception;
	int insertComment(Comment cmt) throws Exception;
	int updateComment(Comment cmt) throws Exception;
	int deleteComment(int commentNo) throws Exception;
}
