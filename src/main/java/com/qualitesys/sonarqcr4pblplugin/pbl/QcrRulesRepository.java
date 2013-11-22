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


import org.apache.commons.io.IOUtils;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;

import java.io.InputStream;
import java.util.List;

import com.qualitesys.sonarqcr4pblplugin.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

public final class QcrRulesRepository extends RuleRepository {

//  private String resourceRepo = 
//            "jar:http://www.qualitesys.com/sonarrepository/"
//          + "sonar-"
//          + QcrCoreSonar.QCR_PLUGIN_NAME.toLowerCase()   //qcr4csh
//          +"-plugin-"
//          + QcrCoreSonar.QCR_MAVEN_VERSION               // 3.11.08.87_4sonar_2.8
//          + ".jar!";
  private String resourcePath      = "/com/qualitesys/sonarqcrplugin/"+Pbl.KEY+"/";
  private String resourceName      = "rules.xml";
  private String rep               = resourcePath+resourceName;
  //private String name         = "Default Qcr for Pbl Profile";
  private XMLRuleParser xmlRuleParser;
  private String CUSTOMERULESFILE  = "customrulesplugin"+Pbl.KEY+".xml";

  public QcrRulesRepository(XMLRuleParser xmlRuleParser) {
    super(QcrCoreSonar.QCR_PLUGIN_KEY, Pbl.KEY);
    setName(QcrCoreSonar.QCR_PLUGIN_NAME);
    this.xmlRuleParser = xmlRuleParser;
  }

  @Override
  public List<Rule> createRules() {
    final String ROUTINE = "createRules";
    QcrUtils.LOG.info(ROUTINE+"00 plugin version "+QcrCoreSonar.QCR_MAVEN_VERSION);
    String fileName = "/conf/sonar.properties";
    String customer = QcrUtils.getProp(fileName, "sonar.customer", this);
    QcrUtils.LOG.info(ROUTINE+"01 customer "+customer);
    InputStream input1 = null;
    if (null == input1) {
        QcrUtils.LOG.info(ROUTINE+"02 loading from local copy  "+rep);
        input1 = getClass().getResourceAsStream(rep);
    }
    // Modif D.C. 2013 09 12 pour custom rules
    InputStream input2 = null;
    String customerRulesRepo = 
            QcrUtils.getProp(fileName, "sonar.customerrulesrepository", this)
            +"/"
            +CUSTOMERULESFILE;
    QcrUtils.LOG.info(ROUTINE + "03 customerrulesrepository "+customerRulesRepo);
    if (!(new File(customerRulesRepo)).exists()) {
        QcrUtils.LOG.info(ROUTINE + "04 Custom file does not exist");
    } else {
        try {
            input2 = new FileInputStream(customerRulesRepo);
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(QcrRulesRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    try {
        List<Rule> ls = new Vector<Rule>();
        for (InputStream in : new InputStream[] {input1, input2}){
            if (null!=in) {
                List<Rule> lr = xmlRuleParser.parse(in);
                // On ne garde que le bon customer
                for (Rule r : lr) {
                    // Modif D.C. 2013 08 05 obsolete
                    //r.setEnabled(Boolean.TRUE);
                    String k = r.getKey();
                    String d = r.getDescription();
                    if (   k.endsWith("ALL")
                        || (!(0==customer.length()) && k.endsWith(customer))
                       ) {
                        QcrUtils.LOG.info(ROUTINE+"05 rule "+r+" description "+d);
                        // On vire la fin
                        r.setKey(k.replace("ALL", "").replace(customer, ""));
                        ls.add(r);
                    } else {
                        QcrUtils.LOG.info(ROUTINE+"06 rule "+r.getKey()+" is removed");
                    }
                }
            }
        }
        return ls;

    } finally {
      IOUtils.closeQuietly(input1);
      IOUtils.closeQuietly(input2);
    }
  }
}
