package StCo

class Course {

    String title
    String description
    Double credits  //  عدد الساعات
    static hasMany = [enrollments: Enrollment]


    static constraints = {
        title blank: false
        description blank: false
        credits nullable: false, min: 1d
    }

    String toString(){
        title
    }
}
