package com.ssafy.myhome.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class HouseDeal {
	private long no;
	private String dealAmount;
	private int dealYear;
	private int dealMonth;
	private int dealDay;
	private String area;
	private String floor;
	private String cancelDealType;
	private long aptCode;
	private String lng;
	private String lat;
	private String apartmentName;
	
}
