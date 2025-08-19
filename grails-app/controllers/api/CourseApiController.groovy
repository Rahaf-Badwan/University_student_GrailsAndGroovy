package api


import grails.rest.RestfulController
import StCo.Course

class CourseApiController extends RestfulController<Course> {
    static namespace = 'api'
    static responseFormats = ['json']
    static allowedMethods = [
            index: 'GET', show: 'GET',
            save: 'POST', update: 'PUT', delete: 'DELETE'
    ]

    CourseApiController() { super(Course) }

    def index(Integer max, Integer offset) {
        params.max = Math.min(max ?: 20, 100)
        params.offset = offset ?: 0
        respond Course.list(params), [status: 200]
    }

    @Override
    def save() {
        if (!(request.contentType ?: '').toLowerCase().contains('application/json')) {
            respond([message: 'Content-Type لازم يكون application/json'], [status: 415]); return
        }
        def json
        try { json = request.JSON } catch (ignored) {
            respond([message: 'Body ليس JSON صالحاً'], [status: 400]); return
        }
        if (!json) { respond([message: 'Body فاضي'], [status: 400]); return }

        Course.withTransaction { status ->
            def course = new Course(json)

            if (!course.validate()) {
                status.setRollbackOnly()
                respond([
                        message: 'Validation failed',
                        fieldErrors: course.errors.fieldErrors.collect { fe ->
                            [field: fe.field, rejectedValue: fe.rejectedValue, code: fe.code, message: fe.defaultMessage]
                        }
                ], [status: 422]); return
            }

            course.save()
            respond course, [status: 201]
        }
    }

    @Override
    def update() {
        if (!(request.contentType ?: '').toLowerCase().contains('application/json')) {
            respond([message: 'Content-Type لازم يكون application/json'], [status: 415]); return
        }
        def id = params.long('id')
        def course = Course.get(id)
        if (!course) { render status: 404; return }

        def json
        try { json = request.JSON } catch (ignored) {
            respond([message: 'Body ليس JSON صالحاً'], [status: 400]); return
        }

        Course.withTransaction { status ->
            course.properties = json
            if (!course.validate()) {
                status.setRollbackOnly()
                respond([
                        message: 'Validation failed',
                        fieldErrors: course.errors.fieldErrors.collect { fe ->
                            [field: fe.field, rejectedValue: fe.rejectedValue, code: fe.code, message: fe.defaultMessage]
                        }
                ], [status: 422]); return
            }
            course.save()
            respond course, [status: 200]
        }
    }

    @Override
    def delete() {
        def id = params.long('id')
        def course = Course.get(id)
        if (!course) { render status: 404; return }
        Course.withTransaction {
            course.delete()
        }
        render status: 204
    }
}
