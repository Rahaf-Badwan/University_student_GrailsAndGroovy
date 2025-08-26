package api

import StCo.Book
import StCo.BookImportService
import grails.rest.RestfulController
import grails.plugin.springsecurity.annotation.Secured
import grails.gorm.transactions.Transactional
import grails.converters.JSON

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class BookApiController extends RestfulController<Book> {

    static responseFormats = ['json']
    BookImportService bookImportService

    BookApiController() { super(Book) }

    // GET /api/book — list all
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

        respond list
    }

    // GET /api/book/fetch — import from Google
    def fetch() {
        try {
            def result = bookImportService.importFromGoogle(params.q ?: '', params.int('externalMax') ?: 20)
            render([status: 'success', imported: result.size(), result: result] as JSON)
        } catch(Exception e) {
            render([status: 'error', message: e.message] as JSON)
        }
    }

    // GET /api/book/{id}
    def show(Long id) {
        def book = Book.get(id)
        if (!book) {
            render([status: 'error', message: 'Book not found'] as JSON)
            return
        }
        respond book
    }

    // POST /api/book
    @Transactional
    def save(Book book) {
        if (!book.save(flush: true)) {
            render([status: 'error', errors: book.errors] as JSON)
            return
        }
        respond book, [status: 201]
    }

    // PUT /api/book/{id}
    @Transactional
    def update(Long id) {
        def book = Book.get(id)
        if (!book) {
            render([status: 'error', message: 'Book not found'] as JSON)
            return
        }
        book.properties = request.JSON
        if (!book.save(flush: true)) {
            render([status: 'error', errors: book.errors] as JSON)
            return
        }
        respond book
    }

    // DELETE /api/book/{id}
    @Transactional
    def delete(Long id) {
        def book = Book.get(id)
        if (!book) {
            render([status: 'error', message: 'Book not found'] as JSON)
            return
        }
        book.delete(flush: true)
        render([status: 'success', message: 'Book deleted'] as JSON)
    }
}
