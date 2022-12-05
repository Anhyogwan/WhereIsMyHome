package com.ssafy.myhome.model.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.myhome.dto.User;
import com.ssafy.myhome.model.dao.UserDao;

@Service
public class UserServiceImpl implements UserService{
	private static UserService instance = new UserServiceImpl();
	
	@Autowired
	private UserDao dao;
	
	private UserServiceImpl() {
		//dao = UserDaoImpl.getInstance();
	}
	
	public static UserService getInstance() {
		return instance;
	}

	@Override
	public int insertUser(User user) throws Exception {
		return dao.insertUser(user);
	}

	@Override
	public void updateUser(User user) throws Exception {
		dao.updateUser(user);
	}

	@Override
	public void deleteUser(String id) throws Exception {
		dao.deleteUser(id);
	}

	@Override
	public User selectUserByUserId(String userId) throws Exception {
		return dao.selectUserByUserId(userId);
	}

	@Override
	public User selectUserByIdPW(User user) throws Exception {
		return dao.selectUserByIdPW(user);
	}

	@Override
	public List<User> selectAll() throws Exception {
		return dao.selectAll();
	}

	
	@Override
	public int deleRefreshToken(String userid) throws Exception {
		Map<String,String> map = new HashMap<>();
		map.put("userid",userid);
		map.put("token",null);
		return dao.deleteRefreshToken(map);
	}

	@Override
	public Object getRefreshToken(String userId) throws Exception {
		return dao.getRefreshToken(userId);
	}	
	
	@Override
	public int saveRefreshToken(String userid, String refreshToken) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", refreshToken);
		return dao.saveRefreshToken(map);
	}

	@Override
	public int insertMailAuth(String code) throws Exception {
		return dao.insertMailAuth(code);
	}

	@Override
	public Map<String,LocalDateTime> checkMailAuth(String code) throws Exception {
		return dao.checkMailAuth(code);
	}

	@Override
	public int deleteMailAuth(String code) throws Exception {
		return dao.deleteMailAuth(code);
	}

	@Override
	public User selectUserByIdEmail(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectUserByIdEmail(map);
	}

	@Override
	public int updatePassword(Map<String, String> map) throws Exception {
		return dao.updatePassword(map);
	}
	
	
}
