import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final VotingService service = new VotingService();

    public static void main(String[] args) {
        // candidates for the college election
        service.addCandidate("Arun");
        service.addCandidate("Divya");
        service.addCandidate("Karthik");

        while (true) {
            System.out.println("\n===== Online Voting System =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            int choice = readInt("Enter choice: ");
            try {
                if (choice == 1) {
                    System.out.print("Choose username: ");
                    String u = sc.nextLine().trim();
                    System.out.print("Choose password: ");
                    String p = sc.nextLine().trim();
                    service.register(u, p);
                    System.out.println("Registered successfully. Please login.");
                } else if (choice == 2) {
                    System.out.print("Username: ");
                    String u = sc.nextLine().trim();
                    System.out.print("Password: ");
                    String p = sc.nextLine().trim();
                    User user = service.login(u, p);
                    System.out.println("Welcome, " + user.getUsername() + "!");
                    userMenu(user);
                } else if (choice == 0) {
                    System.out.println("Goodbye!");
                    return;
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void userMenu(User user) {
        while (true) {
            System.out.println("\n--- Voting Menu (" + user.getUsername() + ") ---");
            System.out.println("1. View candidate list");
            System.out.println("2. Cast vote");
            System.out.println("3. View total votes");
            System.out.println("4. Show result (Finalist)");
            System.out.println("5. Show runner-up");
            System.out.println("0. Logout");
            int choice = readInt("Enter choice: ");
            try {
                switch (choice) {
                    case 1: showCandidates(); break;
                    case 2:
                        showCandidates();
                        int number = readInt("Enter candidate number: ");
                        service.castVote(user.getUsername(), number);
                        System.out.println("Your vote has been recorded. Thank you!");
                        break;
                    case 3: showTotalVotes(); break;
                    case 4: showFinalist(); break;
                    case 5: showRunnerUp(); break;
                    case 0: return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void showCandidates() {
        System.out.println("Candidates:");
        for (Candidate c : service.getCandidates()) {
            System.out.println("  " + c);
        }
    }

    private static void showTotalVotes() {
        System.out.println("Votes so far:");
        for (Candidate c : service.getCandidates()) {
            System.out.println("  " + c.getName() + " : " + service.getVotes(c.getName()));
        }
        System.out.println("Total votes cast: " + service.getTotalVotes());
    }

    private static void showFinalist() {
        if (service.getTotalVotes() == 0) {
            System.out.println("No votes cast yet.");
            return;
        }
        List<Candidate> ranking = service.getRanking();
        Candidate first = ranking.get(0);
        Candidate second = ranking.get(1);
        if (service.getVotes(first.getName()) == service.getVotes(second.getName())) {
            System.out.println("It is currently a tie between " + first.getName()
                    + " and " + second.getName() + ".");
        } else {
            System.out.println("Finalist (Winner): " + first.getName()
                    + " with " + service.getVotes(first.getName()) + " vote(s)");
        }
    }

    private static void showRunnerUp() {
        if (service.getTotalVotes() == 0) {
            System.out.println("No votes cast yet.");
            return;
        }
        Candidate second = service.getRanking().get(1);
        System.out.println("Runner-up: " + second.getName()
                + " with " + service.getVotes(second.getName()) + " vote(s)");
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }
}
