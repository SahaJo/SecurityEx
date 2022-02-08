package com.meme.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // view를 리턴하겠다.
public class IndexController {

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
	@GetMapping("/login")
	public @ResponseBody String login() {
		return "login";
	} // login
	
	@GetMapping("/join")
	public @ResponseBody String join() {
		return "join";
	} // join
	
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "회원가입 완료됨";
	} // joinProc
	
} // end class
