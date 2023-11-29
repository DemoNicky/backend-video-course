package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Request.ChangePasswordRequest;
import com.binar.backendonlinecourseapp.DTO.Request.LoginRequest;
import com.binar.backendonlinecourseapp.DTO.Request.RegisterRequest;
import com.binar.backendonlinecourseapp.DTO.Request.UpdateDataRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import com.binar.backendonlinecourseapp.Entity.Role;
import com.binar.backendonlinecourseapp.Entity.Token;
import com.binar.backendonlinecourseapp.Entity.User;
import com.binar.backendonlinecourseapp.Repository.RoleRepository;
import com.binar.backendonlinecourseapp.Repository.TokenRepository;
import com.binar.backendonlinecourseapp.Repository.UserRepository;
import com.binar.backendonlinecourseapp.Service.UserService;
import com.binar.backendonlinecourseapp.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseHandling<LoginResponse> createJwtToken(LoginRequest jwtRequest) throws Exception {
        ResponseHandling<LoginResponse> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(jwtRequest.getEmail());
        if (user.isPresent() && user.get().getActive() == false){
            response.setMessage("user account not activated yet");
            response.setErrors(true);
            return response;
        }
        try {
            String username = jwtRequest.getEmail();
            String password = jwtRequest.getPassword();

            authenticate(username, password);

            final UserDetails userDetails = loadUserByUsername(username);

            String newToken = jwtUtil.generateToken(userDetails);

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            Set<String> authorityStrings = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            String authoritiesString = String.join(",", authorityStrings);

            response.setData(new LoginResponse(newToken, authoritiesString));
            response.setMessage("Authentication successful");
            response.setErrors(false);
        }catch (Exception e){
            response.setErrors(true);
            response.setMessage("Authentication failed: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseGetUser getUser() {
        String email = getAuth();
        Optional<User> user = userRepository.findByEmail(email);
        User user1 = user.get();
        ResponseGetUser responseGetUser = new ResponseGetUser();
        responseGetUser.setNama(user1.getNama());
        responseGetUser.setEmail(user1.getEmail());
        responseGetUser.setTelp(user1.getTelp());
        return responseGetUser;
    }

    @Transactional
    @Override
    public ResponseHandling<UpdateDataResponse> updateUser(UpdateDataRequest updateDataRequest) throws Exception {
        ResponseHandling<UpdateDataResponse> response = new ResponseHandling<>();
        String email = getAuth();
        Optional<User> user = userRepository.findByEmail(email);

        User changeUser = user.get();
        changeUser.setNama(updateDataRequest.getNama());
        changeUser.setCountry(updateDataRequest.getNegara());
        changeUser.setCity(updateDataRequest.getKota());

        UpdateDataResponse updateDataResponse = new UpdateDataResponse();
        updateDataResponse.setNama(updateDataRequest.getNama());
        updateDataResponse.setNegara(updateDataRequest.getNegara());
        updateDataResponse.setKota(updateDataRequest.getKota());

        response.setData(updateDataResponse);
        response.setMessage("success update data");
        response.setErrors(false);

        return response;
    }

    @Transactional
    @Override
    public ResponseHandling<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest) throws Exception {
        ResponseHandling<ChangePasswordResponse> response = new ResponseHandling<>();
        String email = getAuth();
        Optional<User> user = userRepository.findByEmail(email);

        if (passwordEncoder.matches(changePasswordRequest.getOldpassword(), user.get().getPassword())) {
            User changeUser = user.get();
            String encodePassword = encodePasswordMethod(changePasswordRequest.getNewpassword());
            changeUser.setPassword(encodePassword);
            userRepository.save(changeUser);

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(changeUser.getEmail());
            loginRequest.setPassword(changePasswordRequest.getNewpassword());

            ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse();
            changePasswordResponse.setToken(createJwtToken(loginRequest).getData().getToken());
            response.setData(changePasswordResponse);
            response.setMessage("success update password");
            response.setErrors(false);
            return response;
        }
        else {
            response.setMessage("old Password is wrong");
            response.setErrors(true);
            return response;
        }
    }

    @Transactional
    @Override
    public ResponseHandling<RegisterResponse> register(RegisterRequest registerRequest) throws Exception {
        ResponseHandling<RegisterResponse> response = new ResponseHandling<>();

        try {
            Optional<User> isUserByEmailExists = userRepository.findByEmail(registerRequest.getEmail());
            Optional<User> isUserByTelpExists = userRepository.findByTelp(registerRequest.getTelp());
            if (isUserByEmailExists.isPresent() || isUserByTelpExists.isPresent()) {
                response.setMessage("email/telp number invalid");
                response.setErrors(true);
                return response;
            }

            User user = new User();
            user.setNama(registerRequest.getNama());
            user.setEmail(registerRequest.getEmail());
            user.setTelp(registerRequest.getTelp());
            user.setPassword(registerRequest.getPassword());
            user.setActive(false);
            user.setDeleted(false);
            Role role = roleRepository.findByRoleName("USER");
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);

            int otp = getNumericCode();
            String token1 = String.valueOf(otp);
            Token token = new Token();
            token.setToken(token1);
            token.setExpired(Date.from(Instant.now().plusSeconds(120)));
            token.setUser(user);
            tokenRepository.save(token);

            CompletableFuture<Void> emailTask = CompletableFuture.runAsync(() -> sendEmail(registerRequest.getEmail(), token1));

            CompletableFuture.allOf(emailTask).join();

            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setEmail(registerRequest.getEmail());

            response.setData(registerResponse);
            response.setMessage("success register, token already sent");
            response.setErrors(false);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("An error occurred during registration");
            response.setErrors(true);
        }

        return response;

    }

    @Transactional
    @Override
    public ResponseHandling<TokenResponse> tokenCheck(String code, String email) throws Exception {
        ResponseHandling<TokenResponse> response = new ResponseHandling<>();

        Optional<Token> token1 = tokenRepository.findByToken(code);
        Optional<User> user = userRepository.findByEmail(email);

        if (token1.isPresent()){
            if (token1.get().getUser().getEmail() == email || token1.get().getUser().getEmail().equals(email)){
                if (!token1.get().getExpired().before(new Date())){
                    User user1 = user.get();

                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setEmail(user1.getEmail());
                    loginRequest.setPassword(user1.getPassword());

                    user1.setActive(true);
                    String password = encodePasswordMethod(user1.getPassword());
                    user1.setPassword(password);
                    userRepository.save(user1);

                    TokenResponse tokenResponse = new TokenResponse();
                    tokenResponse.setToken(createJwtToken(loginRequest).getData().getToken());

                    tokenRepository.delete(token1.get());
                    response.setData(tokenResponse);
                    response.setMessage("account active");
                    response.setErrors(false);
                    return response;
                }else {
                    response.setMessage("token is invalid");
                    response.setErrors(true);
                    return response;
                }
            }else {
                response.setMessage("token is invalid");
                response.setErrors(true);
                return response;
            }
        }else {
            response.setMessage("token is invalid");
            response.setErrors(true);
            return response;
        }
    }

    @Override
    public ResponseHandling<TokenResendResponse> resendToken(String email) {
        ResponseHandling<TokenResendResponse> response = new ResponseHandling<>();
        Optional<Token> token = tokenRepository.findByUserEmail(email);
        if (!token.isPresent()){
            response.setMessage("email not found");
            response.setErrors(true);
            return response;
        }
        int otp = getNumericCode();
        String otp1 = String.valueOf(otp);
        Token token1 = token.get();
        token1.setToken(otp1);
        token1.setExpired(Date.from(Instant.now().plusSeconds(120)));
        tokenRepository.save(token1);

        sendEmail(email, otp1);
        TokenResendResponse resendResponse = new TokenResendResponse();
        resendResponse.setEmail(email);
        response.setData(resendResponse);
        response.setMessage("token already sent");
        response.setErrors(false);
        return response;
    }

    public void sendEmail(String toEmail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cokicilox@gmail.com");
        message.setTo(toEmail);
        message.setText("To verify your email address, please use the following One Time Password (OTP): \n" +
                otp + "\n Do not share this OTP with anyone.");
        message.setSubject("Verify your new Course account");
        javaMailSender.send(message);
    }

    private String encodePasswordMethod(String password) {
        String passsword = passwordEncoder.encode(password);
        return passsword;
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch (DisabledException e){
            throw new Exception("User is Disable");
        }catch (BadCredentialsException e){
            System.out.println(e);
            throw new Exception("Bad Credential from user");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).get();
        if (user != null){
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(), user.getPassword(),getAuthorities(user)
            );
        }else {
            throw new UsernameNotFoundException("Username is not found");
        }
    }

    private Set getAuthorities(User user){
        Set authorities = new HashSet();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });
        return authorities;
    }

    private int getNumericCode() {
        UUID uuid = UUID.randomUUID();

        int hashCode = Math.abs(uuid.hashCode());

        int numericCode = (hashCode % 900_000) + 100_000;

        return numericCode;
    }

    private String GetUUIDCode() {
        UUID uuid = UUID.randomUUID();
        String kode = uuid.toString().substring(0, 6);
        return kode;
    }

    private String getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return email;
    }

}
