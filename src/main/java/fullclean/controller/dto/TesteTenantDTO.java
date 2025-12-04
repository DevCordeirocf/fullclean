package fullclean.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TesteTenantDTO {
    @NotBlank
    private String nome;
}
