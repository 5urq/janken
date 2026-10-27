package oit.is.z3479.kaizi.janken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class JankenAuthConfiguration {

  /**
   * 認可処理に関する設定
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login
        .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/"))
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/janken/**", "/janken", "/jankengame/**").authenticated()
            .anyRequest().permitAll())
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**"))
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions
                .sameOrigin()));
    return http.build();
  }

  /**
   * 認証処理に関する設定
   * ユーザID: user1, user2, ほんだ, いがき
   * ロール: USER
   * パスワード: isdev (BCryptハッシュ化)
   */
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    UserDetails user1 = User.withUsername("user1")
        .password("{bcrypt}$2a$10$VO8eAhaTmPLPIPoMGlQ4HOuXz8ci2azqtwziFE.cTo30s4fA2u3Ka")
        .roles("USER").build();
    UserDetails user2 = User.withUsername("user2")
        .password("{bcrypt}$2a$10$VO8eAhaTmPLPIPoMGlQ4HOuXz8ci2azqtwziFE.cTo30s4fA2u3Ka")
        .roles("USER").build();
    UserDetails user3 = User.withUsername("ほんだ")
        .password("{bcrypt}$2a$10$VO8eAhaTmPLPIPoMGlQ4HOuXz8ci2azqtwziFE.cTo30s4fA2u3Ka")
        .roles("USER").build();
    UserDetails user4 = User.withUsername("いがき")
        .password("{bcrypt}$2a$10$VO8eAhaTmPLPIPoMGlQ4HOuXz8ci2azqtwziFE.cTo30s4fA2u3Ka")
        .roles("USER").build();

    return new InMemoryUserDetailsManager(user1, user2, user3, user4);
  }

}
