package sdk.sample;

import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.Group;
import org.pentaho.reporting.engine.classic.core.ItemBand;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.RelationalGroup;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.filter.types.LabelType;
import org.pentaho.reporting.engine.classic.core.filter.types.TextFieldType;
import org.pentaho.reporting.engine.classic.core.modules.gui.base.PreviewDialog;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.JndiConnectionProvider;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.SQLReportDataFactory;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.TextStyleKeys;

import javax.swing.table.DefaultTableModel;

public class GroupsSample {
  public static void main(String[] args) {
    ClassicEngineBoot.getInstance().start();

    MasterReport report = new MasterReport();
    report.setDataFactory(createDataFactory());
    report.setQuery("myQuery");
    report.getReportHeader().addElement(createLabel("Start Report"));
    report.getReportHeader().getStyle().setStyleProperty(TextStyleKeys.BOLD, true);
    report.getReportHeader().getStyle().setStyleProperty(TextStyleKeys.ITALIC, true);
    report.getReportFooter().addElement(createLabel("End of Report"));
    report.getReportFooter().getStyle().setStyleProperty(TextStyleKeys.BOLD, true);
    report.getReportFooter().getStyle().setStyleProperty(TextStyleKeys.ITALIC, true);


    RelationalGroup g = new RelationalGroup();
    g.setName("country-group");
    g.addField("COUNTRY");
    g.getHeader().getStyle().setStyleProperty(TextStyleKeys.BOLD, true);
    g.getHeader().setLayout(BandStyleKeys.LAYOUT_ROW);
    g.getHeader().addElement(createLabel("Start Country: "));
    g.getHeader().addElement(createTextField("COUNTRY"));
    g.getFooter().setLayout(BandStyleKeys.LAYOUT_ROW);
    g.getFooter().getStyle().setStyleProperty(TextStyleKeys.BOLD, true);
    g.getFooter().addElement(createLabel("End Country: "));
    g.getFooter().addElement(createTextField("COUNTRY"));

    report.addGroup(g);

    ItemBand itemBand = report.getItemBand();
    itemBand.addElement(createTextField("CITY"));


    PreviewDialog d = new PreviewDialog(report);
    d.setModal(true);
    d.pack();
    d.setVisible(true);

    System.exit(0);

  }

  private static Element createTextField(String fieldName) {
    Element e = new Element();
    e.setElementType(TextFieldType.INSTANCE);
    e.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.FIELD, fieldName);
    e.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.NULL_VALUE, "<NULL>");
    e.getStyle().setStyleProperty(ElementStyleKeys.WIDTH, 100f);
    return e;
  }

  private static Element createLabel(String text) {
    Element e = new Element();
    e.setElementType(LabelType.INSTANCE);
    e.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE, text);
    e.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.NULL_VALUE, "<NULL>");
    e.getStyle().setStyleProperty(ElementStyleKeys.WIDTH, 100f);
    return e;
  }

  private static DataFactory createDataFactory() {

    DefaultTableModel model = new DefaultTableModel(new Object[][] {
            new Object[]{ "France", "Paris"},
            new Object[]{ "France", "Lyon"},
            new Object[]{ "UK", "London"},
            new Object[]{ "UK", "Manchester"},
            new Object[]{ "UK", "Cambridge"},
            new Object[]{ "UK", "Oxford"},
    }, new String[]{"COUNTRY", "CITY"});

    TableDataFactory dataFactory = new TableDataFactory();
    dataFactory.addTable("myQuery", model);
    return dataFactory;
  }
}
