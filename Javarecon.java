//based off nahamsec lazyscript
import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class JavaRecon {

    private static final String CHROME_PATH = "/snap/bin/chromium";
    private static final int DIRECTORY_SEARCH_THREADS = 50;
    private static final String WAYBACK_DATA_DIR = "wayback-data";
    private static final String REPORTS_DIR = "reports";
    private static final String DIRSEARCH_WORDLIST = "~/tools/dirsearch/db/dicc.txt";
    private static final String MASSDNS_WORDLIST = "~/tools/SecLists/Discovery/DNS/clean-jhaddix-dns.txt";

    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args[0] == null || args[0].isEmpty()) {
            System.out.println("Usage: java JavaRecon.java [-d domain.com] [-e excluded.domain.com,other.domain.com]");
            return;
        }

        boolean excludeSpecified = false;
        List<String> excludedSubdomains = new ArrayList<>();

        String domain = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-d")) {
                if (i + 1 < args.length) {
                    domain = args[++i];
                } else {
                    System.out.println("Error: Please provide a domain with -d option.");
                    return;
                }
            } else if (args[i].equals("-e")) {
                if (i + 1 < args.length) {
                    String[] excluded = args[++i].split(",");
                    excludedSubdomains.addAll(Arrays.asList(excluded));
                    excludeSpecified = true;
                } else {
                    System.out.println("Error: Please provide excluded subdomains with -e option.");
                    return;
                }
            }
        }

        if (domain == null) {
            System.out.println("Error: Please provide a domain with -d option.");
            return;
        }

        JavaRecon recon = new JavaRecon();
        recon.runRecon(domain, excludedSubdomains);
    }

    private void runRecon(String domain, List<String> excludedSubdomains) throws IOException {
        System.out.println("Probing for live hosts...");
        List<String> liveSubdomains = getLiveSubdomains(domain);
        System.out.println("Total of " + liveSubdomains.size() + " live subdomains were found");

        System.out.println("Listing subdomains using sublister...");
        List<String> subdomains = getSubdomainsWithSublister(domain);

        System.out.println("Checking certspotter...");
        List<String> certspotterSubdomains = getCertspotterSubdomains(domain);
        subdomains.addAll(certspotterSubdomains);

        System.out.println("Excluding domains...");
        List<String> finalSubdomains = excludeDomains(subdomains, excludedSubdomains);

        System.out.println("Starting discovery...");
        for (String subdomain : finalSubdomains) {
            discover(subdomain);
        }
    }

    private List<String> getLiveSubdomains(String domain) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("httprobe", "-t", "100", "-s", "0.5", "-c", "50", "-p", "80,443", "-a", "NULL", "-k", "-w", "10", "-f", "host" +
                "s", domain);
        pb.redirectErrorStream(true);

        Process p = pb.start();
        p.waitFor();

        List<String> liveSubdomains = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                liveSubdomains.add(line);
            }
        }
        return liveSubdomains;
    }

    private List<String> getSubdomainsWithSublister(String domain) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("sublister", "-d", domain);
        pb.redirectErrorStream(true);

        Process p = pb.start();
        p.waitFor();

        List<String> subdomains = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) !while ((line = reader.readLine()) != null) {
                subdomains.add(line);
            }
        }
        return subdomains;
    }

    private List<String> getCertspotterSubdomains(String domain) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("cs-active-discovery", "certspotter.com", "dns-01", "-d", domain, "--rate", "100", "--output-file", "certspotter.txt");
        pb.redirectErrorStream(true);

        Process p = pb.start();
        p.waitFor();

        List<String> certspotterSubdomains = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("certspotter.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                certspotterSubdomains.add(line);
            }
        }

        File file = new File("certspotter.txt");
        file.delete();

        return certspotterSubdomains;
    }

    private List<String> excludeDomains(List<String> subdomains, List<String> excludedSubdomains) {
        if (!excludedSubdomains.isEmpty()) {
            return subdomains.stream()
                    .filter(subdomain -> !excludedSubdomains.contains(subdomain))
                    .collect(Collectors.toList());
        }
        return subdomains;
    }

    private void discover(String subdomain) throws IOException {
        // Implement the discovery process based on the given subdomain
    }
}
This is the complete JavaRecon.java file. To run the code, compile it with the following command:
javac JavaRecon.java
Then, execute it with the following command:
java JavaRecon -d example.com -e sub1.com,sub2.com
Remember to replace the paths for httprobe, sublister, and the wordlists with the correct ones for your system. The discover method is left empty for you to implement the discovery process based on the given subdomain.       
