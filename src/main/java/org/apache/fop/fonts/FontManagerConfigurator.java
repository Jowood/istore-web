/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id: FontManagerConfigurator.java 821058 2009-10-02 15:31:14Z jeremias $ */

package org.apache.fop.fonts;

import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fonts.substitute.FontSubstitutions;
import org.apache.fop.fonts.substitute.FontSubstitutionsConfigurator;
import org.apache.fop.util.LogUtil;

import com.framework.excore.template.export.ExportConstants;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-5-11
 * @Time: 下午3:36:33
 * @version 1.0
 * @Description :字体设置,覆盖fop.jar的FontManagerConfigurator类.
 */
public class FontManagerConfigurator {

    /** logger instance */
    private static Log log = LogFactory.getLog(FontManagerConfigurator.class);

    private Configuration cfg;

    /**
     * Main constructor
     * @param cfg the font manager configuration object
     */
    public FontManagerConfigurator(Configuration cfg) {
        this.cfg = cfg;
    }

    /**
     * Initializes font settings from the user configuration
     * @param fontManager a font manager
     * @param strict true if strict checking of the configuration is enabled
     * @throws FOPException if an exception occurs while processing the configuration
     * @throws ConfigurationException 
     * @throws UnsupportedEncodingException 
     */
    public void configure(FontManager fontManager, boolean strict) throws FOPException, UnsupportedEncodingException, ConfigurationException {

        // caching (fonts)
        if (cfg.getChild("use-cache", false) != null) {
            try {
                fontManager.setUseCache(
                        cfg.getChild("use-cache").getValueAsBoolean());
            } catch (ConfigurationException mfue) {
                LogUtil.handleException(log, mfue, true);
            }
        }
        if (cfg.getChild("font-base", false) != null) {
            try {
            	
				String fontPath = cfg.getChild("font-base").getValue(null);

				/** 字体路经变更为classpath下 by 王冲 2012-05-10 **/
				if (fontPath != null) {
					String path = this.changeFontsPath() + fontPath;
					fontManager.setFontBaseURL(path);
				}

				else {
					fontManager.setFontBaseURL(fontPath);
				}

				/** end **/
				
				/** 系统源代码 **/
				// fontManager.setFontBaseURL(pa);
				/*****/
            } catch (MalformedURLException mfue) {
                LogUtil.handleException(log, mfue, true);
            }
        }

        // global font configuration
        Configuration fontsCfg = cfg.getChild("fonts", false);
        if (fontsCfg != null) {
            // font substitution
            Configuration substitutionsCfg = fontsCfg.getChild("substitutions", false);
            if (substitutionsCfg != null) {
                FontSubstitutionsConfigurator fontSubstitutionsConfigurator
                        = new FontSubstitutionsConfigurator(substitutionsCfg);
                FontSubstitutions substitutions = new FontSubstitutions();
                fontSubstitutionsConfigurator.configure(substitutions);
                fontManager.setFontSubstitutions(substitutions);
            }

            // referenced fonts (fonts which are not to be embedded)
            Configuration referencedFontsCfg = fontsCfg.getChild("referenced-fonts", false);
            if (referencedFontsCfg != null) {
                FontTriplet.Matcher matcher = createFontsMatcher(
                        referencedFontsCfg, strict);
                fontManager.setReferencedFontsMatcher(matcher);
            }

        }
    }
    
    
    /**
     * @Author: 王冲
     * @Email:jonw@sina.com
     * @Date: 2012-5-11
     * @Time: 下午3:33:12
     * @version 1.0
     * @throws UnsupportedEncodingException 
     * @Description:   改变字体路经
     */
    private  String changeFontsPath () throws UnsupportedEncodingException{
 		// 得到当前的classpath
 		URL url = this.getClass().getClassLoader().getResource("");
 		// 路经
 		String path = url.getPath();
 		//转义空格 等特殊字符
 		path = URLDecoder.decode(path,ExportConstants.CODE_UTF8);
 		return path ;
 		
    }

    /**
     * Creates a font triplet matcher from a configuration object.
     * @param cfg the configuration object
     * @param strict true for strict configuraton error handling
     * @return the font matcher
     * @throws FOPException if an error occurs while building the matcher
     */
    public static FontTriplet.Matcher createFontsMatcher(
            Configuration cfg, boolean strict) throws FOPException {
        List matcherList = new java.util.ArrayList();
        Configuration[] matches = cfg.getChildren("match");
        for (int i = 0; i < matches.length; i++) {
            try {
                matcherList.add(new FontFamilyRegExFontTripletMatcher(
                        matches[i].getAttribute("font-family")));
            } catch (ConfigurationException ce) {
                LogUtil.handleException(log, ce, strict);
                continue;
            }
        }
        FontTriplet.Matcher orMatcher = new OrFontTripletMatcher(
                (FontTriplet.Matcher[])matcherList.toArray(
                        new FontTriplet.Matcher[matcherList.size()]));
        return orMatcher;
    }

    private static class OrFontTripletMatcher implements FontTriplet.Matcher {

        private FontTriplet.Matcher[] matchers;

        public OrFontTripletMatcher(FontTriplet.Matcher[] matchers) {
            this.matchers = matchers;
        }

        /** {@inheritDoc} */
        public boolean matches(FontTriplet triplet) {
            for (int i = 0, c = matchers.length; i < c; i++) {
                if (matchers[i].matches(triplet)) {
                    return true;
                }
            }
            return false;
        }

    }

    private static class FontFamilyRegExFontTripletMatcher implements FontTriplet.Matcher {

        private Pattern regex;

        public FontFamilyRegExFontTripletMatcher(String regex) {
            this.regex = Pattern.compile(regex);
        }

        /** {@inheritDoc} */
        public boolean matches(FontTriplet triplet) {
            return regex.matcher(triplet.getName()).matches();
        }

    }

}
