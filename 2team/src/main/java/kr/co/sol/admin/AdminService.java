package kr.co.sol.admin;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.co.sol.custom.dto.MemberDTO;
<<<<<<< HEAD
import kr.co.sol.custom.dto.RestaurantDTO;

public interface AdminService {
	List<HashMap<String, Object>> getMember();


	List<HashMap<String, Object>> getMemberList(@Param("searchOption") String searchOption, @Param("keyword") String keyword);


	MemberDTO login(String id, String passwd);


	String login2(MemberDTO mdto);


	MemberDTO loginPro(MemberDTO mdto);


	List<RestaurantDTO> getStore(String searchOption, String keyword);


	List<RestaurantDTO> getStoreList();
=======
<<<<<<< HEAD
import kr.co.sol.custom.dto.RestaurantDTO;

public interface AdminService {
	List<HashMap<String, Object>> getMember();


	List<HashMap<String, Object>> getMemberList(@Param("searchOption") String searchOption, @Param("keyword") String keyword);


	MemberDTO login(String id, String passwd);


	String login2(MemberDTO mdto);


	MemberDTO loginPro(MemberDTO mdto);


	List<RestaurantDTO> getStore(String searchOption, String keyword);


	List<RestaurantDTO> getStoreList();
=======

public interface AdminService {
	List<HashMap<String, Object>> getMember();


	List<HashMap<String, Object>> getMemberList(@Param("searchOption") String searchOption, @Param("keyword") String keyword);


	MemberDTO login(String id, String passwd);


	String login2(MemberDTO mdto);


	MemberDTO loginPro(MemberDTO mdto);
>>>>>>> refs/heads/mypage
>>>>>>> refs/heads/newMaster
}
