package com.accenture.salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Date date;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
     private Set<Score> scores;

    public Game() {}

    public Game(Date date) {
        this.date = date;
    }

    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    @JsonIgnore
    public List<Player> getPlayer() {
        return gamePlayers.stream().map(GamePlayer::getPlayer).collect(Collectors.toList());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Map<String, Object> makeGameDTO() {
        Map<String, Object> gameList = new LinkedHashMap<>();
        gameList.put("id", this.id);
        gameList.put("created", this.date);
        gameList.put("gamePlayers", this.gamePlayers.stream().map(GamePlayer::makeGamePlayerDTO).collect(Collectors.toList()));
        gameList.put("scores", this.scores.stream().map(Score::makeScoreDTO).collect(Collectors.toList()));
        return gameList;
    }

    public List<Object> getSalvosDTO(){
        return gamePlayers.stream().flatMap(s -> s.getSalvos().stream().map(Salvo::makeSalvoDTO)).collect(Collectors.toList());
    }

    public List<Object> getPlayersDTO(){
        return gamePlayers.stream().map(GamePlayer::makeGamePlayerDTO).collect(Collectors.toList());
    }

    public Map<String, Object> getHitsDTO(Player player){
        Map<String, Object> hitsList = new LinkedHashMap<>();
        Player self = null;
        Player opponent = null;
        for (Player play : getPlayer()){
            if (player.getId() == play.getId()){
                self = player;
            }else{
                opponent = play;
            }
        }
        hitsList.put("self", getGameInfoDTO(self));
        hitsList.put("opponent", getGameInfoDTO(opponent));
        return hitsList;
    }

    private List<Map<String, Object>> getGameInfoDTO(Player player){
        List<Map<String, Object>> listMap = new LinkedList<>();
        Set<Long> turnNumbers = gamePlayers.stream()
                .flatMap(gamePlayer -> gamePlayer.getSalvos().stream()
                        .map(Salvo::getTurnNumber)).collect(Collectors.toSet());
        Map<String, Integer> getHitsDamage = new LinkedHashMap<>();
        for (Long turn : turnNumbers){
            List<String> hitsLoc = hitSalvos(player, turn);
            Map<String,Object> mapHit = new LinkedHashMap<>();
            if (turn == 1){
                getHitsDamage = getDamages(player, hitsLoc, damage());
                mapHit.put("turn", turn);
                mapHit.put("hitLocations", hitsLoc);
                mapHit.put("damages", getHitsDamage);
                mapHit.put("missed", missed(player, hitsLoc, turn));
            }else{
                getHitsDamage = getNewDamages(getHitsDamage, getDamages(player, hitsLoc, damage()));
                mapHit.put("turn", turn);
                mapHit.put("hitLocations", hitsLoc);
                mapHit.put("damages", getHitsDamage);
                mapHit.put("missed", missed(player, hitsLoc, turn));
            }
            listMap.add(mapHit);
        }
        return listMap;
    }

    private Integer missed(Player player, List<String> hitsLoc, Long turn){
        Set<String> opSalvoList = new LinkedHashSet<>();
        Integer miMissed = 0;
        for (GamePlayer gamePlayer : gamePlayers){
            if (player.getId() != gamePlayer.getPlayer().getId()){
                //Obtengo, segun el turno, los salvos del jugador contrario
                opSalvoList = gamePlayer.getSalvos().stream()
                        .filter(salvo -> salvo.getTurnNumber() == turn)
                        .flatMap(salvo -> salvo.getSalvoLocations().stream()
                                .map(s -> s.toString())).collect(Collectors.toSet());
            }
        }

        if (hitsLoc.size() != opSalvoList.size()){
            miMissed = opSalvoList.size() - hitsLoc.size();
        }
        return miMissed;
    }

    private Map<String, Integer> getNewDamages(Map<String, Integer> getHitsDamages, Map<String, Integer> getDamages){
        for (ShipTypes typeShip : ShipTypes.values()){
            String type = typeShip.toString().toLowerCase();
            if (getHitsDamages.get(type) != 0){
                getDamages.put(type, (getDamages.get(type) + getHitsDamages.get(type)));
            }
        }
        return getDamages;
    }

    private Map<String, Integer> getDamages (Player player, List<String> hitsLoc, Map<String, Integer> damage){
        Set<Ship> myShips = getShips(player);
        for (Ship ship : myShips){
            String shipType = ship.getShipType();
            List<String> myShipsLocations = ship.getLocations().stream().map(String::toString).collect(Collectors.toList());
            int alwaysZero = 0;
            for (String locShip : myShipsLocations){
                for (String locSalvoes : hitsLoc){
                    if (locShip.equals(locSalvoes)){
                        alwaysZero++;
                        isShipHit(shipType, alwaysZero, damage);
                    }
                }
            }
        }
        return damage;
    }

    private Map<String, Integer> isShipHit(String shipType, int alwaysZero, Map<String, Integer> damage){
        if (alwaysZero == 1){
            damage.put(shipType + "Hits", alwaysZero);
            damage.put(shipType, alwaysZero);
        }else{
            damage.put(shipType + "Hits", alwaysZero);
            damage.put(shipType, (damage.get(shipType)+1));
        }
        return damage;
    }

    private List<String> hitSalvos(Player player, Long turn){
        Set<String> miShipsList = null;
        Set<String> opSalvoList = null;
        List<String> miHitsList = new ArrayList<>();
        for (GamePlayer gamePlayer : gamePlayers){
            if (player.getId() == gamePlayer.getPlayer().getId()){
                //Obtengo los ships del jugador
                miShipsList = gamePlayer.getShips().stream()
                        .flatMap(ship -> ship.getLocations().stream()
                                .map(s -> s.toString())).collect(Collectors.toSet());
            }else{
                //Obtengo, segun el turno, los salvos del jugador contrario
                opSalvoList = gamePlayer.getSalvos().stream()
                        .filter(salvo -> salvo.getTurnNumber() == turn)
                        .flatMap(salvo -> salvo.getSalvoLocations().stream()
                                .map(s -> s.toString())).collect(Collectors.toSet());
            }
        }
        //Averiguo si se produjo algun hit y lo guardo en una lista
        for (String ship : miShipsList){
            for (String salvo : opSalvoList){
                if (ship.equals(salvo)){
                    miHitsList.add(ship);
                }
            }
        }

        return miHitsList;
    }

    private Set<Ship> getShips(Player player){
        Set<Ship> ships = new LinkedHashSet<>();
        for (GamePlayer gamePlayer : gamePlayers){
            if (player.getId() == gamePlayer.getPlayer().getId()){
                ships = gamePlayer.getShips();
            }
        }
        return ships;
    }

    private Map<String, Integer> damage(){
        Map<String, Integer> dam = new LinkedHashMap<>();
        dam.put("carrierHits", 0);
        dam.put("battleshipHits", 0);
        dam.put("submarineHits", 0);
        dam.put("destroyerHits", 0);
        dam.put("patrolboatHits", 0);
        dam.put("carrier", 0);
        dam.put("battleship", 0);
        dam.put("submarine", 0);
        dam.put("destroyer", 0);
        dam.put("patrolboat", 0);
        return dam;
    }

    public GamePlayer getOpponent(Player player){
        GamePlayer playerGame = null;
        for (GamePlayer gamePlayer : gamePlayers){
            if (player.getId() != gamePlayer.getPlayer().getId()){
                playerGame = gamePlayer;
                break;
            }
        }
        return playerGame;
    }

    public GamePlayer getSelf(Player player){
        GamePlayer playerGame = null;
        for (GamePlayer gamePlayer : gamePlayers){
            if (player.getId() == gamePlayer.getPlayer().getId()){
                playerGame = gamePlayer;
                break;
            }
        }
        return playerGame;
    }

    public GameState getShipsSunk(Player player){
        Set<Long> turnNumber = getSelf(player).getSalvos().stream().map(Salvo::getTurnNumber).collect(Collectors.toSet());
        Set<String> miShipsLocs = getSelf(player).getShips().stream().flatMap(ship -> ship.getLocations().stream().map(String::toString)).collect(Collectors.toSet());
        Set<String> opShipsLocs = getOpponent(player).getShips().stream().flatMap(ship -> ship.getLocations().stream().map(String::toString)).collect(Collectors.toSet());
        Integer totalHitsIDo = null;
        Integer totalHitsIGet = null;
        GameState gameState = null;
        for (Long turn : turnNumber){
            if ((totalHitsIDo != null) && (totalHitsIGet != null)){
                totalHitsIGet = totalHitsIGet + hitSalvos(player, turn).size();
                totalHitsIDo = totalHitsIDo + hitSalvos(getOpponent(player).getPlayer(), turn).size();
            }else{
                totalHitsIGet = hitSalvos(player, turn).size();
                totalHitsIDo = hitSalvos(getOpponent(player).getPlayer(), turn).size();
            }
        }
        if ((getSelf(player).getSalvos().size() != 0) && (getOpponent(player).getSalvos().size() != 0)){
            if ((totalHitsIDo == totalHitsIGet) && (totalHitsIDo == opShipsLocs.size()) && (totalHitsIGet == miShipsLocs.size())){
                gameState = GameState.TIE;
                player.addScore(new Score(getSelf(player).getGame(), player, 0.5));
            } else if ((totalHitsIDo == opShipsLocs.size()) && (totalHitsIGet == miShipsLocs.size())){
                gameState = GameState.TIE;
                player.addScore(new Score(getSelf(player).getGame(), player, 0.5));
            } else if (totalHitsIDo == opShipsLocs.size()){
                gameState = GameState.WON;
                player.addScore(new Score(getSelf(player).getGame(), player, 1.0));
            } else if (totalHitsIGet == miShipsLocs.size()){
                gameState = GameState.LOST;
                player.addScore(new Score(getSelf(player).getGame(), player, 0.0));
            }else{
                gameState = GameState.PLAY;
            }
        }else{
            gameState = GameState.PLAY;
        }
        return gameState;
    }
}