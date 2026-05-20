package com.project.auth.auth_backend.dtos;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {
    private UUID id = UUID.randomUUID();
    private String name;
    
}
