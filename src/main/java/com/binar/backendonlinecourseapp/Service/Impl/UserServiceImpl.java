package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Request.LoginRequest;
import com.binar.backendonlinecourseapp.DTO.Request.RegisterRequest;
import com.binar.backendonlinecourseapp.DTO.Response.LoginResponse;
import com.binar.backendonlinecourseapp.DTO.Response.RegisterResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseGetUser;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        User user1 = user.get();
        ResponseGetUser responseGetUser = new ResponseGetUser();
        responseGetUser.setNama(user1.getNama());
        responseGetUser.setEmail(user1.getEmail());
        responseGetUser.setTelp(user1.getTelp());
        return responseGetUser;
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
        String password = passwordEncoder.encode(registerRequest.getPassword());
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



}
