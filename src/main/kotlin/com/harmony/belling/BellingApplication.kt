package com.harmony.belling

import com.harmony.bitable.repository.config.EnableBitableRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableBitableRepositories
@SpringBootApplication
class BellingApplication

fun main(args: Array<String>) {
    runApplication<BellingApplication>(*args)
}
