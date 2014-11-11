//package wepa.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
//import wepa.auth.CustomPersistentTokenService;
//import wepa.auth.CustomUserDetailsService;
//import wepa.auth.CustomUserDetailsService;
//
//@Configuration
//@EnableWebMvcSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    private final String TOKEN_KEY = "a very random token key";
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    @Autowired
//    private CustomPersistentTokenService tokenService;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        PersistentTokenBasedRememberMeServices rememberMe
//                = new PersistentTokenBasedRememberMeServices(TOKEN_KEY, userDetailsService, tokenService);
//        rememberMe.setAlwaysRemember(true);
//
//        http.csrf().disable()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().rememberMe()
//                .rememberMeServices(rememberMe)
//                .key(TOKEN_KEY)
//                .userDetailsService(userDetailsService)
//                .useSecureCookie(false);
//
//        http.authorizeRequests()
//                .antMatchers("/*", "/albums/*", "/albums/*/images/*").permitAll()
//                .anyRequest().authenticated();
//
//        http.formLogin()
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }
//}
