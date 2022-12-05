package com.ssafy.myhome.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
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

import com.ssafy.myhome.dto.KakaoLogin;
import com.ssafy.myhome.dto.NaverLogin;
import com.ssafy.myhome.dto.User;
import com.ssafy.myhome.model.service.JwtService;
import com.ssafy.myhome.model.service.OauthLoginService;
import com.ssafy.myhome.model.service.UserService;
import com.ssafy.myhome.util.Mailsend;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
@Api("유저 컨트롤러 API")
public class UserController extends HttpServlet {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService service;
	
	@Autowired
	private Mailsend smail;
	
	@Autowired
    ApplicationArguments arguments;

	@Autowired
	private OauthLoginService oauthService;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "userid", value = "아이디", required = true, dataType = "String", paramType = "path")
//		@ApiImplicitParam(name = "param1", value = "파라미터1", required = true, dataType = "String", paramType = "query"),
//		@ApiImplicitParam(name = "param2", value = "파마미터2", required = false, dataType = "int", paramType = "query") 
	})
	// 회원 목록 정보 가져오기
	@GetMapping("/users")
	public ResponseEntity<?> userList() throws Exception {
		return new ResponseEntity<List<User>>(service.selectAll(), HttpStatus.OK);
	}

	// 비밀번호 찾기 OK
	@PostMapping("/password")
	public String findpassword(@RequestBody String userid) throws Exception {
		System.out.println(userid);

		User user = service.selectUserByUserId(userid.replaceAll("\"", ""));
		if (user != null) {
			return user.getUserPassword();
		}
		return null;
	}

	// 회원 정보 수정 OK
	@ApiOperation(value = "회원정보수정", notes = "회원정보를 수정합니다.")
	@PutMapping("/update")
	public ResponseEntity<String> update(@RequestBody User user, HttpSession session) throws Exception {
		System.out.println(user.toString());
		service.updateUser(user);
		session.setAttribute("userinfo", user);
		return new ResponseEntity<String>("수정되었습니다", HttpStatus.OK);
	}

	// 회원 탈퇴 OK
	@ApiOperation(value = "회원정보삭제", notes = "회원정보를 삭제합니다.")
	@DeleteMapping("/delete/{userid}")
	public ResponseEntity<String> delete(@PathVariable String userid, HttpSession session)
			throws ServletException, IOException {
		try {
			service.deleteUser(userid);
			session.invalidate();
			return new ResponseEntity<String>("삭제되었습니다", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("에러가 발생하였습니다", HttpStatus.OK);
		}
	}

	// 회원 가입 OK
	@ApiOperation(value = "회원등록", notes = "회원의 정보를 받아 처리.")
	@PostMapping("/join")
	public ResponseEntity<String> join(@RequestBody User user, HttpSession session) throws Exception {
		service.insertUser(user);
		session.invalidate();
		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
	}

	//
	@GetMapping("/idcheck")
	public int idcheck(@RequestParam String userid) throws Exception {
		User result = service.selectUserByUserId(userid);
		return 200;
	}

	// 로그인 OK
	@PostMapping("/login")
	public ResponseEntity<?> login(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody(required = true) Map<String, String> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		User user = new User();
		System.out.println(map.values());
		System.out.println(map.toString());
		String stat = map.get("status");
		HttpStatus status = null;
		user.setUserId(map.get("userId"));
		user.setUserPassword(map.get("userPassword"));
		String saveid = map.get("checkbox");
		System.out.println("여깁니다!!" + user.getUserId() + " " + user.getUserPassword());
		System.out.println(saveid);
		User loginUser = service.selectUserByIdPW(user);
		if (loginUser != null) { // 로그인 성공(id, pwd 일치!!!!)
			String accessToken = jwtService.createAccessToken("userid", loginUser.getUserId());// key, data
			String refreshToken = jwtService.createRefreshToken("userid", loginUser.getUserId());// key, data
			service.saveRefreshToken(user.getUserId(), refreshToken);
			logger.debug("로그인 accessToken 정보 : {}", accessToken);
			logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
			resultMap.put("access-token", accessToken);
			resultMap.put("refresh-token", refreshToken);
			resultMap.put("message", "SUCCESS");
			resultMap.put("status", "wimh");
			status = HttpStatus.ACCEPTED;
		} else { // 로그인 실패(id, pwd 불일치!!!!)
			resultMap.put("message", "로그인 실패");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
	@GetMapping("/info/{userid}/{status}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable("userid") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userid,
			HttpServletRequest request, @PathVariable(value = "status", required = true) String stat) {
		logger.debug("status : {}", stat);
		logger.debug("userid : {} ", userid);
		System.out.println(request.getHeader("access-token"));
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		if (stat.equals("wimh")) {
			if (jwtService.checkToken(request.getHeader("access-token"))) {
				logger.info("사용 가능한 토큰!!!");
				try {
//				로그인 사용자 정보.
					User UserDto = service.selectUserByUserId(userid);
					System.out.println(UserDto);
					resultMap.put("userInfo", UserDto);
					resultMap.put("message", "SUCCESS");
					status = HttpStatus.ACCEPTED;
				} catch (Exception e) {
					logger.error("정보조회 실패 : {}", e);
					resultMap.put("message", e.getMessage());
					status = HttpStatus.INTERNAL_SERVER_ERROR;
				}
			} else {
				logger.error("사용 불가능 토큰!!!");
				resultMap.put("message", "FAIL");
				status = HttpStatus.UNAUTHORIZED;
			}
		} else if (stat.equals("kakao") && oauthService.kakaoTokenCheck(request.getHeader("access-token"), userid)) {
			resultMap.put("message", "SUCCESS");
			status = HttpStatus.ACCEPTED;
			logger.info("카카오 토큰 인증 성공");
		} else if (stat.equals("naver") && oauthService.naverTokenCheck(request.getHeader("access-token"))) {
			resultMap.put("message", "SUCCESS");
			status = HttpStatus.ACCEPTED;
			logger.info("네이버 토큰 인증 성공");
		} else {
			resultMap.put("message", "로그인 실패");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거한다.", response = Map.class)
	@PostMapping("/logout/{userid}/{status}")
	public ResponseEntity<?> removeToken(@PathVariable("userid") String userid, @PathVariable("status") String stat,HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String access_token=request.getHeader("access-token");
		try {
			if (stat.equals("kakao")) {
				if(oauthService.kakaoLogout(access_token,userid)) {
					service.deleRefreshToken(userid);
					resultMap.put("message", "SUCCESS");
					status = HttpStatus.ACCEPTED;
				}else {
					logger.error("카카오 로그아웃 실패 ");
					resultMap.put("message", "fail");
					status = HttpStatus.INTERNAL_SERVER_ERROR;
				}
			} else {
				service.deleRefreshToken(userid);
				resultMap.put("message", "SUCCESS");
				status = HttpStatus.ACCEPTED;
			}
		} catch (Exception e) {
			logger.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.", response = Map.class)
	@PostMapping("/refresh/{status}")
	public ResponseEntity<?> refreshToken(@RequestBody User UserDto, @PathVariable("status") String stat,
			HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refresh-token");
		logger.debug("token : {}, UserDto : {}", token, UserDto);
		if (stat.equals("wimh") && jwtService.checkToken(token)) {
			if (token.equals(service.getRefreshToken(UserDto.getUserId()))) {
				String accessToken = jwtService.createAccessToken("userid", UserDto.getUserId());
				logger.debug("token : {}", accessToken);
				logger.debug("정상적으로 액세스토큰 재발급!!!");
				resultMap.put("access-token", accessToken);
				resultMap.put("message", "SUCCESS");
				status = HttpStatus.ACCEPTED;
			}
		} else if (stat.equals("naver") && token.equals(service.getRefreshToken(UserDto.getUserId()))) {
			String accessToken = oauthService.getNaverAccessToken(token);
			resultMap.put("access-token", accessToken);
			resultMap.put("message", "SUCCESS");
			status = HttpStatus.ACCEPTED;
		} else if (stat.equals("kakao") && token.equals(service.getRefreshToken(UserDto.getUserId()))) {
			String accessToken = oauthService.getKakaoAccessToken(token);
			resultMap.put("access-token", accessToken);
			resultMap.put("message", "SUCCESS");
			status = HttpStatus.ACCEPTED;
		} else {
			logger.debug("리프레쉬토큰도 사용불!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping("/naverlogin/{code}/{state}")
	public ResponseEntity<?> naverLogin(@PathVariable("code") String code, @PathVariable("state") String state)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		logger.debug("code : {}", code);
		NaverLogin userInfo = oauthService.naverLogin(code, state);
		if (userInfo == null) {
			return new ResponseEntity<String>("fail", HttpStatus.UNAUTHORIZED);
		} else {
			service.insertUser(new User(userInfo.getEmailId(), userInfo.getUserName(), "naver", userInfo.getEmailId(),
					userInfo.getEmailDomain(), ""));
			service.saveRefreshToken(userInfo.getEmailId(), userInfo.getRefreshToken());
			resultMap.put("userInfo", userInfo);
			resultMap.put("message", "SUCCESS");
			resultMap.put("status", "naver");
			System.out.println(resultMap.values());
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
		}
	}

	@GetMapping("/kakaologin/{code}")
	public ResponseEntity<?> kakaoLogin(@PathVariable("code") String code) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		logger.debug("code : {}", code);
		KakaoLogin userInfo = oauthService.kakaoLogin(code);
		if (userInfo == null) {
			return new ResponseEntity<String>("fail", HttpStatus.UNAUTHORIZED);
		} else {
			service.insertUser(new User(String.valueOf(userInfo.getUserId()), userInfo.getUserName(), "kakao",
					userInfo.getEmailId(), userInfo.getEmailDomain(), ""));
			service.saveRefreshToken(String.valueOf(userInfo.getUserId()), userInfo.getRefreshToken());
			resultMap.put("userInfo", userInfo);
			resultMap.put("message", "SUCCESS");
			resultMap.put("status", "kakao");
			System.out.println(resultMap.values());
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
		}
	}
	
	@PostMapping("/sendmail")
	public ResponseEntity<?> sendEmail(@RequestBody Map<String,String> Email) throws Exception{
		//smail.getEmail(Email);
		String email = Email.get("Email");
		System.out.println(email);
		service.insertMailAuth(smail.naverMailSend(email));
		return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
	}

	@PostMapping("/checkmail")
	public ResponseEntity<?> checkEmail(@RequestBody Map<String,String> checkmail) throws Exception{
		if(checkmail.get("code").equals("")) return new ResponseEntity<String>("NONE",HttpStatus.OK); 
		Map<String,LocalDateTime> map = service.checkMailAuth(checkmail.get("code"));
		
		if(map!=null && Timestamp.valueOf(map.get("reg_time")).after(new Date())) {
			//service.deleteMailAuth(checkmail.get("code"));
			System.out.println("성공함");
			return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("fail",HttpStatus.UNAUTHORIZED);
		}
		 
	}
	
	@PostMapping("/findmail")
	public ResponseEntity<?> findEmail(@RequestBody Map<String,String> map) throws Exception {
		User check=service.selectUserByIdEmail(map);
		if (check!=null) {
			service.insertMailAuth(smail.naverMailSend(map.get("userEmail")));
			return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
		}else {			
			return new ResponseEntity<String>("NONE",HttpStatus.UNAUTHORIZED); 
		}	
	}
	
	@PutMapping("/updatepwd")
	public ResponseEntity<?> updatePwd(@RequestBody Map<String,String> map) throws Exception {
		if(service.updatePassword(map)==0) {
			return new ResponseEntity<String>("fail",HttpStatus.UNAUTHORIZED);
		}else{;
			return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/deletecode/{code}")
	public ResponseEntity<?> deletecode(@PathVariable("code") String code) throws Exception{
		System.out.println(code);
		if (service.deleteMailAuth(code)==0) {
			return new ResponseEntity<String>("fail",HttpStatus.UNAUTHORIZED);
		}else {
			return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
		}
	}
}