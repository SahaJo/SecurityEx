package com.meme.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.meme.security1.model.User;
import com.meme.security1.repository.UserRepository;

// 발동 시기
// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로
// IoC되어 있는 loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailService implements UserDetailsService {

	@Autowired
		private UserRepository userRepository;
	
	// 자동호출 함수
	// 시큐리티 session(내부 Authentication(내부 UserDetails))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// username으로 안받으면 함수 변수이름 때문에 동작을 안함 (매칭 XXX)
		// SecurityConfig Class에서  configure 함수 값에서 .usernameParameter("변수이름");
		User userEntity = userRepository.findByUsername(username);
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		} // if
		
		return null;
	}// loadUserByUsername(username)

} // end PrincipalDetails class
