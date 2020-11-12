package ptv.main;

import org.jfree.ui.RefineryUtilities;
import ptv.show.ChartsShow;

import static ptv.connect.ConnectDB.connectDB;


public class Main {
    public static void main(String[] args) throws Exception {
        ChartsShow demo = new ChartsShow("Тестовая отрисовка");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

        connectDB();
    }
}
