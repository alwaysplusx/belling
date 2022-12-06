package com.harmony.belling.repository

import com.harmony.belling.domain.Project
import com.harmony.bitable.repository.BitableRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : BitableRepository<Project, String> {
}
