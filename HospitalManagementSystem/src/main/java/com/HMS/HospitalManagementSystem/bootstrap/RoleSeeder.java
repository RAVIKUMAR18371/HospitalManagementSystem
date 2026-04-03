package com.HMS.HospitalManagementSystem.bootstrap;

import java.util.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import com.HMS.HospitalManagementSystem.entities.Role;
import com.HMS.HospitalManagementSystem.entities.RoleEnum;
import com.HMS.HospitalManagementSystem.repositories.RoleRepository;

@Component // ye annotation spring ko automat
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {// This class will listen to the event when the application context is refreshed and then it will seed the roles in the database if they are not already present.
    private final RoleRepository roleRepository;
    public RoleSeeder(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){ 
        Arrays.stream(RoleEnum.values()).forEach(roleName -> {
            if(roleRepository.findByName(roleName).isEmpty()){
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        });
    }

    
}
