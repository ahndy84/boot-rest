package com.web.domain.enums;

/**
 * User 타입만 검사하도록 만들 수도 있지만, 소셜 미디어에 인증된 User를 가져온다는 사실을 더 명확하게 표현하기 위해
 * 파라미터용 어노테이션을 추가적으로 생성하겠습니다.
 */
public enum SocialType {
	FACEBOOK("facebook"),
	GOOGLE("google"),
	KAKAO("kakao");

	private final String ROLE_PREFIX = "ROLE_";
	private String name;

	SocialType(String name) {
		this.name = name;
	}

	public String getRoleType() { return ROLE_PREFIX + name.toUpperCase(); }

	public String getValue() { return name; }

	public boolean isEquals(String authority) {
		return this.name.equals(authority);
	}
}