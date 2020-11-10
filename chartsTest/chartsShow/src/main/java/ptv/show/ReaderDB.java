package ptv.show;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import com.impossibl.postgres.jdbc.PGDataSource;

import java.sql.Statement;

public class ReaderDB {
    public static void listenToNotifyMessage() {
        PGDataSource dataSource = new PGDataSource();
        dataSource.setHost("localhost");
        dataSource.setPort(5432);
        dataSource.setDatabaseName("charts_test");
        dataSource.setUser("tima");
        dataSource.setPassword("");

        PGNotificationListener listener = new PGNotificationListener() {

            @Override
            public void notification(int processId, String channelName, String payload) {
                System.out.println("*********INSIDE NOTIFICATION*************");
            }
        };

        try (
                PGConnection connection = (PGConnection) dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                statement.execute("LISTEN newWrite");
                statement.close();
                connection.addNotificationListener(listener);
        /*    while (true) {
                System.out.println("my new write");
            }*/
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
