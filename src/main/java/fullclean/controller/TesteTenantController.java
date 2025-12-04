package fullclean.controller;

import fullclean.entity.TesteTenant;
import fullclean.service.TesteTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teste-tenant")
public class TesteTenantController {

    @Autowired
    private TesteTenantService service;


    @PostMapping
    public ResponseEntity<TesteTenant> create(@RequestBody TesteTenant joia) {
        TesteTenant savedJoia = service.save(joia);
        return new ResponseEntity<>(savedJoia, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TesteTenant>> findAll() {
        List<TesteTenant> joias = service.findAll();
        return ResponseEntity.ok(joias);
    }
}
