import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        var parser = new CSV_Parser(Path.of("fakePlayers.csv"));
        List<Player> players = parser.parsePlayers();
        ChartMapper mapper = new ChartMapper();
        var dataset = mapper.mapGoalsToTransferCostForForwards(players);
        Scanner scanner = new Scanner(System.in);
        ChartDrawer drawer = new ChartDrawer();
        Resolver resolver = new Resolver(parser);
        mainCycle : while(true){
            System.out.println("\nкоманды:");
            System.out.println("1 - вывести количество игроков, интересы которых не представляет агентство");
            System.out.println("2 - вывести максимальное число голов, забитых защитником");
            System.out.println("3 - вывести русское название позиции самого дорогого немецкого игрока");
            System.out.println("4 - вывести имена игроков, сгруппированных по позициям, на которых они играют");
            System.out.println("5 - вывести множество команд, которые представлены в чемпионате");
            System.out.println("6 - вывести топ-5 команд по количеству забитых мячей и количество этих мячей");
            System.out.println("7 - вывести агентство, сумма игроков которого наименьшая");
            System.out.println("8 - вывести команду с наибольшим средним числом красных карточек на одного игрока");
            System.out.println("9 - демонстрация зависимости количества забитых голов от трансферной стоимости для нападающих");
            System.out.println("0 - выход");
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println("неверный ввод команды");
                continue;
            }
            switch(command){
                case 1 -> {
                    int count = resolver.getCountWithoutAgency();
                    System.out.println("количество игроков без агентства: " + count);
                }

                case 2 -> {
                    int maxDefenderGoals = resolver.getMaxDefenderGoalsCount();
                    System.out.println("максимальное число голов защитника: " + maxDefenderGoals);
                }

                case 3 -> {
                    String pos = resolver.getTheExpensiveGermanPlayerPosition();
                    System.out.println("позиция самого дорогого немецкого игрока: " + pos);
                }

                case 4 -> {
                    System.out.println("игроки по позициям");
                    Map<String, String> grouped = resolver.getPlayersByPosition();
                    grouped.forEach((pos, names) -> System.out.println(pos + ": " + names));
                }

                case 5 -> {
                    Set<String> teams = resolver.getTeams();
                    System.out.println("футбольные команды:");
                    teams.forEach(System.out::println);
                }

                case 6 -> {
                    Map<String, Integer> top = resolver.getTop5TeamsByGoalsCount();
                    System.out.println("топ-5 команд по забитым мячам:");
                    top.forEach((team, goals) ->
                            System.out.println(team + " — " + goals)
                    );
                }

                case 7 -> {
                    String agency = resolver.getAgencyWithMinPlayersCount();
                    System.out.println("агентство с минимальным числом игроков: " + agency);
                }

                case 8 -> {
                    String team = resolver.getTheRudestTeam();
                    System.out.println("команда с наибольшим средним числом красных карточек: " + team);
                }

                case 9 ->{
                    drawer.drawLineChart(
                            "Зависимость голов от трансферной стоимости (нападающие)",
                            "Transfer Cost",
                            "Goals",
                            dataset);
                }
                case 0 ->{
                    System.out.println("выход");
                    break mainCycle;
                }

                default -> System.out.println("неизвестная команда");
            }
        }
    }
}