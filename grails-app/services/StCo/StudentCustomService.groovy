package StCo

import grails.gorm.transactions.Transactional

@Transactional
class StudentCustomService {

    Student findByUser(User user) {
        return Student.findByUser(user)
    }

    void deleteStudentWithUser(Long id) {
        Student student = Student.get(id)
        if(student) {
            User user = student.user
            student.delete(flush: true)
            if(user) {
                user.delete(flush: true)
            }
        }
    }
}

