package com.harmony.belling.service

import com.harmony.belling.constants.AlarmStatus
import com.harmony.belling.domain.Alarm
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AlarmServiceTest {

    @Autowired
    lateinit var alarmService: AlarmService

    @Test
    fun testAlarmBegin() {
        val alarm = Alarm().apply {
            this.projectName = "测试项目"
            this.status = AlarmStatus.HANDLING
            this.reason = "SLOW COUNT > 2000, limitation is 100"
            this.sourceType = "test"
            this.sourceId = System.currentTimeMillis().toString()
        }
        alarmService.beginAlarm(alarm)
    }

    @Test
    fun testAlarmResolve() {
        alarmService.resolveAlarm("om_899497b9feef0c9a96d5ba1f23bf34a6", "测试告警, 标记为解决")
    }

}
