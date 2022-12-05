package com.ssafy.myhome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
	 private String userId; 
	 private String userName;
	 private String userPassword;
	 private String emailId;
	 private String emailDomain;
	 private String joinDate;
	 
}
