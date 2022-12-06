package com.harmony.belling.service

import com.harmony.belling.constants.AlarmStatus
import com.harmony.belling.constants.MemberType
import com.harmony.belling.domain.*
import com.harmony.belling.dto.AlarmCardDto
import com.harmony.belling.lark.LarkFacade
import com.harmony.belling.lark.model.MessageTemplate
import com.harmony.belling.repository.AlarmRepository
import com.harmony.belling.repository.MemberRepository
import com.harmony.belling.repository.ProjectRepository
import com.harmony.bitable.oapi.toStream
import com.lark.oapi.service.bitable.v1.model.Person
import com.lark.oapi.service.im.v1.model.CreateMessageRespBody
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AlarmService(
    alarmRepository: AlarmRepository,
    private val larkFacade: LarkFacade,
    private val projectRepository: ProjectRepository,
    private val memberRepository: MemberRepository,
    private val propertyService: PropertyService,
) : BaseService<Alarm>(alarmRepository) {

    private val log = LoggerFactory.getLogger(AlarmService::class.java)

    fun beginAlarm(alarm: Alarm): Alarm {
        val existsAlarm = repository.firstOrNull {
            Alarm::sourceId eq alarm.sourceId!!
        }

        if (existsAlarm != null) {
            return existsAlarm
        }

        alarm.beforeCreate()
        val card = sendAlarmCard("alarm-begin", alarm)
        alarm.cardId = card.messageId

        return repository.save(alarm)
    }

    fun resolveAlarm(cardId: String, summary: String) {
        val alarm = repository.first { Alarm::cardId eq cardId }
        return resolveAlarm(alarm, summary)
    }

    fun resolveAlarm(alarm: Alarm, summary: String) {

        alarm.status = AlarmStatus.RESOLVED
        alarm.summary = summary
        alarm.resolvedAt = LocalDateTime.now()
        alarm.beforeUpdate()
        val updatedAlarm = repository.update(alarm)

        updateAlarmCard("alarm-resolve", updatedAlarm)
    }

    fun getAlarmCard(id: String): AlarmCardDto {
        return buildAlarmCard(repository.getOne(id))
    }

    fun buildAlarmCard(alarm: Alarm): AlarmCardDto {
        val project = projectRepository.first {
            Project::name eq alarm.projectName!!
        }
        return AlarmCardDto.from(alarm).apply {
            this.project = project
            this.users = getProjectAlarmPersons(project)
        }
    }

    private fun getProjectAlarmPersons(project: Project): List<Person> {
        // @formatter:off
        return memberRepository.scan {
                Member::projectName eq project.name!!
                Member::memberTypes contains listOf(MemberType.OWNER, MemberType.DEVELOPER, MemberType.MAINTAINER)
            }
            .toStream()
            .map { it.person!! }
            .toList()
        // @formatter:on
    }

    private fun sendAlarmCard(templateKey: String, alarm: Alarm): CreateMessageRespBody {
        val chatId = propertyService.getValue("master.chat-id")
        val dataModel = buildAlarmCard(alarm)
        return larkFacade.sendMessageCard(chatId, MessageTemplate(templateKey, dataModel))
    }

    private fun updateAlarmCard(templateKey: String, alarm: Alarm) {
        val dataModel = buildAlarmCard(alarm)
        return larkFacade.updateMessageCard(alarm.cardId!!, MessageTemplate(templateKey, dataModel))
    }

}
