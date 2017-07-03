package dhbw.ka.mwi.businesshorizon2.cf;

/**
 * Diese Klasse CFParameter speichert die Parameter, die für die Berechnung der DCF-Methoden benötigt werden
 */
public class CFParameter {

    private final double[] FCF;
    private final double[] FTE;
    private final double[] FK;

    private final double EKKosten;
    private final double uSteusatz;
    private final double FKKosten;

    /**
     * Konstruktor um das CFParameter Objekt zu erstellen 
     * CFParameter enthält die Mindestanzahl an Parameter die benötigt werden um die DCF Verfahren zu berechnen
     * Alle Werte sind final, da die einzelnen Werte nicht verändert werden dürfen; daher auch keine Setter Methoden
     * 
     * @param FCF speichert alle Free Cashflow Werte in einem double Array; pro Arrayfeld wird ein Periodenbestand gespeichert
     * @param FK speichert alle Fremdkapitalbestände in einem double Array; pro Arrayfeld wird ein Periodenbestand gespeichert
     * @param EKKosten speichert den Eigenkapitalkostensatz als double Wert
     * @param uSteusatz speichert den Unternehmenssteuersatz als double Wert
     * @param FKKosten speichert den Fremdkapitalkostensatz als double Wert
     */
    public CFParameter(final double[] FCF, final double[] FK, final double EKKosten, final double uSteusatz, final double FKKosten) {
    	if(FK[FK.length - 1] != FK[FK.length - 2]){
    		throw new IllegalArgumentException("Letzte beide FKs müssen identisch sein");
    	}
        if(FK.length != FCF.length){
            throw new IllegalArgumentException("FK und FCF haben eine unterschiedliche länge");
        }
        this.FCF = FCF;
        this.FK = FK;
        this.EKKosten = EKKosten;
        this.uSteusatz = uSteusatz;
        this.FKKosten = FKKosten;
        FTE = calculateFTE();
    }

    /**
     * Berechnet aus den Free Cashflows die Flow to Equitys (Zahlungen an den Eigenkapitalgeber)
     * 
     * @return gibt die berechneten FTE Werte als Array zurück
     */
    private double[] calculateFTE(){
        final double[] FTE = new double[FCF.length];
        for (int i = 1; i < FCF.length; i++) {
            final double zinsen =  FK[i - 1] * FKKosten;
            final double taxShield = uSteusatz * zinsen;
            FTE[i] = FCF[i] + taxShield - zinsen + (FK[i] - FK[i - 1]);
        }
        return FTE;
    }


    /**
     * Getter Methode um die Fremdkapitalbestände zu erhalten
     * 
     * @return gibt die Fremdkapitalbestände als double Array zurück
     */
    public double[] getFK() {
        return FK;
    }

    /**
     * Getter Methode um den Eigenkapitalkostensatz zu erhalten
     * 
     * @return gibt den Eigenkapitalkostensatz als double Wert zurück
     */
    public double getEKKosten() {
        return EKKosten;
    }

    /**
     * Getter Methode um den Unternehmenssteuersatz zu erhalten
     * 
     * @return gibt den Unternehmenssteuersatz als double Wert zurück
     */
    public double getuSteusatz() {
        return uSteusatz;
    }

    /**
     * Getter Methode um den Fremdkapitalkostensatz zu erhalten
     * 
     * @return gibt den Fremdkapitalkostensatz als double Wert zurück
     */
    public double getFKKosten() {
        return FKKosten;
    }

    /**
     * Getter Methode um die Anzahl der zukünftigen Perioden zu erhalten
     * Im Konstruktor wurde schon überprüft ob die FCFs und die FKs die gleiche Anzahl Perioden haben
     * 
     * @return gibt die Anzahl der Perioden als integer Wert zurück
     */
    public int numPerioden(){
        return FK.length;
    }

    /**
     * Getter Methode um die Free Cashflows zu erhalten
     * 
     * @return gibt die Free Cashflows als double Array zurück
     */
    public double[] getFCF() {
        return FCF;
    }

    /**
     * Getter Methode um die berechneten FTE Werte zu erhalten
     * 
     * @return gibt die berechneten FTE Werte als double Array zurück
     */
    public double[] getFTE() {
        return FTE;
    }
}
