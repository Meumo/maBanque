package sn.objis.mabanque.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class securityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	DataSource dataSource;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//System.out.println(bCryptPasswordEncoder.encode("1234"));
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery(
						"select username as principal, password as credentials, active from users where username=?")
				.authoritiesByUsernameQuery(
						"select username as principal, roles as role from users_roles where username=?")
				.rolePrefix("ROLE_").passwordEncoder(getBcpe());
	}

@Override
protected void configure(HttpSecurity http) throws Exception {
	http.formLogin();
	http.authorizeRequests().antMatchers("/operations","/").hasRole("USER");
	http.authorizeRequests().antMatchers("/save","/delete").hasRole("ADMIN");
	http.exceptionHandling().accessDeniedPage("/403");
}

	@Bean
	public BCryptPasswordEncoder getBcpe() {
		return new BCryptPasswordEncoder();
	}
}
