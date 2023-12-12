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
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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

    private final Cloudinary cloudinary;

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
            response.setMessage("Invalid Email and Password Combination");
        }
        return response;
    }

    @Override
    public ResponseGetUser getUser() {
        String email = getAuth();
        Optional<User> user = userRepository.findByEmail(email);
        User user1 = user.get();
        ResponseGetUser responseGetUser = new ResponseGetUser();
        responseGetUser.setUrlPicture(user1.getPictureUrl());
        responseGetUser.setNama(user1.getNama());
        responseGetUser.setEmail(user1.getEmail());
        responseGetUser.setTelp(user1.getTelp());
        responseGetUser.setNegara(user1.getCountry());
        responseGetUser.setKota(user1.getCity());
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
        changeUser.setNama(updateDataRequest.getNama());
        changeUser.setCountry(updateDataRequest.getNegara());
        changeUser.setCity(updateDataRequest.getKota());
        userRepository.save(changeUser);

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

            String encodePasswordd = encodePasswordMethod(changePasswordRequest.getNewpassword());

            changeUser.setPassword(encodePasswordd);

            userRepository.save(changeUser);

            ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse();

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(changeUser.getEmail());
            loginRequest.setPassword(changePasswordRequest.getNewpassword());

            changePasswordResponse.setToken(createJwtToken(loginRequest).getData().getToken());
            response.setData(changePasswordResponse);
            response.setMessage("success update password");
            response.setErrors(false);
            return response;
        }else {
            response.setMessage("old Password is wrong");
            response.setErrors(true);
            return response;
        }
    }

    @Override
    public ResponseHandling<ChangeProfilePictureResponse> insertPicture(MultipartFile multipartFile) {
        ResponseHandling<ChangeProfilePictureResponse> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        try {
            Map<?, ?> result = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = result.get("url").toString();

            User userChange = user.get();
            userChange.setPictureUrl(imageUrl);
            userRepository.save(userChange);

            ChangeProfilePictureResponse changeProfilePictureResponse = new ChangeProfilePictureResponse();
            changeProfilePictureResponse.setUrl(imageUrl);

            response.setData(changeProfilePictureResponse);
            response.setMessage("success update profile picture");
            response.setErrors(false);
            return response;
        }catch (IOException e) {
            e.printStackTrace();
        }
        response.setMessage("failed update picture");
        response.setErrors(true);

        return response;
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

            final String url = "http://res.cloudinary.com/duzctbrt5/image/upload/v1701317552/ittcwcftomal7kgpspg7.jpg";

            User user = new User();
            user.setPictureUrl(url);
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
    public ResponseHandling<TokenResponse> tokenCheck(String code) throws Exception {
        ResponseHandling<TokenResponse> response = new ResponseHandling<>();

        Optional<Token> token1 = tokenRepository.findByToken(code);
        Optional<User> user = userRepository.findByEmail(token1.get().getUser().getEmail());

        if (!token1.isPresent()){
            response.setMessage("token is invalid");
            response.setErrors(true);
            return response;
        }else {
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
        resendResponse.setToken(otp1);
        resendResponse.setEmail(email);
        response.setData(resendResponse);
        response.setMessage("token already sent");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<ForgetPasswordEmailResponse> forgetPassword(String email) {
        ResponseHandling<ForgetPasswordEmailResponse> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Token> token2 = tokenRepository.findByUserEmail(email);
        if (!user.isPresent()){
            response.setMessage("user with email " + email + " not found");
            response.setErrors(true);
        }
        if (token2.isPresent()){
            tokenRepository.delete(token2.get());
        }
        int otp = getNumericCode();
        String token1 = String.valueOf(otp);
        sendEmailForgetPassword(email, token1);
        Token token = new Token();
        token.setToken(token1);
        token.setUser(user.get());
        token.setExpired(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));
        tokenRepository.save(token);
        ForgetPasswordEmailResponse forgetPasswordEmailResponse = new ForgetPasswordEmailResponse();
        forgetPasswordEmailResponse.setEmail(email);
        response.setData(forgetPasswordEmailResponse);
        response.setMessage("Your email has been successfully verified. Please check your email message");
        response.setErrors(false);
        return response;
    }

    @Transactional
    @Override
    public ResponseHandling<ForgetPasswordResponse> setForgetPassword(String email, String code, String newPassword) throws Exception {
        ResponseHandling<ForgetPasswordResponse> response = new ResponseHandling<>();
        Optional<Token> token = tokenRepository.findByToken(code);
        Optional<User> user = userRepository.findByEmail(email);
        if (!token.isPresent()){
            response.setMessage("code invalid");
            response.setErrors(true);
            return response;
        }
        if (token.get().getUser() != user.get()){
            response.setMessage("email and code invalid");
            response.setErrors(true);
            return response;
        }
        User user1 = user.get();
        String password = encodePasswordMethod(newPassword);
        user1.setPassword(password);
        userRepository.save(user1);

        tokenRepository.delete(token.get());

        sendEmailSuccessChangePassword(email);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(newPassword);
        ForgetPasswordResponse forgetPasswordResponse = new ForgetPasswordResponse();
        forgetPasswordResponse.setToken(createJwtToken(loginRequest).getData().getToken());

        response.setData(forgetPasswordResponse);
        response.setMessage("Your password has been changed");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<GetUserProfilePicture> getPictureUser() {
        ResponseHandling<GetUserProfilePicture> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        GetUserProfilePicture getUserProfilePicture = new GetUserProfilePicture();
        getUserProfilePicture.setImageUrl(user.get().getPictureUrl());
        response.setData(getUserProfilePicture);
        response.setMessage("success get user picture");
        response.setErrors(false);
        return response;
    }

    public void sendEmailSuccessChangePassword(String toEmail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cokicilox@gmail.com");
        message.setTo(toEmail);
        message.setText(
                "Congratulations! Your password has been changed successfully. Please use your new password to log in"
        );
        message.setSubject("Your password has been successfully updated");
        javaMailSender.send(message);
    }

    public void sendEmailForgetPassword(String toEmail, String code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cokicilox@gmail.com");
        message.setTo(toEmail);
        message.setText(String.format(
                "Someone requested that the password be reset for the following account\n" +
                        "To reset your password, visit the following address: \n\n" +
                        "http://localhost:5173/reset-password?email=%s&code=%s", toEmail, code) +
                "\n\n Do not share this link with anyone."
        );
        message.setSubject("Binar Course Reset Password!!!");
        javaMailSender.send(message);
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
        User user = userRepository.findUserByEmailOrtelp(username).get();
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