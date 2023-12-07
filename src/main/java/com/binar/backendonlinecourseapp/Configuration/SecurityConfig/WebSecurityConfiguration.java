package com.binar.backendonlinecourseapp.Configuration.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors();
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/api/users/login").permitAll()
                .antMatchers("/api/users/register").permitAll()
                .antMatchers("/api/users/otp/{code}").permitAll()
                .antMatchers("/api/users/resend-otp/{email}").permitAll()
                .antMatchers("/api/course/{page}").permitAll()
                .antMatchers("/api/course/get-premium").permitAll()
                .antMatchers("/api/course/get-free").permitAll()
                .antMatchers("/api/course/search/{page}/{course}").permitAll()
                .antMatchers("/api/course/get/{course}").permitAll()
                .antMatchers("/api/category/get").permitAll()

                .antMatchers("/api/category").hasAuthority("ADMIN")
                .antMatchers("/api/category/addpicture/**").hasAuthority("ADMIN")
                .antMatchers("/api/course/create").hasAuthority("ADMIN")
                .antMatchers("/api/course/update").hasAuthority("ADMIN")
                .antMatchers("/api/course/upload-image/{course}").hasAuthority("ADMIN")


                .antMatchers("/api/users").hasAuthority("USER")
                .antMatchers("/api/course/get/{course}").hasAuthority("USER")
                .antMatchers("/api/order").hasAuthority("USER")
                .antMatchers("/api/order/{ordercode}").hasAuthority("USER")
                .antMatchers("/api/users/update-profil-pic").hasAuthority("USER")
                .antMatchers("/api/course/payment-history").hasAuthority("USER")
                .antMatchers("/api/course/watched/{video}").hasAuthority("USER")


                .antMatchers(HttpHeaders.ALLOW).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
  
}

