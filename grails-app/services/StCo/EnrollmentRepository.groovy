package StCo

import grails.gorm.services.Service

@Service(Enrollment)
interface EnrollmentRepository {

    Enrollment get(Serializable id)

    List<Enrollment> list(Map args)

    Long count()

    void delete(Serializable id)

    Enrollment save(Enrollment enrollment)
}
