// ================= Spring Security Core Plugin =================
grails.plugin.springsecurity.userLookup.userDomainClassName = 'StCo.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'StCo.UserRole'
grails.plugin.springsecurity.authority.className = 'StCo.Role'

grails.plugin.springsecurity.password.algorithm = 'bcrypt'

// ================= Static Rules =================
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        // الصفحات العامة
        [pattern: '/', access: ['permitAll']],
        [pattern: '/error', access: ['permitAll']],
        [pattern: '/index', access: ['permitAll']],
        [pattern: '/index.gsp', access: ['permitAll']],
        [pattern: '/shutdown', access: ['permitAll']],
        [pattern: '/assets/**', access: ['permitAll']],
        [pattern: '/**/js/**', access: ['permitAll']],
        [pattern: '/**/css/**', access: ['permitAll']],
        [pattern: '/**/images/**', access: ['permitAll']],
        [pattern: '/**/favicon.ico', access: ['permitAll']],

        // السماح الكامل لكل شيء تحت API (Postman)
        [pattern: '/api/**', access: ['permitAll']],

        // الكنترولرات العادية
        [pattern: '/student/**', access: ['IS_AUTHENTICATED_FULLY']],
        [pattern: '/course/**', access: ['IS_AUTHENTICATED_FULLY']],
        [pattern: '/enrollment/**', access: ['IS_AUTHENTICATED_FULLY']],
        [pattern: '/admin/**', access: ['ROLE_ADMIN']],
        [pattern: '/user/**', access: ['ROLE_USER', 'ROLE_ADMIN']],

        // أي شيء آخر يحتاج تسجيل دخول
        [pattern: '/**', access: ['IS_AUTHENTICATED_FULLY']]
]

// ================= Filter Chain =================
grails.plugin.springsecurity.filterChain.chainMap = [
        // تجاهل الفلاتر للأصول الثابتة
        [pattern: '/assets/**', filters: 'none'],
        [pattern: '/**/js/**', filters: 'none'],
        [pattern: '/**/css/**', filters: 'none'],
        [pattern: '/**/images/**', filters: 'none'],
        [pattern: '/**/favicon.ico', filters: 'none'],

        // تعطيل الفلاتر فقط للـ API
        [pattern: '/api/**', filters: 'none'],

        // باقي النظام يظل محمي
        [pattern: '/**', filters: 'JOINED_FILTERS']
]
