package com.accenture.salvo.model;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    private long turnNumber;

    @ElementCollection
    @Column(name = "location")
    private List<String> salvoLocations;

    public Salvo(){}

    public Salvo(long turnNumber, GamePlayer gamePlayer, List<String> salvoLocations){
        this.turnNumber = turnNumber;
        this.gamePlayer = gamePlayer;
        this.salvoLocations = salvoLocations;
    }

    public long getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(long turnNumber) {
        this.turnNumber = turnNumber;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    public Map<String, Object> makeSalvoDTO(){
        Map<String, Object> salvo = new LinkedHashMap<>();
        salvo.put("turn", this.turnNumber);
        salvo.put("player", this.gamePlayer.getPlayer().getId());
        salvo.put("locations", this.salvoLocations);
        return salvo;
    }
}
