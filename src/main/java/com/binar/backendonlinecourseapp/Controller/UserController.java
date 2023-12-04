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
import com.binar.backendonlinecourseapp.Entity.User;
import com.binar.backendonlinecourseapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ResponseEntity<ResponseHandling<RegisterResponse>>register(@RequestBody RegisterRequest registerRequest) throws Exception {
        ResponseHandling<RegisterResponse> response = userService.register(registerRequest);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = "/otp/{code}/{email}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<TokenResponse>>tokenCheck(@PathVariable("code")String code, @PathVariable("email")String email) throws Exception {
        ResponseHandling<TokenResponse> response = userService.tokenCheck(code, email);
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
    public ResponseEntity<ResponseHandling<LoginResponse>> login(@RequestBody LoginRequest jwtRequest) throws Exception {
        ResponseHandling<LoginResponse> response = userService.createJwtToken(jwtRequest);
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
    public ResponseEntity<ResponseHandling<ChangePasswordResponse>>changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) throws Exception {
        ResponseHandling<ChangePasswordResponse> response = userService.changePassword(changePasswordRequest);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping(
            path = "/update-profil-pic"
    )
    public ResponseEntity<ResponseHandling<ChangeProfilePictureResponse>>changePicture(@RequestHeader MultipartFile multipartFile){
        ResponseHandling<ChangeProfilePictureResponse> response = userService.insertPicture(multipartFile);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
