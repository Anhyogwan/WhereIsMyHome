package com.ssafy.myhome.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ssafy.myhome.dto.Board;
import com.ssafy.myhome.dto.FileInfoDto;
import com.ssafy.myhome.dto.Pagenation;
import com.ssafy.myhome.dto.SearchParam;
import com.ssafy.myhome.dto.User;
import com.ssafy.myhome.model.service.BoardService;
import com.ssafy.myhome.model.service.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/board")
@Api("공지사항 컨트롤러 API")
public class BoardController {
	public static final int PAGE_SIZE = 5;
	public static final int BUTTON_SIZE = 5;
	

	
	@Autowired
	private ServletContext servletContext;

	@Autowired
	private BoardService service;

	private String root = null;
 
	/**
	 * 글 번호를 받아와서 해당하는 글 데이터를 반환한다
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "게시글 등록", notes = "게시글을 등록해줍니다")
	@ApiResponses({ @ApiResponse(code = 200, message = "회원목록 OK!!"), @ApiResponse(code = 404, message = "페이지없어!!"),
			@ApiResponse(code = 500, message = "서버에러!!") })
	@GetMapping("/upload/info/{articleNo}")
	public ResponseEntity<?> uploadinfo(@PathVariable String articleNo) throws Exception {
		return new ResponseEntity<Board>(service.select(articleNo), HttpStatus.OK);
	}

	/**
	 * SearchScope : 검색 필터
	 * SearchData : 검색 데이터
	 * SearchScope,SearchData를 받아와서 검색 목록을 반환한다
	 * @param searchScope
	 * @param searchData
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/search/{searchScope}/{searchData}")
	public ResponseEntity<?> search(@PathVariable String searchScope, @PathVariable String searchData)
			throws Exception {
		List<Board> list = service.search(new SearchParam(searchData, searchScope));
		return new ResponseEntity<List<Board>>(list, HttpStatus.OK);
	}

	/**
	 * 글 번호와 수정할 데이터를 받아와서 번호에 맞는 데이터를 수정한다
	 * @param dto
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/upload/{articleNo}")
	public ResponseEntity<String> modify(@Value("${file.path}") String filepath,@RequestParam(value="upfile", required=false) MultipartFile[] upfile,Board dto, Model model) throws Exception {
		
		System.out.println(upfile);
//		FileUpload 관련 설정.
		if (upfile != null) {
			System.out.println(12312);
//			String realPath = servletContext.getRealPath("/upload");
//			String realPath = servletContext.getRealPath("/resources/img");
			String today = new SimpleDateFormat("yyMMdd").format(new Date());
			String saveFolder = filepath + File.separator + today;
			File folder = new File(saveFolder);
			if (!folder.exists())
				folder.mkdirs();
			List<FileInfoDto> fileInfos = new ArrayList<FileInfoDto>();
			for (MultipartFile mfile : upfile) {
				FileInfoDto fileInfoDto = new FileInfoDto();
				String originalFileName = mfile.getOriginalFilename();
				if (!originalFileName.isEmpty()) {
					String saveFileName = UUID.randomUUID().toString()
							+ originalFileName.substring(originalFileName.lastIndexOf('.'));
					fileInfoDto.setSaveFolder(today);
					fileInfoDto.setOriginalFile(originalFileName);
					fileInfoDto.setSaveFile(saveFileName);
//				logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", mfile.getOriginalFilename(), saveFileName);
					mfile.transferTo(new File(folder, saveFileName));
				}
				fileInfos.add(fileInfoDto);
			}
			dto.setFileInfos(fileInfos);
		}
		service.modify(dto);
		model.addAttribute("dto", dto);
		return new ResponseEntity<String>("수정되었습니다", HttpStatus.OK);
	}

	/**
	 * 글 번호를 받아와서 글 번호에 해당하는 데이터를 삭제한다
	 * @param session
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/delete/{articleNo}")
	public ResponseEntity<String> delete(HttpSession session, @PathVariable String articleNo) throws Exception {

		Board dto = service.select(articleNo);
		System.out.println(dto.getUserId());

		int article_No = Integer.parseInt(articleNo);

		System.out.println(dto.getUserId() + " " + dto.getArticleNo());

//		if (!adminCheck(session)) {
//			return new ResponseEntity<String>("오류발생", HttpStatus.OK);
//		}
		service.delete(article_No, servletContext.getRealPath("/upload"));
		System.out.println("삭제됨");
		return new ResponseEntity<String>("삭제되었습니다", HttpStatus.OK);
	}

	/**
	 * 글 번호와 로그인된 세션값을 받아와 로그인 인증 후에  번호에 맞는 글 정보을 반환한다.
	 * @param articleNo
	 * @param session
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@GetMapping("/detail/{articleNo}")
	public ResponseEntity<Board> detail(@PathVariable String articleNo, HttpSession session)
			throws NumberFormatException, Exception {
		int article_No = Integer.parseInt(articleNo);
//		User user = (User) session.getAttribute("userinfo");
//		String user_id = user.getUserId();
		Board dto = service.detail(article_No, 1);
		return new ResponseEntity<Board>(dto, HttpStatus.OK);
	}

	/**
	 * 페이지 번호를 받아와 페이지에 해당하는 글 리스트와,페이지 정보로 구성된 Map을 반환한다.
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/articles/{articleNo}")
	public ResponseEntity<Map<String, Object>> list(@PathVariable(required = false) Integer articleNo)
			throws Exception {
		ModelAndView mav = new ModelAndView();
		Pagenation pagenation = new Pagenation();
		int totalCount = service.totalCount();
		pagenation.setTotalCount(service.totalCount());
		pagenation.setButtonCount(totalCount%PAGE_SIZE == 0 ? (totalCount/PAGE_SIZE) : (totalCount / PAGE_SIZE   + 1));

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
		pagenation.setStartColumn((articleNo - 1) * PAGE_SIZE);
		pagenation.setEndColumn(PAGE_SIZE);
		List<Board> list = service.list(pagenation);
		Map<String, Object> map = new HashMap<>();

		map.put("list", list);
		map.put("page", pagenation);

		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	/**
	 * 글 정보(Board, 파일 정보 -> 선택)를 받아와 글을 등록한다
	 * @param filepath
	 * @param dto
	 * @param upfile
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/regist")
	public ResponseEntity<String> regist(@Value("${file.path}") String filepath,  Board dto,  @RequestParam(value="upfile",required=false) MultipartFile[] upfile)
			throws Exception {

		System.out.println(dto);

//		FileUpload 관련 설정.
		if (upfile != null) {
			System.out.println(12312);
//			String realPath = servletContext.getRealPath("/upload");
//			String realPath = servletContext.getRealPath("/resources/img");
			String today = new SimpleDateFormat("yyMMdd").format(new Date());
			String saveFolder = filepath + File.separator + today;
			File folder = new File(saveFolder);
			if (!folder.exists())
				folder.mkdirs();
			List<FileInfoDto> fileInfos = new ArrayList<FileInfoDto>();
			for (MultipartFile mfile : upfile) {
				FileInfoDto fileInfoDto = new FileInfoDto();
				String originalFileName = mfile.getOriginalFilename();
				if (!originalFileName.isEmpty()) {
					String saveFileName = UUID.randomUUID().toString()
							+ originalFileName.substring(originalFileName.lastIndexOf('.'));
					fileInfoDto.setSaveFolder(today);
					fileInfoDto.setOriginalFile(originalFileName);
					fileInfoDto.setSaveFile(saveFileName);
//				logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", mfile.getOriginalFilename(), saveFileName);
					mfile.transferTo(new File(folder, saveFileName));
				}
				fileInfos.add(fileInfoDto);
			}
			dto.setFileInfos(fileInfos);
		}
		int result = service.regist(dto);

		// 공지 등록 후 리스트로 이동

		return new ResponseEntity<String>("등록되었습니다", HttpStatus.OK);
	}

	/**
	 * 
	 * @param session
	 * @return
	 * @throws IOException
	 */
	public boolean adminCheck(HttpSession session) throws IOException {
		User user = (User) session.getAttribute("userinfo");
		if (user == null || !(user.getUserId().equals("admin"))) {
			return false;
		}
		return true;
	}

	public boolean loginCheck(HttpSession session) throws IOException {
		User user = (User) session.getAttribute("userinfo");

		// 로그인 안하고 상세 내용 클릭시 alert창 띄움
		if (user == null) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param path
	 * @param sfolder
	 * @param ofile
	 * @param sfile
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@GetMapping(value = "/download")
	public ResponseEntity<Object> downloadFile(@Value("${file.path}") String path, @RequestParam String sfolder,
			@RequestParam String ofile, @RequestParam String sfile, HttpSession session) throws IOException {

//		User user = (User) session.getAttribute("userinfo");
		User user = new User();
		if (user != null) {
			try {
				path += (sfolder + "/" + sfile);
				System.out.println(sfolder);
				System.out.println(ofile);
				System.out.println(sfile);
				System.out.println(path);
				Path filePath = Paths.get(path);
				Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

				File file = new File(path);
				String fileName;
				fileName = new String(ofile.getBytes("UTF-8"), "ISO-8859-1");

				HttpHeaders headers = new HttpHeaders();
				headers.setContentDisposition(ContentDisposition.builder("attachment").filename(fileName).build());

				return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
			} catch (Exception e) {
				System.out.println("에러 남");
				e.printStackTrace();
				return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
			}
		}
		System.out.println("에러 남");
		return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
	}

	@GetMapping("/recent")
	public ResponseEntity<?> selectRecent() throws Exception{
		return new ResponseEntity<List<Board>>(service.selectRecent(),HttpStatus.OK);
	}
}
