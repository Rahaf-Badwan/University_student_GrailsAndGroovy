package StCo

import grails.rest.Resource
import static org.springframework.http.HttpStatus.*
import groovy.util.logging.Slf4j

@Resource()
@Slf4j
class EnrollmentController {

    EnrollmentService enrollmentService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def currentUser = springSecurityService.currentUser
        boolean isAdmin = currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }

        def enrollments = Enrollment.list(params)

        // حساب GPA لجميع الطلاب (لو أدمن) أو GPA الطالب نفسه فقط
        def gpaList
        if (isAdmin) {
            def students = Student.list()
            gpaList = students.collect { student ->
                [
                        studentId: student.id,
                        studentName: student.name,
                        gpa: enrollmentService.calculateGPA(student.id)
                ]
            }
        } else {
            def student = Student.findByUser(currentUser)
            if (student) {
                gpaList = [[
                                   studentId: student.id,
                                   studentName: student.name,
                                   gpa: enrollmentService.calculateGPA(student.id)
                           ]]
            } else {
                gpaList = []
            }
        }

        render(view: "index", model: [
                enrollmentList: enrollments,
                enrollmentCount: enrollments.size(),
                isAdmin: isAdmin,
                gpaList: gpaList
        ])
    }



    def show(Long id) {
        def enrollment = enrollmentService.get(id)
        def currentUser = springSecurityService.currentUser

        if (!enrollment) {
            notFound()
            return
        }
        // تأكد أن المستخدم أدمن أو صاحب التسجيل فقط يمكنه المشاهدة
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' } &&
                enrollment.student.user.id != currentUser.id) {
            flash.message = "Access denied"
            redirect(action: "index")
            return
        }
        render(view: "show", model: [enrollment: enrollment])
    }

    def create() {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to create new enrollments."
            redirect(action: "index")
            return
        }
        render(view: "create", model: [
                enrollment: new Enrollment(params),
                students: Student.list(),
                courses: Course.list()
        ])
    }

    def save(Enrollment enrollment) {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to save enrollments."
            redirect(action: "index")
            return
        }

        if (enrollment == null) {
            notFound()
            return
        }

        try {
            enrollmentService.save(enrollment)
        } catch (RuntimeException e) {
            flash.message = e.message
            render(view: 'create', model: [
                    enrollment: enrollment,
                    students: Student.list(),
                    courses: Course.list()
            ])
            return
        }

        flash.message = message(code: 'default.created.message', args: [
                message(code: 'enrollment.label', default: 'Enrollment'),
                enrollment.id
        ])
        // إعادة توجيه إلى index لعرض تحديث الـ GPA
        redirect(action: "index")
    }


    def edit(Long id) {
        def enrollment = enrollmentService.get(id)
        def currentUser = springSecurityService.currentUser

        if (!enrollment) {
            notFound()
            return
        }
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' } &&
                enrollment.student.user.id != currentUser.id) {
            flash.message = "You do not have permission to edit this enrollment."
            redirect(action: "index")
            return
        }
        render(view: "edit", model: [enrollment: enrollment])
    }

    def update(Enrollment enrollment) {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to update enrollments."
            redirect(action: "index")
            return
        }

        if (enrollment == null) {
            notFound()
            return
        }

        try {
            enrollmentService.save(enrollment)
        } catch (RuntimeException e) {
            flash.message = e.message
            render(view: 'edit', model: [enrollment: enrollment])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [
                message(code: 'enrollment.label', default: 'Enrollment'),
                enrollment.id
        ])
        redirect(action: "show", id: enrollment.id)
    }

    def delete(Long id) {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to delete enrollments."
            redirect(action: "index")
            return
        }

        if (id == null) {
            notFound()
            return
        }

        enrollmentService.delete(id)

        flash.message = message(code: 'default.deleted.message', args: [
                message(code: 'enrollment.label', default: 'Enrollment'),
                id
        ])
        redirect(action: "index")
    }
    def gpa() {
        def currentUser = springSecurityService.currentUser
        boolean isAdmin = currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }

        if (!isAdmin) {
            // إذا مش أدمن، تسمح له يشوف GPA لكل الطلاب (حسب طلبك)
            // هنا لو بدك ممكن تعمل تحقق أو لا
        }

        // نجيب كل الطلاب
        def students = Student.list()

        // نحسب GPA لكل طالب
        def gpaList = students.collect { student ->
            [
                    studentId  : student.id,
                    studentName: student.name,
                    gpa        : enrollmentService.calculateGPA(student.id)
            ]
        }

        render(view: "gpaAll", model: [
                gpaList: gpaList
        ])
    }


}
