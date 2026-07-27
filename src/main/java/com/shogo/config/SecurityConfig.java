@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/index", "/login").permitAll()
            .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
            .requestMatchers("/export/excel").authenticated()
            .anyRequest().authenticated()
        )
        .formLogin(withDefaults())
        .logout(withDefaults());

    return http.build();
}