package com.friday.springsecurityapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DefaultSecurityConfiguration extends WebSecurityConfigurerAdapter {


    //Configuring data source bean, wire in this data source
    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        /*
        when you add an embedded database spring security is smart enough,
        to create that data source for you
        spring security has some default schema for user, we need not to create that default
        tables, we can tell spring security some default users

        we have created a default schema with this information

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema()
                .withUser(
                        User.withUsername("user")
                        .password("user")
                        .roles("USER")
                )
                .withUser(
                       User.withUsername("admin")
                       .password("admin")
                       .roles("ADMIN")
                );
         */

        // schema.sql file is the file that spring boot runs on application startup
        // what if your schema is different than spring security default schema then use this
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled "
                    + "from users "
                    + "where username = ?")
                .authoritiesByUsernameQuery("select username, authority "
                    + "from authoritites "
                    + "where username = ?");

    }

    @Bean
    public PasswordEncoder getPassWordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/", "static/css", "static/js").permitAll()
                .and().formLogin();

        http.csrf().disable();
        http.headers().frameOptions().disable();

    }



}
