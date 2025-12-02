import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IResolver {
    
    List<Player> getPlayers();

    int getCountWithoutAgency();

    int getMaxDefenderGoalsCount();

    String getTheExpensiveGermanPlayerPosition();

    Map<String, String> getPlayersByPosition();

    Set<String> getTeams();

    Map<String, Integer> getTop5TeamsByGoalsCount();

    String getAgencyWithMinPlayersCount();

    String getTheRudestTeam();
}