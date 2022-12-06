package com.harmony.belling.repository

import com.harmony.belling.domain.Member
import com.harmony.bitable.repository.BitableRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : BitableRepository<Member, String> {
}
