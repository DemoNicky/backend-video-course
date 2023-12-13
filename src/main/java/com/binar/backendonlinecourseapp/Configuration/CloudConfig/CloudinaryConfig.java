package com.binar.backendonlinecourseapp.Configuration.CloudConfig;

import com.binar.backendonlinecourseapp.Service.Impl.UserServiceImpl;
import com.binar.backendonlinecourseapp.Service.UserService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "duzctbrt5",
                "api_key", "768613334472254",
                "api_secret", "YbuUq2xSiLvsOUJiy3dNblxOhAE"));
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl(cloudinary());
    }

}
