package com.ssafy.myhome.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagenation {
	private int startColumn;
	private int endColumn;
	private int totalCount;
	private int buttonCount;
	private int nowPage;
	private int startButton;
	private int endButton;
	private boolean prevCheck;
	private boolean nextCheck;
	private int prevClickPage;
	private int nextClickPage;
	private String searchParam;
	private String searchScope;
}
