import java.util.ArrayList;
import java.util.*;
public class CourseManager {
    private ArrayList<Course> courses;

    public CourseManager(ArrayList<Course> courses) {
        this.courses = courses;
    }

    // Display subjects grouped by term
    public void showSubjectsPerTerm() {
        for (Course c : courses) {
            System.out.println("\n" + c.year + " - " + c.term);
            System.out.println(c.code + " | " + c.title + " | " + c.units + " units");
        }
    }

    // Display subjects with grades
    public void showSubjectsWithGrades() {
        for (Course c : courses) {
            System.out.println("\n" + c.year + " - " + c.term);
            System.out.println(c.code + " | " + c.title + " | Grade: " + c.grade);
        }
    }

    // Enter grade for a course
    public void enterGrade(String code, String grade) {
        for (Course c : courses) {
            if (c.code.equalsIgnoreCase(code)) {

                // Validate numeric grade
                try {
                    double g = Double.parseDouble(grade);
                    if (g < 0 || g > 100) {
                        System.out.println("Invalid grade range.");
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid grade input.");
                    return;
                }

                c.grade = grade;
                System.out.println("Grade updated.");
                return;
            }
        }
        System.out.println("Course not found.");
    }

    // Edit course details
    public void editCourse(String code, String newTitle, double newUnits) {
        for (Course c : courses) {
            if (c.code.equalsIgnoreCase(code)) {
                c.title = newTitle;
                c.units = newUnits;
                System.out.println("Course updated.");
                return;
            }
        }
        System.out.println("Course not found.");
    }

    // ================= EXTRA FEATURES =================

    // Calculate GPA (average grade)
    public void calculateGPA() {
        double total = 0;
        int count = 0;

        for (Course c : courses) {
            double g = c.getNumericGrade();
            if (g >= 0) {
                total += g;
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No grades available.");
        } else {
            System.out.println("GPA: " + (total / count));
        }
    }

    // Sort courses by grade descending
    public void sortByGrade() {
        courses.sort((a, b) -> Double.compare(b.getNumericGrade(), a.getNumericGrade()));

        System.out.println("\nCourses sorted by grade:");
        for (Course c : courses) {
            System.out.println(c.code + " | " + c.grade);
        }
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }
}
