package br.com.forum_hub.controller;

import br.com.forum_hub.domain.role.RoleDTO.RoleDTO;
import br.com.forum_hub.domain.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("/add-role/{id}")
    public ResponseEntity addRoleToUser(@RequestBody @Valid RoleDTO role, @PathVariable long id) {
        userService.addRoleToUserById(role, id);

        return ResponseEntity.ok(String.format("Role added to user with id %d.", id));
    }

    @PatchMapping("remove-role/{id}")
    public ResponseEntity removeRoleFromUser(@PathVariable long id, @RequestBody @Valid RoleDTO role) {
        userService.removeRoleFromUser(role, id);

        return ResponseEntity.ok(String.format("Role removed from user with id %d", id));
    }
}
