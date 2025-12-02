import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CSV_Parser{
    private final Path playersFile;

    public CSV_Parser(Path playersFile) {
        this.playersFile = playersFile;
    }
    private static Player parseRow(String line){
        String[] playerAsRow = line.split(";");
        return new Player(playerAsRow[0],
                playerAsRow[1],
                Position.valueOf(playerAsRow[3]),
                playerAsRow[4],playerAsRow[5],
                Integer.parseInt(playerAsRow[6]),
                Integer.parseInt(playerAsRow[8]),
                Integer.parseInt(playerAsRow[11]));
    }
    public List<Player> parsePlayers() throws IOException{
        return Files.readAllLines(playersFile)
                .stream()
                .skip(1)
                .map(line -> parseRow(line))
                .toList();
    }
}
