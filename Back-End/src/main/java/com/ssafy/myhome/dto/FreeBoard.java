package com.ssafy.myhome.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FreeBoard {
	private int articleNo;
	private String userId;
	private String subject;
	private String content;
	private int hit;
	private String registerTime;
	private List<Comment> comments;
}
