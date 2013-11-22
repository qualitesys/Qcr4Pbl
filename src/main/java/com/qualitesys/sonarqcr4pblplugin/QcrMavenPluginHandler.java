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

import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.batch.maven.MavenPlugin;
import org.sonar.api.resources.Project;

public class QcrMavenPluginHandler implements MavenPluginHandler{
  public String getGroupId() {
    //return "com.adobe.ac";
    return QcrCoreSonar.QCR_MAVEN_GROUP_ID;
  }

  public String getArtifactId() {
    //return "flex-pmd-metrics-maven-plugin";
    //return Php.QCR4PHP_MAVEN_ARTIFACT_ID;
      return QcrCoreSonar.QCR_MAVEN_ARTIFACT_ID;
  }

  public String getVersion() {
    //return "1.1-SNAPSHOT";
    return QcrCoreSonar.QCR_MAVEN_VERSION;
  }

  public boolean isFixedVersion() {
    // Modif D.C. 04 12 2010 version non figee, utilise la derniere version disponible
    // dans le repo QualiteSys
    //return true;
    return false;
  }

  public String[] getGoals() {
    //return new String[]{"check"};
    return new String[]{QcrCoreSonar.QCR_MAVEN_GOALS};
  }
  public void configure(Project project, MavenPlugin plugin) {
  }
}
