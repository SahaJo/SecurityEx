package com.meme.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meme.security1.model.User;
import com.meme.security1.repository.UserRepository;

@Controller // view를 리턴하겠다.
public class IndexController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	// localhost:8080
	@GetMapping({"","/"})
	public String index() {
		// 머스테치 기본폴더 src/main/resources/ 
		// viewResolver 설정 : templates (prefix), .mustache(suffix)  생략가능!!
		return "index";	// src/main/resources/templates/index.mustache
	} // index
	
	@GetMapping("/user")
	public @ResponseBody String user() {
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
	

} // end class
