import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;

public class PasswordCracker {

    private List<String> passwordList;
    private String targetHash;

    // Constructor, getters, and setters

    public void crackPassword() {
        // Step 1: Load password list
        // Step 2: Check password candidates against target hash
    }

    public static void main(String[] args) {
        // Step 3: Main method
    }

    public void selectPasswordList() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the path to the password list file:");
        String fileName = scanner.nextLine();
        try {
            loadPasswordList(fileName);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
Next, I'll modify the crackPassword method to loop through multiple password lists if necessary:
public void crackPassword() throws NoSuchAlgorithmException {
    boolean passwordFound = false;
    Scanner scanner = new Scanner(System.in);

    while (!passwordFound) {
        if (!passwordList.isEmpty()) {
            checkPasswordCandidates();
            passwordFound = true;
        } else {
            System.out.println("Password not found in the list. Do you want to try another list? (yes/no)");
            String answer = scanner.nextLine();
            if ("yes".equalsIgnoreCase(answer)) {
                selectPasswordList();
            } else {
                passwordFound = true;
            }
        }
    }
}
Finally, update the main method to call the selectPasswordList method initially:
public static void main(String[] args) {
    PasswordCracker passwordCracker = new PasswordCracker();
    passwordCracker.selectPasswordList();
    passwordCracker.setTargetHash("your-target-hash-here");
    try {
        passwordCracker.crackPassword();
    } catch (NoSuchAlgorithmException e) {
        System.err.println("Error: " + e.getMessage());
    }
}
