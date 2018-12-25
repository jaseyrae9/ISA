package isa.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import isa.project.security.TokenUtils;
import isa.project.security.auth.EntryPoint;
import isa.project.security.auth.TokenAuthenticationFilter;
import isa.project.service.users.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	PasswordEncoder getEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private EntryPoint entryPoint;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	TokenUtils tokenUtils;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override 
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(this.getEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("**/secured/**").authenticated()
			.anyRequest().permitAll().and().formLogin().permitAll().and()
			.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userDetailsService), BasicAuthenticationFilter.class);
	/*	http
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.exceptionHandling().authenticationEntryPoint(entryPoint).and()
			.authorizeRequests()
			.antMatchers("/auth/**").permitAll()
			.antMatchers("/h2-console/**").permitAll()// svaki zahtev mora biti autorizovan
			.antMatchers("**").authenticated()
			.anyRequest().permitAll().and().formLogin().permitAll().and()
			// presretni svaki zahtev filterom
			.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userDetailsService), BasicAuthenticationFilter.class);
		*/
		http.csrf().disable();
	}

	// Generalna bezbednost aplikacije
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.GET, "/aircompanies/all");
		//web.ignoring().antMatchers(HttpMethod.POST,"/customers/register");
		//web.ignoring().antMatchers(HttpMethod.GET, "/customers/confirmRegistration");

	}
}
