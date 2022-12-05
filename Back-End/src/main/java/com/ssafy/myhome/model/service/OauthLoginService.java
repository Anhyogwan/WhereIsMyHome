package com.ssafy.myhome.model.service;

import com.ssafy.myhome.dto.KakaoLogin;
import com.ssafy.myhome.dto.NaverLogin;

public interface OauthLoginService {
	public KakaoLogin kakaoLogin(String code);
	public NaverLogin naverLogin(String code,String state);
	public boolean kakaoTokenCheck(String header, String userid);
	public boolean naverTokenCheck(String header);
	public String getKakaoAccessToken(String token);
	public String getNaverAccessToken(String token);
	public boolean kakaoLogout(String access_token,String userid);
}
