package org.pentaho.reporting.sdk.formula;

public class HelloWorldFormulaTest extends FormulaTestBase
{
  public HelloWorldFormulaTest()
  {
  }

  protected Object[][] createDataTest()
  {
    return new Object[][]
        {
            {"HELLOWORLD()", "Hello World!"},
            {"HELLOWORLD(\"Joe\")", "Hello World, Joe!"},
        };
  }

  public void testDefault() throws Exception
  {
    runDefaultTest();
  }

  public void testTranslations() throws Exception
  {
    performTranslationTest("HELLOWORLD");
  }
}
