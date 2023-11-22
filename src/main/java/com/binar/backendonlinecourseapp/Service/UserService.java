package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Request.LoginRequest;
import com.binar.backendonlinecourseapp.DTO.Request.RegisterRequest;
import com.binar.backendonlinecourseapp.DTO.Request.UpdateDataRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import com.binar.backendonlinecourseapp.DTO.Response.LoginResponse;
import com.binar.backendonlinecourseapp.DTO.Response.RegisterResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseGetUser;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;

public interface UserService {

    ResponseHandling<RegisterResponse> register(RegisterRequest registerRequest) throws Exception;

    ResponseHandling<LoginResponse> createJwtToken(LoginRequest jwtRequest) throws Exception;

    ResponseGetUser getUser();

    ResponseHandling<UpdateDataResponse> updateUser(UpdateDataRequest updateDataRequest) throws Exception;

}
