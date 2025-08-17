package StCo

class Course {

    String title
    String description
    Double credits  //  عدد الساعات
    static hasMany = [enrollments: Enrollment]


    static constraints = {
        title blank: false
        description blank: false
        credits validator: { val, obj ->
            if (val > 10) {
                return "course.credits.tooHigh"
            }
            return true
        }
    }

    String toString(){
        title
    }
}
