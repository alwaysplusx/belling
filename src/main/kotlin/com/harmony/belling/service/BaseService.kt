package com.harmony.belling.service

import com.harmony.bitable.filter.PredicateBuilder
import com.harmony.bitable.filter.RecordFilter
import com.harmony.bitable.repository.BitableRepository
import com.harmony.lark.model.PageResult
import com.harmony.lark.model.Pageable

abstract class BaseService<T : Any>(protected val repository: BitableRepository<T, String>) {

    fun save(domain: T): T {
        return repository.save(domain)
    }

    fun page(recordFilter: RecordFilter): PageResult<T> {
        return repository.scan(recordFilter).nextSlice()
    }

    fun page(
        pageable: Pageable = BitableRepository.DEFAULT_FIRST_PAGE,
        closure: PredicateBuilder<T>.() -> Unit = {},
    ): PageResult<T> {
        return repository.scan(pageable, closure).nextSlice()
    }

}
