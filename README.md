pentaho-reporting-oem-sdk
=========================

A extended SDK containing samples for OEMs

Building the sources
--------------------

  To successfully build the Pentaho Reporting, you do need Apache Ant 1.8.2 or newer.
  Go download it from the Apache Ant Website if you haven?t done it yet.

  After you cloned our Git repository, you have all the source files on your
  computer. But before you can use the project, you will have to download the
  third party libraries used in the code.

  On a command line in the project directory, call

    ant resolve

  to download all libraries.

  If you?re going to use IntelliJ for your work, you are all set now and can
  start the IntelliJ project.

  To build all projects locally, invoke

    ant continuous-local-testless

  to run the compile process for all projects.


Existing samples
----------------

1. "sample-use-lightweight"

  This is a simple example on how to use the Pentaho Reporting engine
  in an embedded scenario. It shows how to show a Swing-preview dialog
  and also shows how to export to a PDF file without any GUI.

  This sample provides a ready to use, minimal runtime configuration
  that does not include the Pentaho Data Integration, Mondrian or OLAP4J
  data-sources. The projects footprint is 30MB.


2. "sample-use-full"

  This is a simple example on how to use the Pentaho Reporting engine
  in an embedded scenario. It shows how to show a Swing-preview dialog
  and also shows how to export to a PDF file without any GUI.

  This sample provides a full runtime configuration that is equivalent
  to the configuration used in the Pentaho Report Designer. It contains
  all data-sources, including the Pentaho Data Integration, Mondrian and
  OLAP4J data-sources. The project footprint is 120MB.


3. "sample-module"

  This SDK module shows how to create a extension module for the Pentaho
  Reporting engine. Extension modules form the basis of all extensions
  and help you to inject your code into the reporting engine in a controlled
  and safe way.

