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

package com.qualitesys.sonarqcr4pblplugin;

import org.apache.commons.io.IOUtils;
import org.codehaus.staxmate.in.SMEvent;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Resource;
import org.sonar.api.utils.StaxParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;

/**
 * @since 1.10
 */
public abstract class AbstractQcrStaxParser {
  protected SensorContext context;

  protected AbstractQcrStaxParser(SensorContext context) {
    this.context = context;
  }

  /**
   * Cursor for child resources to parse, the returned input cursor should be filtered on SMEvent.START_ELEMENT
   * for optimal perfs
   *
   * @param rootCursor the root xml doc cursor
   * @return a cursor with child resources elements to parse
   */
  protected abstract SMInputCursor cursorForResources(SMInputCursor rootCursor) throws XMLStreamException;

  /**
   * Cursor for violations to parse for a given resource, the returned input cursor should be filtered on SMEvent.START_ELEMENT
   * for optimal perfs
   *
   * @param resourcesCursor the current resource cursor
   * @return a cursor with child violations elements to parse
   */
  protected abstract SMInputCursor cursorForSonarCoreMetric(SMInputCursor resourcesCursor) throws XMLStreamException;

  // Metriques propres a QualityChecker exemple DEADLOCK_RESOURCE_COUNT
  protected abstract SMInputCursor cursorForQcrCoreMetric(SMInputCursor resourcesCursor) throws XMLStreamException;
  /**
   * Transforms a given xml resource to a resource Object
   */
  protected abstract Resource toResource(SMInputCursor resourceCursor) throws XMLStreamException;

  protected abstract String messageFor(SMInputCursor measureCursor) throws XMLStreamException;

  //protected abstract String ruleKey(SMInputCursor measureCursor) throws XMLStreamException;
  protected abstract String metricName(SMInputCursor measureCursor) throws XMLStreamException;

//  protected abstract String keyForPlugin();

//  protected abstract String lineNumberForViolation(SMInputCursor measureCursor) throws XMLStreamException;

  public void parse(File violationsXMLFile) throws XMLStreamException {
    if (violationsXMLFile != null && violationsXMLFile.exists()) {
      InputStream input = null;
      try {
        input = new FileInputStream(violationsXMLFile);
        parse(input);

      }
      catch (FileNotFoundException e) {
        throw new XMLStreamException(e);

      }
      finally {
        IOUtils.closeQuietly(input);
      }
    }
  }

  public final void parse(InputStream input) throws XMLStreamException {
    if (input != null) {
      StaxParser parser = new StaxParser(new StaxParser.XmlStreamHandler() {
        public void stream(SMHierarchicCursor rootCursor) throws XMLStreamException {
          parseResources(rootCursor.advance());
        }
      }, true);
      parser.parse(input);
    }
  }

  private void parseResources(SMInputCursor rootCursor) throws XMLStreamException {
    SMInputCursor resourcesCursor = cursorForResources(rootCursor);
    SMEvent event;
    while ((event = resourcesCursor.getNext()) != null) {
      if (event.compareTo(SMEvent.START_ELEMENT) == 0) {
        // Mesures Sonar
        parseSonarCoreMetrics(resourcesCursor);
      }
    }
  }

  private void parseSonarCoreMetrics(SMInputCursor resourcesCursor) throws XMLStreamException {
    Resource resource = toResource(resourcesCursor);
    SMInputCursor metricCursor = cursorForSonarCoreMetric(resourcesCursor);
    SMEvent event;
    while ((event = metricCursor.getNext()) != null) {
      if (event.compareTo(SMEvent.START_ELEMENT) == 0) {
        createMeasureFor(resource, metricCursor);
      }
    }
  }

  private void createMeasureFor(Resource resource, SMInputCursor measureCursor) throws XMLStreamException {
    if (measureCursor != null && resource != null) {
      Metric me = getMetric(metricName(measureCursor));
      if (null != me) {
          int PRECISION = 1;
        Measure m = new Measure (
              me,
              Double.valueOf(messageFor(measureCursor)), PRECISION);
        context.saveMeasure(resource, m);
//        // Maj de la mesure du "parent" soit le package ou dir contenant la resource
//        // Si metrique de type integer, donc cumulable
//        if (   me.isNumericType()
//            && me.getName().startsWith("QCR")) {
//            Resource p = resource.getParent();
//            if (null != p) {
//                Measure  mp = context.getMeasure(p,me);
//                if (null != mp) {
//                    System.out.println("AbstractQcrStaxParser createMeasureFor : maj mesure "+me.getName()+" parent "+p.getName());
//                    mp.setValue(mp.getValue()+m.getValue()); // Somme
//                    context.saveMeasure(p,mp);
//                } else {
//                    System.out.println("AbstractQcrStaxParser createMeasureFor : mesure parent nulle, on la cree "+me.getName()+". Parent : "+p.getName());
//                    // Dans ce cas on cree la mesure avec m
//                    Measure mm = new Measure (m.getMetric(), m.getValue(), PRECISION);
//                    context.saveMeasure(p,mm);
//                }
//            } else {
//                System.out.println("AbstractQcrStaxParser createMeasureFor : resource parent nulle sur "+resource.getName());
//            }
//        }
      }
    }
  }
  private Metric getMetric(String me) {
//      System.out.println("Php AbstractQcrStaxParser getMetric "+me);
      Metric ret = null;
      // Table de transcodage
      if (me.equals("ACCESSORS"))             ret = CoreMetrics.ACCESSORS;
      if (me.equals("ALERT_STATUS"))          ret = CoreMetrics.ALERT_STATUS;
      if (me.equals("BLOCKER_VIOLATIONS"))    ret = CoreMetrics.BLOCKER_VIOLATIONS;
      if (me.equals("BRANCH_COVERAGE"))       ret = CoreMetrics.BRANCH_COVERAGE;
      if (me.equals("BRANCH_COVERAGE_HITS_DATA")) ret = CoreMetrics.BRANCH_COVERAGE_HITS_DATA;
      if (me.equals("CLASSES"))               ret = CoreMetrics.CLASSES;
      if (me.equals("CLASS_COMPLEXITY"))      ret = CoreMetrics.CLASS_COMPLEXITY;
      if (me.equals("CLASS_COMPLEXITY_DISTRIBUTION")) ret = CoreMetrics.CLASS_COMPLEXITY_DISTRIBUTION;
      if (me.equals("COMMENTED_OUT_CODE_LINES")) ret = CoreMetrics.COMMENTED_OUT_CODE_LINES;
      if (me.equals("COMMENT_LINES"))         ret = CoreMetrics.COMMENT_LINES;
      if (me.equals("COMMENT_LINES_DENSITY")) ret = CoreMetrics.COMMENT_LINES_DENSITY;
      if (me.equals("COMPLEXITY"))            ret = CoreMetrics.COMPLEXITY;
      if (me.equals("CONDITIONS_TO_COVER"))   ret = CoreMetrics.CONDITIONS_TO_COVER;
      if (me.equals("COVERAGE"))              ret = CoreMetrics.COVERAGE;
      if (me.equals("COVERAGE_LINE_HITS_DATA")) ret = CoreMetrics.COVERAGE_LINE_HITS_DATA;
      if (me.equals("CRITICAL_VIOLATIONS"))   ret = CoreMetrics.CRITICAL_VIOLATIONS;
      if (me.equals("DIRECTORIES"))           ret = CoreMetrics.DIRECTORIES;
      if (me.equals("DUPLICATED_BLOCKS"))     ret = CoreMetrics.DUPLICATED_BLOCKS;
      if (me.equals("DUPLICATED_FILES"))      ret = CoreMetrics.DUPLICATED_FILES;
      if (me.equals("DUPLICATED_LINES"))      ret = CoreMetrics.DUPLICATED_LINES;
      if (me.equals("DUPLICATED_LINES_DENSITY")) ret = CoreMetrics.DUPLICATED_LINES_DENSITY;
      if (me.equals("DUPLICATIONS_DATA"))     ret = CoreMetrics.DUPLICATIONS_DATA;
      if (me.equals("EFFICIENCY"))            ret = CoreMetrics.EFFICIENCY;
      if (me.equals("FILES"))                 ret = CoreMetrics.FILES;
      if (me.equals("FILE_COMPLEXITY"))       ret = CoreMetrics.FILE_COMPLEXITY;
      if (me.equals("FUNCTIONS"))             ret = CoreMetrics.FUNCTIONS;
      if (me.equals("FUNCTION_COMPLEXITY"))   ret = CoreMetrics.FUNCTION_COMPLEXITY;
      if (me.equals("FUNCTION_COMPLEXITY_DISTRIBUTION")) ret = CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION;
      if (me.equals("INFO_VIOLATIONS"))       ret = CoreMetrics.INFO_VIOLATIONS;
      if (me.equals("LINES"))                 ret = CoreMetrics.LINES;
      if (me.equals("LINES_TO_COVER"))        ret = CoreMetrics.LINES_TO_COVER;
      if (me.equals("LINE_COVERAGE"))         ret = CoreMetrics.LINE_COVERAGE;
      if (me.equals("MAINTAINABILITY"))       ret = CoreMetrics.MAINTAINABILITY;
      if (me.equals("MAJOR_VIOLATIONS"))      ret = CoreMetrics.MAJOR_VIOLATIONS;
      if (me.equals("MINOR_VIOLATIONS"))      ret = CoreMetrics.MINOR_VIOLATIONS;
      if (me.equals("NCLOC"))                 ret = CoreMetrics.NCLOC;
      if (me.equals("PACKAGES"))              ret = CoreMetrics.PACKAGES;
      if (me.equals("PORTABILITY"))           ret = CoreMetrics.PORTABILITY;
      if (me.equals("PROFILE"))               ret = CoreMetrics.PROFILE;
      if (me.equals("PUBLIC_API"))            ret = CoreMetrics.PUBLIC_API;
      if (me.equals("PUBLIC_DOCUMENTED_API_DENSITY")) ret = CoreMetrics.PUBLIC_DOCUMENTED_API_DENSITY;
      if (me.equals("PUBLIC_UNDOCUMENTED_API")) ret = CoreMetrics.PUBLIC_UNDOCUMENTED_API;
      if (me.equals("RELIABILITY"))           ret = CoreMetrics.RELIABILITY;
      if (me.equals("SKIPPED_TESTS"))         ret = CoreMetrics.SKIPPED_TESTS;
      if (me.equals("STATEMENTS"))            ret = CoreMetrics.STATEMENTS;
      if (me.equals("TESTS"))                 ret = CoreMetrics.TESTS;
      if (me.equals("TEST_DATA"))             ret = CoreMetrics.TEST_DATA;
      if (me.equals("TEST_ERRORS"))           ret = CoreMetrics.TEST_ERRORS;
      if (me.equals("TEST_EXECUTION_TIME"))   ret = CoreMetrics.TEST_EXECUTION_TIME;
      if (me.equals("TEST_FAILURES"))         ret = CoreMetrics.TEST_FAILURES;
      if (me.equals("TEST_SUCCESS_DENSITY"))  ret = CoreMetrics.TEST_SUCCESS_DENSITY;
      if (me.equals("UNCOVERED_CONDITIONS"))  ret = CoreMetrics.UNCOVERED_CONDITIONS;
      if (me.equals("UNCOVERED_LINES"))       ret = CoreMetrics.UNCOVERED_LINES;
      if (me.equals("USABILITY"))             ret = CoreMetrics.USABILITY;
      if (me.equals("VIOLATIONS"))            ret = CoreMetrics.VIOLATIONS;
      if (me.equals("VIOLATIONS_DENSITY"))    ret = CoreMetrics.VIOLATIONS_DENSITY;
      if (me.equals("WEIGHTED_VIOLATIONS"))   ret = CoreMetrics.WEIGHTED_VIOLATIONS;

//      // Modif D.C. 29/01/2010 metriques specifique � QualityChecker
//      if (me.equals("QCRDEADLOCK_RESOURCE_COUNT")) ret = QcrMetrics.DEADLOCK_RESOURCE_COUNT;
//      if (me.equals("QCRDEADLOCK_COMPLIANCE"    )) ret = QcrMetrics.DEADLOCK_COMPLIANCE;
//      if (me.equals("QCRLOCKED_RESOURCE_COUNT"  )) ret = QcrMetrics.LOCKED_RESOURCE_COUNT;
//      if (me.equals("QCRLOCKED_COMPLIANCE"      )) ret = QcrMetrics.LOCKED_COMPLIANCE;
//      if (me.equals("QCRSECURITY_RISK"          )) ret = QcrMetrics.SECURITY_RISK;
//      if (me.equals("QCRSECURITY_COMPLIANCE"    )) ret = QcrMetrics.SECURITY_COMPLIANCE;
      return ret;
  }

}
