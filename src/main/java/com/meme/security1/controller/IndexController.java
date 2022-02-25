package com.meme.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meme.security1.config.auth.PrincipalDetails;
import com.meme.security1.model.User;
import com.meme.security1.repository.UserRepository;

@Controller // view를 리턴하겠다.
public class IndexController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(
			Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) { // DI (의존성 주입)
		System.out.println("/test/login ==================");
		
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();		// same
		System.out.println("GetUser : " +principalDetails.getUser() );
		
		System.out.println("userDetails++getUsername : " + userDetails.getUsername());
		System.out.println("userDetails++getUser : " + userDetails.getUser());							// same
		return "세션정보 확인하기 " ;
	} // testLogin
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String tesOauthtLogin(
			Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) { // DI (의존성 주입)
		System.out.println("/test/oauth/login ==================");
		
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();		// same
		System.out.println("authentication : " +oAuth2User.getAttributes() );
		System.out.println();
		System.out.println("oauth2User : " + oauth.getAttributes());
		
		return "Oauth 세션정보 확인하기 " ;
	} // tesOauthtLogin
	
	
	// localhost:8080
	@GetMapping({"","/"})
	public String index() {
		// 머스테치 기본폴더 src/main/resources/ 
		// viewResolver 설정 : templates (prefix), .mustache(suffix)  생략가능!!
		return "index";	// src/main/resources/templates/index.mustache
	} // index
	
	// OAuth 로그인을 해도 PrincipalDetails
	// 일반 로그인을 해도 PrincipalDetails
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails : " + principalDetails.getUser());
		return "user";
	} // user
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	} // admin
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	} // manager
	
	// 스프링시큐리티 해당주소를 낚아채버림 - SecurityConfig 파일 생성 후 작동 안함.
	@GetMapping("/loginForm")
	public  String loginForm() {
		return "loginForm";
	} // loginForm
	
	@GetMapping("/joinForm")
	public  String joinForm() {
		return "joinForm";
	} // joinForm
	
	@PostMapping("/join")
	public  String join(User user) {
		System.out.println("USER_________________________" + user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);		
		userRepository.save(user); // 회원가입 잘됨. 비밀번호 1234 = > 시큐리티로 로그인할 수 없음
															// 패스워드 암호화가 안됨
		return "redirect:/loginForm";
	} // join
	
	// SecurityConfig Class의 @EnableGlobalMethodSecurity(securedEnabled = true) 의 옵션
	@Secured("ROLE_ADMIN")		// 단수
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	} // info
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")	// 다수
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "data info";
	} // data

} // end class
