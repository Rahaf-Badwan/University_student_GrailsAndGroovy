package StCo;

import grails.rest.RestfulController
import grails.plugin.springsecurity.annotation.Secured
import grails.gorm.transactions.Transactional

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class BookController extends RestfulController<Book> {

    static responseFormats = ['html', 'json']
    BookImportService bookImportService

    BookController() { super(Book) }

    // GET /book  (HTML) — import only for HTML
    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        params.offset = params.int('offset') ?: 0

        String q = params.q?.trim()
        Integer externalMax = params.int('externalMax') ?: 20
        Map importResult = [:]

        // Import only for HTML, not JSON API
        if (q && request.format != 'json') {
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

        if (request.format == 'json') {
            respond list, [status: 200]
        } else {
            render view: 'index', model: [
                    bookList   : list,
                    bookCount  : list.totalCount,
                    q          : q,
                    externalMax: externalMax,
                    importInfo : importResult
            ]
        }
    }

    def show(Long id) {
        def book = Book.get(id)
        if (!book) {
            notFound()
            return
        }
        respond book
    }

    def create() { notAllowed() }
    @Transactional def save() { notAllowed() }
    def edit() { notAllowed() }
    @Transactional def update() { notAllowed() }
    @Transactional def delete() { notAllowed() }

    protected void notAllowed() {
        response.sendError(405, 'This operation is disabled.')
    }

    protected void notFound() {
        response.sendError(404, 'Book not found.')
    }
}
