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
    def index(Integer max, Double gpaMin, Double gpaMax, String query, String sortBy) {
        params.max = Math.min(max ?: 10, 100)
        def currentUser = springSecurityService.currentUser
        boolean isAdmin = currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }

        def enrollmentsCriteria = Enrollment.createCriteria()

        def enrollments = enrollmentsCriteria.list(params) {
            if (isAdmin) {
                if (query) {
                    or {
                        // البحث في اسم الطالب
                        student {
                            or {
                                ilike('name', "%${query}%")
                                user { ilike('username', "%${query}%") }
                            }
                        }
                        // البحث في عنوان الكورس
                        course { ilike('title', "%${query}%") }
                        // البحث في الدرجة (لو الرقم ممكن)
                        if (query.isNumber()) eq('grade', query.toDouble())
                    }
                }
            } else {
                // للمستخدم العادي: فقط تسجيلاته
                def student = Student.findByUser(currentUser)
                if (student) eq('student', student)
            }

            // فرز حسب dropdown (default ascending/descending داخلي)
            if (sortBy) {
                switch (sortBy) {
                    case 'username':
                        student { order('name') }
                        break
                    case 'enrollmentDate':
                        order('enrollmentDate')
                        break
                    case 'grade':
                        order('grade')
                        break
                }
            }
        }

        // حساب GPA لكل تسجيل
        def gpaList = enrollments.collect { enrollment ->
            [
                    enrollment: enrollment,
                    gpa: enrollmentService.calculateGPA(enrollment.student.id)
            ]
        }

        // فلترة حسب GPA مع تحقق من الصحة
        if (gpaMin != null && gpaMax != null) {
            if (gpaMax < gpaMin) {
                flash.message = "GPA Max cannot be less than GPA Min."
                gpaList = []  // أو ممكن تجاهل الفلترة بالكامل
            } else {
                gpaList = gpaList.findAll { it.gpa >= gpaMin && it.gpa <= gpaMax }
            }
        }

        render(view: "index", model: [
                enrollmentList: gpaList.collect { it.enrollment },
                enrollmentCount: gpaList.size(),
                isAdmin: isAdmin,
                gpaList: gpaList.collect { [studentId: it.enrollment.student.id, gpa: it.gpa] },
                params: params
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

        def gpaList

        if (isAdmin) {
            // الأدمن يشوف كل الطلاب
            def students = Student.list()
            gpaList = students.collect { student ->
                [
                        studentId  : student.id,
                        studentName: student.name,
                        gpa        : enrollmentService.calculateGPA(student.id)
                ]
            }
        } else {
            // اليوزر يشوف GPA تبعه فقط
            def student = Student.findByUser(currentUser)
            if (student) {
                gpaList = [[
                                   studentId  : student.id,
                                   studentName: student.name,
                                   gpa        : enrollmentService.calculateGPA(student.id)
                           ]]
            } else {
                gpaList = []
            }
        }

        render(view: "gpa", model: [
                gpaList: gpaList,
                isAdmin: isAdmin
        ])
    }


}
