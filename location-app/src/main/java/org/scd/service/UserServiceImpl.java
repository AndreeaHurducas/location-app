package org.scd.service;

import org.scd.config.exception.BusinessException;
import org.scd.model.User;
import org.scd.model.dto.UserLoginDTO;
import org.scd.model.dto.UserRegistrationDTO;
import org.scd.model.security.Role;
import org.scd.repository.RoleRepository;
import org.scd.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User login(UserLoginDTO userLoginDTO) throws BusinessException {

        if (Objects.isNull(userLoginDTO)) {
            throw new BusinessException(401, "Body null !");
        }

        if (Objects.isNull(userLoginDTO.getEmail())) {
            throw new BusinessException(400, "Email cannot be null ! ");
        }

        if (Objects.isNull(userLoginDTO.getPassword())) {
            throw new BusinessException(400, "Password cannot be null !");
        }

        final User user = userRepository.findByEmail(userLoginDTO.getEmail());

        if (Objects.isNull(user)) {
            throw new BusinessException(401, "Bad credentials !");
        }

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "Bad credentials !");
        }

        return user;
    }

    @Override
    public User loginAdmin(UserLoginDTO userLoginDTO) throws BusinessException {

        if (Objects.isNull(userLoginDTO)) {
            throw new BusinessException(401, "Body null !");
        }

        if (Objects.isNull(userLoginDTO.getEmail())) {
            throw new BusinessException(400, "Email cannot be null ! ");
        }

        if (Objects.isNull(userLoginDTO.getPassword())) {
            throw new BusinessException(400, "Password cannot be null !");
        }

        final User user = userRepository.findByEmail(userLoginDTO.getEmail());

        if (Objects.isNull(user)) {
            throw new BusinessException(401, "Bad credentials !");
        }

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "Bad credentials !");
        }


        boolean isAdmin = false;
        String adminRole = "ADMIN";
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRole().equals(adminRole)) {
                isAdmin = true;
            }
        }

        if(!isAdmin) throw new BusinessException(401,"Unauthorized!");

        return user;
    }

    @Override
    public User register(UserRegistrationDTO userRegistrationDTO) throws BusinessException {

        if (Objects.isNull(userRegistrationDTO)) {
            throw new BusinessException(401, "Body null ! ");
        }

        if (Objects.isNull(userRegistrationDTO.getFirstName())) {
            throw new BusinessException(400, " First Name cannot be null !");
        }

        if (Objects.isNull(userRegistrationDTO.getLastName())) {
            throw new BusinessException(400, " Last Name cannot be null !");
        }

        if (Objects.isNull(userRegistrationDTO.getEmail())) {
            throw new BusinessException(400, " Email cannot be null !");
        }

        if (userRepository.findByEmail(userRegistrationDTO.getEmail()) != null) {
            throw new BusinessException(409, "User already exists for this email!");
        }
        if (Objects.isNull(userRegistrationDTO.getPassword())) {
            throw new BusinessException(400, " Password cannot be null !");
        }

        userRegistrationDTO.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        Role userRole = roleRepository.findByRole("BASIC_USER");

        User user = new User();
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(userRegistrationDTO.getPassword());
        Set<Role> roles = Stream.of(userRole)
                .collect(Collectors.toCollection(HashSet::new));
        user.setRoles(roles);

        userRepository.save(user);

        return user;
    }
}
