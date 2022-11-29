package com.harmony.belling.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class UserServiceTest {

    @Autowired
    lateinit var userService: UserService

    @Test
    fun test() {
        userService.sync()
    }

}
