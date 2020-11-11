package ptv.show;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ChartsShow extends ApplicationFrame {

    private static final long serialVersionUID = 1L;

    private static Statement stmt = null;
    private static ResultSet rs = null;

    private XYDataset readerDB() {
        String url = "jdbc:postgresql://127.0.0.1:5432/charts_test";
        String user = "tima";
        String password = "";

        Map<Integer, TimeSeries> series = new HashMap<>();
/*        TimeSeries s1 = new TimeSeries("Датчик №1");
        TimeSeries s2 = new TimeSeries("Датчик №2");
        TimeSeries s3 = new TimeSeries("Датчик №3");
*/
        try (Connection conn = DriverManager.getConnection(
                url, user, password)) {

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

            String queryReadSensor = "SELECT sensor FROM core_sensors";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryReadSensor);




            while (rs.next()) {
                int sensor = rs.getInt(1);
                series.put(sensor, new TimeSeries("Датчик № " + sensor));
            }
//                int id = rs.getInt(1);
//                Timestamp received_at = rs.getTimestamp(2);
//                double value_1 = rs.getDouble(4);
//                double value_2 = rs.getDouble(5);
//                double value_3 = rs.getDouble(6);
//                System.out.println(sensor);
//                Second s = new Second(received_at);

            String queryRead = "SELECT * FROM core_sensors";

            rs = stmt.executeQuery(queryRead);

            while (rs.next()) {
                Timestamp received_at = rs.getTimestamp(2);
                int sensor = rs.getInt(3);
                double value_1 = rs.getDouble(4);
                double value_2 = rs.getDouble(5);
                double value_3 = rs.getDouble(6);
                Second s = new Second(received_at);

                series.get(sensor).addOrUpdate(s, value_1);
            }

            rs.close();
            stmt.close();
            //conn.close();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.println("not connect");
        } catch (Exception e) {
            e.printStackTrace();
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        series.forEach((k, v) -> dataset.addSeries(v));

/*        dataset.addSeries(s1);
        dataset.addSeries(s2);
        dataset.addSeries(s3);*/
        return dataset;
    }

    static {
        // set a theme using the new shadow generator feature available in
        // 1.0.14 - for backwards compatibility it is not enabled by default
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow",
                true));
    }

    public ChartsShow(String title) {
        super(title);
        ChartPanel chartPanel = (ChartPanel) createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(750, 300));
        setContentPane(chartPanel);
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Отрисовка значений датчиков",  // title
                "Время записи в БД",                            // x-axis label
                "Значение",                      // y-axis label
                dataset,                       // data
                true,                          // create legend
                true,                          // generate tooltips
                false                          // generate URLs
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("YYYY.MM.dd HH:mm:ss"));

        return chart;
    }

    public JPanel createDemoPanel() {
        JFreeChart chart = createChart(readerDB());
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(600, 300));
        return panel;
    }
}