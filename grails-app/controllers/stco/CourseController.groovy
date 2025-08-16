package StCo

import grails.rest.Resource
import grails.validation.ValidationException
import groovy.util.logging.Slf4j

import static org.springframework.http.HttpStatus.*

@Resource()
@Slf4j
class CourseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    CourseService courseService
    def springSecurityService
    def index(Integer max, String query, String sortBy, String order) {
        params.max = Math.min(max ?: 10, 100)
        if (!order) order = 'asc'

        def currentUser = springSecurityService.currentUser
        boolean isAdmin = currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }

        def courses
        def totalCount

        if (query) {
            courses = Course.createCriteria().list(params) {
                or {
                    ilike('title', "%${query}%")
                    ilike('description', "%${query}%")

                    // تحقق إن query رقم قبل المقارنة مع credits
                    try {
                        def queryDouble = query.toDouble()
                        eq('credits', queryDouble)
                    } catch (NumberFormatException e) {
                        // إذا لم يكن رقم، تجاهل هذا الشرط
                    }
                }

                if (sortBy) {
                    order(sortBy, order)
                }
            }
            totalCount = courses.totalCount
        } else {
            if (sortBy) {
                params.sort = sortBy
                params.order = order
            }
            courses = courseService.list(params)
            totalCount = courseService.count()
        }

        render(view: "index", model: [
                courseList: courses,
                courseCount: totalCount,
                isAdmin: isAdmin,
                query: query,
                sortBy: sortBy,
                order: order
        ])
    }




    def show(Long id) {
        def course = courseService.get(id)
        if (!course) {
            notFound()
            return
        }
        render(view: "show", model: [course: course])
    }

    def create() {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to create new courses."
            redirect(action: "index")
            return
        }
        render(view: "create", model: [course: new Course(params)])
    }

    def save(Course course) {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to save courses."
            redirect(action: "index")
            return
        }

        if (!course) {
            notFound()
            return
        }

        try {
            courseService.save(course)
        } catch (ValidationException e) {
            render(view: "create", model: [course: course])
            return
        }

        flash.message = message(code: 'default.created.message', args: [
                message(code: 'course.label', default: 'Course'),
                course.id
        ])
        redirect(action: "show", id: course.id)
    }

    def edit(Long id) {
        def course = courseService.get(id)
        if (!course) {
            notFound()
            return
        }

        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to edit courses."
            redirect(action: "index")
            return
        }

        render(view: "edit", model: [course: course])
    }

    def update(Course course) {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to update courses."
            redirect(action: "index")
            return
        }

        if (!course) {
            notFound()
            return
        }

        try {
            courseService.save(course)
        } catch (ValidationException e) {
            render(view: "edit", model: [course: course])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [
                message(code: 'course.label', default: 'Course'),
                course.id
        ])
        redirect(action: "show", id: course.id)
    }

    def delete(Long id) {
        def currentUser = springSecurityService.currentUser
        if (!currentUser.authorities.any { it.authority == 'ROLE_ADMIN' }) {
            flash.message = "You do not have permission to delete courses."
            redirect(action: "index")
            return
        }

        def course = courseService.get(id)
        if (!course) {
            notFound()
            return
        }

        courseService.delete(id)

        flash.message = message(code: 'default.deleted.message', args: [
                message(code: 'course.label', default: 'Course'),
                id
        ])
        redirect(action: "index")
    }

    protected void notFound() {
        flash.message = message(code: 'default.not.found.message', args: [
                message(code: 'course.label', default: 'Course'),
                params.id
        ])
        redirect(action: "index")
    }
}
