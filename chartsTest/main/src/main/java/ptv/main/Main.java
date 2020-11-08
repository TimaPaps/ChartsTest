package ptv.main;

import org.jfree.ui.RefineryUtilities;
import ptv.show.ChartsShow;

import static ptv.connect.ConnectDB.connectDB;

public class Main {
    public static void main(String[] args) throws Exception {
        connectDB();

        ChartsShow demo = new ChartsShow("TimeSeriesChart Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
