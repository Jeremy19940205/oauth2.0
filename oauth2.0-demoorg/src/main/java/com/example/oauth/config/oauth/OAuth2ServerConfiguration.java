package com.example.oauth.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.*;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableOAuth2Client
public class OAuth2ServerConfiguration {

    @Bean
    public SecurityContextHolderAwareRequestFilter securityContextHolderAwareRequestFilter() {
        return new SecurityContextHolderAwareRequestFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @EnableResourceServer
    public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired
        JwtTokenStore tokenStore;

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setTokenStore(tokenStore);
            defaultTokenServices.setSupportRefreshToken(true);
            defaultTokenServices.setReuseRefreshToken(false);
            defaultTokenServices.setAccessTokenValiditySeconds(-1);
//            defaultTokenServices.setRefreshTokenValiditySeconds(this.refreshTokenValiditySeconds);
            return defaultTokenServices;
        }


        @Override
        public void configure(final ResourceServerSecurityConfigurer resources) {
            resources
                    .tokenStore(tokenStore).tokenServices(tokenServices());
        }

        @Override
        public void configure(final HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .cors().disable()
                .logout()
                .and()
                .anonymous().disable()
                .requestMatchers()
                .antMatchers("/api/**")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        JwtAccessTokenConverter jwtAccessTokenConverter;

        private final BCryptPasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;

        @Autowired
        private UserDetailsService userDetailsService;

        @Value("${server.password}")
        protected String client_password;

        @Value("${server.client}")
        protected String client;

        public AuthorizationServerConfiguration(BCryptPasswordEncoder passwordEncoder,
                                                AuthenticationManager authenticationManager) {
            this.passwordEncoder = passwordEncoder;
            this.authenticationManager = authenticationManager;
        }

        @Bean
        public TokenStore tokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter);
        }

        @Override
        public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

            endpoints
                    .tokenStore(tokenStore())
                    .authenticationManager(authenticationManager)
                    .accessTokenConverter(jwtAccessTokenConverter)
                    .pathMapping("/oauth/token", "/login")
                    .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) {
            security
                    .tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()")
                    .allowFormAuthenticationForClients();

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            config.applyPermitDefaultValues();

            source.registerCorsConfiguration("/**", config);
            CorsFilter filter = new CorsFilter(source);
            security.addTokenEndpointAuthenticationFilter(filter);
        }

        @Override
        public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                .withClient(client)
                .secret(passwordEncoder.encode(client_password))
                .authorizedGrantTypes("password", "refresh_token")
                .autoApprove (true)
                .scopes("all");
        }
    }
}
