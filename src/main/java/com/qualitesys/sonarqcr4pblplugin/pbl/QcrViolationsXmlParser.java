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

import com.qualitesys.sonarqcr4pblplugin.QcrCoreSonar;
import java.io.File;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.in.SMEvent;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.Violation;
import org.sonar.api.utils.StaxParser;

class QcrViolationsXmlParser {

  private Project project;
  private RuleFinder ruleFinder;
  private SensorContext context;

  public QcrViolationsXmlParser(Project project, RuleFinder ruleFinder, SensorContext context) {
    this.project = project;
    this.ruleFinder = ruleFinder;
    this.context = context;
  }

  public void parse(File file) throws XMLStreamException {
    StaxParser parser = new StaxParser(new StreamHandler(), true);
    parser.parse(file);
  }

  private class StreamHandler implements StaxParser.XmlStreamHandler {
    public void stream(SMHierarchicCursor rootCursor) throws XMLStreamException {
      rootCursor.advance();

      SMInputCursor fileCursor = rootCursor.descendantElementCursor("file");
      SMEvent event;
      while ((event=fileCursor.getNext()) != null) {
          if (event.compareTo(SMEvent.START_ELEMENT) == 0) {
            String name = fileCursor.getAttrValue("name");
            Resource resource = PblFile.fromAbsolutePath(name, project.getFileSystem().getSourceDirs(), false);
            // Save violations only for existing resources
            if (null!=resource && context.getResource(resource) != null) {
              streamViolations(fileCursor, resource);
            }
          }
      }
    }

    private void streamViolations(SMInputCursor fileCursor, Resource resource) throws XMLStreamException {
      SMInputCursor violationCursor = fileCursor.descendantElementCursor("violation");
      SMEvent event;
      while ((event=violationCursor.getNext()) != null) {
          if (event.compareTo(SMEvent.START_ELEMENT) == 0) {
            int lineId = Integer.parseInt(violationCursor.getAttrValue("beginline"));
            String ruleKey = violationCursor.getAttrValue("rule");
            String message = StringUtils.trim(violationCursor.collectDescendantText());

            Rule rule = ruleFinder.findByKey(QcrCoreSonar.QCR_PLUGIN_KEY, ruleKey);
            // Save violations only for enabled rules
            if (rule != null) {
              Violation violation = Violation.create(rule, resource).setLineId(lineId).setMessage(message);
              context.saveViolation(violation);
            }
          }
      }
    }
  }

}
