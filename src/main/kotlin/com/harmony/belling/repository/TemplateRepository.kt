package com.harmony.belling.repository

import com.harmony.belling.domain.Template
import com.harmony.bitable.repository.BitableRepository
import org.springframework.stereotype.Repository

/**
 * @author wuxin
 */
@Repository
interface TemplateRepository : BitableRepository<Template, String>
