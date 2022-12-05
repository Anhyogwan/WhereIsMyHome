package com.ssafy.myhome.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {
	private int commentNo;
	private int articleNo;
	private int commentGroupNo;
	private String content;
	private String cuserId;
	private String commentDate;
	private int position;
	private int depth;
	private int parentCommentNo;
}
