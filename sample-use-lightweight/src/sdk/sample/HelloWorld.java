package sdk.sample;

import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportHeader;
import org.pentaho.reporting.engine.classic.core.filter.types.LabelType;
import org.pentaho.reporting.engine.classic.core.modules.gui.base.PreviewDialog;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.TextStyleKeys;

public class HelloWorld {
  public static void main(String[] args) {
    // always boot up the engine
    ClassicEngineBoot.getInstance().start();

    MasterReport report = new MasterReport();
    ReportHeader reportHeader = report.getReportHeader();
    reportHeader.addElement(createContainer());

    PreviewDialog d = new PreviewDialog(report);
    d.setModal(true);
    d.pack();
    d.setVisible(true);

    System.exit(0);
  }

  private static Band createContainer() {
    Band band = new Band();
    band.setLayout(BandStyleKeys.LAYOUT_ROW);
    band.getStyle().setStyleProperty(ElementStyleKeys.WIDTH, -100f);
    band.addElement(createLabel("Hello"));
    band.addElement(createLabel("+"));
    band.addElement(createLabel("World"));
    return band;
  }

  private static Element createLabel(String text) {
    Element e = new Element();
    e.setElementType(LabelType.INSTANCE);
    e.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE, text);
    e.getStyle().setStyleProperty(ElementStyleKeys.WIDTH, 100f);
    e.getStyle().setStyleProperty(TextStyleKeys.FONT, "Serif");
    e.getStyle().setStyleProperty(TextStyleKeys.BOLD, true);
    e.getStyle().setStyleProperty(TextStyleKeys.FONTSIZE, 12);
    return e;
  }
}
