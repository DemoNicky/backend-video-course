package com.binar.backendonlinecourseapp.Controller;
import com.binar.backendonlinecourseapp.DTO.Request.ChangePasswordRequest;
import com.binar.backendonlinecourseapp.DTO.Request.LoginRequest;
import com.binar.backendonlinecourseapp.DTO.Request.RegisterRequest;
import com.binar.backendonlinecourseapp.DTO.Request.UpdateDataRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import com.binar.backendonlinecourseapp.DTO.Response.LoginResponse;
import com.binar.backendonlinecourseapp.DTO.Response.RegisterResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseGetUser;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.Entity.Enum.CardType;
import com.binar.backendonlinecourseapp.Entity.User;
import com.binar.backendonlinecourseapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<RegisterResponse>>register(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {
        ResponseHandling<RegisterResponse> response = userService.register(registerRequest);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = "/otp/{code}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<TokenResponse>>tokenCheck(@PathVariable("code")String code) throws Exception {
        ResponseHandling<TokenResponse> response = userService.tokenCheck(code);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = "/resend-otp/{email}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<TokenResendResponse>>resendToken(@PathVariable("email")String email){
        ResponseHandling<TokenResendResponse> response = userService.resendToken(email);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<LoginResponse>> login(@Valid @RequestBody LoginRequest jwtRequest) throws Exception {
        ResponseHandling<LoginResponse> response = userService.createJwtToken(jwtRequest);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(
            path = "/admin-login"
    )
    public ResponseEntity<ResponseHandling<LoginResponse>> adminLogin(@RequestBody LoginRequest loginRequest){
        ResponseHandling<LoginResponse> response = userService.adminLogin(loginRequest);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseGetUser> getData(){
        ResponseGetUser response = userService.getUser();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<UpdateDataResponse>>updateData(@RequestBody UpdateDataRequest updateDataRequest) throws Exception {
        ResponseHandling<UpdateDataResponse> response = userService.updateUser(updateDataRequest);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(
            path = "/change-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<ChangePasswordResponse>>changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws Exception {
        ResponseHandling<ChangePasswordResponse> response = userService.changePassword(changePasswordRequest);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping(
            path = "/forgot-password",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<ForgetPasswordEmailResponse>>forgetPassword(@RequestParam String email){
        ResponseHandling<ForgetPasswordEmailResponse> response = userService.forgetPassword(email);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PutMapping(
            path = "/set-forgot-password",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<ForgetPasswordResponse>>setForgetPassword(@RequestParam String email,
                                                                                     @RequestParam String code,
                                                                                     @RequestParam String newPassword) throws Exception {
        ResponseHandling<ForgetPasswordResponse> response = userService.setForgetPassword(email, code, newPassword);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping(
            path = "/update-profil-pic",
            consumes = "multipart/form-data",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<ChangeProfilePictureResponse>> changePicture(@RequestParam MultipartFile multipartFile) {
        ResponseHandling<ChangeProfilePictureResponse> response = userService.insertPicture(multipartFile);
        if (response.getData() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


    @GetMapping(
            path = "/get-user-profile-pic"
    )
    public ResponseEntity<ResponseHandling<GetUserProfilePicture>>getPictureUser(){
        ResponseHandling<GetUserProfilePicture> response = userService.getPictureUser();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping(
            path = "/create-bank-account"
    )
    public ResponseEntity<String>addUserbalance(@RequestParam BigDecimal balance,
                                                @RequestParam String cardnumber,
                                                @RequestParam CardType cardType){
        String response = userService.addUserBalance(balance, cardnumber, cardType);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
