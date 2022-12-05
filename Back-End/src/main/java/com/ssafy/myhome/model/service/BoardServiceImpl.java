package com.ssafy.myhome.model.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.myhome.dto.Board;
import com.ssafy.myhome.dto.FileInfoDto;
import com.ssafy.myhome.dto.FreeBoard;
import com.ssafy.myhome.dto.Pagenation;
import com.ssafy.myhome.dto.SearchParam;
import com.ssafy.myhome.model.dao.BoardDao;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDao boardDao;
	
	@Override
	@Transactional
	public int regist(Board dto) throws Exception {
		boardDao.regist(dto);
		List<FileInfoDto> fileinfos = dto.getFileInfos();
		if(fileinfos != null && !fileinfos.isEmpty()) {
			boardDao.registerFile(dto);
		}
		
		return 1;
	}

	@Override
	public int modify(Board dto) throws Exception {
		
		int article_no = dto.getArticleNo();
		boardDao.deleteFile(article_no);
		
		List<FileInfoDto> fileinfos = dto.getFileInfos();
		if(fileinfos != null && !fileinfos.isEmpty()) {
			boardDao.registerFile(dto);
		}
		return boardDao.modify(dto);
	}

	@Override
	@Transactional
	public int delete(int article_no, String path) throws Exception {
		List<FileInfoDto> fileList = boardDao.fileInfoList(article_no);
		boardDao.deleteFile(article_no);
		boardDao.delete(article_no);
		for(FileInfoDto fileInfoDto : fileList) {
			File file = new File(path + File.separator + fileInfoDto.getSaveFolder() + File.separator + fileInfoDto.getSaveFile());
			file.delete();
		}
		
		
		return boardDao.delete(article_no);
	}

	@Override
	public List list(Pagenation pagenation) throws Exception {
		return boardDao.list(pagenation);
	}

	@Override
	public Board detail(int article_no, int hit) throws Exception {
		if(hit == 0) return boardDao.detail(article_no);
		boardDao.hit(article_no);
		return boardDao.detail(article_no);
	}

	public List<Board> search(SearchParam searchparam) throws Exception {
		if (searchparam.getSearchScope().equals("subject")) searchparam.setSearchParam("%"+searchparam.getSearchParam() + "%");
		return boardDao.search(searchparam);
	}

	@Override
	public int totalCount() {
		return boardDao.totalCount();
	}
	
	public Board select(String articleNo) throws Exception{
		int articleno = Integer.parseInt(articleNo);
		return boardDao.select(articleno);
	}

	@Override
	public List<Board> selectRecent() throws Exception {
		// TODO Auto-generated method stub
		return boardDao.selectRecent();
	}
	
}
