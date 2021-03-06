package dhbw.ka.mwi.businesshorizon2.demo.ui;

import dhbw.ka.mwi.businesshorizon2.cf.*;
import dhbw.ka.mwi.businesshorizon2.demo.CFAlgo;
import dhbw.ka.mwi.businesshorizon2.demo.calc.CFCalculator;
import dhbw.ka.mwi.businesshorizon2.demo.models.CompanyModelProvider;
import dhbw.ka.mwi.businesshorizon2.demo.saving.CsvExport;
import dhbw.ka.mwi.businesshorizon2.demo.saving.CsvImport;
import dhbw.ka.mwi.businesshorizon2.demo.saving.ExportListener;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow extends JFrame {

    private final CompanyPanel company;
    private final SzenarioPanel szenario;
    private final StochiResultPanel stochiResultPanel;
    private final DeterResultPanel deterResultPanel;
    private final HeaderPanel header;

    public MainWindow() {
        setTitle("Business Horizon Demo");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        final JTabbedPane tab = new JTabbedPane();
        add(tab);

        header = new HeaderPanel();
        tab.addTab("Kopf", header);

        company = new CompanyPanel(header);
        tab.addTab("Unternehmenswerte", company);

        szenario = new SzenarioPanel();
        tab.addTab("Szenario", szenario);

        deterResultPanel = new DeterResultPanel();
        tab.addTab("Unternehmenswert", deterResultPanel);

        stochiResultPanel = new StochiResultPanel();

        final ChangeListener yearAndPeriodenListener = e -> {
            company.setModel(CompanyModelProvider.getModel((Integer) header.getBasisjahr().getValue(), (Integer) header.getPerioden().getValue(), header.getCurrentMode(), company.getDetailMode()));
            company.setDetailModel(CompanyModelProvider.getDetailModel((Integer) header.getBasisjahr().getValue(), (Integer) header.getPerioden().getValue(), header.getCurrentMode()));
        };

        header.getPerioden().addChangeListener(yearAndPeriodenListener);
        header.getBasisjahr().addChangeListener(yearAndPeriodenListener);

        header.getStochi().addActionListener(e -> {
            company.setModel(CompanyModelProvider.getModel((Integer) header.getBasisjahr().getValue(), (Integer) header.getPerioden().getValue(), header.getCurrentMode(), company.getDetailMode()));
            company.setDetailModel(CompanyModelProvider.getDetailModel((Integer) header.getBasisjahr().getValue(), (Integer) header.getPerioden().getValue(), header.getCurrentMode()));
            setTabs(tab);
        });

        header.getDeter().addActionListener(e -> {
            company.setModel(CompanyModelProvider.getModel((Integer) header.getBasisjahr().getValue(), (Integer) header.getPerioden().getValue(), header.getCurrentMode(), company.getDetailMode()));
            company.setDetailModel(CompanyModelProvider.getDetailModel((Integer) header.getBasisjahr().getValue(), (Integer) header.getPerioden().getValue(), header.getCurrentMode()));
            setTabs(tab);
        });

        header.getLoad().addActionListener(e -> {
            final JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    CsvImport.importCSV(chooser.getSelectedFile(), header, company, szenario);
                    setTabs(tab);
                } catch (final Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Datei kann nicht importiert werden: " + e1.getLocalizedMessage());
                }
            }
        });
        header.getSave().addActionListener(new ExportListener(file -> CsvExport.export(header, szenario, company, file)));

        stochiResultPanel.getCalculate().addActionListener(e -> {
            try {
                final long was = System.nanoTime();
                final double[] uWerts = CFCalculator.calculateStochi(company, szenario, stochiResultPanel, (CFAlgo) deterResultPanel.getAlgo().getSelectedItem());
                final double uWert = CFCalculator.avg(uWerts);
                System.out.println("Dauer Stochi:" + (System.nanoTime() - was) / 1000000 + " ms");
                stochiResultPanel.setLastResult(uWerts);
                stochiResultPanel.getuWert().setText(String.valueOf(uWert));
            } catch (final Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler bei der Berechnung: " + e1.getLocalizedMessage());
            }
        });

        deterResultPanel.getCalculate().addActionListener(e -> {
            try {
                final CFParameter parameter = CFCalculator.getParameter(company, szenario);
                switch ((CFAlgo) deterResultPanel.getAlgo().getSelectedItem()) {
                    case APV:
                        final APVResult apvResult = new APV().calculateUWert(parameter);
                        deterResultPanel.displayAPV(apvResult, parameter.getFK()[0]);
                        deterResultPanel.getuWert().setText(String.valueOf(apvResult.getuWert()));
                        break;
                    case FCF:
                        final FCFResult fcfResult = new FCF().calculateUWert(parameter);
                        deterResultPanel.displayFCF(fcfResult, parameter.getFK()[0]);
                        deterResultPanel.getuWert().setText(String.valueOf(fcfResult.getuWert()));
                        break;
                    case FTE:
                        final CFResult fteResult = new FTE().calculateUWert(parameter);
                        deterResultPanel.displayFTE(fteResult);
                        deterResultPanel.getuWert().setText(String.valueOf(fteResult.getuWert()));
                        break;
                }

            } catch (final Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler bei der Berechnung: " + e1.getLocalizedMessage());
            }
        });

        deterResultPanel.getExport().addActionListener(new ExportListener(file -> CsvExport.exportResults(file, new double[]{Double.parseDouble(deterResultPanel.getuWert().getText())})));
        stochiResultPanel.getExport().addActionListener(new ExportListener(file -> CsvExport.exportResults(file, stochiResultPanel.getLastResult())));
    }

    private void setTabs(final JTabbedPane tab) {
        switch (header.getCurrentMode()) {
            case STOCHI:
                tab.remove(deterResultPanel);
                tab.addTab("Unternehmenswert", stochiResultPanel);
                break;
            case DETER:
                tab.remove(stochiResultPanel);
                tab.addTab("Unternehmenswert", deterResultPanel);
                break;
        }
    }

}
