package com.freelance.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private JwtAuthEntryPoint jwtAuthEntryPoint;

	@Bean
	public JwtAuthTokenFilter authenticationJwtTokenFilter() {
		return new JwtAuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
		.antMatchers("/user/login").permitAll()
		.antMatchers("/user/update").permitAll()
		.antMatchers("/user/details/**").hasAnyAuthority("SUPERADMIN", "GESTIONARY")
		.antMatchers("/user/register").hasAnyAuthority("SUPERADMIN", "GESTIONARY")
		.antMatchers("/user/*").hasAnyAuthority("SUPERADMIN", "GESTIONARY")
		.antMatchers("/person/**").hasAnyAuthority("SUPERADMIN", "GESTIONARY")
		.antMatchers("/companyClient/**").hasAuthority("SUPERADMIN")
		.antMatchers("/department/**").hasAnyAuthority("GESTIONARY")
		.antMatchers("/provider/**").hasAnyAuthority("GESTIONARY")
		.antMatchers("/localisation/**").hasAnyAuthority("GESTIONARY")
		.antMatchers("/item-Family/**").hasAnyAuthority("GESTIONARY")
		.anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
