package com.ssafy.myhome.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.myhome.dto.Comment;
import com.ssafy.myhome.dto.FreeBoard;
import com.ssafy.myhome.dto.Pagenation;
import com.ssafy.myhome.dto.SearchParam;
import com.ssafy.myhome.model.dao.FreeBoardDao;

@Service
public class FreeBoardServiceImpl implements FreeBoardService {
	
	@Autowired
	private FreeBoardDao freeBoardDao;
	
	@Override
	public int regist(FreeBoard dto) throws Exception {
		return freeBoardDao.regist(dto);
	}

	@Override
	public int modify(FreeBoard dto) throws Exception {
		return freeBoardDao.modify(dto);
	}

	@Override
	public int delete(int article_no) throws Exception {
		freeBoardDao.deleteArticleComment(article_no);
		return freeBoardDao.delete(article_no);
	}

	@Override
	public List list(Pagenation pagenation) throws Exception {
		return freeBoardDao.list(pagenation);
	}

	@Override
	public FreeBoard detail(int article_no) throws Exception {
		freeBoardDao.hit(article_no);
		//if(hit == 0) return freeBoardDao.detail(article_no);
		return freeBoardDao.detail(article_no);
	}

	public List<FreeBoard> search(SearchParam searchparam) throws Exception {
		if (searchparam.getSearchScope().equals("subject")) searchparam.setSearchParam("%"+searchparam.getSearchParam() + "%");
		return freeBoardDao.search(searchparam);
	}

	@Override
	public int totalCount(SearchParam search) throws Exception {
		// TODO Auto-generated method stub
		return freeBoardDao.totalCount(search);
	}
	
	public FreeBoard select(String articleNo) throws Exception{
		int articleno = Integer.parseInt(articleNo);
		return freeBoardDao.select(articleno);
	}

	// 한번 호출에 두번 실행하는 방법?
	// 댓글을 넣기 전 ? or 넣은 후 그룹 내에서 해당 댓글의 포지션 뒤에 있는 데이터들 전부 position +1 해줘야함
	@Override
	public int insertComment(Comment cmt) throws Exception {
		System.out.println(cmt.toString());
		if (cmt.getPosition()!=0) {
			freeBoardDao.updateOrder(cmt);
		}
		return freeBoardDao.insertComment(cmt);
	}

	@Override
	public int updateComment(Comment cmt) throws Exception {
		return freeBoardDao.updateComment(cmt);
	}

	//parentCommentNo로 해당 CommentNo를 가지는 댓글이 있을 경우 (삭제된 댓글입니다)로 content 내용 을 전환
	//만약 해당이 안된다면 삭제할 댓글 뒤의 포지션을 전부 -1 해주고 댓글을 delete
	@Override
	public int deleteComment(int commentNo) throws Exception {
		System.out.println(freeBoardDao.haveChild(commentNo));
		if (freeBoardDao.haveChild(commentNo)!=null ) {
			Comment cmt= new Comment();
			cmt.setCommentNo(commentNo);
			cmt.setContent("삭제된 댓글입니다");
			freeBoardDao.updateComment(cmt);
			return 1;
		}else {
			freeBoardDao.deleteOrder(commentNo);
			return freeBoardDao.deleteComment(commentNo);
		}
		
	}
}
