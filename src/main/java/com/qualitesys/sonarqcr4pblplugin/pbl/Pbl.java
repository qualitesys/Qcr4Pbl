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


import org.sonar.api.resources.AbstractLanguage;

public class Pbl extends AbstractLanguage {
  public static final Pbl      INSTANCE                   = new Pbl();
  public static final String   KEY                        = "pbl";
  public static final String   DEFAULT_PACKAGE_NAME       = "[default]";
  static        final String[] SUFFIXES                   = {
      // Modif D.C. 17 10 2010 pour powerbuilder
      "sra" ,"srd" ,"srf", "srm" ,"srs", "sru", "srw",
      "sra.res" ,"srd.res" ,"srf.res", "srm.res" ,"srs.res", "sru.res", "srw.res"
  };

  public Pbl() {
    super(KEY, "Pbl");
  }
  
   public String[] getFileSuffixes() {
    return SUFFIXES;
  }

}
