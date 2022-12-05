package com.ssafy.myhome.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.myhome.dto.Board;
import com.ssafy.myhome.dto.FileInfoDto;
import com.ssafy.myhome.dto.FreeBoard;
import com.ssafy.myhome.dto.Pagenation;
import com.ssafy.myhome.dto.SearchParam;

@Mapper
public interface BoardDao {
	// 등록, 수정, 삭제, 조회
	int regist(Board dto) throws Exception;
	int modify(Board dto) throws Exception;
	int delete(int article_no) throws Exception;
	List list(Pagenation pagenation) throws Exception;
	Board detail(int article_no) throws Exception;
	int hit(int article_no) throws Exception;
	List<Board> search(SearchParam searchparam) throws Exception;
	void registerFile(Board dto) throws Exception;
	void deleteFile(int article_no);
	List<FileInfoDto> fileInfoList(int article_no);
	int totalCount();
	Board select(int articleNo);
	List<Board> selectRecent() throws Exception;
}
