package StCo

import static org.springframework.http.HttpStatus.*
import groovy.util.logging.Slf4j

@Slf4j
class EnrollmentController {

    EnrollmentService enrollmentService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond enrollmentService.list(params), model: [enrollmentCount: enrollmentService.count()]
    }

    def show(Long id) {
        respond enrollmentService.get(id)
    }

    def create() {
        respond new Enrollment(params), model: [
                students: Student.list(),
                courses: Course.list()
        ]
    }



    def save(Enrollment enrollment) {
        if (enrollment == null) {
            notFound()
            return
        }

        try {
            enrollmentService.save(enrollment)
        } catch (RuntimeException e) {
            flash.message = e.message
            respond enrollment.errors, view: 'create', model: [
                    students: Student.list(),
                    courses: Course.list()
            ]
            return


    }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [
                        message(code: 'enrollment.label', default: 'Enrollment'),
                        enrollment.id
                ])
                redirect enrollment
            }
            '*' { respond enrollment, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond enrollmentService.get(id)
    }

    def update(Enrollment enrollment) {
        if (enrollment == null) {
            notFound()
            return
        }

        try {
            enrollmentService.save(enrollment)
        } catch (RuntimeException e) {
            flash.message = e.message
            respond enrollment, view: 'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [
                        message(code: 'enrollment.label', default: 'Enrollment'),
                        enrollment.id
                ])
                redirect enrollment
            }
            '*' { respond enrollment, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        enrollmentService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [
                        message(code: 'enrollment.label', default: 'Enrollment'),
                        id
                ])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    def gpa() {
        Long studentId = params.long("studentId")
        if (!studentId) {
            log.warn("⚠️ [gpa] Missing studentId parameter")
            render status: BAD_REQUEST, text: "⚠️ Missing studentId"
            return
        }

        log.info("📊 [gpa] Starting GPA calculation for studentId: $studentId")
        def gpa = enrollmentService.calculateGPA(studentId)
        log.info("🎓 [gpa] GPA for student $studentId is: $gpa")

        render "🎓 GPA for student ${studentId} is: ${gpa}"
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [
                        message(code: 'enrollment.label', default: 'Enrollment'),
                        params.id
                ])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
