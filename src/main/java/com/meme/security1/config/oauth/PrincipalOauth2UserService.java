package com.meme.security1.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.meme.security1.config.auth.PrincipalDetails;
import com.meme.security1.config.oauth.provider.FacebookUserInfo;
import com.meme.security1.config.oauth.provider.GoogleUserInfo;
import com.meme.security1.config.oauth.provider.NaverUserInfo;
import com.meme.security1.config.oauth.provider.OAuth2UserInfo;
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
		
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청!!!");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인 요청!!!");
			oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청!!!");
			oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
		} else {
			System.out.println("구글 로그인 OR 페이스북 OR 네이버 로그인만 요청 허가함!!!");
		}
		
		// super.loadUser(userRequest).getAttributes() 정보 받아와서 강제 회원가입
//		String provider = userRequest.getClientRegistration().getRegistrationId(); // google
		// google 외 다른 계정을 사용할때 변수값이 달라서 인터페이스를 설정해줘야함
//		String providerId = oauth2User.getAttribute("sub");
//		String username = provider + "_" + providerId;	//google_xxxxxxxxxxxxxxxxxxxxxxxxxx
//		String password = bCryptPasswordEncoder.encode("겟인뎅");
//		String email = oauth2User.getAttribute("email");
//		String role = "ROLE_USER";

		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId;	//google_xxxxxxxxxxxxxxxxxxxxxxxxxx
		String password = bCryptPasswordEncoder.encode("겟인뎅");
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			System.out.println(" 최초 로그인입니다.");
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
			System.out.println(" 아이디가 있습니다..");
		}// if
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes() );
	} // loadUser
	
} // end
