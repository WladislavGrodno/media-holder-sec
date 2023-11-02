package com.education.project.media.holder.mediaholder.apiController;

import com.education.project.media.holder.mediaholder.dto.request.MediaRequest;
import com.education.project.media.holder.mediaholder.integration.user.UserClient;
import com.education.project.media.holder.mediaholder.integration.user.dto.ExternalUser;
import com.education.project.media.holder.mediaholder.integration.user.dto.ExternalUserLevel;
import com.education.project.media.holder.mediaholder.integration.user.dto.ExternalUserRole;
import com.education.project.media.holder.mediaholder.repository.MediaRepository;
import com.education.project.media.holder.mediaholder.service.PermissionServiceImp;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class MediaSecControllerTest {
    private final Timestamp timestampBefore = Timestamp.from(Instant.now());

    @Autowired
    private MediaRepository mediaRepository;

    @Mock
    private static UserClient userClient;

    @InjectMocks
    private PermissionServiceImp permissionServiceImp;

    @LocalServerPort
    private Integer port;

    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    private final Path storageRootPath =
            Paths.get("src/test/resources/media-storage").normalize();
    private final UUID mediaID = UUID.fromString("ecbe4167-9fac-4434-b135-8df2074ded29");
    private final String mediaPathString = storageRootPath + "/ecbe/4167/9fac/4434/b135/8df2/074d/ed29";


    private final UUID userID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fceba");
    private final UUID adminID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebb");
    private final UUID anonymousID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebc");
    private final UUID unknownID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebd");
    private final UUID moderatorID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebe");

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



    private final String fileName = "haha.txt";
    private final String newFileName = "hoho.txt";



    @BeforeAll
    static void beforeAll() {
        postgres.start();

        //mediaRepository.save(new Media(,,));
//        ExternalUser user =  userClient.getUser(userID);
//        if (user == null) return Role.UNKNOWN;
//        switch (user.roleDtoResp().roleDescr()){

    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void testPostgresIsRunning() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void createMedia() throws Exception {
        final MultipartFile file = new MockMultipartFile(
                "haha-ha.txt",
                fileName,
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        final MediaRequest mediaRequest = new MediaRequest(
                "Test name", "Test description", 1, file);
/*
        Mockito.when(userClient.getUser(userID)).thenReturn(userUser);
        Mockito.when(userClient.getUser(adminID)).thenReturn(adminUser);
        Mockito.when(userClient.getUser(anonymousID)).thenReturn(anonymousUser);
        Mockito.when(userClient.getUser(moderatorID)).thenReturn(moderatorUser);
        Mockito.when(userClient.getUser(unknownID)).thenReturn(unknownUser);


 */
        //ResponseEntity<MediaInfoResponse> response = mediaClient.createMedia(mediaRequest, file, userID);



        /*
        final MediaRequest mediaRequest = new MediaRequest(
                "Test name",
                "Test description",
                1,
                new MockMultipartFile(
                        "test.odt",
                        new FileInputStream(new File(mediaPathString +"/test.odt"))
                )
        );


         */
/*
        given()
                //.contentType(ContentType.JSON)
                .when()
                //.pathParam("id", mediaID)
                .body(mediaRequest)
                .queryParam("userID", userID)
                .put("/media-service-sec/media-info/{id}")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));


 */
        /*
File file = new File("src/test/resources/input.txt");
FileInputStream input = new FileInputStream(file);
MultipartFile multipartFile = new MockMultipartFile("file",
            file.getName(), "text/plain", IOUtils.toByteArray(input));

MultipartFile multipartFile = new MockMultipartFile("test.xlsx", new FileInputStream(new File("/home/admin/test.xlsx")));
         */

        //System.out.println(Files.exists(Path.of(mediaPathString + "/test.odt")));
        //System.out.println(Files.exists(Path.of("src/test/resources/test.odt")));



    //Files.createFile(newFile);
      //      Files.writeString(newFile, "Hello, Life!");
        //}

/*
        given()
                //.contentType(ContentType.JSON)
                //.body(mediaRequest)
                //.queryParam("userID", mediaID)

                //.queryParam("media", mediaRequest)
                //.param("media", mediaRequest)
                //.head("Content-type", "text/xml")

                //.multiPart(new File(mediaPathString +"/test.odt"))
                //.body("",mediaRequest)
                //.queryParam(mediaRequest)


                //.multiPart("fileBody", file)
                //.multiPart(new File(mediaPathString +"/test.odt"))
                //.queryParam("fileBody", new File(mediaPathString +"/test.odt"))
                .when()
                //.contentType(ContentType.MULTIPART)
                .contentType("multipart/form-data")
                //.body(mediaRequest)
                //.multiPart("media", mediaRequest)
                //.header("Content-type", "multipart/form-data")
                .multiPart("name", mediaRequest.name())
                .multiPart("description", mediaRequest.description())
                .multiPart("type", mediaRequest.type())
                .multiPart("fileBody", mediaRequest.fileBody())
                .multiPart("userID", mediaID)
                //.queryParam("fileBody", new File("./test.odt"))
                //.queryParam("userID", mediaID)
//                .body(userID)

                .post("/media-service-sec/media")
                //.get("/media-service-sec/media")
                .then()
                .statusCode(200);
//                .body(".", hasSize(2));





 */




    }

    @Test
    void getMediaById() {
    }

    @Test
    void getMediaInfoById() {
    }

    @Test
    void updateMedia() {
    }

    private final static String url = "/media-service-sec";

    @Test
    void updateMediaInfo() {
/*
        Mockito.when(userClient.getUser(userID)).thenReturn(userUser);
        Mockito.when(userClient.getUser(adminID)).thenReturn(adminUser);
        Mockito.when(userClient.getUser(anonymousID)).thenReturn(anonymousUser);
        Mockito.when(userClient.getUser(moderatorID)).thenReturn(moderatorUser);
        Mockito.when(userClient.getUser(unknownID)).thenReturn(unknownUser);


        final MediaInfoRequest mediaInfoRequest = new MediaInfoRequest(
                "Updated Test name",
                "Updated Test description"
                );



        given()
                .contentType(ContentType.JSON)
                .pathParam("id", mediaID)
                .body(mediaInfoRequest)
                .queryParam("userID", adminID)
                //.queryParam("userID", userID)
                .when()
                .put(url + "/media-info/{id}")

                .then()
                .log().all()
                .statusCode(403);
                //.body(".", hasSize(2));




 */
    }

    @Test
    void deleteMediaById() {
    }
}