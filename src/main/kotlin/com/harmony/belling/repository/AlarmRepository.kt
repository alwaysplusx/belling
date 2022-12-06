package com.harmony.belling.repository

import com.harmony.belling.domain.Alarm
import com.harmony.bitable.repository.BitableRepository
import org.springframework.stereotype.Repository

@Repository
interface AlarmRepository : BitableRepository<Alarm, String>
