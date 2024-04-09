package com.ecommerce.shop.config;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.shop.entity.Order;
import com.ecommerce.shop.entity.Role;
import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.enums.OrderStatus;
import com.ecommerce.shop.repository.OrderRepository;
import com.ecommerce.shop.repository.RoleRepository;
import com.ecommerce.shop.repository.UserRepository;

@Component
@Transactional
public class AdminAccountInitializer implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrderRepository orderRepository;

    public AdminAccountInitializer(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!adminAccountExists()) {
            createAdminAccount();
        }
    }

    private boolean adminAccountExists() {
        return this.userRepository.existsByEmail("dinesh7627000@gmail.com");
    }

    private void createAdminAccount() {
        User admin = new User();
        admin.setFirstname("Dinesh");
        admin.setLastname("Kumawat");
        admin.setEmail("dinesh7627000@gmail.com");
        admin.setPhone("7627000907");
        admin.setPassword(passwordEncoder.encode("Admin"));
        LocalDateTime currenDateTime = LocalDateTime.now();
        admin.setCreatedAt(currenDateTime);
        Role role = this.roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = createRole();
        }
        admin.setRoles(Arrays.asList(role));
        this.userRepository.save(admin);

        Order order = new Order();
        order.setAmount(0);
        order.setTotalAmount(0);
        order.setDiscount(0);
        order.setUser(admin);
        order.setOrderStatus(OrderStatus.Pending);
        this.orderRepository.save(order);
    }

    private Role createRole() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return this.roleRepository.save(role);
    }

}
