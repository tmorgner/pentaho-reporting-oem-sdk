package sdk.generate.patch;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterException;
import org.pentaho.reporting.libraries.repository.ContentIOException;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    OptionParser parser = new OptionParser();
    OptionSpec<String> source = parser.accepts("source").withRequiredArg().ofType(String.class).required().describedAs("Source File");
    OptionSpec<String> target = parser.accepts("target").withOptionalArg().ofType(String.class).required().describedAs("Target File");

    try {
      OptionSet parse = parser.parse(args);
      String sourceText = parse.valueOf(source);
      String targetText = parse.valueOf(target);

      PatchAReportSample.processReport(sourceText, targetText);

    } catch (OptionException oe) {
      parser.printHelpOn(System.out);
    } catch (ResourceException | ContentIOException | BundleWriterException | IOException e) {
      e.printStackTrace();
    }

  }
}
