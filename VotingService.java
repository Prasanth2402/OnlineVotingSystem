import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingService {
    private Map<String, User> users = new HashMap<>();          // username -> User
    private Map<String, Boolean> hasVoted = new HashMap<>();    // username -> voted or not
    private List<Candidate> candidates = new ArrayList<>();
    private Map<String, Integer> votes = new HashMap<>();       // candidate name -> vote count

    public void register(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be empty.");
        }
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already taken.");
        }
        users.put(username, new User(username, password));
        hasVoted.put(username, false);
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user == null || !user.checkPassword(password)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return user;
    }

    public void addCandidate(String name) {
        Candidate c = new Candidate(candidates.size() + 1, name);
        candidates.add(c);
        votes.put(name, 0);
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void castVote(String username, int candidateNumber) {
        if (hasVoted.get(username)) {
            throw new IllegalStateException("You have already voted. Each student can vote only once.");
        }
        if (candidateNumber < 1 || candidateNumber > candidates.size()) {
            throw new IllegalArgumentException("Invalid candidate number.");
        }
        String name = candidates.get(candidateNumber - 1).getName();
        votes.put(name, votes.get(name) + 1);
        hasVoted.put(username, true);
    }

    public boolean hasUserVoted(String username) {
        return hasVoted.get(username);
    }

    public int getVotes(String candidateName) {
        return votes.get(candidateName);
    }

    public int getTotalVotes() {
        int total = 0;
        for (int v : votes.values()) {
            total += v;
        }
        return total;
    }

    // Candidates sorted from highest votes to lowest (simple bubble sort)
    public List<Candidate> getRanking() {
        List<Candidate> ranking = new ArrayList<>(candidates);
        for (int i = 0; i < ranking.size() - 1; i++) {
            for (int j = 0; j < ranking.size() - 1 - i; j++) {
                if (getVotes(ranking.get(j).getName()) < getVotes(ranking.get(j + 1).getName())) {
                    Candidate temp = ranking.get(j);
                    ranking.set(j, ranking.get(j + 1));
                    ranking.set(j + 1, temp);
                }
            }
        }
        return ranking;
    }
}
