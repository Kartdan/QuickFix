package com.ias.quickfix.controller;

import com.ias.quickfix.dto.ClientDto;
import com.ias.quickfix.dto.ProviderDto;
import com.ias.quickfix.exceptions.UserAlreadyExistsException;
import com.ias.quickfix.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/clients")
    public ResponseEntity<?> saveClient(@RequestBody ClientDto clientDto) {
        try {
            return ResponseEntity.ok(userService.saveClient(clientDto));
        } catch (UserAlreadyExistsException e) {
            log.error("User already exists: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.CONFLICT.value()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }

    }

    @GetMapping("/clients")
    public ResponseEntity<?> getAllClients() {
        try {
            List<ClientDto> clients = userService.getAllClients();
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/clients/{email}")
    public ResponseEntity<?> getClientByEmail(@PathVariable String email) {
        try {
            ClientDto client = userService.getClientByEmail(email);
            return ResponseEntity.ok(client);
        } catch (RuntimeException e) { // Customize exception handling as needed
            log.error("Client not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PutMapping("/clients/{email}")
    public ResponseEntity<?> updateClient(@PathVariable String email, @RequestBody ClientDto clientDto) {
        try {
            return ResponseEntity.ok(userService.updateClient(email, clientDto));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/clients/{email}")
    public ResponseEntity<?> deleteClient(@PathVariable String email) {
        try {
            userService.deleteClient(email);
            return ResponseEntity.ok(Map.of("message", "Client deleted successfully"));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    // providers endpoints
    @PostMapping("/providers")
    public ResponseEntity<?> saveProvider(@RequestBody ProviderDto providerDto) {
        try {
            return ResponseEntity.ok(userService.saveProvider(providerDto));
        } catch (UserAlreadyExistsException e) {
            log.error("Provider already exists: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.CONFLICT.value()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/providers")
    public ResponseEntity<?> getAllProviders() {
        try {
            List<ProviderDto> providers = userService.getAllProviders();
            return ResponseEntity.ok(providers);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("providers/{email}")
    public ResponseEntity<?> getProviderByEmail(@PathVariable String email) {
        try {
            ProviderDto provider = userService.getProviderByEmail(email);
            return ResponseEntity.ok(provider);
        } catch (RuntimeException e) {
            log.error("Provider not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Provider not found");
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PutMapping("providers/{email}")
    public ResponseEntity<?> updateProvider(@PathVariable String email, @RequestBody ProviderDto providerDto) {
        try {
            return ResponseEntity.ok(userService.updateProvider(email, providerDto));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @DeleteMapping("providers/{email}")
    public ResponseEntity<?> deleteProvider(@PathVariable String email) {
        try {
            userService.deleteProvider(email);
            return ResponseEntity.ok(Map.of("message", "Provider deleted successfully"));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }



}
