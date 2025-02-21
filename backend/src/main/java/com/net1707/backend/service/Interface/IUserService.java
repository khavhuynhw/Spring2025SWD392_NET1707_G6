package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.UpdateInfoUserDTO;
import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Staff;

public interface IUserService {
    public Customer updateCustomer(String email, UpdateInfoUserDTO updatedCustomer);
    public Staff updateStaff(String email, UpdateInfoUserDTO updatedStaff);
}
