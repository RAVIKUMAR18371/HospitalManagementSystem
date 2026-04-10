package com.HMS.HospitalManagementSystem.bootstrap;
 
import com.HMS.HospitalManagementSystem.entities.Role;
import com.HMS.HospitalManagementSystem.entities.RoleEnum;
import com.HMS.HospitalManagementSystem.repositories.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
 
@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
 
    private final RoleRepository roleRepository;
 
    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
 
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Seed all roles if they don't already exist
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleRepository.findByName(roleEnum).isEmpty()) {
                Role role = new Role();
                role.setName(roleEnum);
                roleRepository.save(role);
                System.out.println("Seeded role: " + roleEnum);
            }
        }
    }
}