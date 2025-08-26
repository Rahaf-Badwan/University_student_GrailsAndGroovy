package StCo;

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class AdminController {

    def springSecurityService

    def index() {
        render "Welcome Admin!"
    }

    def createUser() {
        respond new User(params)
    }

    def saveUser() {
        def user = new User(params)
        user.password = springSecurityService.encodePassword(params.password)
        if (!user.save(flush: true)) {
            render(view: 'createUser', model: [user: user])
            return
        }

        // ربط اليوزر بدور المستخدم العادي (ROLE_USER)
        def userRole = Role.findByAuthority('ROLE_USER')
        UserRole.create(user, userRole, true)

        flash.message = "User ${user.username} created successfully."
        redirect(action: 'listUsers')
    }

    def listUsers() {
        respond User.list()
    }
}
