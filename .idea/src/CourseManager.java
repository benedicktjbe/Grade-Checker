import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class CourseManager {
    private static final int CODE_WIDTH = 10;
    private static final int TITLE_WIDTH_NO_GRADE = 45;
    private static final int TITLE_WIDTH_WITH_GRADE = 36;

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

        for (Course c : courses) {

            if (!c.year.equals(currentYear) || !c.term.equals(currentTerm)) {
                if (!currentYear.isEmpty()) {
                    System.out.println("Press enter key to see courses for the next term.");
                    scanner.nextLine();
                    System.out.println("------------------------------------------------------------------------------------------------------");
                }

                currentYear = c.year;
                currentTerm = c.term;

                System.out.println("Year = " + currentYear + " Term = " + currentTerm);
                System.out.printf("%-" + CODE_WIDTH + "s %-" + TITLE_WIDTH_NO_GRADE + "s %6s%n",
                        "Course No", "Descriptive title", "Units");
                System.out.println("--------------------------------------------------------------------------------------");
            }

            System.out.printf("%-" + CODE_WIDTH + "s %-" + TITLE_WIDTH_NO_GRADE + "s %6.1f%n",
                    c.code, fitToWidth(c.title, TITLE_WIDTH_NO_GRADE), c.units);
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

        final String HEADER_FORMAT = "%-" + CODE_WIDTH + "s %-" + TITLE_WIDTH_WITH_GRADE + "s %6s %15s%n";
        final String ROW_FORMAT    = "%-" + CODE_WIDTH + "s %-" + TITLE_WIDTH_WITH_GRADE + "s %6.1f %15s%n";
        final String DIVIDER       = "-".repeat(87);

        String currentYear = null;
        String currentTerm = null;

        for (Course c : courses) {
            boolean newGroup = !c.year.equals(currentYear) || !c.term.equals(currentTerm);

            if (newGroup) {
                if (currentYear != null) {
                    System.out.println("Press enter key to see courses for the next term.");
                    scanner.nextLine();
                    System.out.println(DIVIDER);
                }

                currentYear = c.year;
                currentTerm = c.term;

                System.out.printf("Year = %s  Term = %s%n", currentYear, currentTerm);
                System.out.printf(HEADER_FORMAT, "Course No.", "Descriptive title", "Units", "Grade");
                System.out.println(DIVIDER);
            }

            String grade = (c.grade == null || c.grade.isEmpty()) ? "Not yet taken" : c.grade;
            System.out.printf(ROW_FORMAT, c.code, fitToWidth(c.title, TITLE_WIDTH_WITH_GRADE), c.units, fitToWidth(grade, 15));
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
        double weightedSum = 0;
        double totalUnits  = 0;

        for (Course c : courses) {
            double grade = c.getNumericGrade();
            if (grade >= 0) {
                weightedSum += grade * c.units;
                totalUnits  += c.units;
            }
        }

        if (totalUnits == 0) {
            System.out.println("No grades available.");
        } else {
            System.out.printf("GPA: %.2f%n", weightedSum / totalUnits);
        }
    }

    // Sort courses by grade descending
    public void sortByGrade() {
        courses.sort(Comparator.comparingDouble(Course::getNumericGrade).reversed());

        System.out.println("\nCourses sorted by grade:");
        courses.forEach(c -> {
            String grade = (c.grade == null || c.grade.isEmpty()) ? "Not yet taken" : c.grade;
            System.out.printf("%-" + CODE_WIDTH + "s | %s%n", c.code, grade);
        });
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

    private String fitToWidth(String value, int width) {
        if (value == null) {
            return "";
        }
        if (value.length() <= width) {
            return value;
        }
        if (width <= 3) {
            return value.substring(0, width);
        }
        return value.substring(0, width - 3) + "...";
    }
}
