package com.web.resolver;

import com.web.annotation.SocialUser;
import com.web.domain.User;
import com.web.domain.enums.SocialType;
import com.web.repository.UserRepository;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.web.domain.enums.SocialType.*;

/*
	HandlerMethodArgumentResolver 인터페이스는 다음 두메서드를 제공합니다.

	supportsParameter() 메서드 : HandlerMethodArgumentResolver가 해당하는 파라미터를 지원할지 여부를 반환합니다. ture를 반환하면 resolveArgument 메서드가 수행됩니다.

	resolveArgument() 메서드 : 파라미터의 인잣값에 대한 정보를 바탕으로 실제 객체를 생성하여 해당 파라미터 객체에 바인딩합니다.
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	private UserRepository userRepository;

	/*
		MethodParameter로 해당 파라미터의 정보를 받게됩니다.
		이제 파라미터에 @SocialUser 어노테이션이 있고 타입이 User인 파라미터만 true를 반활할 것입니다.
		supportsParameter() 메서드에서 처음 한번 체크된 부분은 캐시되어 이후의 동이랗ㄴ 호출 시에는 체크되지 않고 캐시된 결과값을 바로 반환합니다.
	 */
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(SocialUser.class) != null && parameter.getParameterType().equals(User.class);
	}

	/*
		resolveArgument() 메서드는 검증이 완료된 파라미터 정보를 받습니다. 이미 검증이 되어 세션에 해당 User객체가 있으면
		User 객체를 구성하는 로직을 수행하지 않도록 세션을 먼저 확인하는 코드를 구현하겠습니다.
		세션은 RequestContectHolder를 사용해서 가져올 수 있습니다.
	 */
	public Object resolveArgument(MethodParameter parameter,
		ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
		User user = (User) session.getAttribute("user");
		return getUser(user, session);
	}

	private User getUser(User user, HttpSession session) {
		if(user == null) {
			try{
				OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

				Map<String, Object> map = authentication.getPrincipal().getAttributes();

				User convertUser = convertUser(authentication.getAuthorizedClientRegistrationId(), map);

				user = userRepository.findByEmail(convertUser.getEmail());
				if(user == null) {
					user = userRepository.save(convertUser);
				}

				setRoleIfNotSame(user, authentication, map);
				session.setAttribute("user", user);
			} catch(ClassCastException e) {
				return user;
			}
		}
		return user;
	}

	private User convertUser(String authority, Map<String, Object> map) {
		if(FACEBOOK.getValue().equals(authority)) return getModernUser(FACEBOOK, map);
		else if(GOOGLE.getValue().equals(authority)) return getModernUser(GOOGLE, map);
		else if(KAKAO.getValue().equals(authority)) return getKaKaoUser(map);

		return null;
	}

	private User getModernUser(SocialType socialType, Map<String, Object> map) {
		return User.builder()
			.name(String.valueOf(map.get("name")))
			.email(String.valueOf(map.get("email")))
			.principal(String.valueOf(map.get("id")))
			.socialType(socialType)
			.createdDate(LocalDateTime.now())
			.build();
	}

	private User getKaKaoUser(Map<String, Object> map) {
		HashMap<String, String> propertyMap = (HashMap<String, String>) map.get("properties");
		return User.builder()
			.name(propertyMap.get("nickname"))
			.email(String.valueOf(map.get("kaccount_email")))
			.socialType(KAKAO)
			.createdDate(LocalDateTime.now())
			.build();
	}

	private void setRoleIfNotSame(User user, OAuth2AuthenticationToken authenticaion, Map<String, Object> map) {
		if(!authenticaion.getAuthorities().contains(new SimpleGrantedAuthority(user.getSocialType().getRoleType()))) {
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(map, "N/A", AuthorityUtils.createAuthorityList(user.getSocialType().getRoleType())));
		}
	}
}
