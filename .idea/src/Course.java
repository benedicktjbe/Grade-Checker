public class Course {

    String code;
    String title;
    double units;
    String year;
    String term;
    String grade;

    public Course(String code, String title, double units, String year, String term, String grade) {
        this.code = code;
        this.title = title;
        this.units = units;
        this.year = year;
        this.term = term;
        this.grade = grade;
    }

    public String toFileString() {
        return code + "," + title + "," + units + "," + year + "," + term + "," + grade;
    }

    public double getNumericGrade() {
        try {
            return Double.parseDouble(grade);
        } catch (Exception e) {
            return -1;
        }
    }
}
