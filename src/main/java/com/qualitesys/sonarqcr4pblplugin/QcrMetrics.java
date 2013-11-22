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

import org.sonar.api.measures.Metrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.SumChildValuesFormula;

import java.util.List;
import java.util.Arrays;


/**
 * As the Metrics class implements org.sonar.api.Extension interface,
 * MyOwnMetrics is a Sonar extension which allows to declare as many new Metrics as you want.
 */
public class QcrMetrics implements Metrics {

//    public static final Metric DEADLOCK_RESOURCE_COUNT =
//            new Metric(
//            "deadlock4xxxresourcecount",
//            "Qcr4Xxx Deadlocks",
//            "Number of resources in deadlock",
//            Metric.ValueType.INT,
//            Metric.DIRECTION_WORST,
//            false,                                                  // metrique quantitative
//            CoreMetrics.DOMAIN_GENERAL);
//    public static final Metric DEADLOCK_RESOURCE_COUNT1 =
//            DEADLOCK_RESOURCE_COUNT
//            .setFormula(new SumChildValuesFormula(false))           // Cumul remontant package etc..
//                                                                    // false : ne pas stocker 0 si pas
//                                                                    // de valeurs en dessous
//            .setWorstValue(2000.0)
//            .setBestValue(0.0)
//            .setOptimizedBestValue(false)
//            ;
//
//    public static final Metric DEADLOCK_COMPLIANCE =
//            new Metric(
//            "deadlock4xxxcompliance",
//            "Qcr4Xxx Deadlocks compliance",
//            "Compliance to deadlocks number",
//            Metric.ValueType.PERCENT,
//            Metric.DIRECTION_BETTER,
//            true,                                                   // metrique quali
//            CoreMetrics.DOMAIN_GENERAL);
//
//    public static final Metric LOCKED_RESOURCE_COUNT =
//            new Metric(
//            "locked4xxresourcecount",
//            "Qcr4Xxx Locked resources",
//            "Number of resources in locked",
//            Metric.ValueType.INT,
//            Metric.DIRECTION_WORST,
//            false,                                                  // metrique quantitative
//            CoreMetrics.DOMAIN_GENERAL);
//    public static final Metric LOCKED_RESOURCE_COUNT1 =
//            LOCKED_RESOURCE_COUNT
//            .setFormula(new SumChildValuesFormula(false))           // Cumul remontant package etc..
//                                                                    // false : ne pas stocker 0 si pas
//                                                                    // de valeurs en dessous
//            .setWorstValue(2000.0)
//            .setBestValue(0.0)
//            .setOptimizedBestValue(false)
//            ;
//
//    public static final Metric LOCKED_COMPLIANCE =
//            new Metric(
//            "locked4xxcompliance",
//            "Qcr4Xxx Locked compliance",
//            "Compliance to locked resources number",
//            Metric.ValueType.PERCENT,
//            Metric.DIRECTION_BETTER,
//            true,                                                  // metrique quali
//            CoreMetrics.DOMAIN_GENERAL);
//
//    public static final Metric SECURITY_RISK =
//            new Metric(
//            "securityrisk4xxlevel",
//            "Qcr4Xxx Security Risk Level",
//            "Level of software security risk",
//            Metric.ValueType.INT,
//            Metric.DIRECTION_WORST,
//            false,                                                  // metrique quantitative
//            CoreMetrics.DOMAIN_GENERAL);
//    public static final Metric SECURITY_RISK1 =
//            SECURITY_RISK
//            .setFormula(new SumChildValuesFormula(false))           // Cumul remontant package etc..
//                                                                    // false : ne pas stocker 0 si pas
//                                                                    // de valeurs en dessous
//            .setWorstValue(2000.0)
//            .setBestValue(0.0)
//            .setOptimizedBestValue(false)
//            ;
//
//    public static final Metric SECURITY_COMPLIANCE =
//            new Metric(
//            "security4xxcompliance",
//            "Qcr4Xxx Security Risk compliance",
//            "Compliance to security risk level",
//            Metric.ValueType.PERCENT,
//            Metric.DIRECTION_BETTER,
//            true,                                                   // metrique quali
//            CoreMetrics.DOMAIN_GENERAL);
//
    // getMetrics() method is defined in the Metrics interface and is used by
    // Sonar to retrieve the list of new Metric
    public List<Metric> getMetrics() {
       return Arrays.asList(
//               DEADLOCK_RESOURCE_COUNT1,
//               DEADLOCK_COMPLIANCE    ,
//               LOCKED_RESOURCE_COUNT1  ,
//               LOCKED_COMPLIANCE      ,
//               SECURITY_RISK1          ,
//               SECURITY_COMPLIANCE
               );
    }
}
