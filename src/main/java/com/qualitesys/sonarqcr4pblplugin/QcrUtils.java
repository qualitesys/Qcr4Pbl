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

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.Logs;

public final class QcrUtils {

  private QcrUtils() {
    // only static methods
  }

  /**
   * Plugin logger
   */
  public static final Logger LOG = LoggerFactory.getLogger(QcrPlugin.class.getName());


  /**
   * Console logger, activated on level INFO
   */
  public static final Logger INFO = Logs.INFO;
  
  public static final String getProp(String fileName, String aprop, Object o) {
        Properties prop = new Properties();
        String maprop = "";
        try {
            prop.load(o.getClass().getResourceAsStream(fileName));
        } catch (Exception e) {
            //QcrUtils.LOG.info(e.getMessage());
        }
        maprop = prop.getProperty(aprop);
        if (null == maprop) {
            maprop = "";
        }
        return maprop;
  }

}
