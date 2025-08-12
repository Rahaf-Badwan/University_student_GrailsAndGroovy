package StCo

import grails.plugin.springsecurity.SpringSecurityService

class BootStrap {

    SpringSecurityService springSecurityService

    def init = { servletContext ->

        Role.withTransaction {
            println("------------------------------------------")
            // إنشاء الصلاحيات إذا مش موجودة
            if (!Role.findByAuthority('ROLE_ADMIN')) {
                new Role(authority: 'ROLE_ADMIN').save(flush: true)
            }

            if (!Role.findByAuthority('ROLE_USER')) {
                new Role(authority: 'ROLE_USER').save(flush: true)
            }

            // إنشاء المستخدم admin
            if (!User.findByUsername('admin')) {
                def admin = new User(
                        username: 'admin',
                        password: 'admin123'
                ).save(flush: true)

                def adminRole = Role.findByAuthority('ROLE_ADMIN')
                UserRole.create(admin, adminRole, true)
            }

            // إنشاء المستخدم user
            if (!User.findByUsername('user')) {
                def user = new User(
                        username: 'user',
                        password: 'user123'
                ).save(flush: true)

                def userRole = Role.findByAuthority('ROLE_USER')
                UserRole.create(user, userRole, true)
            }

            // إنشاء مستخدم طالب جديد وربطه مع Student
            if (!User.findByUsername('rahaf')) {
                def rahafUser = new User(
                        username: 'rahaf',
                        password: 'rahaf123'
                ).save(flush: true)

                def userRole = Role.findByAuthority('ROLE_USER')
                UserRole.create(rahafUser, userRole, true)

                def rahafStudent = new Student(
                        name: 'Rahaf Badwan',
                        email: 'rahaf@example.com',
                        user: rahafUser
                ).save(flush: true)
            }
        }

    }

    def destroy = {
    }
}
