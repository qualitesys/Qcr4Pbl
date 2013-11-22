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

import org.sonar.api.web.CodeColorizerFormat;
import org.sonar.colorizer.*;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class QcrColorizerFormat extends CodeColorizerFormat {

  public QcrColorizerFormat() {
    super(Pbl.KEY);
  }

  @Override
  public List<Tokenizer> getTokenizers() {
    return Collections.unmodifiableList(Arrays.asList(
      new StringTokenizer("<span class=\"s\">", "</span>"),
      new CDocTokenizer("<span class=\"cd\">", "</span>"),
      new JavadocTokenizer("<span class=\"cppd\">", "</span>"),
      new CppDocTokenizer("<span class=\"cppd\">", "</span>"),
      new KeywordsTokenizer("<span class=\"k\">", "</span>", QcrKeywords.get())
    ));
  }
}
