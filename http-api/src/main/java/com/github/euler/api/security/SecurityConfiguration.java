package com.github.euler.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/token";
    private static final String REQUIRED_ROLE = "euler-admin";

    private final SecurityService securityService;
    private final AuthService authService;

    @Autowired
    public SecurityConfiguration(SecurityService securityService, AuthService authService) {
        super();
        this.securityService = securityService;
        this.authService = authService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(securityService,
                authenticationManager());
        authenticationFilter.setFilterProcessesUrl(LOGIN_PROCESSING_URL);
        JWTAuthorizationFilter authorizationFilter = new JWTAuthorizationFilter(securityService,
                authenticationManager());

        http.authorizeRequests().antMatchers(LOGIN_PROCESSING_URL, "/", "/swagger-ui.html").permitAll().and()
                .authorizeRequests()
                .antMatchers("/job/**", "/jobs/**", "/statistics/**", "/extensions/**", "/template/**", "/templates/**")
                .hasAnyAuthority(REQUIRED_ROLE).and().addFilter(authenticationFilter).addFilter(authorizationFilter);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new EulerAuthenticationProvider(authService));
    }
}
