package com.harmony.belling.service

import com.harmony.belling.domain.Alarm
import com.harmony.belling.repository.AlarmRepository
import org.springframework.stereotype.Service

@Service
class AlarmService(alarmRepository: AlarmRepository) : BaseService<Alarm>(alarmRepository) {

}
