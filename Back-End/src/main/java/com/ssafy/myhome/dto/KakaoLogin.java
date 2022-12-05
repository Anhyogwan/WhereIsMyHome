package com.ssafy.myhome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KakaoLogin {
	private String accessToken;
	private String refreshToken;
	private String userName;
	private long userId;
	private String emailId;
	private String emailDomain;
	private String profileImg;
	
}
