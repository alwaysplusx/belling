package com.harmony.belling.dto

import com.harmony.belling.constants.AlarmStatus
import com.harmony.belling.domain.Alarm
import com.harmony.belling.domain.Project
import com.lark.oapi.service.bitable.v1.model.Person
import java.time.LocalDateTime

data class AlarmCardDto(
    val reason: String,
    val status: AlarmStatus,
    val sourceType: String?,
    val summary: String?,
    val createdAt: LocalDateTime?,
    val resolvedAt: LocalDateTime?,
    var project: Project,
    var users: List<Person> = listOf(),
    var dashboardUrls: List<DashboardUrl> = listOf(),
) {
    companion object {

        fun from(alarm: Alarm): AlarmCardDto {
            return AlarmCardDto(
                reason = alarm.reason!!,
                status = alarm.status!!,
                sourceType = alarm.sourceType!!,
                summary = alarm.summary,
                createdAt = alarm.createdAt,
                resolvedAt = alarm.resolvedAt,
                project = Project().apply { this.name = alarm.projectName }
            )
        }

    }
}

data class DashboardUrl(val text: String, val link: String)
