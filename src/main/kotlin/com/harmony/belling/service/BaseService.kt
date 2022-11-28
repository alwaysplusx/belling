package com.harmony.belling.service

import com.harmony.bitable.filter.PredicateBuilder
import com.harmony.bitable.filter.RecordFilterPredicate
import com.harmony.bitable.repository.BitableRepository
import com.harmony.lark.model.PageResult

abstract class BaseService<T : Any>(protected val repository: BitableRepository<T, String>) {

    fun save(domain: T): T {
        return repository.save(domain)
    }

    fun page(predicate: RecordFilterPredicate): PageResult<T> {
        return repository.scan(predicate).nextSlice()
    }

    fun page(pageToken: String?, predicate: PredicateBuilder<T>.() -> Unit = {}): PageResult<T> {
        return repository.scan(pageToken, predicate).nextSlice()
    }

}
