package com.ssafy.myhome.model.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.ssafy.myhome.dto.User;

public interface UserService {
	int insertUser(User user) throws Exception;
	void updateUser(User user) throws Exception;
	void deleteUser(String id) throws Exception;
	User selectUserByUserId(String userId) throws Exception;
	User selectUserByIdPW(User user) throws Exception;
	List<User> selectAll() throws Exception;
	int deleRefreshToken(String userid) throws Exception;
	public Object getRefreshToken(String userId) throws Exception;
	int saveRefreshToken(String userid, String refreshToken) throws Exception;
	int insertMailAuth(String code) throws Exception;
	Map<String,LocalDateTime> checkMailAuth(String code) throws Exception;
	int deleteMailAuth(String code) throws Exception;
	User selectUserByIdEmail(Map<String,String> map) throws Exception;
	int updatePassword(Map<String,String> map) throws Exception;
}
