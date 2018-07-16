package com.accenture.salvo.model;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salvo> salvos;

    private GameState gameState;

    public GamePlayer() {}

    public GamePlayer(Player player, Game game){
        date = new Date();
        this.player = player;
        this.game = game;
        this.gameState = GameState.PLACESHIPS;
    }

    public void addShips(Set<Ship> ships){
        ships.stream().forEach(ship -> {
            ship.setGamePlayer(this);
            ship.getShipType();
            ship.getLocations();
        });
        this.ships.addAll(ships);
    }

    public void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        salvos.add(salvo);
    }

    public Set<Salvo> getSalvos() {
        return salvos;
    }

    public void setSalvos(Set<Salvo> salvos) {
        this.salvos = salvos;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Long getId() {
        return id;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public String toString() {
        return date + " - " + player + " - " + game;
    }

    public Map<String, Object> makeGamePlayerDTO() {
        Map<String, Object> gamePlayerList = new LinkedHashMap<>();
        gamePlayerList.put("id", this.id);
        gamePlayerList.put("player", this.player.makePlayerDTO());
        gamePlayerList.put("joinDate", this.date);
        return gamePlayerList;
    }

    public Map<String, Object> getGamePlayerDTO() {
        Map<String, Object> gamePlayerList = new LinkedHashMap<>();
        gamePlayerList.put("id", this.getGame().getId());
        gamePlayerList.put("created", this.getGame().getDate());
        gamePlayerList.put("gameState", this.gameState);
        gamePlayerList.put("gamePlayers", this.game.getPlayersDTO());
        gamePlayerList.put("joinDate", this.date);
        gamePlayerList.put("ships", this.ships.stream().map(Ship::makeShipDTO).collect(Collectors.toList()));
        gamePlayerList.put("salvoes", this.game.getSalvosDTO());
        gamePlayerList.put("hits", this.game.getHitsDTO(getPlayer()));
        return gamePlayerList;
    }

}
