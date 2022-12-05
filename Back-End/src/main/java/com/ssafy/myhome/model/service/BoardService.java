package com.ssafy.myhome.model.service;

import java.util.List;

import com.ssafy.myhome.dto.Board;
import com.ssafy.myhome.dto.FreeBoard;
import com.ssafy.myhome.dto.Pagenation;
import com.ssafy.myhome.dto.SearchParam;

public interface BoardService {
	int regist(Board dto) throws Exception;
	int modify(Board dto) throws Exception;
	int delete(int article_no, String path) throws Exception;
	List list(Pagenation pagenation) throws Exception;
	Board detail(int article_no, int hit) throws Exception;
	List<Board> search(SearchParam searchparam) throws Exception;
	int totalCount();
	Board select(String articleNo) throws Exception;
	List<Board> selectRecent() throws Exception;
}
