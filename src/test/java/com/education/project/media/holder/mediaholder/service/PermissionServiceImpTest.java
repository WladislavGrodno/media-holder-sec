package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.enums.Operation;
import com.education.project.media.holder.mediaholder.enums.Role;
import com.education.project.media.holder.mediaholder.integration.user.UserClient;
import com.education.project.media.holder.mediaholder.integration.user.dto.ExternalUser;
import com.education.project.media.holder.mediaholder.integration.user.dto.ExternalUserLevel;
import com.education.project.media.holder.mediaholder.integration.user.dto.ExternalUserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PermissionServiceImpTest {
    private final Timestamp timestampBefore = Timestamp.from(Instant.now());

    @Mock
    private UserClient userClient;

    @InjectMocks
    private PermissionServiceImp permissionServiceImp;

    private final ExternalUserRole adminRole = new ExternalUserRole(
            UUID.fromString("d7707486-f669-4f4a-85bb-83cb117b03e9"), "system admin");
    private final ExternalUserRole moderatorRole = new ExternalUserRole(
            UUID.fromString("17c19357-16e5-414c-82a3-855de6f35458"), "moderator");
    private final ExternalUserRole userRole = new ExternalUserRole(
            UUID.fromString("43a521fe-37fe-4b90-ae7a-8bead0e73f32"), "user");
    private final ExternalUserRole anonymousRole = new ExternalUserRole(
            UUID.fromString("4e32fbaa-9e20-458f-98bb-207250b63624"), "anonymous");
    private final ExternalUserRole unknownRole = new ExternalUserRole(
            UUID.fromString("4e32fbaa-9e20-458f-98bb-207250b63625"), "unknown");


    private final ExternalUserLevel amateurLevel = new ExternalUserLevel(
            UUID.fromString("6610f657-a9b5-413b-b5e0-2a4062c2d1dd"),"amatueur");
    private final ExternalUserLevel candidLevel = new ExternalUserLevel(
            UUID.fromString("c7247a4b-5377-4c54-bca3-a990aa99615b"),"phd candidate");
    private final ExternalUserLevel phdLevel = new ExternalUserLevel(
            UUID.fromString("51a99f34-7e8e-4f66-b35e-bf6f6e3af54e"),"phd");

    private final UUID userID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fceba");
    private final UUID adminID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebb");
    private final UUID anonymousID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebc");
    private final UUID unknownID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebd");
    private final UUID moderatorID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebe");


    private final Timestamp timestampNow = Timestamp.from(Instant.now());

    private final ExternalUser userUser = new ExternalUser(
            userID, "", "", "", "", "", userRole, amateurLevel, timestampBefore, timestampNow);
    private final ExternalUser adminUser = new ExternalUser(
            adminID, "", "", "", "", "", adminRole, amateurLevel, timestampBefore, timestampNow);
    private final ExternalUser anonymousUser = new ExternalUser(
            anonymousID, "", "", "", "", "", anonymousRole, amateurLevel, timestampBefore, timestampNow);
    private final ExternalUser unknownUser = new ExternalUser(
            unknownID, "", "", "", "", "", unknownRole, amateurLevel, timestampBefore, timestampNow);
    private final ExternalUser moderatorUser = new ExternalUser(
            moderatorID, "", "", "", "", "", moderatorRole, amateurLevel, timestampBefore, timestampNow);



    @Test
    void allowed() {

        Mockito.when(userClient.getUser(userID)).thenReturn(userUser);
        Mockito.when(userClient.getUser(adminID)).thenReturn(adminUser);
        Mockito.when(userClient.getUser(anonymousID)).thenReturn(anonymousUser);
        Mockito.when(userClient.getUser(moderatorID)).thenReturn(moderatorUser);
        Mockito.when(userClient.getUser(unknownID)).thenReturn(unknownUser);



        assertFalse(permissionServiceImp.allowed(anonymousID, Operation.POST));
        assertTrue(permissionServiceImp.allowed(anonymousID, Operation.GET));
        assertTrue(permissionServiceImp.allowed(anonymousID, Operation.GET_INFO));
        assertFalse(permissionServiceImp.allowed(anonymousID, Operation.GET_INFO_LIST));
        assertFalse(permissionServiceImp.allowed(anonymousID, Operation.PUT));
        assertFalse(permissionServiceImp.allowed(anonymousID, Operation.PUT_INFO));
        assertFalse(permissionServiceImp.allowed(anonymousID, Operation.DELETE));
        assertTrue(permissionServiceImp.allowed(userID, Operation.POST));
        assertTrue(permissionServiceImp.allowed(userID, Operation.GET));
        assertTrue(permissionServiceImp.allowed(userID, Operation.GET_INFO));
        assertFalse(permissionServiceImp.allowed(userID, Operation.GET_INFO_LIST));
        assertTrue(permissionServiceImp.allowed(userID, Operation.PUT));
        assertTrue(permissionServiceImp.allowed(userID, Operation.PUT_INFO));
        assertFalse(permissionServiceImp.allowed(userID, Operation.DELETE));
        assertTrue(permissionServiceImp.allowed(adminID, Operation.POST));
        assertTrue(permissionServiceImp.allowed(adminID, Operation.GET));
        assertTrue(permissionServiceImp.allowed(adminID, Operation.GET_INFO));
        assertTrue(permissionServiceImp.allowed(adminID, Operation.GET_INFO_LIST));
        assertTrue(permissionServiceImp.allowed(adminID, Operation.PUT));
        assertTrue(permissionServiceImp.allowed(adminID, Operation.PUT_INFO));
        assertTrue(permissionServiceImp.allowed(adminID, Operation.DELETE));
        assertTrue(permissionServiceImp.allowed(moderatorID, Operation.POST));
        assertTrue(permissionServiceImp.allowed(moderatorID, Operation.GET));
        assertTrue(permissionServiceImp.allowed(moderatorID, Operation.GET_INFO));
        assertFalse(permissionServiceImp.allowed(moderatorID, Operation.GET_INFO_LIST));
        assertTrue(permissionServiceImp.allowed(moderatorID, Operation.PUT));
        assertTrue(permissionServiceImp.allowed(moderatorID, Operation.PUT_INFO));
        assertFalse(permissionServiceImp.allowed(moderatorID, Operation.DELETE));
        assertFalse(permissionServiceImp.allowed(unknownID, Operation.POST));
        assertTrue(permissionServiceImp.allowed(unknownID, Operation.GET));
        assertTrue(permissionServiceImp.allowed(unknownID, Operation.GET_INFO));
        assertFalse(permissionServiceImp.allowed(unknownID, Operation.GET_INFO_LIST));
        assertFalse(permissionServiceImp.allowed(unknownID, Operation.PUT));
        assertFalse(permissionServiceImp.allowed(unknownID, Operation.PUT_INFO));
        assertFalse(permissionServiceImp.allowed(unknownID, Operation.DELETE));

        UUID random = UUID.randomUUID();
        Mockito.when(userClient.getUser(random)).thenThrow();

        assertFalse(permissionServiceImp.allowed(random, Operation.POST));
        assertTrue(permissionServiceImp.allowed(random, Operation.GET));
        assertTrue(permissionServiceImp.allowed(random, Operation.GET_INFO));
        assertFalse(permissionServiceImp.allowed(random, Operation.GET_INFO_LIST));
        assertFalse(permissionServiceImp.allowed(random, Operation.PUT));
        assertFalse(permissionServiceImp.allowed(random, Operation.PUT_INFO));
        assertFalse(permissionServiceImp.allowed(random, Operation.DELETE));

    }
}
