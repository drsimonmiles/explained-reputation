/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr;

import jaspr.domain.Agent;
import jaspr.domain.AgentPreferences;
import jaspr.domain.ReputationType;
import jaspr.domain.Term;
import jaspr.explanation.Explanation;
import jaspr.explanation.ExplanationGenerator;
import jaspr.explanation.ijcai.IJCAIExplanationGenerator;
import jaspr.fire.RecencyWeightFunction;
import jaspr.fire.TrustScore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author ingridnunes
 *
 */
public class JasprApp {

	private static Log log = LogFactory.getLog(JasprApp.class);

	private static void decisiveCriteriaTest() {
		/* Agents */
		Agent a = new Agent("A");
		Agent b = new Agent("B");
		Agent c = new Agent("C");

		/* Terms */
		Term quality = new Term("quality");
		Term timeliness = new Term("timeliness");
		Term cost = new Term("cost");
		Term costumerService = new Term("costumerService");
		Term ecoFriendly = new Term("ecoFriendly");

		/* A's Preferences */
		AgentPreferences preferences = new AgentPreferences();
		preferences.setLambda(0.1);
		preferences.put(ReputationType.R, 1.0);
		preferences.put(quality, 0.35);
		preferences.put(timeliness, 0.05);
		preferences.put(cost, 0.20);
		preferences.put(costumerService, 0.10);
		preferences.put(ecoFriendly, 0.3);
		a.setPreferences(preferences);

		/* B Ratings (from A) */
		a.addRating(b, quality, ReputationType.R, 0.4, 1.0);
		a.addRating(b, timeliness, ReputationType.R, 0.6, 1.0);
		a.addRating(b, cost, ReputationType.R, 0.7, 1.0);
		a.addRating(b, costumerService, ReputationType.R, 0.7, 1.0);
		a.addRating(b, ecoFriendly, ReputationType.R, 0.9, 1.0);

		/* C Ratings (from A) */
		a.addRating(c, quality, ReputationType.R, 0.3, 1.0);
		a.addRating(c, timeliness, ReputationType.R, 0.75, 1.0);
		a.addRating(c, cost, ReputationType.R, 0.6, 1.0);
		a.addRating(c, costumerService, ReputationType.R, 0.9, 1.0);
		a.addRating(c, ecoFriendly, ReputationType.R, 0.7, 1.0);

		/* Running test */
		runTest(a, b, c, "Decisive Criteria: DC(B,C) = quality, ecoFriendly");
	}

	private static void dominationWithDCTest() {
		/* Agents */
		Agent a = new Agent("A");
		Agent b = new Agent("B");
		Agent c = new Agent("C");

		/* Terms */
		Term quality = new Term("quality");
		Term timeliness = new Term("timeliness");
		Term cost = new Term("cost");

		/* A's Preferences */
		AgentPreferences preferences = new AgentPreferences();
		preferences.setLambda(0.1);
		preferences.put(ReputationType.R, 1.0);
		preferences.put(quality, 0.45);
		preferences.put(timeliness, 0.35);
		preferences.put(cost, 0.2);
		a.setPreferences(preferences);

		/* B Ratings (from A) */
		a.addRating(b, quality, ReputationType.R, 0.9, 1.0);
		a.addRating(b, timeliness, ReputationType.R, 0.55, 1.0);
		a.addRating(b, cost, ReputationType.R, 0.4, 1.0);

		/* C Ratings (from A) */
		a.addRating(c, quality, ReputationType.R, 0.6, 1.0);
		a.addRating(c, timeliness, ReputationType.R, 0.4, 1.0);
		a.addRating(c, cost, ReputationType.R, 0.3, 1.0);

		/* Running test */
		runTest(a, b, c, "Domination (decisive criteria): DC(B,C) = quality");
	}

	private static void dominationWithNoDCTest() {
		/* Agents */
		Agent a = new Agent("A");
		Agent b = new Agent("B");
		Agent c = new Agent("C");

		/* Terms */
		Term quality = new Term("quality");
		Term timeliness = new Term("timeliness");
		Term cost = new Term("cost");

		/* A's Preferences */
		AgentPreferences preferences = new AgentPreferences();
		preferences.setLambda(0.1);
		preferences.put(ReputationType.R, 1.0);
		preferences.put(quality, 0.45);
		preferences.put(timeliness, 0.35);
		preferences.put(cost, 0.2);
		a.setPreferences(preferences);

		/* B Ratings (from A) */
		a.addRating(b, quality, ReputationType.R, 0.4, 1.0);
		a.addRating(b, timeliness, ReputationType.R, 0.55, 1.0);
		a.addRating(b, cost, ReputationType.R, 0.9, 1.0);

		/* C Ratings (from A) */
		a.addRating(c, quality, ReputationType.R, 0.3, 1.0);
		a.addRating(c, timeliness, ReputationType.R, 0.4, 1.0);
		a.addRating(c, cost, ReputationType.R, 0.6, 1.0);

		/* Running test */
		runTest(a, b, c, "Domination (no decisive criteria): DC(B,C) = all");
	}

	private static void invert() {
		/* Agents */
		Agent a = new Agent("A");
		Agent b = new Agent("B");
		Agent c = new Agent("C");
		Agent d = new Agent("C");

		/* Terms */
		Term quality = new Term("quality");

		/* A's Preferences */
		AgentPreferences preferences = new AgentPreferences();
		preferences.setLambda(0.5);
		preferences.put(ReputationType.I, 0.4);
		preferences.put(ReputationType.W, 0.2);
		preferences.put(ReputationType.C, 0.4);
		preferences.put(quality, 1.0);
		a.setPreferences(preferences);

		/* B Ratings (from A) */
		a.addRating(b, quality, ReputationType.I, 1.0, 1l);
		a.addRating(d, b, quality, ReputationType.W, 0.0, 1l);
		a.addRating(b, quality, ReputationType.C, 1.0, 1l);

		/* C Ratings (from A) */
		a.addRating(c, quality, ReputationType.I, 0.0, 1l);
		a.addRating(d, c, quality, ReputationType.W, 1.0, 1l);
		a.addRating(c, quality, ReputationType.C, 0.6, 1l);

		/* Running test */
		runTest(a, b, c, "IVT: pi = { (I,W) }");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure(JasprApp.class
				.getResource("log4j.properties"));
		/* Tests */
		dominationWithNoDCTest();
		dominationWithDCTest();
		decisiveCriteriaTest();
		tradeOffResolutionTest();
		recencyOverall();
		invert();
		recency();
		randomTest();
	}

	private static void randomTest() {
		/* Agents */
		Agent a = new Agent("A");
		Agent b = new Agent("B");
		Agent c = new Agent("C");
		Agent d = new Agent("D");

		/* Terms */
		Term quality = new Term("quality");
		Term timeliness = new Term("timeliness");
		Term cost = new Term("cost");

		/* A's Preferences */
		AgentPreferences preferences = new AgentPreferences();
		preferences.setLambda(0.5);
		preferences.put(ReputationType.I, 0.4);
		preferences.put(ReputationType.W, 0.3);
		preferences.put(ReputationType.R, 0.15);
		preferences.put(ReputationType.C, 0.15);
		preferences.put(quality, 0.4);
		preferences.put(timeliness, 0.35);
		preferences.put(cost, 0.25);
		a.setPreferences(preferences);

		/* B Ratings (from A) */
		a.addRating(b, quality, ReputationType.I, 0.3, 1l);
		a.addRating(b, timeliness, ReputationType.I, 0.55, 1l);
		a.addRating(b, cost, ReputationType.I, 0.6, 1l);
		a.addRating(b, quality, ReputationType.I, 0.4, 3l);
		a.addRating(b, timeliness, ReputationType.I, 0.55, 3l);
		a.addRating(b, cost, ReputationType.I, 0.6, 3l);
		a.addRating(b, quality, ReputationType.I, 0.7, 5l);
		a.addRating(b, timeliness, ReputationType.I, 0.55, 5l);
		a.addRating(b, cost, ReputationType.I, 0.5, 5l);
		a.addRating(b, quality, ReputationType.I, 0.9, 9l);
		a.addRating(b, timeliness, ReputationType.I, 0.55, 9l);
		a.addRating(b, cost, ReputationType.I, 0.45, 9l);

		/* B Reputation Values */
		a.addRating(d, b, quality, ReputationType.W, 0.5, 9l);
		a.addRating(b, quality, ReputationType.R, 0.4, 0.3);
		a.addRating(b, quality, ReputationType.C, 0.3, 7l);
		a.addRating(d, b, timeliness, ReputationType.W, 0.4, 9l);
		a.addRating(b, timeliness, ReputationType.R, 0.55, 0.25);
		a.addRating(b, timeliness, ReputationType.C, 0.6, 7l);
		a.addRating(d, b, cost, ReputationType.W, 0.8, 9l);
		a.addRating(b, cost, ReputationType.R, 0.3, 0.8);
		a.addRating(b, cost, ReputationType.C, 0.6, 7l);

		/* C Ratings (from A) */
		a.addRating(c, quality, ReputationType.I, 0.6, 1l);
		a.addRating(c, timeliness, ReputationType.I, 0.7, 1l);
		a.addRating(c, cost, ReputationType.I, 0.55, 1l);
		a.addRating(c, quality, ReputationType.I, 0.55, 3l);
		a.addRating(c, timeliness, ReputationType.I, 0.75, 3l);
		a.addRating(c, cost, ReputationType.I, 0.45, 3l);
		a.addRating(c, quality, ReputationType.I, 0.6, 5l);
		a.addRating(c, timeliness, ReputationType.I, 0.7, 5l);
		a.addRating(c, cost, ReputationType.I, 0.5, 5l);
		a.addRating(c, quality, ReputationType.I, 0.4, 9l);
		a.addRating(c, timeliness, ReputationType.I, 0.3, 9l);
		a.addRating(c, cost, ReputationType.I, 0.7, 9l);

		/* C Reputation Values */
		a.addRating(d, c, quality, ReputationType.W, 0.7, 9l);
		a.addRating(c, quality, ReputationType.R, 0.5, 0.3);
		a.addRating(c, quality, ReputationType.C, 0.6, 7l);
		a.addRating(d, c, timeliness, ReputationType.W, 0.8, 9l);
		a.addRating(c, timeliness, ReputationType.R, 0.3, 0.7);
		a.addRating(c, timeliness, ReputationType.C, 0.5, 7l);
		a.addRating(d, c, cost, ReputationType.W, 0.3, 9l);
		a.addRating(c, cost, ReputationType.R, 0.4, 0.55);
		a.addRating(c, cost, ReputationType.C, 0.4, 7l);

		/* Running test */
		runTest(a, b, c, "Random Test");
	}

	private static void recency() {
		/* Agents */
		Agent a = new Agent("A");
		Agent b = new Agent("B");
		Agent c = new Agent("C");

		/* Terms */
		Term quality = new Term("quality");
		Term cost = new Term("cost");

		/* A's Preferences */
		AgentPreferences preferences = new AgentPreferences();
		preferences.setLambda(0.5);
		preferences.put(ReputationType.I, 1.0);
		preferences.put(quality, 0.5);
		preferences.put(cost, 0.5);
		a.setPreferences(preferences);

		/* B Ratings (from A) */
		a.addRating(b, quality, ReputationType.I, 0.3, 1l);
		a.addRating(b, quality, ReputationType.I, 0.4, 3l);
		a.addRating(b, quality, ReputationType.I, 0.7, 5l);
		a.addRating(b, quality, ReputationType.I, 0.9, 9l);
		a.addRating(b, cost, ReputationType.I, 0.8, 6l);
		a.addRating(b, cost, ReputationType.I, 0.6, 7l);
		a.addRating(b, cost, ReputationType.I, 0.5, 8l);
		a.addRating(b, cost, ReputationType.I, 0.9, 9l);

		/* C Ratings (from A) */
		a.addRating(c, quality, ReputationType.I, 0.9, 1l);
		a.addRating(c, quality, ReputationType.I, 0.8, 3l);
		a.addRating(c, quality, ReputationType.I, 0.4, 5l);
		a.addRating(c, quality, ReputationType.I, 0.3, 9l);
		a.addRating(c, cost, ReputationType.I, 0.6, 6l);
		a.addRating(c, cost, ReputationType.I, 0.55, 7l);
		a.addRating(c, cost, ReputationType.I, 0.6, 8l);
		a.addRating(c, cost, ReputationType.I, 0.4, 9l);

		/* Running test */
		runTest(a, b, c, "Recency (quality)");
	}

	private static void recencyOverall() {
		/* Agents */
		Agent a = new Agent("A");
		Agent b = new Agent("B");
		Agent c = new Agent("C");

		/* Terms */
		Term quality = new Term("quality");
		Term cost = new Term("cost");

		/* A's Preferences */
		AgentPreferences preferences = new AgentPreferences();
		preferences.setLambda(0.5);
		preferences.put(ReputationType.I, 1.0);
		preferences.put(quality, 0.5);
		preferences.put(cost, 0.5);
		a.setPreferences(preferences);

		/* B Ratings (from A) */
		a.addRating(b, quality, ReputationType.I, 0.1, 1l);
		a.addRating(b, quality, ReputationType.I, 0.4, 3l);
		a.addRating(b, quality, ReputationType.I, 0.7, 5l);
		a.addRating(b, quality, ReputationType.I, 0.9, 9l);
		a.addRating(b, cost, ReputationType.I, 0.5, 6l);
		a.addRating(b, cost, ReputationType.I, 0.6, 7l);
		a.addRating(b, cost, ReputationType.I, 0.9, 8l);
		a.addRating(b, cost, ReputationType.I, 0.8, 9l);

		/* C Ratings (from A) */
		a.addRating(c, quality, ReputationType.I, 0.9, 1l);
		a.addRating(c, quality, ReputationType.I, 0.8, 3l);
		a.addRating(c, quality, ReputationType.I, 0.4, 5l);
		a.addRating(c, quality, ReputationType.I, 0.3, 9l);
		a.addRating(c, cost, ReputationType.I, 0.9, 3l);
		a.addRating(c, cost, ReputationType.I, 0.8, 5l);
		a.addRating(c, cost, ReputationType.I, 0.6, 7l);
		a.addRating(c, cost, ReputationType.I, 0.4, 9l);

		/* Running test */
		runTest(a, b, c, "Recency Overall (quality)");
	}

	private static void runTest(Agent a, Agent b, Agent c, String msg) {
		/* Trust Calculation */
		TrustScore trustB = new TrustScore(a, b,
				RecencyWeightFunction.DEFAULT_CURRENT_TIME);
		TrustScore trustC = new TrustScore(a, c,
				RecencyWeightFunction.DEFAULT_CURRENT_TIME);

		/* Explanation Generation */
		ExplanationGenerator explanationGenerator = new IJCAIExplanationGenerator();
		Explanation explanation = explanationGenerator.generateExplanation(
				trustB, trustC);

		/* Printing Results */
		log.info(msg);
		log.info(trustB);
		log.info(trustC);
		log.info(explanation);
		log.debug(trustB.toStringDetailed());
		log.debug(trustC.toStringDetailed());
	}

	private static void tradeOffResolutionTest() {
		/* Agents */
		Agent a = new Agent("A");
		Agent b = new Agent("B");
		Agent c = new Agent("C");

		/* Terms */
		Term quality = new Term("quality");
		Term timeliness = new Term("timeliness");
		Term cost = new Term("cost");
		Term costumerService = new Term("costumerService");
		Term ecoFriendly = new Term("ecoFriendly");

		/* A's Preferences */
		AgentPreferences preferences = new AgentPreferences();
		preferences.setLambda(0.1);
		preferences.put(ReputationType.R, 1.0);
		preferences.put(quality, 0.35);
		preferences.put(timeliness, 0.05);
		preferences.put(cost, 0.20);
		preferences.put(costumerService, 0.10);
		preferences.put(ecoFriendly, 0.3);
		a.setPreferences(preferences);

		/* B Ratings (from A) */
		a.addRating(b, quality, ReputationType.R, 0.4, 1.0);
		a.addRating(b, timeliness, ReputationType.R, 0.1, 1.0);
		a.addRating(b, cost, ReputationType.R, 0.7, 1.0);
		a.addRating(b, costumerService, ReputationType.R, 0.7, 1.0);
		a.addRating(b, ecoFriendly, ReputationType.R, 0.9, 1.0);

		/* C Ratings (from A) */
		a.addRating(c, quality, ReputationType.R, 0.3, 1.0);
		a.addRating(c, timeliness, ReputationType.R, 0.95, 1.0);
		a.addRating(c, cost, ReputationType.R, 0.6, 1.0);
		a.addRating(c, costumerService, ReputationType.R, 0.9, 1.0);
		a.addRating(c, ecoFriendly, ReputationType.R, 0.7, 1.0);

		/* Running test */
		runTest(a, b, c,
				"Decisive Criteria: DC(B,C) = <{quality, ecoFriendly}, {timeliness}>");
	}

}
