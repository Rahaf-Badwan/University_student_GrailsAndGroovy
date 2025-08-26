package api

import StCo.Student
import StCo.User
import StCo.Role
import StCo.UserRole

import grails.rest.RestfulController

class StudentApiController extends RestfulController<Student> {
    static namespace = 'api'
    static responseFormats = ['json']
    static allowedMethods = [
            index: 'GET', show: 'GET',
            save : 'POST', update: 'PUT', delete: 'DELETE'
    ]

    StudentApiController() { super(Student) }

    def index(Integer max, Integer offset) {
        params.max = Math.min(max ?: 20, 100)
        params.offset = offset ?: 0
        respond Student.list(params), [status: 200]
    }

    // ===== Helper: استخراج userId بأمان من JSON =====
    private Long extractUserId(def json) {
        def v = null
        if (json?.user instanceof Map && json.user.containsKey('id')) {
            v = json.user.id
        } else if (json?.containsKey('userId')) {
            v = json.userId
        }
        if (v == null) return null
        if (v instanceof Number) return ((Number) v).longValue()
        def s = v.toString().trim()
        return (s.isNumber()) ? s.toLong() : null
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

                // إنشاء الطالب وربطه بالمستخدم
                def student = new Student(
                        name: json.name,
                        email: json.email,
                        photoUrl: json.photoUrl,
                        user: user
                )
                if (!student.save(flush: true)) {
                    status.setRollbackOnly()
                    respond([message: 'فشل إنشاء الطالب', errors: student.errors], [status: 422])
                    return
                }

                respond student, [status: 201]

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
            if (json.containsKey('photoUrl')) student.photoUrl = json.photoUrl
            if (json.containsKey('profilePhotoFilename')) student.profilePhotoFilename = json.profilePhotoFilename

            // ✅ التعامل مع user
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

            student.save()
            respond student, [status: 200]
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
