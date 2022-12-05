package com.ssafy.myhome.model.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ssafy.myhome.dto.KakaoLogin;
import com.ssafy.myhome.dto.NaverLogin;

@Service
public class OauthLoginServiceImpl implements OauthLoginService {
	@Override
	public NaverLogin naverLogin(String code, String state) {
		NaverLogin userInfo = null;
		String access_token = "";
		String refresh_token = "";
		String apiURL = "https://nid.naver.com/oauth2.0/token?";
		apiURL += "grant_type=" + "authorization_code";
		apiURL += "&client_id=" + "";
		apiURL += "&client_secret=" + "";
		apiURL += "&code=" + code;
		apiURL += "&state=" + state;
		RestTemplate restTeplate = new RestTemplate();
		try {
			URL url = new URL(apiURL);
			System.out.println(url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			BufferedReader br;

			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				System.out.println("정상 호출됨");
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
				System.out.println("에러 발생");
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
//	        System.out.println(res);
			if (responseCode == 200) { // 성공적으로 토큰을 가져온다면

				JSONObject parser = new JSONObject((JSONObject) new JSONParser().parse(res.toString()));
				// System.out.println(parser.get("access_token"));
				NaverLogin token = new NaverLogin((String) parser.get("access_token"),
						(String) parser.get("refresh_token"), (String) parser.get("profile_image"),
						(String) parser.get("token_type"), (String) parser.get("expires_in"), "", "");

				userInfo = getUserInfo(token);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userInfo;
	}

	public NaverLogin getUserInfo(NaverLogin token) {
		String header = "Bearer " + token.getAccessToken(); // Bearer 다음에 공백 추가
		try {
			String apiURL = "https://openapi.naver.com/v1/nid/me";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", header);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			System.out.println(res.toString());
			JSONObject parser = new JSONObject((JSONObject) new JSONParser().parse(res.toString()));

			JSONObject naverInfo = (JSONObject) new JSONParser().parse(parser.get("response").toString());
			token.setProfileImg((String) naverInfo.get("profile_image"));
			token.setUserName((String) naverInfo.get("name"));
			token.setUserId((String) naverInfo.get("id"));
			token.setEmailId(((String) naverInfo.get("email")).split("@")[0]);
			token.setEmailDomain(((String) naverInfo.get("email")).split("@")[1]);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return token;
	}

	public KakaoLogin kakaoLogin(String code) {
		KakaoLogin userInfo = null;
		String access_token = "";
		String refresh_token = "";
		String apiURL = "https://kauth.kakao.com/oauth/token";
		try {
			URL url = new URL(apiURL);
			System.out.println(url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			StringBuilder sb = new StringBuilder().append("grant_type=authorization_code")
					.append("&client_id=")
					.append("&redirect_uri=http://localhost:8080/user/login/oauth2/kakao").append("&code=" + code);

			bw.write(sb.toString());
			bw.flush();

			int responseCode = con.getResponseCode();
			System.out.println("responseCode = " + responseCode);

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";
			String result = "";
			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("result = " + result);

			// json parsing
			JSONParser parser = new JSONParser();
			JSONObject element = (JSONObject) parser.parse(result);

			access_token = element.get("access_token").toString();
			refresh_token = element.get("refresh_token").toString();
			System.out.println("refresh_token = " + refresh_token);
			System.out.println("access_token = " + access_token);
			br.close();
			bw.close();
			userInfo = getKakaoInfo(access_token, refresh_token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userInfo;
	}

	public KakaoLogin getKakaoInfo(String access_token, String refresh_token) {
		String host = "https://kapi.kakao.com/v2/user/me";
		Map<String, Object> result = new HashMap<>();
		KakaoLogin token = null;
		try {
			URL url = new URL(host);

			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
			urlConnection.setRequestMethod("GET");

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				String line = "";
				String res = "";
				while ((line = br.readLine()) != null) {
					res += line;
				}

				System.out.println("res = " + res);

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(res);
				JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
				JSONObject properties = (JSONObject) obj.get("properties");
				System.out.println((String) obj.get("email"));
				token = new KakaoLogin(access_token, refresh_token, (String) properties.get("nickname"),
						(long) obj.get("id"), ((String) kakao_account.get("email")).split("@")[0],
						((String) kakao_account.get("email")).split("@")[1], (String) properties.get("profile_image"));
				br.close();
			}

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		return token;
	}

	@Override
	public boolean kakaoTokenCheck(String access_token, String userid) {
		String header = "Bearer " + access_token; // Bearer 다음에 공백 추가
		try {
			String apiURL = "https://kapi.kakao.com/v1/user/access_token_info";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", header);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			System.out.println(res.toString());
			JSONObject parser = new JSONObject((JSONObject) new JSONParser().parse(res.toString()));
			
			return userid.equals(String.valueOf((long) parser.get("id")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean naverTokenCheck(String access_token) {
		System.out.println("여기 들어오긴함");
		String header = "Bearer " + access_token; // Bearer 다음에 공백 추가
		try {
			String apiURL = "https://openapi.naver.com/v1/nid/verify";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", header);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			System.out.println("res : " + res.toString());
			JSONObject parser = new JSONObject((JSONObject) new JSONParser().parse(res.toString()));
			if (((String) parser.get("message")).equals("success")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getKakaoAccessToken(String token) {
		String access_token = "";
		String apiURL = "https://kauth.kakao.com/oauth/token";
		try {
			URL url = new URL(apiURL);
			System.out.println(url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			StringBuilder sb = new StringBuilder()
					.append("grant_type=refresh_token")
					.append("&client_id=")
					.append("&refresh_token=" + token);

			bw.write(sb.toString());
			bw.flush();

			int responseCode = con.getResponseCode();
			System.out.println("responseCode = " + responseCode);

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";
			String result = "";
			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("result = " + result);

			// json parsing
			JSONParser parser = new JSONParser();
			JSONObject element = (JSONObject) parser.parse(result);

			access_token = element.get("access_token").toString();
			System.out.println("access_token = " + access_token);
			br.close();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return access_token;
	}

	@Override
	public String getNaverAccessToken(String token) {
		String access_token = "";
		String apiURL = "https://nid.naver.com/oauth2.0/token";
		try {
			URL url = new URL(apiURL);
			System.out.println(url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			StringBuilder sb = new StringBuilder()
					.append("grant_type=refresh_token")
					.append("&client_id=")
					.append("&client_secret=")
					.append("&refresh_token=" + token);

			bw.write(sb.toString());
			bw.flush();

			int responseCode = con.getResponseCode();
			System.out.println("responseCode = " + responseCode);

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";
			String result = "";
			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("result = " + result);

			// json parsing
			JSONParser parser = new JSONParser();
			JSONObject element = (JSONObject) parser.parse(result);

			access_token = element.get("access_token").toString();
			System.out.println("access_token = " + access_token);
			br.close();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return access_token;
	}

	@Override
	public boolean kakaoLogout(String access_token, String userid) {
		String apiURL = "https://kapi.kakao.com/v1/user/logout";
		System.out.println(access_token);
		try {
			URL url = new URL(apiURL);
			System.out.println(url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization","Bearer "+ access_token);
			con.setDoOutput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			bw.flush();

			int responseCode = con.getResponseCode();
			System.out.println("responseCode = " + responseCode);

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";
			String result = "";
			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("result = " + result);

			// json parsing
			JSONParser parser = new JSONParser();
			JSONObject element = (JSONObject) parser.parse(result);
			br.close();
			bw.close();
			return userid.equals(element.get("id").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
}
