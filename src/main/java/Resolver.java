import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Resolver implements IResolver{
    private final List<Player> players;

    public Resolver(CSV_Parser parser) throws IOException {
        this.players = parser.parsePlayers();
    }

    @Override
    public List<Player> getPlayers() {
        return List.copyOf(this.players);
    }

    @Override
    public int getCountWithoutAgency() {
        return (int)players.stream()
                .filter(player -> player.Agency() != null && !player.Agency().trim().isEmpty())
                .count();
    }

    @Override
    public int getMaxDefenderGoalsCount() {
        return players.stream()
                .filter(player -> player.Position() == Position.DEFENDER)
                .mapToInt(player -> player.Goals())
                .max()
                .orElse(0);
    }

    @Override
    public String getTheExpensiveGermanPlayerPosition() {
        return players.stream()
                .filter(player -> player.Nationality() != null && player.Nationality().equals("Germany"))
                .max(Comparator.comparing(player -> player.TransferCost()))
                .map(player -> Translator.translatePosition(player.Position()))
                .orElse("ошибка");
    }

    @Override
    public Map<String, String> getPlayersByPosition() {
        return Arrays.stream(Position.values())
                .collect(Collectors.toMap(
                        Position::name,
                        pos -> players.stream()
                                .filter(player -> player.Position() == pos)
                                .map(Player::Name)
                                .collect(Collectors.joining(", "))));
    }

    @Override
    public Set<String> getTeams() {
        return players.stream()
                .filter(player -> player.Team() != null && !player.Team().isEmpty())
                .map(player -> player.Team().toString())
                .collect(Collectors.toSet());
    }

    @Override
    public Map<String, Integer> getTop5TeamsByGoalsCount() {
        return players.stream()
                .filter(player -> player.Team() != null && !player.Team().isEmpty())
                .collect(Collectors.groupingBy(Player::Team, Collectors.summingInt(Player::Goals)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public String getAgencyWithMinPlayersCount() {
        return players.stream()
                .filter(player -> player.Agency() != null && !player.Agency().isEmpty())
                .collect(Collectors.groupingBy(Player::Agency, Collectors.counting()))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("агентства не найдены");
    }

    @Override
    public String getTheRudestTeam() {
        return players.stream()
                .filter(player -> player.Team() != null && !player.Team().trim().isEmpty())
                .collect(Collectors.groupingBy(Player::Team, Collectors.averagingDouble(Player::RedCards)))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("команды не найдены");
    }
}
