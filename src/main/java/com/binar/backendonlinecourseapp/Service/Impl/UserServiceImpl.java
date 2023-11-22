package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Request.LoginRequest;
import com.binar.backendonlinecourseapp.DTO.Request.RegisterRequest;
import com.binar.backendonlinecourseapp.DTO.Request.UpdateDataRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import com.binar.backendonlinecourseapp.Entity.Role;
import com.binar.backendonlinecourseapp.Entity.User;
import com.binar.backendonlinecourseapp.Repository.RoleRepository;
import com.binar.backendonlinecourseapp.Repository.UserRepository;
import com.binar.backendonlinecourseapp.Service.UserService;
import com.binar.backendonlinecourseapp.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseHandling<LoginResponse> createJwtToken(LoginRequest jwtRequest) throws Exception {
        ResponseHandling<LoginResponse> response = new ResponseHandling<>();
        try {
            String username = jwtRequest.getEmail();
            String password = jwtRequest.getPassword();

            authenticate(username, password);

            final UserDetails userDetails = loadUserByUsername(username);

            String newToken = jwtUtil.generateToken(userDetails);

            response.setData(new LoginResponse(newToken));
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

        if (passwordEncoder.matches(updateDataRequest.getOldpassword(), user.get().getPassword())){
            User changeUser = user.get();
            changeUser.setNama(updateDataRequest.getNama());
            if (userRepository.findByEmail(updateDataRequest.getEmail()).isPresent()){
                if (updateDataRequest.getEmail().equals(user.get().getEmail()) || updateDataRequest.getEmail() == user.get().getEmail()){
                    changeUser.setEmail(updateDataRequest.getEmail());
                }else {
                    response.setMessage("email is already exists");
                    response.setErrors(true);
                    return response;
                }
            }
            System.out.println(user.get().getEmail());
            System.out.println(changeUser.getEmail());
            if (userRepository.findByTelp(updateDataRequest.getTelp()).isPresent()){
                if (updateDataRequest.getTelp().equals(user.get().getTelp()) ||updateDataRequest.getTelp() == user.get().getTelp()){
                    changeUser.setTelp(updateDataRequest.getTelp());
                }else {
                    response.setMessage("telp is already exists");
                    response.setErrors(true);
                    return response;
                }
            }
            System.out.println(user.get().getTelp());
            System.out.println(changeUser.getTelp());
            String encodePasswordd = encodePasswordMethod(updateDataRequest.getNewpassword());
            changeUser.setPassword(encodePasswordd);

            userRepository.save(changeUser);

            UpdateDataResponse updateDataResponse = new UpdateDataResponse();
            updateDataResponse.setNama(updateDataRequest.getNama());
            updateDataResponse.setEmail(updateDataRequest.getEmail());
            updateDataResponse.setTelp(updateDataRequest.getTelp());

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(updateDataRequest.getEmail());
            loginRequest.setPassword(updateDataRequest.getNewpassword());

            updateDataResponse.setToken(createJwtToken(loginRequest).getData().getToken());

            response.setData(updateDataResponse);
            response.setMessage("success update data");
            response.setErrors(false);

            return response;

        }else {
            response.setMessage("cant update data wrong old password");
            response.setErrors(true);
            return response;
        }
    }

    @Override
    public ResponseHandling<RegisterResponse> register(RegisterRequest registerRequest) throws Exception {
        ResponseHandling<RegisterResponse> response = new ResponseHandling<>();
        Optional<User> isUserByEmailExists = userRepository.findByEmail(registerRequest.getEmail());
        Optional<User> isUserByTelpExists = userRepository.findByTelp(registerRequest.getTelp());
        if (isUserByEmailExists.isPresent() || isUserByTelpExists.isPresent()){
            response.setMessage("email/telp number invalid");
            response.setErrors(true);
            return response;
        }

        User user = new User();
        user.setNama(registerRequest.getNama());
        user.setEmail(registerRequest.getEmail());
        user.setTelp(registerRequest.getTelp());
        String password = encodePasswordMethod(registerRequest.getPassword());
        user.setPassword(password);

        Role role = roleRepository.findByRoleName("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);

        RegisterResponse registerResponse = new RegisterResponse();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(registerRequest.getEmail());
        loginRequest.setPassword(registerRequest.getPassword());

        registerResponse.setToken(createJwtToken(loginRequest).getData().getToken());

        response.setData(registerResponse);
        response.setMessage("success register");
        response.setErrors(false);
        return response;
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



    private String GetUUIDCode() {
        UUID uuid = UUID.randomUUID();
        String kode = uuid.toString().substring(0, 5);
        return kode;
    }

    private String getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return email;
    }

}
