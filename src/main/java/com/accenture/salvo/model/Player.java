package com.accenture.salvo.model;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    private String firstName;

    private String lastName;

    private String userName;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Score> scores;

    private String password;

    public Player() {}

    public Player(String firstName, String lastName, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }

    public Player(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public List<Game> getGame() {
        return gamePlayers.stream().map(GamePlayer::getGame).collect(Collectors.toList());
    }

    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public void addScore(Score score){
        score.setPlayer(this);
        scores.add(score);
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + userName;
    }

    public Map<String, Object> makePlayerDTO() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("id", this.id);
        p.put("email", this.userName);
        return p;
    }

    public Map<String, Object> getScoreDTO(){
        Map<String, Object> scoreList = new LinkedHashMap<>();
        scoreList.put("name", this.getUserName());
        scoreList.put("score", this.getScoreWTL());
        return scoreList;
    }

    public Map<String, Object> getScoreWTL(){
        long won = scores.stream().filter(s -> s.getScore() == 1).count();
        long tied = scores.stream().filter(s -> s.getScore() == 0.5).count();
        long lost = scores.stream().filter(s -> s.getScore() == 0).count();
        double total = scores.stream().filter(sc -> sc.getScore() != null).mapToDouble(Score::getScore).sum();

        Map<String, Object> listScore = new LinkedHashMap<>();
        listScore.put("total", total);
        listScore.put("won", won);
        listScore.put("tied", tied);
        listScore.put("lost", lost);
        return listScore;
    }
}
