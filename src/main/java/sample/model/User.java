package sample.model;

import java.time.LocalDateTime;

public class User {
    private String username;
    private String password;
    private int score;
    private LocalDateTime lastScoreChange;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
        this.lastScoreChange = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getLastScoreChange() {
        return lastScoreChange;
    }

    public int getScore() {
        return score;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastScoreChange() {
        this.lastScoreChange = LocalDateTime.now();
    }

    public void increaseScore(int amount) {
        this.score += amount;
    }
}
