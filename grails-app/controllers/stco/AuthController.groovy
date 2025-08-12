package StCo

import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService

class AuthController {

    SpringSecurityService springSecurityService

    def login() {
        render view: 'login'
    }

    def register() {
        render view: 'register'
    }

    def saveUser() {
        def user = new User(username: params.username)
        user.password = springSecurityService.encodePassword(params.password)
        user.save(flush: true)

        def role = Role.findByAuthority("ROLE_USER")
        if (role && user) {
            UserRole.create(user, role, true)
        }

        flash.message = "Account created successfully. Please log in."
        redirect action: 'login'
    }

    def logout() {
        redirect uri: '/logout'  // Spring Security will handle it
    }
}
