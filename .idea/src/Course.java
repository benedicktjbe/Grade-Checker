public class Course {

    String code;
    String title;
    double units;
    String year;
    String term;
    String grade;

    public Course(String code, String title, double units, String year, String term, String grade) {
        this.code  = code;
        this.title = title;
        this.units = units;
        this.year  = year;
        this.term  = term;
        this.grade = grade;
    }

    public String toFileString() {
        return String.join(",", code, title, String.valueOf(units), year, term, grade);
    }

    public double getNumericGrade() {
        if (grade == null || grade.isEmpty()) return -1;
        try {
            return Double.parseDouble(grade);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}