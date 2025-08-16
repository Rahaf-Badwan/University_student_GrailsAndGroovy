package StCo

import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j

@Slf4j
@Transactional
class EnrollmentService {

    /**
     * تسجيل طالب في كورس بعد التحقق من عدم التكرار
     */
    def save(Enrollment enrollment) {
        if (!enrollment) {
            log.error("❌ [save] Enrollment object is null")
            throw new RuntimeException("Enrollment object is null")
        }

        log.info("🔍 [save] Checking enrollment for student ${enrollment.student?.id} in course ${enrollment.course?.id}")

        def existing = Enrollment.findByStudentAndCourse(enrollment.student, enrollment.course)
        if (existing) {
            log.warn("⚠️ [save] Duplicate enrollment for student ${enrollment.student?.id}")
            throw new RuntimeException("Student is already enrolled in this course.")
        }

        log.info("✅ [save] Saving enrollment for student ${enrollment.student?.id} in course ${enrollment.course?.id}")
        enrollment.save(failOnError: true)
        return enrollment
    }

    /**
     * جلب تسجيل معيّن
     */
    def get(Long id) {
        log.info("📦 [get] Fetching enrollment with ID: $id")
        Enrollment.get(id)
    }

    /**
     * إرجاع قائمة تسجيلات
     */
    def list(Map args) {
        log.info("📋 [list] Listing enrollments with params: $args")
        Enrollment.list(args)
    }

    /**
     * عدد كل التسجيلات
     */
    def count() {
        def total = Enrollment.count()
        log.info("🔢 [count] Total number of enrollments: $total")
        return total
    }

    /**
     * حذف تسجيل معيّن
     */
    def delete(Long id) {
        log.info("🗑️ [delete] Deleting enrollment with ID: $id")
        def enrollment = Enrollment.get(id)
        if (enrollment) {
            enrollment.delete(flush: true)
            log.info("✅ Enrollment $id deleted successfully")
        } else {
            log.warn("⚠️ Enrollment $id not found, cannot delete")
        }
    }

    /**
     * حساب المعدل التراكمي (GPA) لطالب معيّن
     */
    Double calculateGPA(Long studentId) {
        log.info("📊 [calculateGPA] Calculating GPA for student ID: $studentId")

        def student = Student.get(studentId)
        def enrollments = Enrollment.findAllByStudent(student)

        if (!student || enrollments.isEmpty()) {
            log.info("ℹ️ Student $studentId has no enrollments. GPA = 0.0")
            return 0.0
        }

        double totalPoints = 0
        double totalCredits = 0

        enrollments.each { enrollment ->
            def grade = enrollment.grade
            def credits = enrollment.course?.credits

            if (grade != null && credits != null) {
                totalPoints += grade * credits
                totalCredits += credits
                log.info("✅ Course: ${enrollment.course?.title}, Grade: $grade, Credits: $credits")
            }
        }

        def gpa = totalCredits > 0 ? (totalPoints / totalCredits).round(2) : 0.0
        log.info("🎓 Final GPA for student $studentId is: $gpa")
        return gpa
    }

}
