package org.scd.service;

import org.scd.config.exception.BusinessException;
import org.scd.model.User;
import org.scd.model.dto.UserLoginDTO;
import org.scd.model.dto.UserRegistrationDTO;

import java.util.List;

public interface UserService {
    /**
     * Get existing list of users from database
     *
     * @return
     */
    List<User> getUsers();

    /**
     * Login into application
     *
     * @param userLoginDTO - user information
     * @return
     */
    User login(final UserLoginDTO userLoginDTO) throws BusinessException;


    /**
     * Admin Login into app
     *
     * @param userLoginDTO
     * @return
     * @throws BusinessException
     */
    User loginAdmin(final UserLoginDTO userLoginDTO) throws BusinessException;


    /**
     * Registration
     *
     * @param userRegistrationDTO - information for registration
     * @return
     * @throws BusinessException
     */

   User register(final UserRegistrationDTO userRegistrationDTO) throws BusinessException;
}
