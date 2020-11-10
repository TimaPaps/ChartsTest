package ptv.connect;

import java.sql.*;

public class ConnectDB {
    private static Statement stmt = null;
    private static ResultSet rs = null;

    public static void connectDB() {
        String url = "jdbc:postgresql://127.0.0.1:5432/charts_test";
        String user = "tima";
        String password = "";
        try (Connection conn = DriverManager.getConnection(
                url, user, password)) {

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

            String queryRead = "SELECT * FROM core_sensors";

            stmt = conn.createStatement();
/*            rs = stmt.executeQuery(queryRead);

            while (rs.next()) {
                int id = rs.getInt(1);
                String create_at = rs.getString(2);
                int value_1 = rs.getInt(3);
                int value_2 = rs.getInt(4);
                int value_3 = rs.getInt(5);

                System.out.printf("id: %d, create_at: %s, value_1: %s, value_2: %s, value_3: %s; ", id, create_at, value_1, value_2, value_3);
            }
*/
            int i = 0;
            int[] list = {001, 002, 003};
            while (i < 1) {
                for (int j = 0; j < list.length; j++) {
                    double valSinOne = Math.sin(0.1 * (i + 1)) * (j + 1);
                    double valSinTwo = Math.sin(0.2 * (i + 1)) * (j + 1);
                    double valSinFree = Math.sin(0.3 * (i + 1)) * (j + 1);
                    int sensor = list[j];

                    String queryWrite = "INSERT INTO core_sensors (value_1, value_2, value_3, sensor) " +
                            "VALUES (" + valSinOne + ", " + valSinTwo + ", " + valSinFree + ", " + sensor + " )";
                    stmt.executeUpdate(queryWrite);
                }
                Thread.sleep(1000);
                i++;
            }

            //rs.close();
            stmt.close();
            //conn.close();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.println("not connect");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
