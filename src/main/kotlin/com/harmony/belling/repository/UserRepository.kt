package com.harmony.belling.repository

import com.harmony.belling.domain.User
import com.harmony.bitable.repository.BitableRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : BitableRepository<User, String>
