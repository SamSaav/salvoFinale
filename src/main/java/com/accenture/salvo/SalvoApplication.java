package com.accenture.salvo;

import com.accenture.salvo.model.*;
import com.accenture.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	Date date = new Date();

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
									  GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository,
									  ShipRepository shipRepository,
									  SalvoRepository salvoRepository,
									  ScoreRepository scoreRepository){
		return args -> {
			long turn1 = 1;
			long turn2 = 2;
			long turn3 = 3;

			Player jBauer = playerRepository.save(new Player("Jack", "Bauer", "j.bauer@ctu.gov", "24"));
			Player cObrian = playerRepository.save(new Player("Chloe", "O'Brian", "c.obrian@ctu.gov", "42"));
			Player kimBauer = playerRepository.save(new Player("Kim", "Bauer", "kim_bauer@gmail.com", "kb"));
			Player tAlmeida = playerRepository.save(new Player("Tony", "Almeida", "t.almeida@ctu.gov", "mole"));


			Game game1 = gameRepository.save(new Game(date));
			Game game2 = gameRepository.save(new Game(Date.from(date.toInstant().plusSeconds(3600))));
			Game game3 = gameRepository.save(new Game(Date.from(date.toInstant().plusSeconds(7200))));
			Game game4 = gameRepository.save(new Game(date));
			Game game5 = gameRepository.save(new Game(date));
			Game game6 = gameRepository.save(new Game(date));
			Game game7 = gameRepository.save(new Game(date));
			Game game8 = gameRepository.save(new Game(date));


			GamePlayer gamePlayer1 = gamePlayerRepository.save(new GamePlayer(jBauer, game1));
			//LOCATIONsSHIPS
			List<String> shipLocation1 = new ArrayList<>(Arrays.asList("H2", "H3", "H4"));
			List<String> shipLocation2 = new ArrayList<>(Arrays.asList("E1", "F1", "G1"));
			List<String> shipLocation3 = new ArrayList<>(Arrays.asList("B4", "B5"));
			//SHIPS
			shipRepository.save(new Ship(ShipTypes.DESTROYER.toString().toLowerCase(), gamePlayer1, shipLocation1));
			shipRepository.save(new Ship(ShipTypes.SUBMARINE.toString().toLowerCase(), gamePlayer1, shipLocation2));
			shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer1, shipLocation3));
			//SALVOLOCATIONS
			List<String> salLocation1 = new ArrayList<>(Arrays.asList("B5", "C5", "F1"));
			List<String> salLocation2 = new ArrayList<>(Arrays.asList("F2", "D5"));
			//SALVOS
			salvoRepository.save(new Salvo(turn1, gamePlayer1, salLocation1));
			salvoRepository.save(new Salvo(turn2, gamePlayer1, salLocation2));


			//==========================================================================================================


			GamePlayer gamePlayer2 = gamePlayerRepository.save(new GamePlayer(cObrian, game1));
			//LOCATIONsSHIPS
			List<String> shipLocation4 = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
			List<String> shipLocation5 = new ArrayList<>(Arrays.asList("F1", "F2"));
			//SHIPS
			shipRepository.save(new Ship(ShipTypes.DESTROYER.toString().toLowerCase(), gamePlayer2, shipLocation4));
			shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer2, shipLocation5));
			//SALVOLOCATIONS
			List<String> salLocation3 = new ArrayList<>(Arrays.asList("B4", "B5", "B6"));
			List<String> salLocation4 = new ArrayList<>(Arrays.asList("E1", "H3", "A2"));
			//SALVOS
			salvoRepository.save(new Salvo(turn1, gamePlayer2, salLocation3));
			salvoRepository.save(new Salvo(turn2, gamePlayer2, salLocation4));


			//==========================================================================================================


			GamePlayer gamePlayer3 = gamePlayerRepository.save(new GamePlayer(jBauer, game2));
			//LOCATIONsSHIPS
			List<String> shipLocation6 = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
			List<String> shipLocation7 = new ArrayList<>(Arrays.asList("C6", "C7"));
			//SHIPS
			Ship ship5 = shipRepository.save(new Ship(ShipTypes.DESTROYER.toString().toLowerCase(), gamePlayer3, shipLocation6));
			Ship ship6 = shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer3, shipLocation7));
			//SALVOLOCATIOS
			List<String> salLocation5 = new ArrayList<>(Arrays.asList("A2", "A4", "G6"));
			List<String> salLocation6 = new ArrayList<>(Arrays.asList("A3", "H6"));
			//SALVOS
			salvoRepository.save(new Salvo(turn1, gamePlayer3, salLocation5));
			salvoRepository.save(new Salvo(turn2, gamePlayer3, salLocation6));


			//==========================================================================================================


			GamePlayer gamePlayer4 = gamePlayerRepository.save(new GamePlayer(cObrian, game2));
			//LOCATIONsSHIPS
			List<String> shipLocation8 = new ArrayList<>(Arrays.asList("A2", "A3", "A4"));
			List<String> shipLocation9 = new ArrayList<>(Arrays.asList("G6", "H6"));
			//SHIPS
			Ship ship7 = shipRepository.save(new Ship(ShipTypes.SUBMARINE.toString().toLowerCase(), gamePlayer4, shipLocation8));
			Ship ship8 = shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer4, shipLocation9));
			//SALVOLOCATIOS
			List<String> salLocation7 = new ArrayList<>(Arrays.asList("B5", "D5", "C7"));
			List<String> salLocation8 = new ArrayList<>(Arrays.asList("C5", "C6"));
			//SALVOS
			salvoRepository.save(new Salvo(turn1, gamePlayer4, salLocation7));
			salvoRepository.save(new Salvo(turn2, gamePlayer4, salLocation8));


			//==========================================================================================================


			GamePlayer gamePlayer5 = gamePlayerRepository.save(new GamePlayer(cObrian, game3));
			//LOCATIONsSHIPS
			List<String> shipLocation10 = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
			List<String> shipLocation11 = new ArrayList<>(Arrays.asList("C6", "C7"));
			//SHIPS
			Ship ship9 = shipRepository.save(new Ship(ShipTypes.DESTROYER.toString().toLowerCase(), gamePlayer5, shipLocation10));
			Ship ship10 = shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer5, shipLocation11));
			//SALVOLOCATIOS
			List<String> salLocation9 = new ArrayList<>(Arrays.asList("G6", "H6", "A4"));
			List<String> salLocation10 = new ArrayList<>(Arrays.asList("A2", "A3", "D8"));
			//SALVOS
			salvoRepository.save(new Salvo(turn1, gamePlayer5, salLocation9));
			salvoRepository.save(new Salvo(turn2, gamePlayer5, salLocation10));


			//==========================================================================================================


			GamePlayer gamePlayer6 = gamePlayerRepository.save(new GamePlayer(tAlmeida, game3));
			//LOCATIONsSHIPS
			List<String> shipLocation12 = new ArrayList<>(Arrays.asList("A2", "A3", "A4"));
			List<String> shipLocation13 = new ArrayList<>(Arrays.asList("G6", "H6"));
			//SHIPS
			Ship ship11 = shipRepository.save(new Ship(ShipTypes.SUBMARINE.toString().toLowerCase(), gamePlayer6, shipLocation12));
			Ship ship12 = shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer6, shipLocation13));
			//SALVOLOCATIOS
			List<String> salLocation11 = new ArrayList<>(Arrays.asList("H1", "H2", "H3"));
			List<String> salLocation12 = new ArrayList<>(Arrays.asList("E1", "F2", "G3"));
			//SALVOS
			salvoRepository.save(new Salvo(turn1, gamePlayer6, salLocation11));
			salvoRepository.save(new Salvo(turn2, gamePlayer6, salLocation12));


			//==========================================================================================================


			GamePlayer gamePlayer7 = gamePlayerRepository.save(new GamePlayer(cObrian, game4));
			//LOCATIONsSHIPS
			List<String> shipLocation14 = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
			List<String> shipLocation15 = new ArrayList<>(Arrays.asList("C6", "C7"));
			//SHIPS
			Ship ship13 = shipRepository.save(new Ship(ShipTypes.DESTROYER.toString().toLowerCase(), gamePlayer7, shipLocation14));
			Ship ship14 = shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer7, shipLocation15));
			//SALVOLOCATIOS
			List<String> salLocation13 = new ArrayList<>(Arrays.asList("A3", "A4", "F7"));
			List<String> salLocation14 = new ArrayList<>(Arrays.asList("A2", "G6", "H6"));
			//SALVOS
			salvoRepository.save(new Salvo(turn1, gamePlayer7, salLocation13));
			salvoRepository.save(new Salvo(turn2, gamePlayer7, salLocation14));


			//==========================================================================================================


			GamePlayer gamePlayer8 = gamePlayerRepository.save(new GamePlayer(jBauer, game4));
			//LOCATIONsSHIPS
			List<String> shipLocation16 = new ArrayList<>(Arrays.asList("A2", "A3", "A4"));
			List<String> shipLocation17 = new ArrayList<>(Arrays.asList("G6", "H6"));
			//SHIPS
			Ship ship15 = shipRepository.save(new Ship(ShipTypes.SUBMARINE.toString().toLowerCase(), gamePlayer8, shipLocation16));
			Ship ship16 = shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer8, shipLocation17));
			//SALVOLOCATIOS
			List<String> salLocation15 = new ArrayList<>(Arrays.asList("B5", "C6", "H1"));
			List<String> salLocation16 = new ArrayList<>(Arrays.asList("C5", "C7", "D5"));
			//SALVOS
			salvoRepository.save(new Salvo(turn1, gamePlayer8, salLocation15));
			salvoRepository.save(new Salvo(turn2, gamePlayer8, salLocation16));


			//==========================================================================================================


			GamePlayer gamePlayer9 = gamePlayerRepository.save(new GamePlayer(tAlmeida, game5));
			//LOCATIONsSHIPS
			List<String> shipLocation18 = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
			List<String> shipLocation19 = new ArrayList<>(Arrays.asList("C6", "C7"));
			//SHIPS
			Ship ship17 = shipRepository.save(new Ship(ShipTypes.DESTROYER.toString().toLowerCase(), gamePlayer9, shipLocation18));
			Ship ship18 = shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer9, shipLocation19));
			//SALVOLOCATIOS
			List<String> salLocation17 = new ArrayList<>(Arrays.asList("A1", "A2", "A3"));
			List<String> salLocation18 = new ArrayList<>(Arrays.asList("G6", "G7", "G8"));
			//SALVOS
			salvoRepository.save(new Salvo(turn1, gamePlayer9, salLocation17));
			salvoRepository.save(new Salvo(turn2, gamePlayer9, salLocation18));


			//==========================================================================================================


			GamePlayer gamePlayer10 = gamePlayerRepository.save(new GamePlayer(jBauer, game5));
			//LOCATIONsSHIPS
			List<String> shipLocation20 = new ArrayList<>(Arrays.asList("A2", "A3", "A4"));
			List<String> shipLocation21 = new ArrayList<>(Arrays.asList("G6", "H6"));
			//SHIPS
			Ship ship19 = shipRepository.save(new Ship(ShipTypes.SUBMARINE.toString().toLowerCase(), gamePlayer10, shipLocation20));
			Ship ship20 = shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer10, shipLocation21));
			//SALVOLOCATIOS
			List<String> salLocation19 = new ArrayList<>(Arrays.asList("B5", "B6", "C7"));
			List<String> salLocation20 = new ArrayList<>(Arrays.asList("C6", "D6", "E6"));
			List<String> salLocation21 = new ArrayList<>(Arrays.asList("H1", "H8"));
			//SALVOS
			salvoRepository.save(new Salvo(turn1, gamePlayer10, salLocation19));
			salvoRepository.save(new Salvo(turn2, gamePlayer10, salLocation20));
			salvoRepository.save(new Salvo(turn3, gamePlayer10, salLocation21));


			//==========================================================================================================


			GamePlayer gamePlayer11 = gamePlayerRepository.save(new GamePlayer(kimBauer, game6));
			//LOCATIONsSHIPS
			List<String> shipLocation22 = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
			List<String> shipLocation23 = new ArrayList<>(Arrays.asList("C6", "C7"));
			//SHIPS
			Ship ship21 = shipRepository.save(new Ship(ShipTypes.DESTROYER.toString().toLowerCase(), gamePlayer11, shipLocation22));
			Ship ship22 = shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer11, shipLocation23));
			//SALVOLOCATIOS
			//SALVOS


			//==========================================================================================================


			GamePlayer gamePlayer12 = gamePlayerRepository.save(new GamePlayer(tAlmeida, game7));
			//LOCATIONsSHIPS
			//SHIPS
			//SALVOLOCATIOS
			//SALVOS


			//==========================================================================================================

			
			GamePlayer gamePlayer13 = gamePlayerRepository.save(new GamePlayer(kimBauer, game8));
			//LOCATIONsSHIPS
			List<String> shipLocation24 = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
			List<String> shipLocation25 = new ArrayList<>(Arrays.asList("C6", "C7"));
			//SHIPS
			Ship ship23 = shipRepository.save(new Ship(ShipTypes.DESTROYER.toString().toLowerCase(), gamePlayer13, shipLocation24));
			Ship ship24 = shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer13, shipLocation25));
			//SALVOLOCATIOS
			//SALVOS


			//==========================================================================================================


			GamePlayer gamePlayer14 = gamePlayerRepository.save(new GamePlayer(tAlmeida, game8));
			//LOCATIONsSHIPS
			List<String> shipLocation26 = new ArrayList<>(Arrays.asList("A2", "A3", "A4"));
			List<String> shipLocation27 = new ArrayList<>(Arrays.asList("G6", "H6"));
			//SHIPS
			Ship ship25 = shipRepository.save(new Ship(ShipTypes.SUBMARINE.toString().toLowerCase(), gamePlayer14, shipLocation26));
			Ship ship26 = shipRepository.save(new Ship(ShipTypes.PATROLBOAT.toString().toLowerCase(), gamePlayer14, shipLocation27));
			//SALVOLOCATIONS
			//SALVOS



			Score score = scoreRepository.save(new Score(game1, jBauer, 1.0));
			Score score1 = scoreRepository.save(new Score(game1, cObrian, 0.0));

			Score score2 = scoreRepository.save(new Score(game2, jBauer, 0.5));
			Score score3 = scoreRepository.save(new Score(game2, cObrian, 0.5));

			Score score4 = scoreRepository.save(new Score(game3, cObrian, 1.0));
			Score score5 = scoreRepository.save(new Score(game3, tAlmeida, 0.0));

			Score score6 = scoreRepository.save(new Score(game4, cObrian, 0.5));
			Score score7 = scoreRepository.save(new Score(game4, jBauer, 0.5));

			Score score8 = scoreRepository.save(new Score(game5, tAlmeida, null));
			Score score9 = scoreRepository.save(new Score(game5, jBauer, null));

			Score score10 = scoreRepository.save(new Score(game6, kimBauer, null));

			Score score12 = scoreRepository.save(new Score(game7, tAlmeida, null));

			Score score14 = scoreRepository.save(new Score(game8, kimBauer, null));
			Score score15 = scoreRepository.save(new Score(game8, tAlmeida, null));
		};
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter{

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(inputName -> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null){
				return new User(player.getUserName(), player.getPassword(), AuthorityUtils.createAuthorityList("USER"));
			}else{
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
				.antMatchers("/h2-console").permitAll()
				.antMatchers("/web/game.html").hasAuthority("USER")
                .antMatchers("/api/game_view").hasAuthority("USER")
				.antMatchers("/api/games/players/**").hasAuthority("USER")
				.antMatchers("/web/**").permitAll()
                .antMatchers("/api/**").permitAll()
				.anyRequest().hasAuthority("USER");

		http.formLogin().usernameParameter("name").passwordParameter("pwd").loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

		http.headers().frameOptions().disable();
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}