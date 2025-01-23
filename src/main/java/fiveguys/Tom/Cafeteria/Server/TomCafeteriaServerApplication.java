package fiveguys.Tom.Cafeteria.Server;

import fiveguys.Tom.Cafeteria.Server.auth.feignClient.config.FeignConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableFeignClients(defaultConfiguration = FeignConfig.class)
@EnableJpaAuditing
@EnableScheduling
// 수동으로 FeignAutoConfiguration 가져오기
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableAsync
public class TomCafeteriaServerApplication {
	@PostConstruct
	public void init() {
		// JVM 기본 시간대를 설정합니다.
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(TomCafeteriaServerApplication.class, args);
	}

}
