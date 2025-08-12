package StCo

import grails.gorm.services.Service

@Service(Student)
interface StudentService {

    Student get(Serializable id)

    List<Student> list(Map args)

    Long count()

    void delete(Serializable id)

    Student save(Student student)

    // دالة جديدة للبحث عن طالب حسب المستخدم المرتبط
    Student findByUser(User user)
}
