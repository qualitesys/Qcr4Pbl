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

import com.qualitesys.sonarqcr4pblplugin.*;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.XmlParserException;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import org.sonar.api.rules.RuleFinder;

public class QcrSensorPbl implements Sensor {

  private RulesProfile profile;
  private RuleFinder   rulesFinder;
  public QcrSensorPbl(
          RulesProfile          profile,
          RuleFinder            rulesFinder) {
    this.profile       = profile;
    this.rulesFinder   = rulesFinder;
  }

  public void analyse(Project project, SensorContext context) {
    try {
      System.out.println("Pbl QcrSensorPbl analyse debut");
      QcrViolationsXmlParser parser = getStaxParser(project, context);
      File report = new File(
              project.getFileSystem().getBuildDir(),
              QcrCoreSonar.QCR_RESULTS_FILE);
      if (!report.exists()) {
          System.out.println("Pbl QcrSensorPbl report file "+report.getName()+" doesn't exist");
      } else {
          System.out.println("Pbl QcrSensorPbl report pour violations "+report.toString());
          parser.parse(report);

          AbstractQcrStaxParser qcrparser = getQcrStaxParser(project, context);
          System.out.println("Pbl QcrSensorPbl report pour compteurs  "+report.toString());
          qcrparser.parse(report);
      }

      System.out.println("Pbl QcrSensorPbl analyse fin");

    } catch (XMLStreamException e) {
      throw new XmlParserException(e);
    }
  }

  public boolean shouldExecuteOnProject(Project project) {
    // Modif D.C. 30 10 2010 on le fait toujours
    return project.getLanguage().equals(Pbl.INSTANCE);
    //return true;
  }

  private QcrViolationsXmlParser getStaxParser(Project project, SensorContext context) {
    return new QcrViolationsXmlParser(project, rulesFinder, context);
  }

  private AbstractQcrStaxParser getQcrStaxParser(Project project, SensorContext context) {
    return new QcrQcrXmlParser(project, context);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}