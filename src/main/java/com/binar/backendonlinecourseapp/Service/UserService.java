package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Request.*;
import com.binar.backendonlinecourseapp.DTO.Response.*;

public interface UserService {

    ResponseHandling<RegisterResponse> register(RegisterRequest registerRequest) throws Exception;

    ResponseHandling<LoginResponse> createJwtToken(LoginRequest jwtRequest) throws Exception;

    ResponseGetUser getUser();

    ResponseHandling<UpdateDataResponse> updateUser(UpdateDataRequest updateDataRequest) throws Exception;

    ResponseHandling<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest) throws Exception;

    ResponseHandling<TokenResponse> tokenCheck(String code, String email) throws Exception;

    ResponseHandling<TokenResendResponse> resendToken(String email);

}
