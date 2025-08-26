package stco

class UrlMappings {

    static mappings = {

        // ================== Student GSP Pages ==================
        "/student"(controller: 'student', action: 'index', method: 'GET')
        "/student"(controller: 'student', action: 'save', method: 'POST')
        "/student/create"(controller: 'student', action: 'create', method: 'GET')
        "/student/edit/$id"(controller: 'student', action: 'edit', method: 'GET')
        "/student/show/$id"(controller: 'student', action: 'show', method: 'GET')
        "/student/delete/$id"(controller: 'student', action: 'delete', method: 'DELETE')

        // ================== Course GSP Pages ==================
        "/course"(controller: 'course', action: 'index', method: 'GET')
        "/course"(controller: 'course', action: 'save', method: 'POST')
        "/course/create"(controller: 'course', action: 'create', method: 'GET')
        "/course/edit/$id"(controller: 'course', action: 'edit', method: 'GET')
        "/course/show/$id"(controller: 'course', action: 'show', method: 'GET')
        "/course/delete/$id"(controller: 'course', action: 'delete', method: 'DELETE')

        // ================== Enrollment GSP Pages ==================
        "/enrollment"(controller: 'enrollment', action: 'index', method: 'GET')
        "/enrollment"(controller: 'enrollment', action: 'save', method: 'POST')
        "/enrollment/create"(controller: 'enrollment', action: 'create', method: 'GET')
        "/enrollment/edit/$id"(controller: 'enrollment', action: 'edit', method: 'GET')
        "/enrollment/show/$id"(controller: 'enrollment', action: 'show', method: 'GET')
        "/enrollment/delete/$id"(controller: 'enrollment', action: 'delete', method: 'DELETE')
        "/enrollment/gpa"(controller: 'enrollment', action: 'gpa', method: 'GET')

        // ================== Book GSP Pages ==================
        "/book"(controller: 'book', action: 'index', method: 'GET')
        "/book"(controller: 'book', action: 'save', method: 'POST')
        "/book/fetch"(controller: 'book', action: 'fetch', method: 'GET')
        "/book/create"(controller: 'book', action: 'create', method: 'GET')
        "/book/edit/$id"(controller: 'book', action: 'edit', method: 'GET')
        "/book/show/$id"(controller: 'book', action: 'show', method: 'GET')
        "/book/delete/$id"(controller: 'book', action: 'delete', method: 'DELETE')

        // ================== API Endpoints ==================
        "/api/student"(controller: 'studentApi', action: 'index', method: 'GET')
        "/api/student"(controller: 'studentApi', action: 'save', method: 'POST')
        "/api/student/$id"(controller: 'studentApi', action: 'show', method: 'GET')
        "/api/student/$id"(controller: 'studentApi', action: 'update', method: 'PUT')
        "/api/student/$id"(controller: 'studentApi', action: 'delete', method: 'DELETE')

        "/api/course"(controller: 'courseApi', action: 'index', method: 'GET')
        "/api/course"(controller: 'courseApi', action: 'save', method: 'POST')
        "/api/course/$id"(controller: 'courseApi', action: 'show', method: 'GET')
        "/api/course/$id"(controller: 'courseApi', action: 'update', method: 'PUT')
        "/api/course/$id"(controller: 'courseApi', action: 'delete', method: 'DELETE')

        "/api/enrollment"(controller: 'enrollmentApi', action: 'index', method: 'GET')
        "/api/enrollment"(controller: 'enrollmentApi', action: 'save', method: 'POST')
        "/api/enrollment/$id"(controller: 'enrollmentApi', action: 'show', method: 'GET')
        "/api/enrollment/$id"(controller: 'enrollmentApi', action: 'update', method: 'PUT')
        "/api/enrollment/$id"(controller: 'enrollmentApi', action: 'delete', method: 'DELETE')

        // ================== Book API Endpoints ==================
        "/api/book"(controller: 'bookApi', action: 'index', method: 'GET')
        "/api/book"(controller: 'bookApi', action: 'save', method: 'POST')
        "/api/book/fetch"(controller: 'bookApi', action: 'fetch', method: 'GET')
        "/api/book/$id"(controller: 'bookApi', action: 'show', method: 'GET')
        "/api/book/$id"(controller: 'bookApi', action: 'update', method: 'PUT')
        "/api/book/$id"(controller: 'bookApi', action: 'delete', method: 'DELETE')

        // ================== Fallback ==================
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
            }
        }

        // home page → Dashboard
        "/"(controller: "dashboard", action: "index")

        // errors
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
