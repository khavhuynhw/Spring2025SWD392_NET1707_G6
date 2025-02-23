package com.net1707.backend.service.Interface;


import com.net1707.backend.dto.request.ChangePasswordRequestDTO;
import com.net1707.backend.dto.request.LoginRequestDTO;
import com.net1707.backend.dto.request.RegisterRequestDTO;
import com.net1707.backend.dto.request.StaffRegisterDTO;

public interface IAuthService {
    String register(RegisterRequestDTO registerRequestDTO);
    String registerStaff(StaffRegisterDTO staffRegisterDTO);
    String login(LoginRequestDTO loginRequestDTO);

    boolean changePassword(Long userId, ChangePasswordRequestDTO dto);
}
