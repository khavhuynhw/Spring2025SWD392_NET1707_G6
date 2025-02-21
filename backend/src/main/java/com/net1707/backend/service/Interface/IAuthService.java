package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.RegisterRequestDTO;
import com.net1707.backend.dto.StaffRegisterDTO;

public interface IAuthService {
    String register(RegisterRequestDTO registerRequestDTO);
    String registerStaff(StaffRegisterDTO staffRegisterDTO);
    String login(LoginRequestDTO loginRequestDTO);
}
