package com.ssafy.myhome.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.myhome.dto.Apt;
import com.ssafy.myhome.dto.AptChart;
import com.ssafy.myhome.dto.AptTypeCode;
import com.ssafy.myhome.dto.HouseDeal;
import com.ssafy.myhome.dto.HouseInfo;
import com.ssafy.myhome.dto.InterestArea;
import com.ssafy.myhome.dto.MainChart;
import com.ssafy.myhome.model.dao.AptDao;


@Service
public class AptServiceImpl implements AptService {
	@Autowired
	private AptDao dao;
	
	
	@Override
	public List<Apt> getList(AptTypeCode apttypecode) throws Exception {
		switch (apttypecode.getType()) {
		case "sido":
			return dao.selectSido();
		case "gugun":
			return dao.selectGugun(apttypecode.getCode());
		case "dong":
			return dao.selectDong(apttypecode.getCode());
		}
		return null;
	}

	@Override
	public List<HouseInfo> selectHouseInfoByCode(String code) throws Exception{
		return dao.selectHouseInfoByCode(code);
	}

	@Override
	public List<HouseDeal> selectHouseDealByCode(String aptcode) throws Exception {
		return dao.selectHouseDealByCode(aptcode);
	}

	@Override
	public int insertInterestArea(InterestArea dto) throws Exception {
		return dao.insertInterestArea(dto);
	}

	@Override
	public List<InterestArea> selectInterestAreaByUserId(String userid) throws Exception {
		return dao.selectInterestAreaByUserId(userid);
	}

	@Override
	public List<HouseDeal> selectHouseDealByDate(Map<String, String> map) throws Exception {
		
		return dao.selectHouseDealByDate(map);
	}

	@Override
	public int deleteInterestArea(InterestArea dto) throws Exception {
		return dao.deleteInterestArea(dto);
	}

	@Override
	public List<HouseDeal> selectRecently(int qty) throws Exception {
		return dao.selectRecently(qty);
	}

	@Override
	public HouseInfo selectHouseInfoByAptCode(String aptCode) throws Exception {
		return dao.selectHouseInfoByAptCode(aptCode);
	}
	public List<MainChart> selectDealCountByYear(String code) throws Exception{
		return dao.selectDealCountByYear(code);
	}

	@Override
	public List<AptChart> selectAvgByaptcode(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectAvgByaptcode(map);
	}
}
