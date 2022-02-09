package com.meme.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.meme.security1.model.User;

// 시큐리티가 /login 주소 요청이 오면 낚아채서  진행
// 로그인 진행이 완료가 되면 session안에 시큐리트의 session을 만들어줌.
// (Security ContextHolder)
// 오브젝트 => Authentication 타입 객체
// Authenticatoin 안에 User정보가 있어야됨.
// User오브젝트Type => UserDatails 타입 객체

// Security Session => Authentication => UserDetails 타입
public class PrincipalDetails implements UserDetails {

	private User user; // 콤포지션
	
	// user 생성자
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	// 해당 User의 권한을 리턴하는 곳!!
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	} // getAuthorities

	// password Return
	@Override
	public String getPassword() {
		return user.getPassword();
	}  // getPassword

	// password Return
	@Override
	public String getUsername() {
		return user.getUsername();
	}// getUsername

	// 계정 만료됐냐 질의 ture = 아니요
	@Override
	public boolean isAccountNonExpired() {
		return true;
	} // isAccountNonExpired

	// 계정 잠겼나 true = NO
	@Override
	public boolean isAccountNonLocked() {
		return true;
	} // isAccountNonLocked

	// 비밀번호 기간이 ~~일 지났니
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	} // isCredentialsNonExpired

	// 계정 활성화
	@Override
	public boolean isEnabled() {
		// 우리 사이트!! 1년동안 회원이 로그인을 안하면!! 휴먼 계정으로 하기로 한다면
		// User 변수에 private Timestamp loginDate; 등록해서 확인
		// user.getLoginDate 들고와서 현재시간 - 로긴시간 => 1년 초과하면 return false;
		return true;
	} // isEnabled

} // end PrincipalDetailClass
