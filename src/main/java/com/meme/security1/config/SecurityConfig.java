package com.meme.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity	// 시큐리티 활성화 스프링 시큐리티 필터가 스프링 필터체인에 등록 됨
	// secured 어노테이션 활성화   	prePostEnabled = true -> preAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
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
			.defaultSuccessUrl("/"); // login 성공시 기본 path
	} // configure
	
} // end class
