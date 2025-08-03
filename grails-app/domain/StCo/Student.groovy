package StCo

class Student {

    String name
    String email
    static hasMany = [enrollments: Enrollment]


    static constraints = {
        name blank: false
        email email: true, blank: false
    }

    String toString(){
       name
      }
}
