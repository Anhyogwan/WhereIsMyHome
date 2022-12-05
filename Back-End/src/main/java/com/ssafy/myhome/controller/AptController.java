package com.ssafy.myhome.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.myhome.dto.Apt;
import com.ssafy.myhome.dto.AptChart;
import com.ssafy.myhome.dto.AptTypeCode;
import com.ssafy.myhome.dto.HouseDeal;
import com.ssafy.myhome.dto.HouseInfo;
import com.ssafy.myhome.dto.InterestArea;
import com.ssafy.myhome.dto.MainChart;
import com.ssafy.myhome.model.service.AptService;
import com.ssafy.myhome.model.service.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/apt")
@Api("아파트 컨트롤러 API")
public class AptController {
	
	@Autowired
	private AptService service;
	
	/**
	 * type과 code의 데이터를 받아 데이터에 맞는 시,군,구를 반환
	 * type= sido (시,도)
	 * 		 gugun(구,군)
	 * 		 dong(동)
	 * @param type
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "아파트 시군구동 정보", notes = "아파트의 시,군,구,동 목록을 반환해줌")
	@GetMapping(value = {"/search/{type}/{code}","/search/{type}"})
	public ResponseEntity<List<Apt>> search(@PathVariable String type,@PathVariable(required = false) String code) throws Exception {
		return new ResponseEntity<List<Apt>> (service.getList(new AptTypeCode(type,code)),HttpStatus.OK);
	}

	/**
	 * 지역 코드를 받아 아파트의 상세 정보를 반환한다.
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "아파트 정보", notes = "아파트 정보를 불러온다")
	@GetMapping("/houseInfoList/{code}")
	public ResponseEntity<List<HouseInfo>> houseInfoList(@PathVariable String code) throws Exception {
		return new ResponseEntity<List<HouseInfo>>(service.selectHouseInfoByCode(code),HttpStatus.OK);
	}
	
	/**
	 * 아파트 코드를 받아 아파트 실거래 상세 정보를 반환한다.
	 * @param aptcode
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "아파트 거래 정보", notes = "아파트 거래정보를 출력한다")
	@GetMapping("/houseDealList/{aptCode}")
	public ResponseEntity<List<HouseDeal>> houseDealList(@PathVariable(value= "aptCode") String aptcode)throws Exception{
		return new ResponseEntity<List<HouseDeal>> (service.selectHouseDealByCode(aptcode),HttpStatus.OK);
	}
	
	/**
	 * 아파트 코드와 기간을 받아 아파트 실거래 상세 정보를 반환한다.
	 * @param aptcode, start, end
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "아파트 거래 정보", notes = "아파트 거래정보를 출력한다")
	@GetMapping("/houseDealList")
	public ResponseEntity<List<HouseDeal>> houseDealList(@RequestParam(value= "aptCode") String aptcode,
			@RequestParam(value= "start") String start, @RequestParam(value= "end") String end)throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("aptCode",aptcode);
		map.put("start",start);
		map.put("end",end);
		return new ResponseEntity<List<HouseDeal>> (service.selectHouseDealByDate(map),HttpStatus.OK);
	}
	
	/**
	 * InterestArea(user_Id,지역 코드)를 받아 관심지역을 등록한다  
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "관심 매물 등록", notes = "관심 매물을 등록한다")
	@PostMapping("/interestarea")
	public int interestArea(@RequestBody InterestArea dto) throws Exception {
		System.out.println(dto);
		return service.insertInterestArea(dto);
	}
	
	/**
	 * 유저아이디를 받아 유저아이디에 연결된 관심 지역 매물을 불러온다.
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "관심 매물 불러오기", notes = "유저 아이디에 따른 관심 매물을 불러온다")
	@GetMapping("/interestarea/{userid}")
	public List<InterestArea> interestAreaList(@PathVariable(value= "userid") String userid)throws Exception{
		List<InterestArea> list = service.selectInterestAreaByUserId(userid);
		
		for (InterestArea dto : list) {
			System.out.println(dto);
		}
		
		return list;
	}
	
	@DeleteMapping("/interestarea")
	public int deleteArea(InterestArea dto) throws Exception {
		System.out.println(dto);
		return service.deleteInterestArea(dto);
	}
	
	@GetMapping("/recently/{qty}")
	public List<HouseDeal> selectRecently(@PathVariable(value= "qty") String qty)throws Exception{
		
		List<HouseDeal> list = service.selectRecently(Integer.parseInt(qty));
		
		for (HouseDeal dto : list) {
			System.out.println(dto);
		}
		
		return list;
	}
	
	@GetMapping("/houseinfo/{aptCode}")
	public HouseInfo houseInfo(@PathVariable(value= "aptCode") String aptCode)throws Exception{
		return service.selectHouseInfoByAptCode(aptCode);
	}
@GetMapping("/mainchart/{code}")
	public ResponseEntity<?> mainchart(@PathVariable("code") String code) throws Exception {
		return new ResponseEntity<List<MainChart>>(service.selectDealCountByYear(code),HttpStatus.OK);
	}
	
	@GetMapping("/aptchart")
	public ResponseEntity<?> aptchart(@RequestParam(value="aptCode",required=true) String aptCode,
									  @RequestParam(value="startDate",required=false) String startDate,
									  @RequestParam(value="endDate",required=false) String endDate) throws Exception {
		Map<String,String> map = new HashMap<>();
		map.put("aptCode",aptCode);
		map.put("startDate",startDate);
		map.put("endDate",endDate);
		return new ResponseEntity<List<AptChart>>(service.selectAvgByaptcode(map),HttpStatus.OK);
	}

}
