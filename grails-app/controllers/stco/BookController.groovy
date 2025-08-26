package StCo

import grails.rest.RestfulController
import grails.plugin.springsecurity.annotation.Secured
import grails.gorm.transactions.Transactional

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class BookController extends RestfulController<Book> {

    static responseFormats = ['html', 'json']

    BookController() {
        super(Book)
    }

    BookImportService bookImportService

    // GET /books  (HTML + JSON)
    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        params.offset = params.int('offset') ?: 0

        String q = params.q?.trim()
        Integer externalMax = params.int('externalMax') ?: 20
        Map importResult = [:]

        if (q) {
            try {
                importResult = bookImportService.importFromGoogle(q, externalMax)
            } catch (Exception e) {
                importResult = [error: e.message ?: 'Import failed']
            }
        }

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

        if (request.format == 'json' || request.getHeader('Accept')?.contains('application/json')) {
            respond([books: list, total: list.totalCount, importInfo: importResult], [status: 200])
        } else {
            render(view: 'index', model: [
                    bookList   : list,
                    bookCount  : list.totalCount,
                    q          : q,
                    externalMax: externalMax,
                    importInfo : importResult
            ])
        }
    }

    // GET /book/{id} → HTML
    // GET /api/book/{id} → JSON
    def show(Long id) {
        def book = Book.get(id)
        if (!book) {
            if (request.format == 'json' || request.getHeader('Accept')?.contains('application/json')) {
                render(status: 404, text: '{"error":"Book not found"}', contentType: 'application/json')
            } else {
                flash.message = "Book not found"
                redirect(action: "index")
            }
            return
        }

        if (request.format == 'json' || request.getHeader('Accept')?.contains('application/json')) {
            respond book, [status: 200]
        } else {
            render(view: 'show', model: [book: book])
        }
    }

    // Disable manual changes
    def create() { notAllowed() }

    @Transactional
    def save() { notAllowed() }

    def edit() { notAllowed() }

    @Transactional
    def update() { notAllowed() }

    @Transactional
    def delete() { notAllowed() }

    protected void notAllowed() {
        if (request.format == 'json' || request.getHeader('Accept')?.contains('application/json')) {
            render(status: 405, text: '{"error":"This operation is disabled."}', contentType: 'application/json')
        } else {
            response.sendError(405, 'This operation is disabled.')
        }
    }
}
