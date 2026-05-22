package com.project.auth.auth_backend.auth.payload;

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
