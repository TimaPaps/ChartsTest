package ptv.connect;

import java.sql.*;

public class ConnectDB {
    private static Statement stmt = null;
    private static ResultSet rs = null;

    public static void main(String[] args) throws Exception {
        connectDB();
    }

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

            stmt = conn.createStatement();

            int i = 0;
            int[] list = {001, 002, 003};
            while (i < 30) {
                for (int j = 0; j < list.length; j++) {
                    double valSinOne = Math.sin(0.1 * (i + 1)) * (j + 1);
                    double valSinTwo = Math.sin(0.2 * (i + 1)) * (j + 1);
                    double valSinFree = Math.sin(0.3 * (i + 1)) * (j + 1);
                    int sensor = list[j];

                    String queryWrite = "INSERT INTO core_sensors (value_1, value_2, value_3, sensor) " +
                            "VALUES (" + valSinOne + ", " + valSinTwo + ", " + valSinFree + ", " + sensor + " )";
                    stmt.executeUpdate(queryWrite);
                }
                Thread.sleep(3000);
                i++;
            }

            //rs.close();
            stmt.close();
            //conn.close();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.println("что то пошло не так");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
