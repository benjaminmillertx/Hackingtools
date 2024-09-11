import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import java.util.stream.Stream;

public class DomainBreachAnalyzer {

    // Display usage information if the input arguments are incorrect
    private static void printUsage() {
        System.out.println("Domain Breach Analyzer by Benjamin Miller");
        System.out.println();
        System.out.println("Usage: java DomainBreachAnalyzer <domain to search> <file to output> [breach data location]");
        System.out.println("Example: java DomainBreachAnalyzer \"@gmail.com\" gmail.txt");
        System.out.println("Example: java DomainBreachAnalyzer \"@gmail.com\" gmail.txt \"/path/to/BreachCompilation/data\"");
        System.out.println("For multiple domains: java DomainBreachAnalyzer \"<domain>|<domain>\" <file to output>");
        System.out.println("Example: java DomainBreachAnalyzer \"@gmail.com|@yahoo.com\" multiple.txt");
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2 || args.length > 3) {
            printUsage();
            return;
        }

        // Parse input arguments
        String domainToSearch = args[0];
        String outputFileName = args[1];
        String breachDataLocation = "/opt/breach-parse/BreachCompilation/data";  // default location for breach data

        // If a custom breach data location is provided, validate it
        if (args.length == 3) {
            File customLocation = new File(args[2]);
            if (!customLocation.exists() || !customLocation.isDirectory()) {
                System.out.println("Invalid directory: " + args[2]);
                printUsage();
                return;
            }
            breachDataLocation = args[2];  // Use the custom location
        }

        // Validate the default or provided breach data directory
        File breachDirectory = new File(breachDataLocation);
        if (!breachDirectory.exists() || !breachDirectory.isDirectory()) {
            System.out.println("Could not find a directory at " + breachDataLocation);
            printUsage();
            return;
        }

        // Define output file paths
        File masterFile = new File(outputFileName);
        File usersFile = new File(masterFile.getName().replace(".txt", "-users.txt"));
        File passwordsFile = new File(masterFile.getName().replace(".txt", "-passwords.txt"));

        // Create output files
        masterFile.createNewFile();
        usersFile.createNewFile();
        passwordsFile.createNewFile();

        // Count total number of files in breach data folder for progress tracking
        long totalFiles = Files.walk(Paths.get(breachDataLocation)).filter(Files::isRegularFile).count();
        long[] fileCount = {0};  // to track progress

        // Function to display a progress bar
        Runnable progressBar = () -> {
            int progress = (int) ((fileCount[0] * 100) / totalFiles);
            int done = (progress * 4) / 10;
            int left = 40 - done;

            StringBuilder fill = new StringBuilder();
            for (int i = 0; i < done; i++) fill.append("#");
            StringBuilder empty = new StringBuilder();
            for (int i = 0; i < left; i++) empty.append("-");

            System.out.printf("\rProgress: [%s%s] %d%%", fill, empty, progress);
        };

        // Search for the domain across all files in the breach data folder
        try (Stream<Path> paths = Files.walk(Paths.get(breachDataLocation))) {
            try (BufferedWriter masterWriter = new BufferedWriter(new FileWriter(masterFile))) {
                paths.filter(Files::isRegularFile).forEach(filePath -> {
                    try {
                        // Read the content of each file and search for the domain
                        List<String> lines = Files.readAllLines(filePath);
                        for (String line : lines) {
                            if (Pattern.compile(domainToSearch).matcher(line).find()) {
                                masterWriter.write(line + System.lineSeparator());  // Write matches to master file
                            }
                        }
                        fileCount[0]++;
                        progressBar.run();  // Update progress bar after each file
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        // Extract usernames and passwords from the matched data
        extractFields(masterFile, usersFile, passwordsFile);
        System.out.println("\nExtraction complete.");
    }

    /**
     * This function extracts usernames and passwords from the master file.
     *
     * @param masterFile    The file that contains the domain matches (username:password pairs)
     * @param usersFile     The file where extracted usernames are written
     * @param passwordsFile The file where extracted passwords are written
     */
    private static void extractFields(File masterFile, File usersFile, File passwordsFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(masterFile));
             BufferedWriter userWriter = new BufferedWriter(new FileWriter(usersFile));
             BufferedWriter passWriter = new BufferedWriter(new FileWriter(passwordsFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");  // Assume the format is "username:password"
                if (parts.length >= 2) {
                    userWriter.write(parts[0] + System.lineSeparator());  // Extract username
                    passWriter.write(parts[1] + System.lineSeparator());  // Extract password
                }
            }
        }
    }
}
Summary of What the Tool Does:
The Domain Breach Analyzer is a Java tool that helps users search for compromised email domains in breached data files. Here’s a breakdown of its functionality:

Domain Search: It searches for the specified domain (e.g., @gmail.com) in a breached data repository, which contains files with email credentials.
File Output: The tool outputs the results to a master file that contains all the matches (email
).
Username and Password Extraction: The tool extracts the usernames (email addresses) and passwords from the master file and writes them into separate files.
Progress Bar: It displays a progress bar to show how much of the file scanning has been completed.
Usage Flexibility: Users can specify multiple domains, customize the breached data directory location, and handle large datasets efficiently.
Commentary and Usage Guide:
Input Arguments:
domainToSearch: The domain(s) you are looking for, like @gmail.com.
outputFileName: The output file where the results will be saved.
breachDataLocation (optional): The directory containing the breach data files. If not provided, a default location is used.
Output:
masterFile: Contains all lines from the breach data matching the domain(s).
usersFile: Extracts the usernames (emails) from the masterFile.
passwordsFile: Extracts the corresponding passwords from the masterFile.





