package com.community.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DataRestWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(DataRestWebApplication.class, args);
	}

	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	@EnableWebSecurity
	static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//		@Bean
//		InMemoryUserDetailsManager userDetailsManager() {
//			User.UserBuilder commonUser = User.withUsername("commonUser");
//			User.UserBuilder havi = User.withUsername("havi");
//
//			List<UserDetails> userDetailsList = new ArrayList<>();
//			userDetailsList.add(commonUser.password("{noop}common").roles("USER").build());
//			userDetailsList.add(havi.password("{noop}test").roles("USER", "ADMIN").build());
//
//			return new InMemoryUserDetailsManager(userDetailsList);
//		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			CorsConfiguration configuration = new CorsConfiguration();

			/*
				CorsConfiguration 객체를 생성하여 CORS에서 Origin, Method, Header별로 허용할 값을 설정할 수 있습니다.
				CorsConfiguration.ALL은 * 와 같습니다. 모든 경로에 대해 허용합니다.
			 */
			configuration.addAllowedOrigin("*");
			configuration.addAllowedMethod("*");
			configuration.addAllowedHeader("*");
			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

			/*
				특정 경로에 대해 CorsConfiguration 객체에서 설정한 값을 CorsConfigurationSource 인터페이스를 구현한 UrlBasedCorsConfigurationSource에 적용시킵니다.
				여기서는 모든 경로로 설정되어 있습니다.
			 */
			source.registerCorsConfiguration("/**", configuration);

			http.httpBasic()
					.and().authorizeRequests()
					//.antMatchers(HttpMethod.POST, "/Boards/**").hasRole("ADMIN")
					.anyRequest().permitAll()
					.and().cors().configurationSource(source)
					.and().csrf().disable();
		}
	}



}
