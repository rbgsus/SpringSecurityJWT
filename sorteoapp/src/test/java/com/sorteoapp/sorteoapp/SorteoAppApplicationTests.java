package com.sorteoapp.sorteoapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SorteoAppApplicationTests {

    @Test
    void main() {
        String[] args = {};
        SorteoAppApplication.main(args);
    }

}
