package com.HMS.HospitalManagementSystem.bootstrap;

import com.HMS.HospitalManagementSystem.entities.Role;
import com.HMS.HospitalManagementSystem.entities.RoleEnum;
import com.HMS.HospitalManagementSystem.entities.User;
import com.HMS.HospitalManagementSystem.repositories.RoleRepository;
import com.HMS.HospitalManagementSystem.repositories.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public SuperAdminSeeder(UserRepository userRepository, RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepository.findByEmail("superadmin@gmail.com").isEmpty()) {
            User superAdmin = new User();
            superAdmin.setEmail("superadmin@gmail.com");
            superAdmin.setPassword(passwordEncoder.encode("super123"));

            Role superAdminRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN)
                    .orElseThrow(() -> new RuntimeException("SUPER_ADMIN Role not found — ensure RoleSeeder runs first"));
            superAdmin.setRole(superAdminRole);
            userRepository.save(superAdmin);
            System.out.println("SuperAdmin created: superadmin@gmail.com / super123");
        }
    }
}
