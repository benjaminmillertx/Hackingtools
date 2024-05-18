import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ChatBot {

    private List<String> names;
    private List<String> conversationStarters;
    private List<String> basicPhrases;
    private List<String> predatorPhrases;
    private List<String> underagePhrases;
    private String name;

    public ChatBot() {
        names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy");
        Random random = new Random();
        int randomIndex = random.nextInt(names.size());
        name = names.get(randomIndex);
    }

    public void startConversation() {
        conversationStarters = Arrays.asList(
                name + ", how are you today?",
                "Hello, " + name + ". What's new?",
                "Hey there, " + name + ". How's it going?",
                "Good morning, " + name + ". How can I help you?"
        );

        Random random = new Random();
        int randomIndex = random.nextInt(conversationStarters.size());
        System.out.println(conversationStarters.get(randomIndex));
    }

    private List<String> basicPhrases = Arrays.asList(
            "That's great!",
            "I see.",
            "I understand.",
            "Interesting.",
            "Can you tell me more about that?",
            "Sure thing!",
            "No problem!",
            "I'm glad I could help!",
            "You're welcome!",
            "My pleasure!"
    );

    public void respondWithBasicPhrase() {
        Random random = new Random();
        int randomIndex = random.nextInt(basicPhrases.size());
        System.out.println(basicPhrases.get(randomIndex));
    }

    private List<String> predatorPhrases = Arrays.asList(
            "Are you alone?",
            "How old are you?",
            "What are you wearing?",
            "Do you want to meet up?"
    );

    private List<String> underagePhrases = Arrays.asList(
            "I'm sorry, but I can't continue this conversation. It's important to keep online conversations safe and appropriate.",
            "I'm reporting this conversation to the authorities. Please stop trying to solicit minors online.",
            "This conversation is inappropriate and illegal. Please stop immediately.",
            "I'm here to provide helpful information and support, not to engage in illegal activities. Please stop trying to solicit minors online.",
            "I'm obligated to report this conversation to the authorities. Soliciting minors online is a serious crime."
    );

    public void detectPotentialPredator(String message) {
        if (predatorPhrases.contains(message)) {
            if (message.contains("underage") || message.contains("minor") || message.contains("young")) {
                Random random = new Random();
                int randomIndex = random.nextInt(underagePhrases.size());
                System.out.println(underagePhrases.get(randomIndex));
            } else {
                System.out.println("Potential online predator detected.");
            }
        } else {
            respondWithBasicPhrase();
        }
    }

    public static void main(String[] args) {
        ChatBot chatBot = new ChatBot();
        Scanner scanner = new Scanner(System.in);

        // Start the conversation
        chatBot.startConversation();

        // Listen for user input
        while (true) {
            String input = scanner.nextLine();
            chatBot.detectPotentialPredator(input);
        }
    }
}
