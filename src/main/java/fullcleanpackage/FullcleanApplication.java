package fullcleanpackage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.web.servlet.ServletComponentScan;
import lombok.experimental.SuperBuilder;

@SpringBootApplication

@ServletComponentScan
public class FullcleanApplication {

		@Bean
	public FilterRegistrationBean<TenantIdFilter> tenantIdFilter() {
		FilterRegistrationBean<TenantIdFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new TenantIdFilter());
		registration.addUrlPatterns("/api/*");
		registration.setOrder(1);
		return registration;
	}

	public static void main(String[] args) {
		SpringApplication.run(FullcleanApplication.class, args);
	}


}
