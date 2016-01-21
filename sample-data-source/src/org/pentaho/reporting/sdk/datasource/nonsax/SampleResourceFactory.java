package org.pentaho.reporting.sdk.datasource.nonsax;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceData;
import org.pentaho.reporting.libraries.resourceloader.ResourceFactory;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.SimpleResource;
import org.pentaho.reporting.sdk.datasource.SampleDataFactory;
import org.pentaho.reporting.sdk.datasource.parser.SampleDataFactoryTags;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SampleResourceFactory implements ResourceFactory {
  @Override
  public Resource create(final ResourceManager resourceManager, final ResourceData resourceData, final ResourceKey contextKey)
          throws ResourceCreationException, ResourceLoadingException {
    final byte[] resource = resourceData.getResource(resourceManager);
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);

    try {
      final DocumentBuilder builder = factory.newDocumentBuilder();
      final Document parse = builder.parse(new ByteArrayInputStream(resource));
      // parse me here //
      final DataFactory result = doParse(parse);
      return new SimpleResource(resourceData.getKey(), result, getFactoryType(), resourceData.getVersion(resourceManager));
    } catch (final ParserConfigurationException e) {
      throw new ResourceLoadingException("Failed", e);
    } catch (final SAXException e) {
      throw new ResourceLoadingException("Failed", e);
    } catch (final IOException e) {
      throw new ResourceLoadingException("Failed", e);
    }
  }

  private DataFactory doParse(final Document parse) {
    final Element root = parse.getDocumentElement();
    final SampleDataFactory df = new SampleDataFactory();
    final String url = parseConfig(root);
    if (url != null) {
      df.setUrlPattern(url);
    }
    parseGlobalScript(root, df);
    parseQueries(root, df);
    return df;
  }

  private void parseGlobalScript(final Element root, SampleDataFactory df) {
    final Element e = getFirst(root, SampleDataFactoryTags.GLOBAL_SCRIPT_TAG);
    if (e == null) {
      return;
    }
    String lang = e.getAttribute(SampleDataFactoryTags.LANGUAGE_ATTR);
    if (lang != null) {
      String script = e.getTextContent();
      if (!StringUtils.isEmpty(script)) {
        df.setGlobalScript(script);
        df.setGlobalScriptLanguage(lang);
      }
    }
  }

  private void parseQueries(final Element root, SampleDataFactory df) {
    final Element e = getFirst(root, SampleDataFactoryTags.QUERY_DEFINITIONS_TAG);
    if (e == null) {
      return;
    }
    NodeList maybeQuery = e.getElementsByTagNameNS(e.getNamespaceURI(), SampleDataFactoryTags.QUERY_TAG);
    for (int i = 0; i < maybeQuery.getLength(); i += 1) {
      final Element q = (Element) maybeQuery.item(i);
      parseQuery(q, df);
    }
  }

  private void parseQuery(Element e, SampleDataFactory df) {
    String name = e.getAttribute("name");
    if (StringUtils.isEmpty(name)) {
      return;
    }
    Element query = getFirst(e, "static-query");
    String queryText = null;
    if (query != null) {
      queryText = query.getTextContent();
    }
    Element queryScript = getFirst(e, "script");
    String queryScriptText = null;
    String queryLanguage = null;
    if (query != null) {
      queryScriptText = queryScript.getTextContent();
      queryLanguage = queryScript.getAttribute("language");
      if (StringUtils.isEmpty(queryLanguage)) {
        queryLanguage = df.getGlobalScriptLanguage();
      }
    }
    df.setQuery(name, queryText, queryLanguage, queryScriptText);
  }

  private Element getFirst(Element e, String tag) {
    NodeList maybeQueries = e.getElementsByTagNameNS(e.getNamespaceURI(), tag);
    if (maybeQueries == null || maybeQueries.getLength() == 0) {
      return null;
    }
    return (Element) maybeQueries.item(0);
  }

  private String parseConfig(final Element root) {
    final Element e = getFirst(root, SampleDataFactoryTags.CONFIG_TAG);
    if (e == null) {
      return null;
    }
    return e.getAttribute(SampleDataFactoryTags.URL_PATTERN_ATTR);
  }

  @Override
  public Class getFactoryType() {
    return DataFactory.class;
  }

  @Override
  public void initializeDefaults() {

  }
}
