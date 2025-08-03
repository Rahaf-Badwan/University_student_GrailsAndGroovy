package StCo

class Enrollment {

    Date enrollmentDate = new Date()
    Double grade  //  علامة الطالب

    static belongsTo = [student: Student, course: Course]

    static constraints = {
        student nullable: false
        course nullable: false
        enrollmentDate nullable: false
        grade nullable: true, min: 0.0d, max: 4.0d  // GPA عادة من 0 إلى 4
    }

    String toString() {
        "${student?.name ?: 'No Student'} -> ${course?.title ?: 'No Course'}"
    }
}
