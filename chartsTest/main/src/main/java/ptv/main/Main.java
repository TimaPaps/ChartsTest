package ptv.main;

import org.jfree.ui.RefineryUtilities;
import ptv.show.ChartsShow;


import java.sql.Connection;
import java.sql.DriverManager;

import static ptv.connect.ConnectDB.connectDB;
import static ptv.show.ChartsShow.readerDB;
import static ptv.show.ReaderDB.listenToNotifyMessage;

public class Main {
    public static void main(String[] args) throws Exception {
        connectDB();

        //listenToNotifyMessage();

        readerDB();

        ChartsShow demo = new ChartsShow("Тестовая отрисовка");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
/*
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://127.0.0.1:5432/charts_test";
        String user = "tima";
        String password = "";

        // Create two distinct connections, one for the notifier
        // and another for the listener to show the communication
        // works across connections although this example would
        // work fine with just one connection.
        Connection lConn = DriverManager.getConnection(url, user, password);
        Connection nConn = DriverManager.getConnection(url, user, password);

        // Create two threads, one to issue notifications and
        // the other to receive them.
        Listener listener = new Listener(lConn);
        Notifier notifier = new Notifier(nConn);
        listener.start();
        notifier.start();
 */
    }
}
