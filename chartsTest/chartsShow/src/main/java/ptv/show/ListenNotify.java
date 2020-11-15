package ptv.show;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import com.impossibl.postgres.jdbc.PGDataSource;
import org.jfree.data.time.Second;
import org.jfree.ui.RefineryUtilities;

import org.json.JSONObject;

/**
 * This program uses the pgjdbc_ng driver which has an asynchronous
 * implementation for blocking on the Postgres NOTIFY/LISTEN events.
 *
 * No polling is done using this driver. You will see a forever loop
 * "while(true)" in the main(). This is done to keep the program running
 * and listening to multiple events happening in Postgres. Normally you
 * would just take one event and then do something with it.
 *
 */
public class ListenNotify {
    // Create the queue that will be shared by the producer and consumer
    private BlockingQueue queue = new ArrayBlockingQueue(10);

    // Database connection
    PGConnection connection;

    public ListenNotify() {
// Get database info from environment variables
        String DBHost = System.getenv("localhost");
        String DBName = System.getenv("charts_test");
        String DBUserName = System.getenv("tima");
        String DBPassword = System.getenv("");

// Create the listener callback
        PGNotificationListener listener = new PGNotificationListener() {
            @Override
            public void notification(int processId, String channelName, String payload) {
// Add event and payload to the queue
                queue.add("/channels/" + channelName + " " + payload);
            }
        };

        try {
// Create a data source for logging into the db
            PGDataSource dataSource = new PGDataSource();
            dataSource.setHost(DBHost);
            dataSource.setPort(5432);
            dataSource.setDatabaseName("charts_test");
            dataSource.setUser(DBUserName);
            dataSource.setPassword(DBPassword);

// Log into the db
            connection = (PGConnection) dataSource.getConnection();

// add the callback listener created earlier to the connection
            connection.addNotificationListener(listener);

// Tell Postgres to send NOTIFY q_event to our connection and listener
            Statement statement = connection.createStatement();
            statement.execute("LISTEN q_event");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return shared queue
     */
    public BlockingQueue getQueue() {
        return queue;
    }

//    static String json = "...";

    /**
     *
     * main entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        ChartsShow demo = new ChartsShow("Тестовая отрисовка");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

// Create a new listener
        ListenNotify ln = new ListenNotify();

// Get the shared queue
        BlockingQueue queue = ln.getQueue();

// Loop forever pulling messages off the queue
        while (true) {
            try {
// queue blocks until something is placed on it
                String msg = (String) queue.take();
                msg = msg.substring(msg.indexOf("{"));
                System.out.println(msg);

                JSONObject obj = new JSONObject(msg);
                String received_at = obj.getJSONObject("data").getString("created_at");
                int sensor = obj.getJSONObject("data").getInt("sensor");
                double value_1 =  obj.getJSONObject("data").getDouble("value_1");
                double value_2 =  obj.getJSONObject("data").getDouble("value_2");
                double value_3 =  obj.getJSONObject("data").getDouble("value_3");

                received_at = received_at.replace("T", " ");
                SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
                Timestamp timestamp = Timestamp.valueOf(received_at);
                Second sec = new Second(timestamp);

                demo.addPoint(sec, sensor, value_1, value_2, value_3);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}