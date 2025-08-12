package StCo

class Student {

    String name
    String email

    User user   // العلاقة مع حساب المستخدم

    byte[] profilePhoto
    String profilePhotoFilename

    static hasMany = [enrollments: Enrollment]

    static constraints = {
        name blank: false
        email email: true, blank: false
        user nullable: false  // شيلنا unique:true من هنا

        profilePhoto nullable: true, maxSize: 1024 * 1024 * 5
        profilePhotoFilename nullable: true
    }

    String toString() {
        name
    }
}
