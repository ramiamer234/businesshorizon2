package dhbw.ka.mwi.businesshorizon2.demo.calc;

import dhbw.ka.mwi.businesshorizon2.ar.AR;
import dhbw.ka.mwi.businesshorizon2.ar.model.ARModel;
import dhbw.ka.mwi.businesshorizon2.ar.trendy.TrendRemovedTimeSeries;
import dhbw.ka.mwi.businesshorizon2.ar.trendy.TrendRemover;
import dhbw.ka.mwi.businesshorizon2.demo.FCFMode;
import dhbw.ka.mwi.businesshorizon2.demo.converter.ModelToArrayConverter;
import dhbw.ka.mwi.businesshorizon2.demo.ui.CompanyPanel;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

class DetailFCFSupplier implements Supplier<double[]> {

    private static final class CF {
        private final TrendRemovedTimeSeries timeSeries;
        private final FCFMode fcfMode;
        private final ARModel model;

        private CF(final TrendRemovedTimeSeries timeSeries, final FCFMode fcfMode, final ARModel model) {
            this.timeSeries = timeSeries;
            this.fcfMode = fcfMode;
            this.model = model;
        }

        private TrendRemovedTimeSeries getTimeSeries() {
            return timeSeries;
        }

        private FCFMode getFcfMode() {
            return fcfMode;
        }

        private ARModel getModel() {
            return model;
        }
    }
    private final Collection<CF> cfs = new ArrayList<>();
    private final int numPeriods;

    DetailFCFSupplier(final CompanyPanel companyPanel, final int numPeriods){
        final DefaultTableModel detailModel = companyPanel.getDetailModel();
        for (int row = 0; row < detailModel.getRowCount(); row++) {
            final TrendRemovedTimeSeries timeSeries = TrendRemover.removeTrend(ModelToArrayConverter.getRow(detailModel,row,2));
            cfs.add(new CF(timeSeries, (FCFMode) detailModel.getValueAt(row, 1), AR.getModel(timeSeries.getTimeSeriesWithoutTrend())));
        }

        this.numPeriods = numPeriods;
    }

    @Override
    public double[] get() {
        final double[] fcf = new double[numPeriods];

        for (final CF cf : cfs) {
            final double[] cfPrediction = cf.getTimeSeries().getTimeSeriesWithTrend(cf.getModel().predict(numPeriods));
            add(fcf,cfPrediction,cf.getFcfMode());
        }

        return fcf;
    }

    private static void add(final double[] fcf, final double[] cf, final FCFMode mode){
        for (int i = 0; i < fcf.length; i++) {
            switch (mode){
                case EINNAHMEN:
                    fcf[i] += cf[i];
                    break;
                case AUSGABEN:
                    fcf[i] -= cf[i];
                    break;
            }

        }
    }
}
