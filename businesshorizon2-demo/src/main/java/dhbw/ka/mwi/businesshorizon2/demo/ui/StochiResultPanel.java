package dhbw.ka.mwi.businesshorizon2.demo.ui;

import dhbw.ka.mwi.businesshorizon2.demo.CFAlgo;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import javax.swing.*;
import java.awt.*;

public class StochiResultPanel extends JPanel {

    private final JLabel uWert = new JLabel("0", SwingConstants.CENTER);
    private final JButton calculate = new JButton("Berechnen");
    private final JButton export = new JButton("Exportieren");
    private final JFreeChart chart;
    private final JComboBox<CFAlgo> algo = new JComboBox<>(CFAlgo.values());
    private final JSpinner horizont = new JSpinner();
    private final JSpinner iter = new JSpinner();
    private final JSpinner grad = new JSpinner();
    private final JCheckBox trendy = new JCheckBox();

    private double[] lastResult = {};

    StochiResultPanel() {
        setLayout(new BorderLayout());

        final JPanel northPanel = new JPanel(new GridBagLayout());

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        uWert.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));

        c.gridx = 0;
        c.gridy = 0;
        northPanel.add(new JLabel("Algo"), c);

        c.gridx = 1;
        c.gridy = 0;
        northPanel.add(algo, c);

        c.gridx = 0;
        c.gridy = 2;
        northPanel.add(new JLabel("PrognosePerioden"), c);
        horizont.setModel(new SpinnerNumberModel(3, 2, 20, 1));

        c.gridx = 1;
        c.gridy = 2;
        northPanel.add(horizont, c);

        c.gridx = 0;
        c.gridy = 3;
        northPanel.add(new JLabel("Unternehmenswerte"), c);
        iter.setModel(new SpinnerNumberModel(10000, 1, 1000000, 1000));

        c.gridx = 1;
        c.gridy = 3;
        northPanel.add(iter, c);

        c.gridx = 0;
        c.gridy = 4;
        northPanel.add(new JLabel("Grad"), c);
        grad.setModel(new SpinnerNumberModel(2, 1, 10, 1));

        c.gridx = 1;
        c.gridy = 4;
        northPanel.add(grad, c);

        c.gridx = 0;
        c.gridy = 5;
        northPanel.add(new JLabel("Trend entfernen"), c);

        c.gridx = 1;
        c.gridy = 5;
        northPanel.add(trendy, c);

        c.gridx = 0;
        c.gridy = 6;
        northPanel.add(calculate, c);

        c.gridx = 1;
        c.gridy = 6;
        northPanel.add(uWert, c);

        c.gridx = 0;
        c.gridy = 7;
        northPanel.add(export, c);

        add(northPanel, BorderLayout.NORTH);
        chart = ChartFactory.createHistogram("Histogram", "Euros", "Wahrscheinlichkeit", null, PlotOrientation.VERTICAL, true, true, true);
        add(new ChartPanel(chart));
    }

    JLabel getuWert() {
        return uWert;
    }

    JButton getCalculate() {
        return calculate;
    }

    JComboBox<CFAlgo> getAlgo() {
        return algo;
    }

    public JSpinner getIter() {
        return iter;
    }

    public JSpinner getHorizont() {
        return horizont;
    }

    JButton getExport() {
        return export;
    }

    double[] getLastResult() {
        return lastResult;
    }

    public void setLastResult(final double[] lastResult) {
        this.lastResult = lastResult;
        displayStochi(lastResult);
    }

    private void displayStochi(final double[] result) {
        final HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        dataset.addSeries("Unternehmenswert", result, 100);
        chart.getXYPlot().setDataset(dataset);
    }

    public JSpinner getGrad() {
        return grad;
    }

    public JCheckBox getTrendy() {
        return trendy;
    }
}
