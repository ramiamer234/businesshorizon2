package dhbw.ka.mwi.businesshorizon2.demo.ui;

import dhbw.ka.mwi.businesshorizon2.demo.CFMode;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class HeaderPanel extends JPanel {
    private final JLabel status = new JLabel();
    private final JRadioButton deter = new JRadioButton("Deterministisch");
    private final JRadioButton stochi = new JRadioButton("Stochastisch");

    private final JSpinner basisjahr = new JSpinner();
    private final JSpinner perioden = new JSpinner();
    private final JSpinner horizont = new JSpinner();
    private final JSpinner iter = new JSpinner();
    private final JPanel stochiPanel = new JPanel(new  GridBagLayout());

    HeaderPanel() {
        final JPanel innerPanel = new JPanel(new GridBagLayout());
        setLayout(new BorderLayout());
        add(innerPanel,BorderLayout.NORTH);
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        final ButtonGroup inputMethod = new ButtonGroup();
        inputMethod.add(deter);
        inputMethod.add(stochi);

        deter.setSelected(true);
        c.gridx = 0;
        c.gridy = 0;
        innerPanel.add(deter,c);
        c.gridx = 1;
        c.gridy = 0;
        innerPanel.add(stochi,c);



        c.gridx = 0;
        c.gridy = 1;
        innerPanel.add(new JLabel("Basisjahr"),c);
        basisjahr.setModel(new SpinnerNumberModel(new GregorianCalendar().get(Calendar.YEAR), 1900, 3000, 1));
        c.gridx = 1;
        c.gridy = 1;
        innerPanel.add(basisjahr,c);

        c.gridx = 0;
        c.gridy = 2;
        innerPanel.add(new JLabel("Perioden"),c);
        perioden.setModel(new SpinnerNumberModel(3, 3, 10, 1));
        c.gridx = 1;
        c.gridy = 2;
        innerPanel.add(perioden,c);

        stochiPanel.setVisible(false);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        innerPanel.add(stochiPanel,c);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        stochiPanel.add(new JLabel("PrognosePerioden"),c);
        horizont.setModel(new SpinnerNumberModel(3, 1, 10, 1));
        c.gridx = 1;
        c.gridy = 0;
        stochiPanel.add(horizont,c);

        c.gridx = 0;
        c.gridy = 1;
        stochiPanel.add(new JLabel("Iterationen"),c);
        iter.setModel(new SpinnerNumberModel(10000, 1, 100000, 1000));
        c.gridx = 1;
        c.gridy = 1;
        stochiPanel.add(iter,c);

        stochi.addChangeListener(e -> stochiPanel.setVisible(stochi.isSelected()));

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        innerPanel.add(status,c);

    }

    JLabel getStatus() {
        return status;
    }

    JSpinner getPerioden() {
        return perioden;
    }

    JRadioButton getDeter() {
        return deter;
    }

    JRadioButton getStochi() {
        return stochi;
    }

    public JSpinner getIter() {
        return iter;
    }

    CFMode getCurrentMode(){
        return deter.isSelected() ? CFMode.DETER : CFMode.STOCHI;
    }

    public JSpinner getHorizont() {
        return horizont;
    }

    JSpinner getBasisjahr() {
        return basisjahr;
    }
}