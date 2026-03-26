package br.com.forum_hub.domain.role.RoleDTO;

import br.com.forum_hub.domain.role.RoleEnum;
import jakarta.validation.constraints.NotNull;

public record RoleDTO (@NotNull RoleEnum name){
}
