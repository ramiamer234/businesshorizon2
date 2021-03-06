package dhbw.ka.mwi.businesshorizon2.tests.methods.timeseries;

import org.junit.Test;

import dhbw.ka.mwi.businesshorizon2.methods.timeseries.CalculateTide;
import dhbw.ka.mwi.businesshorizon2.methods.timeseries.Trendgerade;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
/**
 * Diese Klasse dient dazu die Erstellung der Trendgerade und Überprüfung auf Stationarität zu überprüfen.
 * Stationaritätstest wird weggelassen - bei korrektem Abzug der Trendgerade wird von Stationarität ausgegangen.
 * @author: Philipp Nagel, Jonathan Janke
*/

public class TestStationaritaetTrendgerade extends TestCase{
	
	private static final Logger logger = Logger.getLogger(CalculateTide.class);
	
	@Test public void testTrendGerade(){
		double[] zeitreihe = {1,2,3,4,5,6};
		Trendgerade gerade = new Trendgerade(zeitreihe);
		assertEquals(1.0, gerade.getM());
		assertEquals(0.0, gerade.getB());
		
		
		zeitreihe = new double[]{3,5,7};
		gerade = new Trendgerade(zeitreihe);
		assertEquals(2.0, gerade.getM());
		assertEquals(1.0, gerade.getB());
		
		double [] logZeitreihe = {138.61, 202.31, 174.41, 202.52};
		gerade=new Trendgerade(logZeitreihe);
		logger.info ("m: " + gerade.getM());
		logger.info ("b: " + gerade.getB());
		
		
	}
	
	@Test public void testStationaritaet(){
		
	}
	@Test public void testAutokovarianz(){
		double[] zeitreihe = {1,2,3,4};
		
		double autoKovarianz = Trendgerade.getAutoKoVarianz(1., 0., zeitreihe);
		assertEquals(autoKovarianz, 0.0);
	}
}
