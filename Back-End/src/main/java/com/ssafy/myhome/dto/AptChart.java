package com.ssafy.myhome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AptChart {
	private String dealAmount;
	private String dealYear;
	private String dealMonth;
}
