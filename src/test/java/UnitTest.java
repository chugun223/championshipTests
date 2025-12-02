import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UnitTest {
    private static final CSV_Parser mockParser = mock(CSV_Parser.class);

    @Test
    public void getCountWithoutAgency_EmptyAgency() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(new Player("Iavn", " ", Position.DEFENDER, "", "",10, 5, 5)));
        Resolver resolver = new Resolver(mockParser);
        assertEquals(0, resolver.getCountWithoutAgency());
    }

    @Test
    public void getCountWithoutAgency_SeveralSpacesAgency() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(new Player("Iavn", " ", Position.DEFENDER, "", "  ",10, 5, 5)));
        Resolver resolver = new Resolver(mockParser);
        assertEquals(0, resolver.getCountWithoutAgency());
    }

    @Test
    public void getCountWithoutAgency_NormalAgency() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(new Player("Iavn", " ", Position.DEFENDER, "", "Madrid",10, 5, 5)));
        Resolver resolver = new Resolver(mockParser);
        assertEquals(1, resolver.getCountWithoutAgency());
    }

    @Test
    public void getCountWithoutAgency_NormalAgencies() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(new Player("Iavn", " ", Position.DEFENDER, "", "madrid",10, 5, 5),
                                    new Player("Iavn", " ", Position.DEFENDER, "", "pidrid",10, 5, 5)));
        Resolver resolver = new Resolver(mockParser);
        assertEquals(2, resolver.getCountWithoutAgency());
    }

    @Test
    public void getMaxDefenderGoalsCount_OneDefender() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("Ivan", "Team1", Position.DEFENDER, "RU", "", 10, 5, 1)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals(5, resolver.getMaxDefenderGoalsCount());
    }

    @Test
    public void getMaxDefenderGoalsCount_MultipleDefenders() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("Ivan", "Team1", Position.DEFENDER, "RU", "", 10, 5, 1),
                        new Player("John", "Team2", Position.DEFENDER, "DE", "", 12, 7, 0),
                        new Player("Alex", "Team3", Position.MIDFIELD, "ES", "", 15, 10, 2)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals(7, resolver.getMaxDefenderGoalsCount());
    }

    @Test
    public void getMaxDefenderGoalsCount_NoDefenders() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("Alex", "Team3", Position.MIDFIELD, "ES", "", 15, 10, 2)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals(0, resolver.getMaxDefenderGoalsCount());
    }

    @Test
    public void getTheExpensiveGermanPlayerPosition_OneGerman() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("Max", "TeamA", Position.MIDFIELD, "Germany", "", 50, 5, 0)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals("ПОЛУЗАЩИТНИК", resolver.getTheExpensiveGermanPlayerPosition());
    }

    @Test
    public void getTheExpensiveGermanPlayerPosition_MultipleGermans() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("Max", "TeamA", Position.MIDFIELD, "Germany", "", 50, 5, 0),
                        new Player("Leo", "TeamB", Position.DEFENDER, "Germany", "", 80, 3, 1),
                        new Player("John", "TeamC", Position.FORWARD, "Spain", "", 90, 10, 2)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals("ЗАЩИТНИК", resolver.getTheExpensiveGermanPlayerPosition());
    }

    @Test
    public void getTheExpensiveGermanPlayerPosition_NoGermans() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("John", "TeamC", Position.FORWARD, "Spain", "", 90, 10, 2)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals("ошибка", resolver.getTheExpensiveGermanPlayerPosition());
    }

    @Test
    public void getPlayersByPosition_Test() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("Ivan", "T1", Position.DEFENDER, "", "", 10, 5, 0),
                        new Player("Alex", "T2", Position.MIDFIELD, "", "", 15, 7, 1),
                        new Player("Leo", "T3", Position.DEFENDER, "", "", 20, 8, 2)
                ));
        Resolver resolver = new Resolver(mockParser);
        Map<String, String> byPos = resolver.getPlayersByPosition();
        assertEquals("Ivan, Leo", byPos.get("DEFENDER"));
        assertEquals("Alex", byPos.get("MIDFIELD"));
        assertEquals("", byPos.get("FORWARD"));
    }

    @Test
    public void getPlayersByPosition_NoPolayers() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of());
        Resolver resolver = new Resolver(mockParser);
        Map<String, String> byPos = resolver.getPlayersByPosition();
        for (String value : byPos.values()) {
            assertEquals("", value);
        }
    }

    @Test
    public void getTeams_Test() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("Ivan", "T1", Position.DEFENDER, "", "", 10, 5, 0),
                        new Player("Alex", "T2", Position.MIDFIELD, "", "", 15, 7, 1),
                        new Player("Leo", "T1", Position.DEFENDER, "", "", 20, 8, 2)
                ));
        Resolver resolver = new Resolver(mockParser);
        Set<String> teams = resolver.getTeams();
        assertEquals(Set.of("T1", "T2"), teams);
    }

    @Test
    public void getTeams_NoPlayers() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of());
        Resolver resolver = new Resolver(mockParser);
        Set<String> teams = resolver.getTeams();
        assertTrue(teams.isEmpty());
    }

    @Test
    public void getTop5TeamsByGoalsCount_Test() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("P1", "A", Position.DEFENDER, "", "", 10, 5, 0),
                        new Player("P2", "B", Position.MIDFIELD, "", "", 15, 7, 1),
                        new Player("P3", "A", Position.DEFENDER, "", "", 20, 8, 2),
                        new Player("P4", "C", Position.FORWARD, "", "", 10, 4, 0),
                        new Player("P5", "B", Position.MIDFIELD, "", "", 15, 3, 1),
                        new Player("P6", "D", Position.FORWARD, "", "", 12, 2, 0),
                        new Player("P7", "E", Position.GOALKEEPER, "", "", 10, 1, 0)
                ));
        Resolver resolver = new Resolver(mockParser);
        Map<String, Integer> top5 = resolver.getTop5TeamsByGoalsCount();
        assertEquals(List.of("A","B","C","D","E"), List.copyOf(top5.keySet()));
    }

    @Test
    public void getTop5TeamsByGoalsCount_LessThan5Teams() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("P1", "A", Position.DEFENDER, "", "", 10, 5, 0),
                        new Player("P2", "B", Position.MIDFIELD, "", "", 15, 7, 1),
                        new Player("P3", "A", Position.DEFENDER, "", "", 20, 8, 2),
                        new Player("P4", "C", Position.FORWARD, "", "", 10, 4, 0),
                        new Player("P5", "B", Position.MIDFIELD, "", "", 15, 3, 1),
                        new Player("P6", "D", Position.FORWARD, "", "", 12, 2, 0),
                        new Player("P7", "D", Position.GOALKEEPER, "", "", 10, 1, 0)
                ));
        Resolver resolver = new Resolver(mockParser);
        Map<String, Integer> top5 = resolver.getTop5TeamsByGoalsCount();
        assertEquals(List.of("A","B","C","D"), List.copyOf(top5.keySet()));
    }

    @Test
    public void getTop5TeamsByGoalsCount_MoreThan5Teams() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("P1", "A", Position.DEFENDER, "", "", 10, 5, 0),
                        new Player("P2", "B", Position.MIDFIELD, "", "", 15, 7, 1),
                        new Player("P3", "A", Position.DEFENDER, "", "", 20, 8, 2),
                        new Player("P4", "F", Position.FORWARD, "", "", 10, 10, 0),
                        new Player("P5", "B", Position.MIDFIELD, "", "", 15, 3, 1),
                        new Player("P6", "D", Position.FORWARD, "", "", 12, 4, 0),
                        new Player("P6", "C", Position.FORWARD, "", "", 12, 2, 0),
                        new Player("P7", "E", Position.GOALKEEPER, "", "", 10, 14, 0)
                ));
        Resolver resolver = new Resolver(mockParser);
        Map<String, Integer> top5 = resolver.getTop5TeamsByGoalsCount();
        assertEquals(List.of("E", "A","B","F","D"), List.copyOf(top5.keySet()));
    }

    @Test
    public void getAgencyWithMinPlayersCount_Test() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("P1", "A", Position.DEFENDER, "", "X", 10, 5, 0),
                        new Player("P2", "B", Position.MIDFIELD, "", "Y", 15, 7, 1),
                        new Player("P3", "A", Position.DEFENDER, "", "X", 20, 8, 2)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals("Y", resolver.getAgencyWithMinPlayersCount());
    }

    @Test
    public void getAgencyWithMinPlayersCount_EqualPlayersCount() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("P1", "A", Position.DEFENDER, "", "A", 10, 5, 0),
                        new Player("P2", "B", Position.MIDFIELD, "", "B", 15, 7, 1)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals("A", resolver.getAgencyWithMinPlayersCount());
    }

    @Test
    public void getAgencyWithMinPlayersCount_NoPlayers() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of());
        Resolver resolver = new Resolver(mockParser);
        assertEquals("агентства не найдены", resolver.getAgencyWithMinPlayersCount());
    }

    @Test
    public void getTheRudestTeam_Test() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("P1", "A", Position.DEFENDER, "", "", 10, 5, 3),
                        new Player("P2", "B", Position.MIDFIELD, "", "", 15, 7, 4),
                        new Player("P3", "A", Position.DEFENDER, "", "", 20, 8, 5)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals("A", resolver.getTheRudestTeam());
    }

    @Test
    public void getTheRudestTeam_NoPlayers() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of());
        Resolver resolver = new Resolver(mockParser);
        assertEquals("команды не найдены", resolver.getTheRudestTeam());
    }

    @Test
    public void getTheRudestTeam_EqualRude() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("P1", "V", Position.DEFENDER, "", "", 10, 5, 3),
                        new Player("P2", "Z", Position.MIDFIELD, "", "", 15, 7, 3)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals("V", resolver.getTheRudestTeam());
    }

    @Test
    public void mapGoalsToTransferCostForForwards_Test() {
        List<Player> players = List.of(
                new Player("Forward1", "TeamA", Position.FORWARD, "Spain", "X", 10, 5, 0),
                new Player("Forward2", "TeamB", Position.FORWARD, "Germany", "Y", 20, 8, 1),
                new Player("Mid1", "TeamC", Position.MIDFIELD, "France", "Z", 15, 7, 0), // не должен попадать
                new Player("Def1", "TeamD", Position.DEFENDER, "Italy", "X", 12, 3, 0)  // не должен попадать
        );

        ChartMapper mapper = new ChartMapper();
        XYSeriesCollection dataset = mapper.mapGoalsToTransferCostForForwards(players);
        assertEquals(1, dataset.getSeriesCount());
        var series = dataset.getSeries(0);
        assertEquals(2, series.getItemCount());
        assertEquals(10.0, series.getX(0).doubleValue());
        assertEquals(5.0, series.getY(0).doubleValue());
        assertEquals(20.0, series.getX(1).doubleValue());
        assertEquals(8.0, series.getY(1).doubleValue());
    }

    @Test
    public void getTheExpensiveGermanPlayerPosition_NullNationality() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("John", "TeamX", Position.FORWARD, null, "", 10, 5, 0)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals("ошибка", resolver.getTheExpensiveGermanPlayerPosition());
    }

    @Test
    public void getTeams_NullAndEmptyTeam() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("John", null, Position.FORWARD, "", "", 10, 5, 0),
                        new Player("Alex", "", Position.MIDFIELD, "", "", 12, 3, 0),
                        new Player("Leo", "T1", Position.DEFENDER, "", "", 15, 7, 0)
                ));
        Resolver resolver = new Resolver(mockParser);
        Set<String> teams = resolver.getTeams();
        assertEquals(Set.of("T1"), teams);
    }

    @Test
    public void getAgencyWithMinPlayersCount_NullAgency() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("P1", "A", Position.DEFENDER, "", null, 10, 5, 0),
                        new Player("P2", "B", Position.MIDFIELD, "", "X", 12, 7, 0)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals("X", resolver.getAgencyWithMinPlayersCount());
    }

    @Test
    public void getTheRudestTeam_NullAndBlankTeams() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("P1", null, Position.DEFENDER, "", "", 10, 5, 3),
                        new Player("P2", "   ", Position.MIDFIELD, "", "", 12, 7, 4),
                        new Player("P3", "T1", Position.DEFENDER, "", "", 15, 6, 5)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals("T1", resolver.getTheRudestTeam());
    }

    @Test
    public void getCountWithoutAgency_NullAgency() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("Ivan", "Team", Position.DEFENDER, "", null, 10, 5, 0)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals(0, resolver.getCountWithoutAgency());
    }

    @Test
    public void getTop5TeamsByGoalsCount_WithNullAndEmptyTeam() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("P1", null, Position.DEFENDER, "", "", 10, 5, 0),
                        new Player("P2", "", Position.MIDFIELD, "", "", 12, 7, 0),
                        new Player("P3", "A", Position.FORWARD, "", "", 15, 4, 1)
                ));
        Resolver resolver = new Resolver(mockParser);
        Map<String, Integer> top5 = resolver.getTop5TeamsByGoalsCount();
        assertEquals(List.of("A"), List.copyOf(top5.keySet()));
    }

    @Test
    public void getAgencyWithMinPlayersCount_EmptyAgency() throws IOException {
        when(mockParser.parsePlayers())
                .thenReturn(List.of(
                        new Player("P1", "A", Position.DEFENDER, "", "", 10, 5, 0)
                ));
        Resolver resolver = new Resolver(mockParser);
        assertEquals("агентства не найдены", resolver.getAgencyWithMinPlayersCount());
    }
}
