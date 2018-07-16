package com.accenture.salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String shipType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "location")
    private List<String> shipLocations;

    public Ship(){}

    public Ship(String shipType, GamePlayer gamePlayer, List<String> shipLocations){
        this.shipType = shipType;
        this.gamePlayer = gamePlayer;
        this.shipLocations = shipLocations;
    }

    @JsonIgnore
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getLocations() {
        return shipLocations;
    }

    public void setShipLocations(List<String> locations) {
        this.shipLocations = locations;
    }

    public Map<String, Object> makeShipDTO(){
        Map<String, Object> shipList = new LinkedHashMap<>();
        shipList.put("type", this.shipType);
        shipList.put("locations", this.shipLocations);
        return shipList;
    }
}
