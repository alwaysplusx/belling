package com.harmony.belling.service

import com.harmony.belling.domain.User
import com.harmony.belling.repository.UserRepository
import com.harmony.lark.LarkClient
import com.harmony.lark.service.contact.listCursor
import com.lark.oapi.service.bitable.v1.model.Person
import com.lark.oapi.service.contact.v3.ContactService
import com.lark.oapi.service.contact.v3.model.Department
import com.lark.oapi.service.contact.v3.model.FindByDepartmentUserReq
import com.lark.oapi.service.contact.v3.model.ListDepartmentReq
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    larkClient: LarkClient,
) : BaseService<User>(userRepository) {

    private val contactService = larkClient.unwrap(ContactService::class.java)

    fun sync() {
        val request = ListDepartmentReq().apply {
            this.parentDepartmentId = "0"
            this.pageSize = 20
        }
        val cursor = contactService.department().listCursor(request)
        for (department in cursor) {
            println(">>>> $department")
        }
    }

    fun users(department: Department): List<Person> {
        val request = FindByDepartmentUserReq().apply {
            this.pageSize = 50
            this.departmentId = department.departmentId
        }
        contactService.user().findByDepartment(request)
        return listOf()
    }

}
