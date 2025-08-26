package StCo

import grails.validation.ValidationException
import groovy.util.logging.Slf4j
import grails.gorm.transactions.Transactional
import org.springframework.security.crypto.password.PasswordEncoder

import static org.springframework.http.HttpStatus.*

@Slf4j
class StudentController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    StudentService studentService
    def springSecurityService

    def index(Integer max, String query, String sortBy) {
        // Pagination: أقصى عدد 100، الافتراضي 10
        params.max = Math.min(max ?: 10, 100)

        // جلب المستخدم الحالي
        def currentUser = springSecurityService.currentUser
        if (!currentUser) {
            flash.message = "Please login first"
            redirect(controller: "login", action: "auth")
            return
        }

        boolean isAdmin = currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }

        def studentList
        def studentCount

        if (isAdmin) {
            studentList = query ? Student.createCriteria().list(params) {
                // البحث فقط في الاسم
                ilike('name', "%${query}%")
                // الترتيب فقط على الاسم إذا تم تحديد sortBy
                if (sortBy == 'name') order('name', 'asc')
            } : Student.createCriteria().list(params) {
                // بدون بحث، فقط ترتيب على الاسم إذا تم تحديد sortBy
                if (sortBy == 'name') order('name', 'asc')
            }
            studentCount = studentList.totalCount
        } else {
            // Normal User يرى بياناته فقط
            def student = Student.findByUser(currentUser)
            studentList = student ? [student] : []
            studentCount = studentList.size()
        }

        // تمرير البيانات للـ GSP view
        render(view: "index", model: [
                studentList: studentList,
                studentCount: studentCount,
                isAdmin: isAdmin,
                query: query,
                sortBy: sortBy
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
        def currentUser = springSecurityService.currentUser
        if (!currentUser) {
            flash.message = "Please login first"
            redirect(controller: "login", action: "auth")
            return
        }

        boolean isAdmin = currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }
        def student

        if (isAdmin && id) {
            student = Student.get(id)
        } else {
            student = Student.findByUser(currentUser)
        }

        if (!student) {
            flash.message = "Student data not found."
            redirect(action: "index")
            return
        }

        def enrollments = Enrollment.findAllByStudent(student)
        def courses = enrollments.collect { it.course }

        render(view: "show", model: [student: student, courses: courses])
    }

    @Transactional
    def save() {
        if (params.password != params.passwordConfirm) {
            flash.message = "Passwords do not match"
            render(view: 'create', model: [params: params])
            return
        }

        def user = new User(username: params.username, password: passwordEncoder.encode(params.password))
        if (!user.save(flush: true)) {
            render(view: 'create', model: [params: params, user: user])
            return
        }

        def student = new Student(
                name: params.name,
                email: params.email,
                user: user
        )

        def photoFile = request.getFile('profilePhoto')
        if (photoFile && !photoFile.empty) {
            student.profilePhoto = photoFile.bytes
            student.profilePhotoFilename = photoFile.originalFilename
        }

        if (!student.save(flush: true)) {
            render(view: 'create', model: [student: student])
            return
        }

        def role = Role.findByAuthority('ROLE_STUDENT')
        UserRole.create(user, role, true)

        flash.message = "Student and user created successfully"
        redirect(action: "index")
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

    @Transactional
    def update() {
        def student = Student.get(params.id)
        if (!student) {
            notFound()
            return
        }

        // تحديث خصائص الطالب فقط
        student.properties['name','email'] = params

        // تحديث اسم المستخدم
        if (params.username) {
            student.user.username = params.username
        }

        // تحديث كلمة المرور (مع تشفير)
        if (params.password && params.password == params.confirmPassword) {
            student.user.password = params.password
        } else if (params.password != params.confirmPassword) {
            flash.message = "Password and Confirm Password do not match"
            render(view: "edit", model: [student: student])
            return
        }

        // ✅ تحديث صورة البروفايل
        def photoFile = request.getFile('profilePhoto')
        if (photoFile && !photoFile.empty) {
            student.profilePhoto = photoFile.bytes
            student.profilePhotoFilename = photoFile.originalFilename
        }

        try {
            if (!student.user.save(flush: true)) {
                student.user.errors.allErrors.each { log.error it }
                render(view: "edit", model: [student: student])
                return
            }
            if (!student.save(flush: true)) {
                student.errors.allErrors.each { log.error it }
                render(view: "edit", model: [student: student])
                return
            }
        } catch (Exception e) {
            flash.message = e.message
            render(view: "edit", model: [student: student])
            return
        }

        flash.message = "Student updated successfully"
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
        response.contentType = 'image/png'
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
