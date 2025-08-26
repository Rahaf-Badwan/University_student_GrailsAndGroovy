package StCo

import grails.rest.RestfulController
import grails.plugin.springsecurity.annotation.Secured
import grails.gorm.transactions.Transactional

class BookController extends RestfulController<Book> {

    static responseFormats = ['html']

    BookController() { super(Book) }

    // GET /books — عرض قائمة الكتب
    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        params.offset = params.int('offset') ?: 0

        String q = params.q?.trim()

        def list = Book.createCriteria().list(params) {
            if (q) {
                or {
                    ilike('title', "%${q}%")
                    ilike('authors', "%${q}%")
                    ilike('categories', "%${q}%")
                }
            }
            order('dateCreated', 'desc')
        }

        render(view: 'index', model: [
                bookList  : list,
                bookCount : list.totalCount,
                q         : q
        ])
    }

    // GET /book/{id} — عرض تفاصيل كتاب
    def show(Long id) {
        def book = Book.get(id)
        if (!book) {
            flash.message = "Book not found"
            redirect(action: "index")
            return
        }
        render(view: 'show', model: [book: book])
    }

    // منع أي تعديل من الـ HTML
    def create() { notAllowed() }
    @Transactional def save() { notAllowed() }
    def edit() { notAllowed() }
    @Transactional def update() { notAllowed() }
    @Transactional def delete() { notAllowed() }

    protected void notAllowed() {
        response.sendError(405, 'This operation is disabled.')
    }
}
