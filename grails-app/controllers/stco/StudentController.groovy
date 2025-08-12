package StCo

import grails.rest.Resource
import grails.validation.ValidationException
import groovy.util.logging.Slf4j

import static org.springframework.http.HttpStatus.*

@Resource()
@Slf4j
class StudentController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    StudentService studentService
    def springSecurityService

    def index(Integer max) {
        def currentUser = springSecurityService.currentUser
        params.max = Math.min(max ?: 10, 100)

        def studentList = studentService.list(params)
        def studentCount = studentService.count()

        render(view: "index", model: [
                studentList: studentList,
                studentCount: studentCount,
                isAdmin: currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }
        ])
    }

    def create() {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to create new students."
            redirect(action: "index")
            return
        }
        render(view: "create", model: [student: new Student(params)])
    }
    def show(Long id) {
        def student = studentService.get(id)
        if (!student) {
            notFound()
            return
        }

        def currentUser = springSecurityService.currentUser

        if (!currentUser.authorities.any { it.authority in ['ROLE_ADMIN', 'ROLE_STUDENT'] }) {
            flash.message = "Access denied"
            redirect(action: "index")
            return
        }

        render(view: "show", model: [student: student])
    }


    def save(Student student) {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to save students."
            redirect(action: "index")
            return
        }

        if (!student) {
            notFound()
            return
        }

        // عيّن المستخدم الحالي للطالب تلقائياً
        student.user = currentUser

        def profilePhotoFile = request.getFile('profilePhoto')
        if (profilePhotoFile && !profilePhotoFile.empty) {
            if (!profilePhotoFile.contentType.startsWith('image/')) {
                student.errors.rejectValue('profilePhoto', 'invalid.file.type', 'File must be an image.')
                render(view: "create", model: [student: student])
                return
            }
            if (profilePhotoFile.size > 5 * 1024 * 1024) {
                student.errors.rejectValue('profilePhoto', 'file.too.large', 'File size must be less than 5MB.')
                render(view: "create", model: [student: student])
                return
            }

            student.profilePhoto = profilePhotoFile.bytes
            student.profilePhotoFilename = profilePhotoFile.originalFilename
        }

        try {
            studentService.save(student)
        } catch (ValidationException e) {
            render(view: "create", model: [student: student])
            return
        }

        flash.message = message(code: 'default.created.message', args: [
                message(code: 'student.label', default: 'Student'),
                student.id
        ])
        redirect(action: "show", id: student.id)
    }

    def edit(Long id) {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to edit students."
            redirect(action: "index")
            return
        }

        def student = studentService.get(id)
        if (!student) {
            notFound()
            return
        }

        render(view: "edit", model: [student: student])
    }

    def update(Student student) {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to update students."
            redirect(action: "index")
            return
        }

        if (!student) {
            notFound()
            return
        }

        def existingStudent = studentService.get(student.id)
        // حافظ على المستخدم القديم بدون تغيير
        student.user = existingStudent.user

        def profilePhotoFile = request.getFile('profilePhoto')
        if (profilePhotoFile && !profilePhotoFile.empty) {
            if (!profilePhotoFile.contentType.startsWith('image/')) {
                student.errors.rejectValue('profilePhoto', 'invalid.file.type', 'File must be an image.')
                render(view: "edit", model: [student: student])
                return
            }
            if (profilePhotoFile.size > 5 * 1024 * 1024) {
                student.errors.rejectValue('profilePhoto', 'file.too.large', 'File size must be less than 5MB.')
                render(view: "edit", model: [student: student])
                return
            }

            student.profilePhoto = profilePhotoFile.bytes
            student.profilePhotoFilename = profilePhotoFile.originalFilename
        } else {
            student.profilePhoto = existingStudent.profilePhoto
            student.profilePhotoFilename = existingStudent.profilePhotoFilename
        }

        try {
            studentService.save(student)
        } catch (ValidationException e) {
            render(view: "edit", model: [student: student])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [
                message(code: 'student.label', default: 'Student'),
                student.id
        ])
        redirect(action: "show", id: student.id)
    }

    def delete(Long id) {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to delete students."
            redirect(action: "index")
            return
        }

        def student = studentService.get(id)
        if (!student) {
            notFound()
            return
        }

        studentService.delete(id)

        flash.message = message(code: 'default.deleted.message', args: [
                message(code: 'student.label', default: 'Student'),
                id
        ])
        redirect(action: "index")
    }

    def profilePhoto(Long id) {
        def student = studentService.get(id)
        if (!student || !student.profilePhoto) {
            response.status = 404
            return
        }
        response.contentType = 'image/png' // يمكن ضبطها حسب نوع الصورة
        response.outputStream << student.profilePhoto
        response.outputStream.flush()
    }

    protected void notFound() {
        flash.message = message(code: 'default.not.found.message', args: [
                message(code: 'student.label', default: 'Student'),
                params.id
        ])
        redirect(action: "index")
    }
}
