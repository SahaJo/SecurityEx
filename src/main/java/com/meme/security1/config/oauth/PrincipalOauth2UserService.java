package com.meme.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.meme.security1.config.auth.PrincipalDetails;
import com.meme.security1.model.User;
import com.meme.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public PrincipalOauth2UserService(@Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Autowired
	private UserRepository userRepository;
	
	// 구글로 부터 받은 userRequest데이터에 대한 Oauth 후처리 메소드
	// 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration : " + userRequest.getClientRegistration());	// registrationId로 어떤 OAuth로 로그인했는지 확인 가능
		System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		// 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴 (OAuth-Client라이브러리) -> AccessToken 요청
		// userRequest 정보 -> loadUser함수 -> 구글로부터 회원프로필 받아준다
		System.out.println(" getAttributes : " +  oauth2User.getAttributes());
		
		
		
		// super.loadUser(userRequest).getAttributes() 정보 받아와서 강제 회원가입
		String provider = userRequest.getClientRegistration().getRegistrationId(); // google
		String providerId = oauth2User.getAttribute("sub");
		String username = provider + "_" + providerId;	//google_xxxxxxxxxxxxxxxxxxxxxxxxxx
		String password = bCryptPasswordEncoder.encode("겟인뎅");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			System.out.println("구글로그인이 최초입니다.");
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerid(providerId)
					.build();
			userRepository.save(userEntity);
		} else{
			System.out.println("구글로그인 아이디가 있습니다..");
		}// if
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes() );
	} // loadUser
	
} // end
