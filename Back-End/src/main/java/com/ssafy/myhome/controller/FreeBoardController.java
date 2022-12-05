package com.ssafy.myhome.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ssafy.myhome.dto.Comment;
import com.ssafy.myhome.dto.FreeBoard;
import com.ssafy.myhome.dto.Pagenation;
import com.ssafy.myhome.dto.SearchParam;
import com.ssafy.myhome.dto.User;
import com.ssafy.myhome.model.service.FreeBoardService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/freeboard")
@Api("자유게시판 컨트롤러 API")
public class FreeBoardController {
	public static final int PAGE_SIZE = 10;
	public static final int BUTTON_SIZE = 5;

	@Autowired
	private FreeBoardService service;

	private String root = null;

	/**
	 * SearchScope : 검색 필터 SearchData : 검색 데이터 SearchScope와 SearchData를 받아와 검색 한
	 * 결과값을 반환한다
	 * 
	 * @param searchScope
	 * @param searchData
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/search/{searchScope}/{searchData}")
	public ResponseEntity<?> search(@PathVariable String searchScope, @PathVariable String searchData)
			throws Exception {
		List<FreeBoard> list = service.search(new SearchParam(searchData, searchScope));

		return new ResponseEntity<List<FreeBoard>>(list, HttpStatus.OK);
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/articles")
	public ModelAndView articles() throws Exception {
		return new ModelAndView("freeboard/list");
	}

	/**
	 * 
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/upload/info/{articleNo}")
	public ResponseEntity<?> uploadinfo(@PathVariable String articleNo) throws Exception {
		return new ResponseEntity<FreeBoard>(service.select(articleNo), HttpStatus.OK);
	}

	/**
	 * 
	 * @param dto
	 * @param model
	 * @return
	 * @throws Exception
	 */

	@PutMapping("/update/{articleNo}")
	public ResponseEntity<?> modify(@RequestBody FreeBoard dto, @PathVariable int articleNo) throws Exception {
		service.modify(dto);
		// model.addAttribute("dto", dto);
		return new ResponseEntity<String>("수정되었습니다", HttpStatus.OK);
	}

	/**
	 * 
	 * @param session
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/delete/{articleNo}")
	public ResponseEntity<?> delete(HttpSession session, @PathVariable String articleNo) throws Exception {

		FreeBoard dto = service.select(articleNo);
		// System.out.println(dto.getUserId());

		int article_No = Integer.parseInt(articleNo);

		System.out.println(dto.getUserId() + " " + dto.getArticleNo());

//		if (!idCheck(session, dto)) {
//			return new ResponseEntity<String>("오류발생", HttpStatus.OK);
//		}

		service.delete(article_No);
		System.out.println("삭제됨");
		return new ResponseEntity<String>("삭제되었습니다", HttpStatus.OK);
	}

	/**
	 * 
	 * @param session
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/detail/{articleNo}")
	public ResponseEntity<?> detail(HttpSession session, @PathVariable int articleNo) throws Exception {
//		User user = (User) session.getAttribute("userinfo");
		// String user_id = user.getUserId();

		FreeBoard dto;
		dto = service.detail(articleNo);
		dto.setHit(dto.getHit() + 1);
		service.modify(dto);
		return new ResponseEntity<FreeBoard>(dto, HttpStatus.OK);
	}

	/**
	 * 
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/articles/{articleNo}")
	public ResponseEntity<?> list(@PathVariable(required = false) Integer articleNo,
			@RequestParam(value = "searchParam", required = false) String searchParam,
			@RequestParam(value = "searchScope", required = false) String searchScope) throws Exception {
		Pagenation pagenation = new Pagenation();
		searchParam = "%" + searchParam + "%";
		pagenation.setSearchParam(searchParam);
		pagenation.setSearchScope(searchScope);
		SearchParam search = new SearchParam(searchParam, searchScope);
		int totalCount = service.totalCount(search);
		pagenation.setTotalCount(totalCount);
		pagenation
				.setButtonCount(totalCount % PAGE_SIZE == 0 ? (totalCount / PAGE_SIZE) : (totalCount / PAGE_SIZE + 1));

		if (articleNo == null)
			articleNo = 1;

		pagenation.setNextCheck(true); // >> 버튼 활성화
		// 시작 버튼 계산
		// 현재 페이지 -1 / 버튼표시할 갯수 * 버튼 표시할 갯수 + 1
		pagenation.setStartButton((articleNo.intValue() - 1) / BUTTON_SIZE * BUTTON_SIZE + 1);

		if (pagenation.getStartButton() + BUTTON_SIZE > pagenation.getButtonCount()) {
			pagenation.setEndButton(pagenation.getButtonCount());
		} else {
			pagenation.setEndButton(pagenation.getStartButton() + BUTTON_SIZE - 1);
		}

		if (pagenation.getStartButton() + BUTTON_SIZE - 1 >= pagenation.getButtonCount()) {
			pagenation.setNextCheck(false);
		}

		if (articleNo.intValue() - 1 < BUTTON_SIZE)
			pagenation.setPrevCheck(false);
		else
			pagenation.setPrevCheck(true);

		pagenation.setPrevClickPage((articleNo.intValue() - 1 - BUTTON_SIZE) / BUTTON_SIZE * BUTTON_SIZE + BUTTON_SIZE);
		// 5 -> 11
		// (5 + 5)/5 * 5 +1
		pagenation.setNextClickPage((articleNo.intValue() - 1 + BUTTON_SIZE) / BUTTON_SIZE * BUTTON_SIZE + 1);

		pagenation.setNowPage(articleNo.intValue());
		ModelAndView mav = new ModelAndView();
		pagenation.setStartColumn((articleNo - 1) * PAGE_SIZE);
		pagenation.setEndColumn(PAGE_SIZE);
		List<FreeBoard> list = service.list(pagenation);
		Map<String, Object> map = new HashMap<>();

		map.put("list", list);
		map.put("page", pagenation);

		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

	}

	/**
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/upload")
	public ResponseEntity<?> regist(@RequestBody FreeBoard dto) throws Exception {
		int result = service.regist(dto);
		return new ResponseEntity<FreeBoard>(dto, HttpStatus.OK);
	}

	/**
	 * 
	 * @param session
	 * @param dto
	 * @return
	 * @throws IOException
	 */
	public boolean idCheck(HttpSession session, FreeBoard dto) throws IOException {
		User user = (User) session.getAttribute("userinfo");
		String userId = dto.getUserId();
		int articleNo = dto.getArticleNo();

		// 본인이 아니면 alert창 띄움 and 관리자는 접근 가능
		if (!(user.getUserId().equals(userId) || user.getUserId().equals("admin"))) {
			return false;
		}

		return true;
	}

	@PostMapping("/comment")
	private ResponseEntity<?> cregist(@RequestBody Comment cmt) throws Exception {
		System.out.println(cmt);
		service.insertComment(cmt);
		return new ResponseEntity<FreeBoard>(service.detail(cmt.getArticleNo()), HttpStatus.OK);
	}

	@PutMapping("/comment")
	private ResponseEntity<?> cmodify(@RequestBody Comment cmt) throws Exception {
		System.out.println(cmt.toString());
		service.updateComment(cmt);
		return new ResponseEntity<FreeBoard>(service.detail(cmt.getArticleNo()), HttpStatus.OK);
	}

	@DeleteMapping("/comment/{articleNo}/{commentNo}")
	private ResponseEntity<?> cdelete(@PathVariable("commentNo") int commentNo,
			@PathVariable("articleNo") int articleNo) throws Exception {
		service.deleteComment(commentNo);
		return new ResponseEntity<FreeBoard>(service.detail(articleNo), HttpStatus.OK);
	}
}
