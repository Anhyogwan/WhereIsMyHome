 package com.ssafy.myhome.model.dao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.myhome.dto.User;

@Mapper
public interface UserDao {
	int insertUser(User user) throws Exception;
	int updateUser(User user) throws Exception;
	int deleteUser(String id) throws Exception;
	User selectUserByIdPW(User user) throws Exception;
	User selectUserByUserId(String userId) throws Exception;
	List<User> selectAll() throws Exception;
	public int saveRefreshToken(Map<String, String> map) throws SQLException;
	public Object getRefreshToken(String userid) throws SQLException;
	public int deleteRefreshToken(Map<String, String> map) throws SQLException;
	int insertMailAuth(String code) throws Exception;
	Map<String,LocalDateTime> checkMailAuth(String code) throws Exception;
	int deleteMailAuth(String code) throws Exception;
	User selectUserByIdEmail(Map<String,String> map) throws Exception;
	int updatePassword(Map<String,String> map) throws Exception;
}
