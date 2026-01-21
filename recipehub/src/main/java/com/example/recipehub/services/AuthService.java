package com.example.recipehub.services;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import com.example.recipehub.dto.LoginDto;
import com.example.recipehub.dto.RegisterDto;
import com.example.recipehub.models.entities.User;

public interface AuthService {

    boolean login(LoginDto dto, HttpServletRequest request, HttpServletResponse response);


    void register(RegisterDto dto);
}

