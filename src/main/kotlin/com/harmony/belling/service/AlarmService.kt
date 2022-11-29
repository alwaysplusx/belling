package com.harmony.belling.service

import com.harmony.belling.domain.Alarm
import com.harmony.belling.repository.AlarmRepository
import com.harmony.lark.LarkClient
import com.lark.oapi.service.im.v1.ImService
import org.springframework.stereotype.Service

@Service
class AlarmService(alarmRepository: AlarmRepository, larkClient: LarkClient) : BaseService<Alarm>(alarmRepository) {

    private val imService = larkClient.unwrap(ImService::class.java)

    fun createAlarm(alarm: Alarm) {

    }

}
