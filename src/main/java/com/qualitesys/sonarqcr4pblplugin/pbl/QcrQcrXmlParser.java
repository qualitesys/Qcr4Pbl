/*
 * QualityChecker4Pbl for Sonar
 * Copyright (C) 2013 QualiteSys
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.qualitesys.sonarqcr4pblplugin.pbl;

import com.qualitesys.sonarqcr4pblplugin.AbstractQcrStaxParser;
import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

import javax.xml.stream.XMLStreamException;

public class QcrQcrXmlParser extends AbstractQcrStaxParser {

  private Project project;

  public QcrQcrXmlParser(
          Project        project,
          SensorContext  context) {
    super(context);
//    System.out.println("php QcrQcrXmlParser QcrQcrXmlParser "
//            +" project "+project.toString()
//            +" context "+context.toString()
//            );
    this.project = project;
  }

  @Override
  protected SMInputCursor cursorForResources(SMInputCursor rootCursor) throws XMLStreamException {
//    System.out.println("php QcrQcrXmlParser cursorForResources "
//            +" rootCursor "+rootCursor.toString()
//            );
    return rootCursor.descendantElementCursor("file");
  }

  @Override
  protected SMInputCursor cursorForSonarCoreMetric(SMInputCursor resourcesCursor) throws XMLStreamException {
//    System.out.println("php QcrQcrXmlParser cursorForSonarCoreMetric "
//            +" resourcesCursor "+resourcesCursor.toString()
//            );
    return resourcesCursor.descendantElementCursor("sonarcoremetric");
  }

  @Override
  protected SMInputCursor cursorForQcrCoreMetric(SMInputCursor resourcesCursor) throws XMLStreamException {
//    System.out.println("php QcrQcrXmlParser cursorForSonarCoreMetric "
//            +" resourcesCursor "+resourcesCursor.toString()
//            );
    return resourcesCursor.descendantElementCursor("qcrcoremetric");
  }

  @Override
  protected String messageFor(SMInputCursor measureCursor) throws XMLStreamException {
//    System.out.println("php QcrQcrXmlParser messageFor "
//            +" measureCursor "+measureCursor.toString()
//            );
    return StringUtils.trim(measureCursor.collectDescendantText());
  }

  @Override
  protected String metricName(SMInputCursor measureCursor) throws XMLStreamException {
//    System.out.println("php QcrQcrXmlParser ruleKey "
//            +" measureCursor "+measureCursor.toString()
//            );
    return measureCursor.getAttrValue("metricname");
  }

  @Override
  protected Resource toResource(SMInputCursor resourcesCursor) throws XMLStreamException {
//    System.out.println("php QcrQcrXmlParser toResource "
//            +" resourcesCursor "+resourcesCursor.toString()
//            );
    return PblFile.fromAbsolutePath(
            resourcesCursor.getAttrValue("name"),
            project.getFileSystem().getSourceDirs(),
            false);
  }

}
