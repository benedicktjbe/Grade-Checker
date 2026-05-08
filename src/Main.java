import java.io.*;
import java.nio.file.*;
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
            System.out.println("\nMy Checklist Monitoring Application");
            System.out.println("<1> Show subjects for each school term");
            System.out.println("<2> Show subjects with grades for each term");
            System.out.println("<3> Enter grades for subjects recently finished");
            System.out.println("<4> Edit a course");
            System.out.println("<5> Calculate GPA");
            System.out.println("<6> Show courses sorted by grade (descending)");
            System.out.println("<7> Quit");

            switch (readMenuChoice(scanner, 1, 7)) {
                case 1 -> manager.showSubjectsPerTerm(scanner);
                case 2 -> manager.showSubjectsWithGrades(scanner);
                case 3 -> {
                    System.out.print("Enter course code: ");
                    String code = scanner.nextLine().trim();
                    System.out.print("Enter grade (0-100, or 'Not yet taken'): ");
                    manager.enterGrade(code, scanner.nextLine().trim());
                    saveCoursesToFile(manager.getCourses());
                }
                case 4 -> {
                    System.out.print("Enter course code to edit: ");
                    String editCode = scanner.nextLine().trim();
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine().trim();
                    System.out.print("Enter new units: ");
                    manager.editCourse(editCode, newTitle, readDouble(scanner));
                    saveCoursesToFile(manager.getCourses());
                }
                case 5 -> manager.calculateGPA();
                case 6 -> manager.sortByGrade();
                case 7 -> {
                    running = false;
                    System.out.println("..Thank you.");
                }
            }
        }

        saveCoursesToFile(manager.getCourses());
        scanner.close();
    }

    private static int readMenuChoice(Scanner scanner, int min, int max) {
        while (true) {
            System.out.printf("Enter your choice (%d-%d): ", min, max);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) return value;
                System.out.printf("Please enter a number from %d to %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double readDouble(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid numeric value:");
            }
        }
    }

    private static ArrayList<Course> loadCoursesFromFile() {
        ArrayList<Course> courses = new ArrayList<>();
        Path path = Path.of(DATA_FILE);

        if (!Files.exists(path)) return courses;

        try (Scanner fileScanner = new Scanner(path)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;

                double units;
                try {
                    units = Double.parseDouble(parts[2]);
                } catch (NumberFormatException e) {
                    units = 0.0;
                }

                courses.add(new Course(parts[0], parts[1], units, parts[3], parts[4], parts[5]));
            }
        } catch (IOException e) {
            System.out.println("Error reading data file: " + e.getMessage());
        }

        return courses;
    }

    private static void saveCoursesToFile(ArrayList<Course> courses) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(DATA_FILE))) {
            for (Course c : courses) {
                writer.write(c.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data file: " + e.getMessage());
        }
    }
}