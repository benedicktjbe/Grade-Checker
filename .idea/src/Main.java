import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String DATA_FILE = "courses.txt";

    public static void main(String[] args) {
        ArrayList<Course> courses = loadCoursesFromFile();
        CourseManager manager = new CourseManager(courses);
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            System.out.println("My Checklist Monitoring Application");
            System.out.println("<1> Show subjects for each school term");
            System.out.println("<2> Show subjects with grades for each term");
            System.out.println("<3> Enter grades for subjects recently finished");
            System.out.println("<4> Edit a course");
            System.out.println("<5> Calculate GPA");
            System.out.println("<6> Show courses sorted by grade (descending)");
            System.out.println("<7> Quit");

            int choice = readMenuChoice(scanner, 1, 7);

            switch (choice) {
                case 1:
                    manager.showSubjectsPerTerm(scanner);
                    break;
                case 2:
                    manager.showSubjectsWithGrades(scanner);
                    break;
                case 3:
                    System.out.print("Enter course code: ");
                    String code = scanner.nextLine().trim();
                    System.out.print("Enter grade (0-100, or 'Not yet taken'): ");
                    String grade = scanner.nextLine().trim();
                    manager.enterGrade(code, grade);
                    saveCoursesToFile(manager.getCourses());
                    break;
                case 4:
                    System.out.print("Enter course code to edit: ");
                    String editCode = scanner.nextLine().trim();
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine().trim();
                    System.out.print("Enter new units: ");
                    double newUnits = readDouble(scanner);
                    manager.editCourse(editCode, newTitle, newUnits);
                    saveCoursesToFile(manager.getCourses());
                    break;
                case 5:
                    manager.calculateGPA();
                    break;
                case 6:
                    manager.sortByGrade();
                    break;
                case 7:
                    running = false;
                    System.out.println("..Thank you.");
                    break;
                default:
                    // should never happen because of validation
                    break;
            }
        }

        saveCoursesToFile(manager.getCourses());
        scanner.close();
    }

    private static int readMenuChoice(Scanner scanner, int min, int max) {
        while (true) {
            System.out.print("Enter a number corresponding to your choice: ");
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("The number must be from " + min + " to " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("You entered an invalid integer. Please enter integer:");
            }
        }
    }

    private static double readDouble(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid numeric value:");
            }
        }
    }

    private static ArrayList<Course> loadCoursesFromFile() {
        ArrayList<Course> courses = new ArrayList<>();
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            // No data file yet; start with empty list
            return courses;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length < 6) {
                    continue;
                }
                String code = parts[0];
                String title = parts[1];
                double units;
                try {
                    units = Double.parseDouble(parts[2]);
                } catch (NumberFormatException e) {
                    units = 0.0;
                }
                String year = parts[3];
                String term = parts[4];
                String grade = parts[5];
                courses.add(new Course(code, title, units, year, term, grade));
            }
        } catch (IOException e) {
            System.out.println("Error reading data file: " + e.getMessage());
        }

        return courses;
    }

    private static void saveCoursesToFile(ArrayList<Course> courses) {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            for (Course c : courses) {
                writer.write(c.toFileString());
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error saving data file: " + e.getMessage());
        }
    }
}
