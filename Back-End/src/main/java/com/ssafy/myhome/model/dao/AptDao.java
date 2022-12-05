package com.ssafy.myhome.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.myhome.dto.Apt;
import com.ssafy.myhome.dto.AptChart;
import com.ssafy.myhome.dto.HouseDeal;
import com.ssafy.myhome.dto.HouseInfo;
import com.ssafy.myhome.dto.InterestArea;
import com.ssafy.myhome.dto.MainChart;

@Mapper
public interface AptDao {
	public List<Apt> selectSido() throws Exception;
	public List<Apt> selectGugun(String sidoCode) throws Exception;
	public List<Apt> selectDong(String gugunCode) throws Exception;
	public List<HouseInfo> selectHouseInfoByCode(String code) throws Exception;;
	List<HouseDeal> selectHouseDealByCode(String aptcode)throws Exception;
	public int insertInterestArea(InterestArea dto) throws Exception;
	public List<InterestArea> selectInterestAreaByUserId(String userid) throws Exception;
	List<HouseDeal> selectHouseDealByDate(Map<String, String> map) throws Exception;
	int deleteInterestArea(InterestArea dto) throws Exception;
	List<HouseDeal> selectRecently(int qty) throws Exception;
	HouseInfo selectHouseInfoByAptCode(String aptCode)throws Exception;
	List<MainChart> selectDealCountByYear(String code) throws Exception;
	List<AptChart> selectAvgByaptcode(Map<String,String> map) throws Exception;
	
}
