package dhbw.ka.mwi.businesshorizon2.cf;


public class APV implements CFAlgorithm<APVResult> {

	/**
	 * getUWFiktiv berechnet den Unternehmenswert eines fiktiv unverschuldeten Unternehmens mit Hilfe der übergebenen Parameter
	 * 
	 * @param parameter enthält alle Parameter zur Bestimmung des Unternehmenswerts
	 * @param periode gibt die Periode an für den der Unternehmenswert zu berechnen ist
	 * @return gibt den Unternehmenswert eines fiktiv unverschuldeten Unternehmen der definierten Periode als double Wert zurück
	 */
    private static double getUWFiktiv(final CFParameter parameter, final int periode) {
        double summe = 0;
        for (int i = periode + 1; i < parameter.numPerioden() - 1; i++) {
            summe += parameter.getFCF()[i] / Math.pow(1 + parameter.getEKKosten(), i - periode);
        }
        return summe + parameter.getFCF()[parameter.numPerioden() - 1] /  (parameter.getEKKosten() * Math.pow(1 + parameter.getEKKosten(), parameter.numPerioden() - 2 - periode));
    }

    @Override
    /**
     * calculateUWert überschreibt die Methode aus dem implementierten Interface
     * calculateUWert berechnet den Unternehmenswert mittels APV Verfahren
     * 
     * @return gibt alle Parameter der Berechnung inklusive Unternehmenswert als APVResult zurück
     */
    public APVResult calculateUWert(final CFParameter parameter) {
        final double uwFiktiv = getUWFiktiv(parameter,0);
        final double taxShield = TaxShieldCalculator.calculateTaxShield(parameter,0);
        final double gk = uwFiktiv + taxShield;
        final double uWert = gk - parameter.getFK()[0];
        return new APVResult(uWert,uwFiktiv,taxShield,gk);
    }
}
