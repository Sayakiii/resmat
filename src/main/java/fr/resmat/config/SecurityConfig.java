package fr.resmat.config;

import fr.resmat.entity.Utilisateur;
import fr.resmat.repository.UtilisateurRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF est activé par défaut : chaque formulaire POST doit contenir le jeton.
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/error").permitAll()
                .requestMatchers("/gestionnaire/**").hasRole("GESTIONNAIRE")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll())
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** L'identifiant de connexion est l'adresse e-mail. */
    @Bean
    public UserDetailsService userDetailsService(UtilisateurRepository repository) {
        return email -> {
            Utilisateur u = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur inconnu : " + email));
            return User.withUsername(u.getEmail())
                .password(u.getMotDePasse())
                .authorities(u.getRole().name())
                .build();
        };
    }
}
