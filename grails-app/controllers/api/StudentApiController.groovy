package api

import StCo.Student
import StCo.User
import StCo.Role
import StCo.UserRole

import grails.rest.RestfulController
import grails.converters.JSON

class StudentApiController extends RestfulController<Student> {
    static namespace = 'api'
    static responseFormats = ['json']
    static allowedMethods = [
            index: 'GET', show: 'GET',
            save : 'POST', update: 'PUT', delete: 'DELETE'
    ]

    StudentApiController() { super(Student) }

    // 🔹 Helper: تجهيز JSON مرتب
    private Map toJson(Student s) {
        return [
                id        : s.id,
                name      : s.name,
                email     : s.email,
                profilePhotoFilename: s.profilePhotoFilename,
                profilePhotoUrl     : createLink(
                        controller: 'student',
                        action: 'profilePhoto',
                        id: s.id,
                        absolute: true
                )
        ]
    }

    def index(Integer max, Integer offset) {
        params.max = Math.min(max ?: 20, 100)
        params.offset = offset ?: 0
        def students = Student.list(params)
        render students.collect { toJson(it) } as JSON
    }

    def show(Long id) {
        def student = Student.get(id)
        if (!student) {
            render status: 404; return
        }
        render toJson(student) as JSON
    }

    def save() {
        if (!(request.contentType?.toLowerCase()?.contains('application/json'))) {
            respond([message: 'Content-Type يجب أن يكون application/json'], [status: 415])
            return
        }

        def json = request.JSON
        if (!json) {
            respond([message: 'Body فارغ'], [status: 400]); return
        }

        User.withTransaction { status ->
            try {
                def user = new User(
                        username: json.username,
                        password: json.password
                )
                if (!user.save(flush: true)) {
                    status.setRollbackOnly()
                    respond([message: 'فشل إنشاء المستخدم', errors: user.errors], [status: 422])
                    return
                }

                def student = new Student(
                        name : json.name,
                        email: json.email,
                        profilePhotoFilename: json.profilePhotoFilename,
                        user : user
                )
                if (!student.save(flush: true)) {
                    status.setRollbackOnly()
                    respond([message: 'فشل إنشاء الطالب', errors: student.errors], [status: 422])
                    return
                }

                render toJson(student) as JSON
            } catch (Exception e) {
                status.setRollbackOnly()
                respond([message: 'حدث خطأ', error: e.message], [status: 500])
            }
        }
    }

    @Override
    def update() {
        if (!(request.contentType ?: '').toLowerCase().contains('application/json')) {
            respond([message: 'Content-Type لازم يكون application/json'], [status: 415])
            return
        }

        def id = params.long('id')
        def student = Student.get(id)
        if (!student) {
            render status: 404; return
        }

        def json
        try {
            json = request.JSON
        } catch (ignored) {
            respond([message: 'Body ليس JSON صالحاً'], [status: 400])
            return
        }

        Student.withTransaction { status ->
            if (json.containsKey('name')) student.name = json.name
            if (json.containsKey('email')) student.email = json.email
            if (json.containsKey('profilePhotoFilename')) student.profilePhotoFilename = json.profilePhotoFilename

            if (json.user?.id) {
                def newUser = User.get(json.user.id)
                if (!newUser) {
                    respond([message: "User ${json.user.id} غير موجود"], [status: 404]); return
                }
                def other = Student.findByUser(newUser)
                if (other && other.id != student.id) {
                    respond([message: "User ${json.user.id} مربوط لطالب آخر id=${other.id}"], [status: 409])
                    return
                }
                student.user = newUser
            }

            if (!student.validate()) {
                status.setRollbackOnly()
                respond([
                        message    : 'Validation failed',
                        fieldErrors: student.errors.fieldErrors.collect { fe ->
                            [field: fe.field, rejectedValue: fe.rejectedValue, code: fe.code, message: fe.defaultMessage]
                        }
                ], [status: 422])
                return
            }

            student.save(flush: true)
            render toJson(student) as JSON
        }
    }

    @Override
    def delete() {
        def id = params.long('id')
        def student = Student.get(id)
        if (!student) {
            render status: 404; return
        }
        Student.withTransaction { student.delete() }
        render status: 204
    }
}
