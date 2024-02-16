package com.example.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ControllerSupportService {
    void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
