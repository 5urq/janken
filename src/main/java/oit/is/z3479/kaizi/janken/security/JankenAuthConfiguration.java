package oit.is.z3479.kaizi.janken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * セキュリティ（認証・認可）の設定クラス
 *
 * 【AI活用と学習に関する注記】
 * 本認証設定コードは生成AIを活用して作成していますが、解説ドキュメントの作成および対話を通じて、
 * 認証（InMemoryUserDetailsManager・BCrypt）および認可（SecurityFilterChain）の挙動について、
 * 受講生自身がAIと一緒に動作原理を理解した上で組み込んでいます。
 */
@Configuration
@EnableWebSecurity
public class JankenAuthConfiguration {

  /**
   * 認可処理に関する設定
   * 1. Spring Securityのフォームを利用する
   * 2. /janken で始まるURLへのアクセス時に認証を必要とする
   * 3. ログアウト時にはトップページ (http://localhost:8080/) に戻る
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
   * ユーザID: user1, user2, ほんだ
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

    return new InMemoryUserDetailsManager(user1, user2, user3);
  }

}
