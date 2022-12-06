package com.harmony.belling.controller

import com.harmony.belling.domain.Alarm
import com.harmony.belling.dto.AlarmCardDto
import com.harmony.belling.service.AlarmService
import com.harmony.lark.model.PageResult
import com.harmony.lark.model.Pageable
import org.springframework.web.bind.annotation.*

@RequestMapping("/alarms")
@RestController
class AlarmController(private val alarmService: AlarmService) {

    @PostMapping
    fun create(@RequestBody alarm: Alarm): Alarm {
        return alarmService.beginAlarm(alarm)
    }

    @GetMapping
    fun page(pageRequest: Pageable): PageResult<Alarm> {
        return alarmService.page(pageRequest)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): AlarmCardDto {
        return alarmService.getAlarmCard(id)
    }

}
