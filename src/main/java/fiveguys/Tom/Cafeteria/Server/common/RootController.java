package fiveguys.Tom.Cafeteria.Server.common;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @Value("${version}")
    public String version;
    @GetMapping("/health")
    public String healthCheck(){
        return "i'm healthy!!!";

    }
    @Operation(summary = "서버의 현재 버전을 조회하는 메서드")
    @GetMapping("/version")
    public String versionCheck(){
        return version;
    }
}
