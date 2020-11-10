package ptv.show;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class ChartsShow extends ApplicationFrame {

    private static final long serialVersionUID = 1L;

    private static Statement stmt = null;
    private static ResultSet rs = null;




    public static void readerDB() {
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
            rs = stmt.executeQuery(queryRead);

            while (rs.next()) {
                int id = rs.getInt(1);
                String received_at = rs.getString(2);
                int sensor = rs.getInt(3);
                double value_1 = rs.getDouble(4);
                double value_2 = rs.getDouble(5);
                double value_3 = rs.getDouble(6);

                System.out.printf("id: %d, received_at: %s, value_1: %s, value_2: %s, value_3: %s; ", id, received_at, value_1, value_2, value_3);
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
        axis.setDateFormatOverride(new SimpleDateFormat("MM.YYYY"));

        return chart;
    }

    private XYDataset createDataset() {
        TimeSeries s1 = new TimeSeries("Датчик №1");
        s1.add(new Month(7, 2013), 142.9);
        s1.add(new Month(8, 2013), 138.7);
        s1.add(new Month(9, 2013), 137.3);
/*        s1.add(new Month(10, 2013), 143.9);
        s1.add(new Month(11, 2013), 139.8);
        s1.add(new Month(12, 2013), 137.0);
        s1.add(new Month(1, 2014), 132.8);
        s1.add(new Month(2, 2014), 181.8);
        s1.add(new Month(3, 2014), 167.3);
        s1.add(new Month(4, 2014), 153.8);
        s1.add(new Month(5, 2014), 167.6);
        s1.add(new Month(6, 2014), 158.8);
        s1.add(new Month(7, 2014), 148.3);
        s1.add(new Month(8, 2014), 153.9);
        s1.add(new Month(9, 2014), 142.7);
        s1.add(new Month(10, 2014), 123.2);
        s1.add(new Month(11, 2014), 131.8);
        s1.add(new Month(12, 2014), 139.6);*/

        TimeSeries s2 = new TimeSeries("Датчик №2");
        s2.add(new Month(7, 2013), 111.7);
        s2.add(new Month(8, 2013), 111.0);
        s2.add(new Month(9, 2013), 109.6);
/*        s2.add(new Month(10, 2013), 113.2);
        s2.add(new Month(11, 2013), 111.6);
        s2.add(new Month(12, 2013), 108.8);
        s2.add(new Month(1, 2014), 101.6);
        s2.add(new Month(2, 2014), 129.6);
        s2.add(new Month(3, 2014), 123.2);
        s2.add(new Month(4, 2014), 117.2);
        s2.add(new Month(5, 2014), 124.1);
        s2.add(new Month(6, 2014), 122.6);
        s2.add(new Month(7, 2014), 119.2);
        s2.add(new Month(8, 2014), 116.5);
        s2.add(new Month(9, 2014), 112.7);
        s2.add(new Month(10, 2014), 101.5);
        s2.add(new Month(11, 2014), 106.1);
        s2.add(new Month(12, 2014), 110.3);*/

        TimeSeries s3 = new TimeSeries("Датчик №3");
        s3.add(new Month(7, 2013), 165.7);
        s3.add(new Month(8, 2013), 155.0);
        s3.add(new Month(9, 2013), 160.6);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        dataset.addSeries(s3);

        return dataset;
    }

    public JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
//      panel.setFillZoomRectangle(true);
//      panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(600, 300));
        return panel;
    }
}