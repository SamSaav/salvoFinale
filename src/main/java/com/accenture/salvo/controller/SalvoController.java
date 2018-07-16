package com.accenture.salvo.controller;

import com.accenture.salvo.model.*;
import com.accenture.salvo.repository.GamePlayerRepository;
import com.accenture.salvo.repository.GameRepository;
import com.accenture.salvo.repository.PlayerRepository;
import com.accenture.salvo.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShipRepository shipRepository;

    // GAMES VIEW

    @RequestMapping(path = "/games", method = RequestMethod.GET)
    public Object getGames(){
        Map<String, Object> gamesList = new LinkedHashMap<>();
        List<Game> games = gameRepository.findAll();
        gamesList.put("player", getTheUserName());
        gamesList.put("games", games.stream().map(Game::makeGameDTO).collect(Collectors.toList()));
        return gamesList;
    }

    // GAME VIEW

    @RequestMapping( value = "/game_view/{gamePlayerID}", method = RequestMethod.GET)
    public Object getGameById(@PathVariable("gamePlayerID") Long gamePlayerId){
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        if(isAuthenticated() != gamePlayer.getPlayer().getUserName()){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }else if (isGamePlaying(gamePlayer)){
            return getNewScores(gamePlayer);
        }else{
            return isAlreadyPlaying(gamePlayer);
        }
    }

    //VALIDACION PARA LOS USUARIOS

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> createPlayer(@RequestParam("email") String userName, @RequestParam("password") String password) {
        if (userName.isEmpty()) {
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.FORBIDDEN), HttpStatus.FORBIDDEN);
        }

        Player player = playerRepository.findByUserName(userName);
        if (player != null) {
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.CONFLICT), HttpStatus.CONFLICT);
        }

        playerRepository.save(new Player(userName, password));
        return new ResponseEntity<>(getMapVariables(Consts.CREATE, Consts.SUCCESS), HttpStatus.CREATED);
    }

    //LEADER BOARD

    @RequestMapping("/leaderBoard")
    public List<Object> getLeader(){
        List<Player> players = playerRepository.findAll();
        return players.stream().map(Player::getScoreDTO).collect(Collectors.toList());
    }

    //CREATE NEW GAME

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createNewGame(){
        if (isAuthenticated() == null){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }else{
            Player player = playerRepository.findByUserName(isAuthenticated());
            Game game = gameRepository.save(new Game(new Date()));
            gamePlayerRepository.save(new GamePlayer(player, game));
            return new ResponseEntity<>(getMapVariables(Consts.ID, gamePlayerRepository.count()), HttpStatus.CREATED);
        }
    }

    //JOIN GAME

    @RequestMapping(path = "/game/{gamePlayerID}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame(@PathVariable("gamePlayerID") Long gameId){
        Game game = gameRepository.findOne(gameId);
        if (isAuthenticated() == null){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }else if (game.getId() == null){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.FORBIDDEN), HttpStatus.FORBIDDEN);
        }else if (game.getPlayer().stream().count() == 2){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.FORBIDDEN), HttpStatus.FORBIDDEN);
        }else{
            Player player = playerRepository.findByUserName(isAuthenticated());
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(player, game));
            return new ResponseEntity<>(getMapVariables(Consts.ID, gamePlayer.getId()), HttpStatus.CREATED);
        }
    }

    //SHIPS

    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.GET)
    public ResponseEntity<Object> getPlacedShips(@PathVariable("gamePlayerId") Long gamePlayerId){
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        if(gamePlayer == null){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }else if (isAuthenticated() == gamePlayer.getPlayer().getUserName()){
            return new ResponseEntity<>(getMapVariables(Consts.SHIP, gamePlayer.getShips()), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.FORBIDDEN), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Object> getShips(@PathVariable("gamePlayerId") Long gamePlayerId, @RequestBody Set<Ship> ships){
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        Player player = playerRepository.findByUserName(isAuthenticated());
        if (isAuthenticated() == null){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer == null){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        if (player.getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        if (isShipsEmpty(gamePlayer)){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }else{
            gamePlayer.addShips(ships);
            gamePlayer.setGameState(GameState.WAITINGFOROPP);
            gamePlayerRepository.save(gamePlayer);
            return new ResponseEntity<>(getMapVariables(Consts.CREATE, Consts.SUCCESS), HttpStatus.CREATED);
        }
    }

    //SALVOS

    @RequestMapping(path = "/games/players/{gamePlayerID}/salvoes", method = RequestMethod.GET)
    public ResponseEntity<Object> getStoreSalvoes(@PathVariable("gamePlayerID") Long gamePlayerId){
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        if(gamePlayer == null){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }else if (isAuthenticated() == gamePlayer.getPlayer().getUserName()){
            return new ResponseEntity<>(getMapVariables(Consts.SALVO, gamePlayer.getSalvos()), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.FORBIDDEN), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/games/players/{gamePlayerID}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Object> setSalvoes(@PathVariable("gamePlayerID") Long gamePlayerId, @RequestBody Salvo salvo){
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        Player player = playerRepository.findByUserName(isAuthenticated());
        if(isAuthenticated() == null){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }else if (gamePlayer == null){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }else if (player.getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }else if (salvo.getSalvoLocations().size() > 5){
            return new ResponseEntity<>(getMapVariables(Consts.ERROR, Consts.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }else{
            salvo.setTurnNumber((gamePlayer.getSalvos().stream().mapToLong(Salvo::getTurnNumber).count()) + 1);
            gamePlayer.addSalvo(salvo);
            gamePlayer.setGameState(GameState.WAIT);
            gamePlayerRepository.save(gamePlayer);
            return new ResponseEntity<>(getMapVariables(Consts.CREATE, Consts.SUCCESS), HttpStatus.CREATED);
        }
    }

    // FUNCIONES PARA OBTENER Y/O VALIDAR INFORMACION

    private Object isAlreadyPlaying(GamePlayer gamePlayer){
        if (gamePlayer.getGame().getPlayer().size() == 2){
            if (gamePlayer.getSalvos().size() > gamePlayer.getGame().getOpponent(gamePlayer.getPlayer()).getSalvos().size()){
                gamePlayer.setGameState(GameState.WAIT);
            }else if (gamePlayer.getSalvos().size() < gamePlayer.getGame().getOpponent(gamePlayer.getPlayer()).getSalvos().size()){
                gamePlayer.setGameState(GameState.PLAY);
            }
        }else if (gamePlayer.getShips().size() != 0){
            gamePlayer.setGameState(GameState.WAITINGFOROPP);
        }
        return gamePlayer.getGamePlayerDTO();
    }

    private Boolean isGamePlaying(GamePlayer gamePlayer){
        if (gamePlayer.getGame().getPlayer().size() != 2){
            return false;
        }else if (gamePlayer.getShips().size() == 0){
            return false;
        }else if (gamePlayer.getGame().getOpponent(gamePlayer.getPlayer()).getShips().size() == 0){
            return false;
        }else if (gamePlayer.getSalvos().size() != gamePlayer.getGame().getOpponent(gamePlayer.getPlayer()).getSalvos().size()){
            return false;
        }else{
            return gamePlayer.getSalvos().size() == gamePlayer.getGame().getOpponent(gamePlayer.getPlayer()).getSalvos().size();
        }
    }

    private Object getNewScores(GamePlayer gamePlayer){
        gamePlayer.setGameState(gamePlayer.getGame().getShipsSunk(gamePlayer.getPlayer()));
        if (gamePlayer.getGame().getScores().size() == 2){
            return gamePlayer.getGamePlayerDTO();
        }else{
            gamePlayerRepository.save(gamePlayer);
            return gamePlayer.getGamePlayerDTO();
        }
    }

    public Object getTheUserName(){
        if (isAuthenticated() == null){
            return "Guest";
        }else{
            Player player = playerRepository.findByUserName(isAuthenticated());
            Map<String, Object> idUserName = new LinkedHashMap<>();
            idUserName.put("id", player.getId());
            idUserName.put("name", isAuthenticated());
            return idUserName;
        }
    }

    private Object getMapVariables(String clave, Object respuesta){
        Map<String, Object> makeMap = new LinkedHashMap<>();
        makeMap.put(clave, respuesta);
        return makeMap;
    }

    private String isAuthenticated(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return null;
        }else{
            return authentication.getName();
        }
    }

    private Boolean isShipsEmpty(GamePlayer gamePlayer){
        if(!(gamePlayer.getShips().isEmpty())){
            return true;
        }else{
            return (gamePlayer.getShips().size() >= 5);
        }
    }
}
