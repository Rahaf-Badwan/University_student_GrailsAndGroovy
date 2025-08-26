package StCo


import groovy.json.JsonSlurper
import grails.gorm.transactions.Transactional

@Transactional
// <-- run the whole import in a DB transaction
class BookImportService {

    /**
     * Search Google Books and upsert results into DB.
     * @param query search term
     * @param maxResults Google max per request (1..40)
     * @return [saved:<int>, updated:<int>, skipped:<int>, items:<List<Book>>]
     */
    Map importFromGoogle(String query, Integer maxResults = 20) {
        if (!query) return [saved: 0, updated: 0, skipped: 0, items: []]

        int limit = Math.max(1, Math.min(maxResults ?: 20, 40))
        String urlStr = "https://www.googleapis.com/books/v1/volumes?q=${URLEncoder.encode(query, 'UTF-8')}&maxResults=${limit}"

        def json
        try {
            def conn = new URL(urlStr).openConnection()
            conn.setRequestProperty('Accept', 'application/json')
            conn.setConnectTimeout(10000)
            conn.setReadTimeout(15000)
            json = new JsonSlurper().parse(conn.inputStream.newReader('UTF-8'))
        } catch (Throwable t) {
            throw new RuntimeException("Google Books fetch failed: ${t.message}", t)
        }

        int saved = 0, updated = 0, skipped = 0
        List<Book> items = []

        (json?.items ?: []).each { item ->
            String gid = item?.id as String
            def vi = item?.volumeInfo ?: [:]
            if (!gid || !vi?.title) {
                skipped++; return
            }

            String authors = (vi.authors instanceof List) ? (vi.authors as List).join(", ") : (vi.authors ?: null)
            String categories = (vi.categories instanceof List) ? (vi.categories as List).join(", ") : (vi.categories ?: null)
            String thumb = vi?.imageLinks?.thumbnail ?: vi?.imageLinks?.smallThumbnail
            String preview = vi?.previewLink
            String desc = vi?.description
            String pub = vi?.publishedDate
            Integer pages = (vi?.pageCount instanceof Number) ? (vi.pageCount as Integer) : null

            Book b = Book.findByGoogleId(gid)
            if (!b) {
                b = new Book(
                        googleId: gid,
                        title: vi.title,
                        authors: authors,
                        description: desc,
                        thumbnailUrl: thumb,
                        previewLink: preview,
                        publishedDateRaw: pub,
                        pageCount: pages,
                        categories: categories
                )
                if (b.save(flush: false, failOnError: false)) {
                    saved++; items << b
                } else {
                    skipped++
                }
            } else {
                b.with {
                    title = vi.title ?: title
                    authors = authors ?: authors
                    description = desc ?: description
                    thumbnailUrl = thumb ?: thumbnailUrl
                    previewLink = preview ?: previewLink
                    publishedDateRaw = pub ?: publishedDateRaw
                    pageCount = pages ?: pageCount
                    categories = categories ?: categories
                }
                if (b.isDirty() && b.save(flush: false, failOnError: false)) {
                    updated++
                } else {
                    skipped++
                }
                items << b
            }
        }

        // No explicit flush here; commit will flush automatically.
        return [saved: saved, updated: updated, skipped: skipped, items: items]
    }
}
