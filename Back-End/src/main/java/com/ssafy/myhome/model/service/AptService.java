package com.ssafy.myhome.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.myhome.dto.Apt;
import com.ssafy.myhome.dto.AptChart;
import com.ssafy.myhome.dto.AptTypeCode;
import com.ssafy.myhome.dto.HouseDeal;
import com.ssafy.myhome.dto.HouseInfo;
import com.ssafy.myhome.dto.InterestArea;
import com.ssafy.myhome.dto.MainChart;

public interface AptService {
	List<Apt> getList(AptTypeCode apttypecode) throws Exception;
	List<HouseInfo> selectHouseInfoByCode(String code)throws Exception;
	List<HouseDeal> selectHouseDealByCode(String aptcode)throws Exception;
	int insertInterestArea(InterestArea dto) throws Exception;
	List<InterestArea> selectInterestAreaByUserId(String userid) throws Exception;
	List<HouseDeal> selectHouseDealByDate(Map<String, String> map) throws Exception;
	int deleteInterestArea(InterestArea dto) throws Exception;
	List<HouseDeal> selectRecently(int qty) throws Exception;
	HouseInfo selectHouseInfoByAptCode(String aptCode)throws Exception;
		List<MainChart> selectDealCountByYear(String code) throws Exception;
	List<AptChart> selectAvgByaptcode(Map<String,String> map) throws Exception;
	
}
