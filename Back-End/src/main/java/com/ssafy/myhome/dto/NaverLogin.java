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
public class NaverLogin {
	private String accessToken;
	private String refreshToken;
	private String profileImg;
	private String userName;
	private String userId;
	private String emailId;
	private String emailDomain;
	
}
