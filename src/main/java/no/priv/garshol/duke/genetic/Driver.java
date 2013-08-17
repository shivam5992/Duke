
package no.priv.garshol.duke.genetic;

import java.io.IOException;
import org.xml.sax.SAXException;

import no.priv.garshol.duke.Duke;
import no.priv.garshol.duke.ConfigLoader;
import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.utils.CommandLineParser;

// Keep answers from user
//   (so that if rerunning don't have to answer again)
// Multithreading

/**
 * Command-line interface to the genetic algorithm.
 */
public class Driver {

  public static void main(String[] argv) throws IOException, SAXException {
    // parse command-line
    CommandLineParser parser = new CommandLineParser();
    parser.setMinimumArguments(1);
    parser.setMaximumArguments(1);
    parser.addStringOption("testfile", 'T');
    parser.addBooleanOption("scientific", 's');
    parser.addStringOption("generations", 'G');
    parser.addStringOption("population", 'P');
    parser.addStringOption("questions", 'Q');
    parser.addStringOption("output", 'O');

    try {
      argv = parser.parse(argv);
    } catch (CommandLineParser.CommandLineParserException e) {
      System.err.println("ERROR: " + e.getMessage());
      usage();
      System.exit(1);
    }

    String testfile = parser.getOptionValue("testfile");
    if (parser.getOptionState("scientific") && testfile == null) {
      System.err.println("ERROR: scientific mode requires a test file");
      System.exit(1);
    }

    // get started
    Configuration config = ConfigLoader.load(argv[0]);
    GeneticAlgorithm genetic =
      new GeneticAlgorithm(config, testfile,
                           parser.getOptionState("scientific"));
    genetic.setPopulation(parser.getOptionInteger("population", 100));
    genetic.setGenerations(parser.getOptionInteger("generations", 100));
    genetic.setQuestions(parser.getOptionInteger("questions", 10));
    genetic.setConfigOutput(parser.getOptionValue("output"));
    genetic.run();
  }

  private static void usage() {
    System.out.println("");
    System.out.println("java no.priv.garshol.duke.genetic.Driver [options] <cfgfile>");
    System.out.println("");
    System.out.println("  --testfile=<file>     use a test file for evaluation");
    System.out.println("  --scientific          test active learning");
    System.out.println("  --generations=N       number of generations to run (100)");
    System.out.println("  --population=N        number of configurations in population (100)");
    System.out.println("  --questions=N         questions to ask per generation (10)");
    System.out.println("  --output=<file>       file to write best configuration to");
    System.out.println("                        (a new export after every generation)");
    System.out.println("");
    System.out.println("Duke version " + Duke.getVersionString());
  }
  
}