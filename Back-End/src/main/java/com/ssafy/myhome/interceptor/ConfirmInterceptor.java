package com.ssafy.myhome.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.myhome.dto.User;

//@SuppressWarnings("deprecation")
//public class ConfirmInterceptor extends HandlerInterceptorAdapter {
// spring 5.3 부터는 HandlerInterceptor implements
@Component
public class ConfirmInterceptor implements HandlerInterceptor { 

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("userinfo");
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index");
			return false;
		}
		return true;
	}
	
}
