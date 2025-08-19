package api

import StCo.Enrollment
import StCo.Student
import StCo.Course
import grails.gorm.transactions.Transactional
import java.text.SimpleDateFormat

@Transactional
class EnrollmentApiService {

    def enroll(Long studentId, Long courseId, Date enrollmentDate = new Date(), Double grade = null) {
        def student = Student.get(studentId)
        def course = Course.get(courseId)

        if (!student) return [ok: false, msg: "Student not found", status: 404]
        if (!course) return [ok: false, msg: "Course not found", status: 404]

        def enrollment = new Enrollment(
                student: student,
                course: course,
                enrollmentDate: enrollmentDate,
                grade: grade
        )

        if (!enrollment.validate()) {
            return [ok: false, errors: enrollment.errors.allErrors*.toString(), status: 422]
        }

        enrollment.save(flush: true)
        return [ok: true, data: enrollment, status: 201]
    }

    def updateEnrollment(Long id, def json) {
        def enrollment = Enrollment.get(id)
        if (!enrollment) return [ok: false, msg: "Enrollment not found", status: 404]

        if (json.studentId) {
            def s = Student.get(json.getLong('studentId'))
            if (!s) return [ok: false, msg: "Student not found", status: 404]
            enrollment.student = s
        }
        if (json.courseId) {
            def c = Course.get(json.getLong('courseId'))
            if (!c) return [ok: false, msg: "Course not found", status: 404]
            enrollment.course = c
        }
        if (json.enrollmentDate) {
            enrollment.enrollmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(json.enrollmentDate.toString())
        }
        if (json.grade != null) {
            enrollment.grade = json.grade as Double
        }

        if (!enrollment.validate()) {
            return [ok: false, errors: enrollment.errors.allErrors*.toString(), status: 422]
        }

        enrollment.save(flush: true)
        return [ok: true, data: enrollment, status: 200]
    }

    def deleteEnrollment(Long id) {
        def enrollment = Enrollment.get(id)
        if (!enrollment) return [ok: false, msg: "Enrollment not found", status: 404]

        enrollment.delete(flush: true)
        return [ok: true, msg: "Enrollment deleted", status: 204]
    }
}
