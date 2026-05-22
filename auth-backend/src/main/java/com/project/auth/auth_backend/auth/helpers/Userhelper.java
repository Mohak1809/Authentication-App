package com.project.auth.auth_backend.auth.helpers;

import java.util.UUID;

public class Userhelper {
    public static UUID parseUUID(String uuid) {
        return UUID.fromString(uuid);
    }
}
