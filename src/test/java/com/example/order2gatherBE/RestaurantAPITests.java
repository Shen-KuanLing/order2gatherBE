package com.example.order2gatherBE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.Assertions;

@SpringBootTest
public class RestaurantAPITests {
    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    void hello_ReturnHello() throws Exception {

        String url = "http://localhost:8080/restaurant/test";
        System.out.println(url);

        String testStr = testRestTemplate.getForObject(url, String.class); // pass
        Assertions.assertEquals(testStr, "Test");
    }

}
