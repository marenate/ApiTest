package suitTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateAnnouncementEndToEnd {
    // Variables
    static String base_url = "https://webapi.segundamano.mx";
    static String email;
    static int password;
    static String urlId;
    static String newAccountId;
    static String uuid;
    static String Token;
    static String originalName;
    static String newNameUser;
    static String token_type;
    static String token_anuncioURL;
    static int phone;
    static String token_anuncioId;
    static String addressUserID;
    static String tokendToAddress;

    @Test
    public void t01_creteUser() {
        double rdnNumero = Math.random() + 6 + 1;
        email = "ventas_" + rdnNumero + "@ventas.net";
        password = (int) (Math.random() * 99999 + 9999);
        String datos = email + ":" + password;
        String encodeString = Base64.getEncoder().encodeToString(datos.getBytes());
        String bodyRequest = "{\"account\":{\"email\":\"" + email + "\"}}";
        RestAssured.baseURI = format("%s/nga/api/v1.1/private/accounts", base_url);
        Response response = given().log().all().header("Authorization", "Basic " + encodeString).body(bodyRequest).post();
        String body = response.getBody().asString();
        //Validations
        assertNotNull(body);
        assertEquals(201, response.getStatusCode());
        assertNotNull("access_token");
        assertNotNull("account.account_id");
        assertEquals(email, response.jsonPath().getString("account.email"));


        // Group settings static variables
        Token = response.jsonPath().getString("access_token");
        System.out.println("This is token: " + Token);

        urlId = response.jsonPath().getString("account.account_id");
        System.out.println("This is urlId: " + urlId);

        String[] parts = urlId.split("/");
        newAccountId = parts[3];
        System.out.println("Getting [3]" + parts[3]);

        uuid = response.jsonPath().getString("account.uuid");
        System.out.println("This is uuid: " + uuid);

        urlId = response.jsonPath().getString("account.account_id");
        System.out.println("This is urlId: " + urlId);

        originalName = response.jsonPath().getString("account.name");
        System.out.println("This is original Name: " + originalName);

        token_type = response.jsonPath().getString("token_type");
        System.out.println("This is original Name: " + token_type);

    }

    @Test
    public void t02_updateUserPhone() {
        phone = (int) (Math.random() * 99999 + 999999999);
        newNameUser = "Johanna " + originalName;
        System.out.println("This is newNameUser: " + newNameUser);
        System.out.println("This is the phone: " + phone);
        RestAssured.baseURI = format("%s/nga/api/v1.1%s", base_url, urlId);
        Response response = given().header("Authorization", token_type + " " + Token).body("{\"account\":{\"name\":\"" + newNameUser + "\"," +
                "\"phone\":\"" + phone + "\", \"phone_hidden\": true}}").patch();
        String body = response.getBody().asString();
        //Validations
        assertEquals(200, response.getStatusCode());
        assertNotNull(body);
        assertEquals(uuid, response.jsonPath().getString("account.uuid"));
        assertEquals(urlId, response.jsonPath().getString("account.account_id"));
    }

    @Test
    public void t03_createAnnouncement() {
        String subject = "Estetica mascot  " + (int) (Math.random() * 99999 + 9999);
        RestAssured.baseURI = format("%s/nga/api/v1.7%s", base_url, urlId);
        String bodyRequest = "{\"ad\":{\"locations\":" +
                "[{\"code\":\"11\",\"key\":\"region\",\"label\":" +
                "\"Ciudad de México\",\"locations\":[{\"code\":\"300\",\"key\":\"municipality\"," +
                "\"label\":\"Miguel Hidalgo\",\"locations\":[{\"code\":\"8086\",\"key\":\"area\"," +
                "\"label\":\"Anzures\"}]}]}],\"subject\":\"" + subject + "\"," +
                "\"body\":\"Servicios generales tus mascotas\",\"category\":{\"code\":\"8223\"}," +
                "\"images\":[],\"price\":{\"currency\":\"mxn\",\"price_value\":18},\"ad_details\":{}," +
                "\"phone_hidden\":1,\"plate\":\"\",\"vin\":\"\",\"type\":{\"code\":\"s\",\"label\":\"\"}," +
                "\"ad\":\"Servicios generales tus mascotas\"},\"category_suggestion\":false,\"commit\":true}";
        Response response = given()
                .log().all()
                .header("Authorization", token_type + " " + Token)
                .header("x-nga-source", "PHOENIX_DESKTOP")
                .contentType("application/json")
                .body(bodyRequest)
                .post("/klfst?lang=es");

        String body = response.getBody().asString();
        //Validations
        assertEquals(201, response.getStatusCode());
        assertNotNull(body);
        assertEquals("new", response.jsonPath().getString("action.action_type"));
        assertNotNull(response.jsonPath().getString("action.action_id"));
        //setup static variable
        token_anuncioURL = response.jsonPath().getString("ad.ad_id");
    }

    @Test
    public void t04_getUserDataUpdated() {
        RestAssured.baseURI = format("%s/nga/api/v1.1/%s", base_url, urlId);
        Response response = given().log().all().header("Authorization", token_type + " " + Token).contentType("application/json").get();
        String body = response.getBody().asString();
        //Validaciones
        assertEquals(200, response.getStatusCode());
        assertNotNull(body);
        assertEquals(uuid, response.jsonPath().getString("account.uuid"));
        assertEquals(urlId, response.jsonPath().getString("account.account_id"));
        assertEquals(email, response.jsonPath().getString("account.email"));
        assertEquals(phone, response.jsonPath().getInt("account.phone"));
    }

    @Test
    public void t05_updateUserPassword() {
        int newPassword = (int) (Math.random() * 99999 + 9999);
        RestAssured.baseURI = format("%s/nga/api/v1%s", base_url, urlId);
        //RestAssured.basePath = format("/nga/api/v1/%s",urlId);
        String bodyRequest ="{\"account\":{\"password\":\"52145\"}}";
        Response response = given().log().all()
                .header("Authorization", token_type + " " + Token)
                .header("accept-language","es")
                .accept("application/json, text/plain, */*")
                .contentType("application/json;charset=UTF-8")
                .body(bodyRequest)
                .patch();

        String body = response.getBody().asString();
        //Validaciones
        assertEquals(200, response.getStatusCode());
        assertNotNull(body);
        assertEquals(uuid, response.jsonPath().getString("account.uuid"));
        assertEquals(urlId, response.jsonPath().getString("account.account_id"));
        assertEquals(email, response.jsonPath().getString("account.email"));
        assertEquals(phone, response.jsonPath().getInt("account.phone"));

        // set the new access_token variables
        Token = response.jsonPath().getString("access_token");
    }

    @Test
    public void t06_getUseBalance() {
        // String [] transacction = new String[0];
        RestAssured.baseURI = format("%s/credits/v1%s", base_url, urlId);
        Response response = given().log().all()
                .header("Authorization", token_type + " " + Token)
                .accept("application/json, text/plain, */*")
                .contentType("application/json").get();
        String body = response.getBody().asString();
        //Validaciones
        assertEquals(200, response.getStatusCode());
        assertNotNull(body);
        //assertEquals(transacction, response.jsonPath().getString("Transactions"));
    }

    @Test
    public void t07_getUserMessage() {
        RestAssured.baseURI = format("%s/nga/api/v1/api/hal/%s", base_url, uuid);
        Response response = given().log().all()
                .header("Authorization", token_type + " " + Token)
                .accept("application/json, text/plain, */*")
                .get("/conversations/");
        String body = response.getBody().asString();
        //Validaciones
        assertEquals(200, response.getStatusCode());
        assertNotNull(body);
        assertEquals(uuid, response.jsonPath().getString("userId"));
    }

    @Test
    public void t08_searchAnnouncementCreted() {
        RestAssured.baseURI = format("%s/nga/api/v1%s", base_url, urlId, "/ads/%s", newAccountId);
        Response response = given().log().all()
                .header("Authorization", token_type + " " + Token)
                .accept("application/json, text/plain, */*")
                .contentType("application/json")
                .get("/stats?lang=es");
        String body = response.getBody().asString();
        //Validaciones
        assertEquals(200, response.getStatusCode());
        assertNotNull(body);
        assertNotNull(response.jsonPath().param("grouped_by_day.dates", "grouped_by_day.dates"));
    }

    @Test
    public void t09_editAnnouncementCreted() {
        String[] parts = token_anuncioURL.split("/");
        // getting  announcement id
        token_anuncioId = parts[5];
        System.out.println("Este es el Anuncio Id: "+token_anuncioId);
        String newSubject = "Estetica mascot " + (int) (Math.random() * 99999 + 9999);

        RestAssured.baseURI = format("%s/nga/api/v1.7%s", base_url, urlId);
        RestAssured.basePath = format("/klfst/%s", token_anuncioId);
        String bodyRequest = "{\"ad\":{\"locations\":[{\"code\":\"11\",\"key\":\"region\"," +
                "\"label\":\"Ciudad de México\",\"locations\":[{\"code\":\"300\"," +
                "\"key\":\"municipality\",\"label\":\"Miguel Hidalgo\",\"locations\":[{\"code\":\"8086\"," +
                "\"key\":\"area\",\"label\":\"Anzures\"}]}]}],\"subject\":\"" + newSubject + "\"," +
                "\"body\":\"Engrie a tu mascota como si fuera para ud mismo traerle a boby\"," +
                "\"category\":{\"code\":\"8223\"},\"images\":[],\"price\":{\"currency\":\"MXN\"," +
                "\"price_value\":30},\"ad_details\":{},\"phone_hidden\":1,\"type\":{\"code\":\"s\"," +
                "\"label\":\"Vendo\"},\"adId\":\"" + token_anuncioId + "\"},\"category_suggestion\":false,\"commit\":true}";
        Response response = given().log().all()
                .header("Authorization", token_type + " " + Token)
                .header("x-nga-source", "PHOENIX_DESKTOP")
                .accept("application/json, text/plain, */*")
                .contentType("application/json")
                .body(bodyRequest)
                .post("/actions?lang=es");
        String body = response.getBody().asString();
        //Validaciones
        assertEquals(201, response.getStatusCode());
        assertNotNull(body);
        assertEquals("edit", response.jsonPath().getString("action.action_type"));
        assertEquals(newSubject, response.jsonPath().getString("ad.subject"));
    }

    @Test
    public void t1_addUserAddress() {
        int exteriorStreetNumber = (int) (Math.random() * 9 + 9);
        String stringToAddress = uuid + ":" + Token;
        tokendToAddress = Base64.getEncoder().encodeToString(stringToAddress.getBytes());
        RestAssured.baseURI = format(base_url);
        RestAssured.basePath = format("/addresses/v1/create");
        String bodyRequest = "contact=Mi direccion " + exteriorStreetNumber + "&phone=5198579735&rfc=VECM810820V59&zipCode=01003&" +
                "exteriorInfo=Calle Rivadavia" + exteriorStreetNumber + "&interiorInfo=172&region=11&municipality=300&alias=Mi Miguel";

        Response response = given().log().all().header("Authorization", "Basic "+tokendToAddress)
                .contentType("application/x-www-form-urlencoded")
                .body(bodyRequest).post();

        String body = response.getBody().asString();
        //Validaciones
        assertEquals(201, response.getStatusCode());
        assertNotNull(body);
        assertNotNull(response.jsonPath().param("addressID", "addressID"));
        //  set  address ID variables to be used in modify address
        addressUserID = response.jsonPath().getString("addressID");
    }

    @Test
    public void t2_modifyUserAddressAdd() {
        int exteriorStreetNumber = (int) (Math.random() * 9 + 9);

        RestAssured.baseURI = format( base_url);
        RestAssured.basePath = format("/addresses/v1/modify/%s", addressUserID);
        String bodyRequest = "contact=Mi direccion " + exteriorStreetNumber + "&phone=5198579735&rfc=VECM810820V59&zipCode=01003&" +
                "exteriorInfo=Calle Rivadavia" + exteriorStreetNumber + "&interiorInfo=172&region=11&municipality=300&alias=Mi Miguel";

        Response response = given().log().all().header("Authorization", "Basic "+tokendToAddress)
                .contentType("application/x-www-form-urlencoded")
                .body(bodyRequest).put();

        String body = response.getBody().asString();
        //Validaciones
        assertEquals(200, response.getStatusCode());
        assertNotNull(body);
        assertNotNull(response.jsonPath().param("message", "message"));
    }

    @Test
    public void t3_deleteAnnouncement() {
        RestAssured.baseURI = format("%s/nga/api/v1%s", base_url, urlId);
        RestAssured.basePath = format("/klfst/%s", token_anuncioId);
        String bodyRequest ="{\"delete_reason\":{\"code\":\"5\"}}";
                //Response response;
        Response response = given().log().all().header("Authorization", token_type + " " + Token)
                //.header("x-nga-source", "PHOENIX_DESKTOP")
                //.header("accept-language", "es")
                //.header("referer", "https://www.segundamano.mx/mi-cuenta/mis-anuncios")
                .accept("application/json, text/plain, */*")
                .contentType("application/json;charset=UTF-8")
                .body(bodyRequest).delete();

        String body = response.getBody().asString();
        //Validaciones
        assertEquals(200, response.getStatusCode());
        assertNotNull(body);
        assertEquals("delete", response.jsonPath().getString("action.action_type"));
    }
}
