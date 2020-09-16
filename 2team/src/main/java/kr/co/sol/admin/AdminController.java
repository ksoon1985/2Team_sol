package kr.co.sol.admin;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.sol.common.dto.BookingDTO;
import kr.co.sol.common.dto.MemberDTO;
import kr.co.sol.common.dto.PageDTO;
import kr.co.sol.common.dto.RestaurantDTO;
import kr.co.sol.common.service.MemberService;

@Controller
public class AdminController {
	@Autowired
	AdminService adminService;
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping(value="/admin/login",method= {RequestMethod.POST,RequestMethod.GET})
	public String login(HttpServletRequest request , @RequestParam(required = false) String id, @RequestParam(required = false) String passwd, Model model, MemberDTO mdto) {
		HttpSession session = request.getSession();
		if(id!=null && passwd !=null) {
			MemberDTO mdto2 = adminService.login(id, passwd);
			session.setAttribute("mdto", mdto2);
		} else {
			session.setAttribute("mdto",null);
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "test_id");
			map.put("passwd", "1234");
			session.setAttribute("data", map);
		}
		return "/admin/login";
	}
	// 로그인 처리는 common에서 공통으로 처리.
	
	@GetMapping(value="/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
//	@RequestMapping(value="/loginPro", method ={RequestMethod.GET,RequestMethod.POST})
//	public String loginPro(MemberDTO mdto, Model model, HttpServletRequest request) {
//		String role = adminService.login2(mdto);
//		if("admin".equals(role)) {
//			HttpSession session = request.getSession();
//			Integer no = mdto.getNo();
//			session.setAttribute("idKey", no);
//		} else if(role==null) { 
//			String msg = "올바른 아이디와 비밀번호가 맞지 않습니다";
//			model.addAttribute("msg",msg);
//			return "/admin/index";
//		}
//		
//		return "/admin/index";
//	}
	
	@RequestMapping(value="/admin/index", method= {RequestMethod.GET, RequestMethod.POST})
	public String adminIndex(Model model, HttpServletRequest request,
			HttpServletResponse response){
		return "admin/index";
	}
	
	
	@RequestMapping(value="admin/mem_manage", method = {RequestMethod.GET,RequestMethod.POST})
	public String mm(Model model, HttpServletRequest request, HttpServletResponse response,MemberDTO mdto,
			@RequestParam(required=false) String searchOption,
			@RequestParam(required=false) String keyword) {
			
		List<HashMap<String, Object>> map= new ArrayList<HashMap<String, Object>>();
		
		// 맨 처음 들어왔을 때 전체 리스트 불러오기 위해.
		if(searchOption==null && keyword==null) {
			// 전체 리스트 불러온다.
			map = adminService.getMember();
		} else {
			// 검색어와 옵션이 있을 경우 해당 내용으로 검색한 결과를 가져온다.
			map = adminService.getMemberList(searchOption,keyword);
		}
		model.addAttribute("mList",map);
		// 검색 후 옵션 유지하기 위해서.
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("searchOption", searchOption);
		map2.put("keyword",keyword);
		
		model.addAttribute("map",map2);
		return "admin/mem_manage";
	}
	
	@GetMapping("/admin/store_manage")
	public String sm(HttpServletRequest request, String searchOption, String keyword,
			@RequestParam(value="curPage", defaultValue="1") int curPage, Model model, RestaurantDTO resdto, PageDTO pdto) {
		HttpSession session = request.getSession();
		if(session.getAttribute("mdto")==null) {
			return "redirect:/";
		} // 세션에 담긴게 없으면 로그인 화면으로
		List<RestaurantDTO> resdto2;
		
		// paging 처리
					
		// 검색어가 있는지 확인
		
		if(searchOption==null && keyword==null) {
			// 없을 경우, 전체List에서 paging처리 
			resdto2 = adminService.getStoreList(pdto,curPage);
		} else { // 있을 경우, 검색조건에 맞는 애들에서 가져오는 작업
			// 현재 페이지를 넘겨서 출력할 list 가져와야함.
			resdto2 = adminService.getStore(searchOption, keyword);
		}
		model.addAttribute("resdto",resdto2);
		model.addAttribute("curPage", curPage);
		return "/admin/store_manage";
	}
	
	
//	@GetMapping("admin/store_manage")
//	public String sm(Model model, HttpServletRequest request, HttpServletResponse response, StoreDTO sdto,
//			@RequestParam(required=false) String searchOption,
//			@RequestParam(required=false) String keyword) {
//		List<StoreDTO> sdto2;
//		if(searchOption==null && keyword==null) {
//			sdto2 = storeService.getStore();
//		} else {
////			sdto2 = storeService.getStoreList(searchOption, keyword);
//		}
//		model.addAttribute("sdto",sdto2);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("searchOption", searchOption);
//		map.put("keyword",keyword);
//		model.addAttribute("map",map);
//		
//		return "admin/store_manage";
//	}
	
	@GetMapping("admin/report")
	public String report(Model model, HttpServletRequest request, HttpServletResponse response, RestaurantDTO resdto, MemberDTO mdto) {
		HttpSession session = request.getSession();
		if(session.getAttribute("mdto")==null) {
			
			return "redirect:/";
		}
		return "admin/report";
	}
	
	@RequestMapping(value="admin/reg_store", method= {RequestMethod.GET,RequestMethod.POST})
	public String reg_store(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("mdto")==null) {
			
			return "redirect:/";
		}
		return "admin/reg_store";
	}
	
	@RequestMapping(value="/admin/reg_storePro", method ={RequestMethod.GET,RequestMethod.POST})
	public String reg_storePro(RestaurantDTO rdto, @RequestParam(value="fileName", required=false) MultipartFile file, Model model) {
		String sourceFileName = file.getOriginalFilename();
		File destinationFile; 
		if (sourceFileName == null || sourceFileName.length()==0) { 
			return "admin/reg_store";
		}else {
			destinationFile = new File("res_no_"+rdto.getNo()+"jpg"); 
	        
			destinationFile.getParentFile().mkdirs(); 
		    try {
				file.transferTo(destinationFile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				 e.printStackTrace();
			}
		}
		return "redirect:admin/store_manage";
	}
	
	@RequestMapping(value="/admin/reg_storePro/nameChk", method={RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody int nameChk(@RequestParam("nameChk") String name) {
			int ckResult = adminService.nameChk(name);
			if (ckResult==0) {
				return 0;
			}
		return 1;
	}
	
	@RequestMapping(value="/admin/reserv_manage", method= {RequestMethod.GET, RequestMethod.POST})
	public String reserv_manage(HttpServletRequest request, Model model,String searchOption, String keyword
			) {
		HttpSession session = request.getSession();
		if(session.getAttribute("mdto")==null) {
			
			return "redirect:/";
		}
		List<HashMap<String,Object>> bdto;
		System.out.println("dfasldkjf"+searchOption+"      "+keyword);
		if(searchOption==null && keyword==null) {
			// 없을 경우, 전체List에서 paging처리
			System.out.println("없습니다");
			bdto = adminService.getBookingList();
		} else { // 있을 경우, 검색조건에 맞는 애들에서 가져오는 작업
			System.out.println("있습니다");
			// 현재 페이지를 넘겨서 출력할 list 가져와야함.
			bdto = adminService.getBooking(searchOption, keyword);
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("searchOption", searchOption);
		map2.put("keyword",keyword);
		model.addAttribute("map",map2);
		model.addAttribute("bdto",bdto);
		System.out.println(bdto);
		return "/admin/reserv_manage";
	}
	
	
	@PostMapping("/booking_cancel")
	public @ResponseBody int bCancel(@RequestParam("no") int no) {
		int r = adminService.bCancel(no);
		if(r==0) {
			return r;
		}
		return no;
	}
	
}