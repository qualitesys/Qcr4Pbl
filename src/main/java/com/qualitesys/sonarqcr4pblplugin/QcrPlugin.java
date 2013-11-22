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

import org.sonar.api.Extension;
import org.sonar.api.Plugin;

import java.util.ArrayList;
import java.util.List;


public class QcrPlugin implements Plugin {

  public String getKey() {
    //return CoreProperties.PMD_PLUGIN;
    return QcrCoreSonar.QCR_PLUGIN_KEY;
  }

  public String getName() {
    //return "PMD";
    return QcrCoreSonar.QCR_PLUGIN_NAME;
  }

  public String getDescription() {
    //return "PMD is a tool that looks for potential problems like possible bugs, dead code, suboptimal code,  overcomplicated expressions or duplicate code. You can find more by going to the <a href='http://pmd.sourceforge.net'>PMD web site</a>.";
    return "QualityChecker is a tool that looks for potential problems like possible bugs, dead code, suboptimal code,  overcomplicated expressions or duplicate code. You can find more by going to the <a href='http://www.qualitesys.com'>QualityChecker web site</a>.";
  }

  public List<Class<? extends Extension>> getExtensions() {
    String d = "QcrPlugin : getExtensions start ";
    QcrUtils.LOG.info(d);
    System.out.println(d);
    String e = "QcrPlugin : Plugin "+QcrCoreSonar.QCR_PLUGIN_KEY+
            " artifact-id "+QcrCoreSonar.QCR_MAVEN_ARTIFACT_ID+
            " version "+QcrCoreSonar.QCR_MAVEN_VERSION;
    QcrUtils.LOG.info(e);
    System.out.println(e);
    List<Class<? extends Extension>> extensions = new ArrayList<Class<? extends Extension>>();

    // Metriques de Qualitychecker
    extensions.add(com.qualitesys.sonarqcr4pblplugin.QcrMavenPluginHandler.class);
    extensions.add(com.qualitesys.sonarqcr4pblplugin.QcrMetrics.class);

    extensions.add(com.qualitesys.sonarqcr4pblplugin.pbl.Pbl.class);
    extensions.add(com.qualitesys.sonarqcr4pblplugin.pbl.QcrSourceImporterPbl.class);
    extensions.add(com.qualitesys.sonarqcr4pblplugin.pbl.QcrColorizerFormat.class);
    extensions.add(com.qualitesys.sonarqcr4pblplugin.pbl.QcrSensorPbl.class);
    extensions.add(com.qualitesys.sonarqcr4pblplugin.pbl.QcrRulesRepository.class);


    d = "QcrPlugin : getExtensions end ";
    QcrUtils.LOG.info(d);
    System.out.println(d);

    return extensions;
  }
  @Override
  public String toString() {
    return getKey();
  }
}
