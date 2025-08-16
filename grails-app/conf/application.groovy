// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'StCo.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'StCo.UserRole'
grails.plugin.springsecurity.authority.className = 'StCo.Role'

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        [pattern: '/',               access: ['permitAll']],
        [pattern: '/error',          access: ['permitAll']],
        [pattern: '/index',          access: ['permitAll']],
        [pattern: '/index.gsp',      access: ['permitAll']],
        [pattern: '/shutdown',       access: ['permitAll']],
        [pattern: '/assets/**',      access: ['permitAll']],
        [pattern: '/**/js/**',       access: ['permitAll']],
        [pattern: '/**/css/**',      access: ['permitAll']],
        [pattern: '/**/images/**',   access: ['permitAll']],
        [pattern: '/**/favicon.ico', access: ['permitAll']],

        // فتح الوصول لكل المستخدمين المسجلين لكل الروابط المهمة
        [pattern: '/student/**',     access: ['IS_AUTHENTICATED_FULLY']],
        [pattern: '/course/**',      access: ['IS_AUTHENTICATED_FULLY']],
        [pattern: '/enrollment/**',  access: ['IS_AUTHENTICATED_FULLY']],

        // السماح فقط للأدمن بتعديل وحذف عبر @Secured في الكنترولر نفسه
        [pattern: '/admin/**',       access: ['ROLE_ADMIN']],
        [pattern: '/user/**',        access: ['ROLE_USER', 'ROLE_ADMIN']],

        // أي شيء آخر يحتاج تسجيل دخول
        [pattern: '/**',             access: ['IS_AUTHENTICATED_FULLY']]
]


grails.plugin.springsecurity.filterChain.chainMap = [
		[pattern: '/assets/**',      filters: 'none'],
		[pattern: '/**/js/**',       filters: 'none'],
		[pattern: '/**/css/**',      filters: 'none'],
		[pattern: '/**/images/**',   filters: 'none'],
		[pattern: '/**/favicon.ico', filters: 'none'],
		[pattern: '/**',             filters: 'JOINED_FILTERS']
]
