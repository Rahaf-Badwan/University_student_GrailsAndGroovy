package stco

class UrlMappings {

    static mappings = {

        // ================== GSP Pages ==================
        "/student"(controller: 'student') {
            action = [GET: 'index', POST: 'save']
        }
        "/student/create"(controller: 'student', action: 'create')
        "/student/edit/$id"(controller: 'student', action: 'edit')
        "/student/show/$id"(controller: 'student', action: 'show')
        "/student/delete/$id"(controller: 'student', action: 'delete', method: "DELETE")

        "/course"(controller: 'course') {
            action = [GET: 'index', POST: 'save']
        }
        "/course/create"(controller: 'course', action: 'create')
        "/course/edit/$id"(controller: 'course', action: 'edit')
        "/course/show/$id"(controller: 'course', action: 'show')
        "/course/delete/$id"(controller: 'course', action: 'delete', method: "DELETE")

        "/enrollment"(controller: 'enrollment') {
            action = [GET: 'index', POST: 'save']
        }
        "/enrollment/create"(controller: 'enrollment', action: 'create')
        "/enrollment/edit/$id"(controller: 'enrollment', action: 'edit')
        "/enrollment/show/$id"(controller: 'enrollment', action: 'show')
        "/enrollment/delete/$id"(controller: 'enrollment', action: 'delete', method: "DELETE")

        // GPA custom route
        "/enrollment/gpa"(controller: 'enrollment', action: 'gpa', method: 'GET')


        // ================== API Endpoints ==================
        "/api/student"(controller: 'studentApi') {
            action = [GET: 'index', POST: 'save']
        }
        "/api/student/$id"(controller: 'studentApi') {
            action = [GET: 'show', PUT: 'update', DELETE: 'delete']
        }

        "/api/course"(controller: 'courseApi') {
            action = [GET: 'index', POST: 'save']
        }
        "/api/course/$id"(controller: 'courseApi') {
            action = [GET: 'show', PUT: 'update', DELETE: 'delete']
        }

        "/api/enrollment"(controller: 'enrollmentApi') {
            action = [GET: 'index', POST: 'save']
        }
        "/api/enrollment/$id"(controller: 'enrollmentApi') {
            action = [GET: 'show', PUT: 'update', DELETE: 'delete']
        }

        // fallback for GSP or API
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        // home page → Dashboard
        "/"(controller: "dashboard", action: "index")

        // errors
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
