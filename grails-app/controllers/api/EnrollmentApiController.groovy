package api

import StCo.Enrollment
import grails.rest.RestfulController
import java.text.SimpleDateFormat

class EnrollmentApiController extends RestfulController<Enrollment> {
    static namespace = 'api'
    static responseFormats = ['json']
    static allowedMethods = [
            index: 'GET', show: 'GET',
            save: 'POST', update: 'PUT', delete: 'DELETE'
    ]

    EnrollmentApiService enrollmentApiService

    EnrollmentApiController() { super(Enrollment) }

    def index(Integer max, Integer offset) {
        params.max = Math.min(max ?: 20, 100)
        params.offset = offset ?: 0
        respond Enrollment.list(params), [status: 200]
    }

    @Override
    def save() {
        def json = request.JSON

        try {
            Long studentId = json.getLong('studentId')
            Long courseId = json.getLong('courseId')
            Date enrollmentDate = json.enrollmentDate ?
                    new SimpleDateFormat("yyyy-MM-dd").parse(json.enrollmentDate.toString()) : new Date()
            Double grade = json.grade ? json.grade as Double : null

            def result = enrollmentApiService.enroll(studentId, courseId, enrollmentDate, grade)

            if (!result.ok) {
                respond(result.errors ? [errors: result.errors] : [message: result.msg],
                        [status: result.status as int])
                return
            }

            respond result.data, [status: 201]

        } catch (Exception e) {
            respond([message: "Invalid request data: ${e.message}"], [status: 400])
        }
    }

    @Override
    def update() {
        def json = request.JSON
        Long id = params.long('id')

        def result = enrollmentApiService.updateEnrollment(id, json)

        if (!result.ok) {
            respond(result.errors ? [errors: result.errors] : [message: result.msg],
                    [status: result.status as int])
            return
        }

        respond result.data, [status: 200]
    }

    @Override
    def delete() {
        Long id = params.long('id')
        def result = enrollmentApiService.deleteEnrollment(id)

        if (!result.ok) {
            respond([message: result.msg], [status: result.status as int])
            return
        }

        render status: 204
    }
}
