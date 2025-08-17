package StCo

class Enrollment {

    Date enrollmentDate = new Date()
    Double grade  // علامة الطالب (GPA)

    static belongsTo = [student: Student, course: Course]

    static constraints = {
        student nullable: false
        course nullable: false
        enrollmentDate nullable: false

        // ✅ custom validator على العلاقة بين grade و enrollmentDate
        grade nullable: true, min: 0.0d, max: 4.0d, validator: { val, obj ->
            if (val != null && val < 2.0 && obj.enrollmentDate?.year + 1900 < 2020) {
                return ['enrollment.invalidForLowGpa']
            }
        }
    }

    String toString() {
        "${student?.name ?: 'No Student'} -> ${course?.title ?: 'No Course'}"
    }
}
