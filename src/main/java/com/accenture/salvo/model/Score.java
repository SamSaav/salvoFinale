package com.accenture.salvo.model;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    private Double number;

    private Date finishDate;

    public Score(){}

    public Score(Game game, Player player, Double number){
        this.game = game;
        this.player = player;
        this.number = number;
        if(number == null){
            this.finishDate = null;
        }else {
            this.finishDate = new Date();
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Double getScore() {

        return number;
    }

    public void setScore(Double number) {
        this.number = number;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Map<String, Object> makeScoreDTO(){
        Map<String, Object> listScore = new LinkedHashMap<>();
        listScore.put("playerID", this.player.getId());
        listScore.put("score", this.number);
        listScore.put("finishDate", this.finishDate);
        return listScore;
    }
}
