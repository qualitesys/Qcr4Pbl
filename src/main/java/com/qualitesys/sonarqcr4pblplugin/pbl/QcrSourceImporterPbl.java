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

import org.sonar.api.batch.AbstractSourceImporter;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

import java.io.File;
import java.util.List;

public class QcrSourceImporterPbl extends AbstractSourceImporter {

  public QcrSourceImporterPbl() {
    super(Pbl.INSTANCE);
  }

  @Override
  protected Resource createResource(File file, List<File> sourceDirs, boolean unitTest) {
    System.out.println("Pbl QcrSourceImporterPbl createResource "+file+" sourceDirs "+sourceDirs.toString());
    // Modif D.C. 2012 07 11 taille
    final double NBCARMAX = 2500000.0;
    return (file != null && file.length()<NBCARMAX ) ? PblFile.fromIOFile(file, sourceDirs) : null;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

// Modif D.C. 2013 08 05 a virer, force le enabled pour upload source
//  @Override
//  public boolean shouldExecuteOnProject(Project project) {
//      boolean res = project.getLanguage().equals(Pbl.INSTANCE);
//      // Modif D.C. 25 11 2010 on force
//      //res = true;
//      System.out.println("Pbl QcrSourceImporterPbl shouldExecuteOnProject "+res);
//      return res;
//  }

}