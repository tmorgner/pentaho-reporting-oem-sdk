package org.pentaho.reporting.sdk.sequence.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleDataFactoryWriterHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterException;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterState;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.docbundle.BundleUtilities;
import org.pentaho.reporting.libraries.docbundle.WriteableDocumentBundle;
import org.pentaho.reporting.libraries.xmlns.common.AttributeList;
import org.pentaho.reporting.libraries.xmlns.writer.DefaultTagDescription;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriterSupport;
import org.pentaho.reporting.sdk.sequence.SampleDataFactory;
import org.pentaho.reporting.sdk.sequence.SampleDataSourceModule;

public class SampleDataFactoryBundleWriterHandler implements BundleDataFactoryWriterHandler
{
  public static final String ROOT_ELEMENT_TAG = "sdk-datasource";

  public SampleDataFactoryBundleWriterHandler()
  {
  }

  public String writeDataFactory(final WriteableDocumentBundle bundle,
                                 final DataFactory rawDataFactory,
                                 final BundleWriterState state) throws IOException, BundleWriterException
  {
    final String fileName = BundleUtilities.getUniqueName(bundle, state.getFileName(), "datasources/sdk-ds{0}.xml");
    if (fileName == null)
    {
      throw new IOException("Unable to generate unique name for cda-Data-Source");
    }
    //TODO: refactor with CdaDataFactoryWriteHandler
    final OutputStream outputStream = bundle.createEntry(fileName, "text/xml");
    final DefaultTagDescription tagDescription = new DefaultTagDescription();
    final XmlWriter xmlWriter = new XmlWriter
        (new OutputStreamWriter(outputStream, "UTF-8"), tagDescription, "  ", "\n");

    final AttributeList rootAttrs = new AttributeList();
    rootAttrs.addNamespaceDeclaration("data", SampleDataSourceModule.NAMESPACE);

    xmlWriter.writeTag(SampleDataSourceModule.NAMESPACE, ROOT_ELEMENT_TAG, rootAttrs, XmlWriter.OPEN);

    final SampleDataFactory dataFactory = (SampleDataFactory) rawDataFactory;
    writeConfig(xmlWriter, dataFactory);
    writeGlobalScript(xmlWriter, dataFactory);
    writeQueries(xmlWriter, dataFactory);

    xmlWriter.writeCloseTag();
    xmlWriter.close();
    return fileName;
  }

  private void writeConfig(final XmlWriter xmlWriter, final SampleDataFactory dataFactory) throws IOException
  {
    final AttributeList configAttrs = new AttributeList();
    if (StringUtils.isEmpty(dataFactory.getQueryPattern()) == false)
    {
      configAttrs.setAttribute(SampleDataSourceModule.NAMESPACE, "url-pattern", dataFactory.getQueryPattern());
    }
    xmlWriter.writeTag(SampleDataSourceModule.NAMESPACE, "config", configAttrs, XmlWriterSupport.CLOSE);
  }

  private void writeGlobalScript(final XmlWriter xmlWriter, final SampleDataFactory dataFactory) throws IOException
  {
    final String globalScript = dataFactory.getGlobalScript();
    final String globalScriptLanguage = dataFactory.getGlobalScriptLanguage();
    if (StringUtils.isEmpty(globalScript) == false && StringUtils.isEmpty(globalScriptLanguage) == false)
    {
      xmlWriter.writeTag
          (SampleDataSourceModule.NAMESPACE, "global-script", "language", globalScriptLanguage, XmlWriterSupport.OPEN);
      xmlWriter.writeTextNormalized(globalScript, false);
      xmlWriter.writeCloseTag();
    }
  }

  private void writeQueries(final XmlWriter xmlWriter,
                            final SampleDataFactory dataFactory) throws IOException
  {
    final String globalScriptLanguage = dataFactory.getGlobalScriptLanguage();
    xmlWriter.writeTag(SampleDataSourceModule.NAMESPACE, "query-definitions", XmlWriterSupport.OPEN);
    final String[] queryNames = dataFactory.getQueryNames();
    for (int i = 0; i < queryNames.length; i++)
    {
      final String queryName = queryNames[i];
      final String query = dataFactory.getQuery(queryName);
      xmlWriter.writeTag(SampleDataSourceModule.NAMESPACE, "query", "name", queryName, XmlWriterSupport.OPEN);

      xmlWriter.writeTag(SampleDataSourceModule.NAMESPACE, "static-query", XmlWriterSupport.OPEN);
      xmlWriter.writeTextNormalized(query, false);
      xmlWriter.writeCloseTag();

      final String queryScriptLanguage = dataFactory.getScriptingLanguage(queryName);
      final String queryScript = dataFactory.getScript(queryName);

      if (StringUtils.isEmpty(queryScript) == false &&
          (StringUtils.isEmpty(queryScriptLanguage) == false || StringUtils.isEmpty(globalScriptLanguage) == false))
      {
        if (StringUtils.isEmpty(queryScriptLanguage))
        {
          xmlWriter.writeTag(SampleDataSourceModule.NAMESPACE, "script", XmlWriterSupport.OPEN);
        }
        else
        {
          xmlWriter.writeTag(SampleDataSourceModule.NAMESPACE, "script", "language", queryScriptLanguage, XmlWriterSupport.OPEN);
        }
        xmlWriter.writeTextNormalized(queryScript, false);
        xmlWriter.writeCloseTag();
      }

      xmlWriter.writeCloseTag();
    }

    xmlWriter.writeCloseTag();
  }
}
