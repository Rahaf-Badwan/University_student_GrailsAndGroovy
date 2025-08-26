package StCo;


class Book {
    // Unique ID from Google Books (used to prevent duplicates)
    String googleId

    // Basic metadata
    String title
    String authors              // can be long -> TEXT in mapping
    String description          // long text -> TEXT
    String categories           // comma-separated -> TEXT

    // Links (can be long) -> TEXT
    String thumbnailUrl
    String previewLink

    // Other metadata
    String  publishedDateRaw    // keep as String because Google may return just a year
    Integer pageCount

    Date dateCreated
    Date lastUpdated

    static constraints = {
        googleId blank: false, unique: true
        title    blank: false

        authors         nullable: true
        description     nullable: true, maxSize: 10000  // validation only; DB type set in mapping
        categories      nullable: true

        thumbnailUrl    nullable: true
        previewLink     nullable: true

        publishedDateRaw nullable: true
        pageCount        nullable: true, min: 0
    }

    static mapping = {
        // Make long fields TEXT in the database (avoids MySQL VARCHAR(255) truncation)
        description   type: 'text'
        categories    type: 'text'
        authors       type: 'text'
        thumbnailUrl  type: 'text'
        previewLink   type: 'text'

        // Optional but explicit:
        // id generator: 'identity'   // good for MySQL auto-increment
        // table 'book'               // default is fine, this is just explicit
    }

    String toString() {
        title ?: "Book #${id}"
    }
}
