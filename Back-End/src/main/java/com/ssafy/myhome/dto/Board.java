package com.ssafy.myhome.dto;

import java.util.List;
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
public class Board {
	private int articleNo;
	private String userId;
	private String subject;
	private String content;
	private int hit;
	private String registerTime;
	private List<FileInfoDto> fileInfos;
	
}
