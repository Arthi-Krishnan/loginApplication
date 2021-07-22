package com.example.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.service.UserService;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
    private CustomOAuth2UserService oauthUserService;
	@Autowired
	private UserService userService;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.antMatcher("/**")
		.authorizeRequests()
		.antMatchers("/","/login", "/oauth/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin().permitAll()
		.loginPage("/login")
		.usernameParameter("email")
		.passwordParameter("pass")
        .and()
        .oauth2Login()
            .loginPage("/login")
            .userInfoEndpoint()
                .userService(oauthUserService)
                .and().successHandler(new AuthenticationSuccessHandler() {
					
                	@Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                            Authentication authentication) throws IOException, ServletException {
             HttpSession session = request.getSession();
                		DefaultOidcUser oc = (DefaultOidcUser) authentication.getPrincipal();
                        
             
                    String userType = userService.processOAuthPostLogin(oc.getEmail(),request);
                    session.setAttribute("user", oc);   
                    if(userType.equals("0")) {
                        response.sendRedirect("/uRolePage");
        	        }
        	        else
        	        {
        	        	response.sendRedirect("/success");	
        	        }
             
                      //  response.sendRedirect("/success");
                    }
                }).and().rememberMe().key("uniqueKey").tokenValiditySeconds(84600).and()
    			.logout().logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true).
    			logoutSuccessUrl("/").permitAll()
    			.and()
    			.exceptionHandling().accessDeniedPage("/403")
    			;
		
	}

	
	
}
