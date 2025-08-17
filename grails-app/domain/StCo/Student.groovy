package StCo;
class Student {

    String name
    String email
    User user
    static belongsTo = [user: User]  // مهم لي cascade الحذف

    byte[] profilePhoto
    String profilePhotoFilename

    static hasMany = [enrollments: Enrollment]

    static constraints = {
        name blank: false
        email blank: false, email: true, validator: { val, obj ->
            if (!val?.endsWith("@university.edu")) return ['student.email.invalidDomain', val]
        }
        user nullable: false
        profilePhoto nullable: true, maxSize: 1024 * 1024 * 5
        profilePhotoFilename nullable: true
    }

    static mapping = {
        user cascade: 'all-delete-orphan' // حذف الـ User تلقائيًا مع الـ Student
    }

    String toString() {
        name
    }
}
