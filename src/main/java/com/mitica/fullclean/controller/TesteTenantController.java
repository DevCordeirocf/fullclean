package com.mitica.fullclean.controller;

import com.mitica.fullclean.model.TesteTenant;
import com.mitica.fullclean.service.TesteTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teste")
public class TesteTenantController {

    private final TesteTenantService service;

    @Autowired
    public TesteTenantController(TesteTenantService service) {
        this.service = service;
    }

    @PostMapping("/salvar")
    public TesteTenant salvar(@RequestBody TesteTenant testeTenant) {
        return service.salvar(testeTenant);
    }

    @GetMapping("/todos")
    public List<TesteTenant> buscarTodos() {
        return service.buscarTodos();
    }
}
