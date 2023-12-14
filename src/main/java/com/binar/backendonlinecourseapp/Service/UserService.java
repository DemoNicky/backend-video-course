package com.binar.backendonlinecourseapp.Service;

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
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface UserService {

    ResponseHandling<RegisterResponse> register(RegisterRequest registerRequest) throws Exception;

    ResponseHandling<LoginResponse> createJwtToken(LoginRequest jwtRequest) throws Exception;

    ResponseGetUser getUser();

    ResponseHandling<UpdateDataResponse> updateUser(UpdateDataRequest updateDataRequest) throws Exception;

    ResponseHandling<TokenResponse> tokenCheck(String code) throws Exception;

    ResponseHandling<TokenResendResponse> resendToken(String email);

    ResponseHandling<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest) throws Exception;

    ResponseHandling<ChangeProfilePictureResponse> insertPicture(MultipartFile multipartFile);

    ResponseHandling<ForgetPasswordEmailResponse> forgetPassword(String email);

    ResponseHandling<ForgetPasswordResponse> setForgetPassword(String email, String code, String newPassword) throws Exception;

    ResponseHandling<GetUserProfilePicture> getPictureUser();

    String addUserBalance(BigDecimal balance, String cardnumber, CardType cardType);
}
