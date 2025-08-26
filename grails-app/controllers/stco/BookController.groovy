package StCo

import grails.rest.RestfulController
import grails.plugin.springsecurity.annotation.Secured
import grails.gorm.transactions.Transactional

class BookController extends RestfulController<Book> {

    static responseFormats = ['html', 'json']
    BookImportService bookImportService

    BookController() { super(Book) }

    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        params.offset = params.int('offset') ?: 0

        String q = params.q?.trim()
        Integer externalMax = params.int('externalMax') ?: 20
        Map importResult = [:]

        // استيراد من Google إذا فيه q
        if (q) {
            try {
                importResult = bookImportService.importFromGoogle(q, externalMax)
            } catch (Exception e) {
                importResult = [error: e.message ?: 'Import failed']
            }
        }

        // بعد الاستيراد: جلب كل الكتب مباشرة بدون فلترة q
        def list = Book.createCriteria().list(params) {
            order('dateCreated', 'desc')  // ترتيب حسب تاريخ الإنشاء
        }

        render view: 'index', model: [
                bookList   : list,
                bookCount  : list.totalCount,
                q          : q,
                externalMax: externalMax,
                importInfo : importResult
        ]
    }

    // GET /books/{id} (HTML) أو JSON
    def show(Long id) {
        def book = Book.get(id)
        if (!book) {
            flash.message = "Book not found"
            redirect(action: "index")
            return
        }

        if (request.format == 'json' || request.getHeader('Accept')?.contains('application/json')) {
            respond book
        } else {
            render(view: 'show', model: [book: book])
        }
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

    protected void notFound() {
        response.sendError(404, 'Book not found.')
    }
}
