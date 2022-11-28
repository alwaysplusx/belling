package com.harmony.belling.repository

import com.harmony.belling.domain.MessageTemplate
import com.harmony.bitable.repository.BitableRepository
import org.springframework.stereotype.Repository

/**
 * @author wuxin
 */
@Repository
interface MessageTemplateRepository : BitableRepository<MessageTemplate, String>
