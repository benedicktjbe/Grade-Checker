import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class CourseManager {
    private ArrayList<Course> courses;

    public CourseManager(ArrayList<Course> courses) {
        this.courses = courses;
    }

    // Display subjects grouped by term
    public void showSubjectsPerTerm(Scanner scanner) {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }

        courses.sort(this::compareByYearAndTerm);

        String currentYear = "";
        String currentTerm = "";

        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);

            if (!c.year.equals(currentYear) || !c.term.equals(currentTerm)) {
                if (!currentYear.isEmpty()) {
                    System.out.println("Press enter key to see courses for the next term.");
                    scanner.nextLine();
                    System.out.println("------------------------------------------------------------------------------------------------------");
                }

                currentYear = c.year;
                currentTerm = c.term;

                System.out.println("Year = " + currentYear + " Term = " + currentTerm);
                System.out.println("Course No\tDescriptive title\tUnits");
                System.out.println("------------------------------------------------------------------------------------------------------");
            }

            System.out.println(c.code + "\t" + c.title + "\t" + c.units);
        }

        System.out.println("Press enter key to go back to the menu.");
        scanner.nextLine();
    }

    // Display subjects with grades
    public void showSubjectsWithGrades(Scanner scanner) {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }

        courses.sort(this::compareByYearAndTerm);

        String currentYear = "";
        String currentTerm = "";

        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);

            if (!c.year.equals(currentYear) || !c.term.equals(currentTerm)) {
                if (!currentYear.isEmpty()) {
                    System.out.println("Press enter key to see courses for the next term.");
                    scanner.nextLine();
                    System.out.println("------------------------------------------------------------------------------------------------");
                }

                currentYear = c.year;
                currentTerm = c.term;

                System.out.println("Year = " + currentYear + " Term = " + currentTerm);
                System.out.println("Course No.\tDescriptive title\tUnits\tGrade");
                System.out.println("------------------------------------------------------------------------------------------------");
            }

            String gradeToShow = c.grade == null || c.grade.isEmpty() ? "Not yet taken" : c.grade;
            System.out.println(c.code + "\t" + c.title + "\t" + c.units + "\t" + gradeToShow);
        }

        System.out.println("Press enter key to go back to the menu.");
        scanner.nextLine();
    }

    // Enter grade for a course
    public void enterGrade(String code, String grade) {
        for (Course c : courses) {
            if (c.code.equalsIgnoreCase(code)) {

                // Allow "Not yet taken" explicitly
                if (!grade.equalsIgnoreCase("Not yet taken")) {
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
        double totalUnits = 0;

        for (Course c : courses) {
            double g = c.getNumericGrade();
            if (g >= 0) {
                total += g * c.units;
                totalUnits += c.units;
            }
        }

        if (totalUnits == 0) {
            System.out.println("No grades available.");
        } else {
            System.out.println("GPA: " + (total / totalUnits));
        }
    }

    // Sort courses by grade descending
    public void sortByGrade() {
        courses.sort(Comparator.comparingDouble(Course::getNumericGrade).reversed());

        System.out.println("\nCourses sorted by grade:");
        for (Course c : courses) {
            String gradeToShow = c.grade == null || c.grade.isEmpty() ? "Not yet taken" : c.grade;
            System.out.println(c.code + " | " + gradeToShow);
        }
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    private int compareByYearAndTerm(Course a, Course b) {
        int yearCompare = a.year.compareToIgnoreCase(b.year);
        if (yearCompare != 0) {
            return yearCompare;
        }
        return a.term.compareToIgnoreCase(b.term);
    }
}
