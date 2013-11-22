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

import org.sonar.api.utils.WildcardPattern;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.DefaultProjectFileSystem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;
import java.util.List;

public class PblFile extends Resource<PblPackage> {

  private String filename;
  private String longName;
  private String packageKey;
  private boolean unitTest = false;
  private PblPackage parent = null;

  /**
   * SONARPLUGINS-666: For backward compatibility
   */
  public PblFile(String key) {
    this(key, false);
  }

  /**
   * @param unitTest whether it is a unit test file or a source file
   */
  public PblFile(String key, boolean unitTest) {
    super();
    if (key != null && key.indexOf('$') >= 0) {
      throw new IllegalArgumentException("Pbl inner classes are not supported : " + key);
    }
    String realKey = StringUtils.trim(key);
    this.unitTest = unitTest;

    if (realKey.contains(".")) {
      this.filename = StringUtils.substringAfterLast(realKey, ".");
      this.packageKey = StringUtils.substringBeforeLast(realKey, ".");
      this.longName = realKey;

    } else {
      this.filename = realKey;
      this.longName = realKey;
      this.packageKey = PblPackage.DEFAULT_PACKAGE_NAME;
      realKey = new StringBuilder().append(PblPackage.DEFAULT_PACKAGE_NAME).append(".").append(realKey).toString();
    }
    setKey(realKey);
  }

  /**
   * @param unitTest whether it is a unit test file or a source file
   */
  public PblFile(String packageKey, String className, boolean unitTest) {
    super();
    if (className != null && className.indexOf('$') >= 0) {
      throw new IllegalArgumentException("Java inner classes are not supported : " + className);
    }
    this.filename = className.trim();
    String key;
    if (StringUtils.isBlank(packageKey)) {
      this.packageKey = PblPackage.DEFAULT_PACKAGE_NAME;
      this.longName = this.filename;
      key = new StringBuilder().append(this.packageKey).append(".").append(this.filename).toString();
    } else {
      this.packageKey = packageKey.trim();
      key = new StringBuilder().append(this.packageKey).append(".").append(this.filename).toString();
      this.longName = key;
    }
    setKey(key);
    this.unitTest = unitTest;
  }

  public PblPackage getParent() {
    if (parent == null) {
      parent = new PblPackage(packageKey);
    }
    return parent;
  }

  public String getDescription() {
    return null;
  }

  public Language getLanguage() {
    return Pbl.INSTANCE;
  }

  public String getName() {
    return filename;
  }

  public String getLongName() {
    return longName;
  }

  public String getScope() {
    return Resource.SCOPE_ENTITY;
  }

  public String getQualifier() {
    return unitTest ? Resource.QUALIFIER_UNIT_TEST_CLASS : Resource.QUALIFIER_CLASS;
  }

  public boolean isUnitTest() {
    return unitTest;
  }

  public boolean matchFilePattern(String antPattern) {
    String patternWithoutFileSuffix = StringUtils.substringBeforeLast(antPattern, ".");
    WildcardPattern matcher = WildcardPattern.create(patternWithoutFileSuffix, ".");
    return matcher.match(getKey());
  }

  /**
   * SONARPLUGINS-666: For backward compatibility
   */
  public static PblFile fromIOFile(File file, List<File> sourceDirs) {
    return fromIOFile(file, sourceDirs, false);
  }

  /**
   * Creates a {@link PblFile} from a file in the source directories.
   *
   * @param unitTest whether it is a unit test file or a source file
   * @return the {@link PblFile} created if exists, null otherwise
   */
  public static PblFile fromIOFile(File file, List<File> sourceDirs, boolean unitTest) {
    if (file == null) {
      return null;
    }
    //System.out.println("PblFile fromIOFile file "+file+" sourceDirs "+sourceDirs.toString());
    String relativePath = DefaultProjectFileSystem.getRelativePath(file, sourceDirs);
    if (relativePath != null) {
      String pacname = null;
      String classname = relativePath;

      if (relativePath.indexOf('/') >= 0) {
        pacname = StringUtils.substringBeforeLast(relativePath, "/");
        pacname = StringUtils.replace(pacname, "/", ".");
        classname = StringUtils.substringAfterLast(relativePath, "/");
      }
      // Modif D.C. 01 11 2010 en Pb deux fichiers peuvent avoir le meme nom
      // et des extensions differentes. On garde l'extension
      //classname = StringUtils.substringBeforeLast(classname, ".");
      return new PblFile(pacname, classname, unitTest);
    }
    return null;
  }

  /**
   * Shortcut to {@link #fromIOFile(File, List, boolean)} with an absolute path.
   */
  public static PblFile fromAbsolutePath(String path, List<File> sourceDirs, boolean unitTest) {
    if (path == null) {
      return null;
    }
    return fromIOFile(new File(path), sourceDirs, unitTest);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("key", getKey())
        .append("package", packageKey)
        .append("longName", longName)
        .append("filename", filename)
        .append("unitTest", unitTest)
        .toString();
  }
}

