package com.meme.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.meme.security1.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity	// 시큐리티 활성화 스프링 시큐리티 필터가 스프링 필터체인에 등록 됨
	// secured 어노테이션 활성화   	prePostEnabled = true -> preAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	// 해당 메서드의 리턴되는 오브젝트를 Ioc로 등록해준다.
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	} // encodePwd
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()	// 권한 설정
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()	// 위의 3가지 주소가 아니면 누구나 접속 가능
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login")	// /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌
			.defaultSuccessUrl("/") // login 성공시 기본 path
			.and()		// oauth2 Google 집어넣음
			.oauth2Login()
			.loginPage("/loginForm")	// 구글 로그인이 완료된 뒤의 후처리가 필요함.
			.userInfoEndpoint()
			.userService(principalOauth2UserService)
			;	// 1. 코드받기(인증), 2. 엑세스토큰 받기(권한), 3. 사용자 프로필 정보 가져옴,
				// 4-1. 그 정보를 바탕으로 회원가입을 자동으로 진행 시키기도 함
				// 4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소), 백화점몰 -> (vip등급, 일반등급) 추가필요 정보
				// TIP 코드X, (엑세스토큰 + 사용자프로필정보(O)
	} // configure
	
} // end class
