package com.hrms.app;

import com.hrms.app.entity.UserRole;
import com.hrms.app.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@EnableScheduling
@ComponentScan(basePackages = "com.hrms.app")
@EntityScan(basePackages = {"com.hrms.app.entity"})
@SpringBootApplication
public class HrmsApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	UserRoleRepository userRoleRepository;


	public static void main(String[] args) {
		SpringApplication.run(HrmsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			UserRole role1 = new UserRole();
			UserRole role2 = new UserRole();

			role1.setId(501);
			role1.setRole("ADMIN_USER");


			role2.setId(502);
			role2.setRole("NORMAL_USER");



			List<UserRole> roles = List.of(role1,role2);

			List<UserRole> result = this.userRoleRepository.saveAll(roles);
			result.forEach(r ->{
				System.out.println(r.getRole());
			});

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
