import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.List;

public class ChartMapper {

    public XYSeriesCollection mapGoalsToTransferCostForForwards(List<Player> players) {
        XYSeries series = new XYSeries(" Goals - Transfer Cost ");
        players.stream()
                .filter(p -> p.Position() == Position.FORWARD)
                .forEach(p -> series.add(p.TransferCost(), p.Goals()));
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }
}
