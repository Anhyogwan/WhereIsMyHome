package com.ssafy.myhome.ControllerAdvice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j;

@ControllerAdvice
public class CommonExceptionAdvice {
	@ExceptionHandler(Exception.class)
	public String Exception(Throwable ex,Model model){
		System.out.println("체크됐습니다");
		ex.printStackTrace();
		return "error";
	}
}
