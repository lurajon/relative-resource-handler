/*
 * Copyright 2011-2012, Jakob Korherr
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apacheextras.myfaces.resourcehandler;

import org.apache.myfaces.test.base.junit4.AbstractJsfTestCase;
import org.apache.myfaces.test.mock.resource.MockResourceHandler;
import org.apacheextras.myfaces.resourcehandler.config.Library;
import org.apacheextras.myfaces.resourcehandler.provider.ClassPathResourceProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import java.util.Locale;

/**
 * Test class for RelativeResourceHandler (ProjectStage = Development).
 *
 * @author Jakob Korherr
 */
@RunWith(JUnit4.class)
public class RelativeResourceHandlerTest extends AbstractJsfTestCase
{

    private RelativeResourceHandler relativeResourceHandler;
    private MockResourceHandler mockResourceHandler;

    @Before
    public void setUp() throws Exception
    {
        super.setUp();

        // ProjectStage = Development --> disables GZIP compression
        servletContext.addInitParameter(ProjectStage.PROJECT_STAGE_PARAM_NAME, ProjectStage.Development.name());

        // set necessary path elements for resource handler
        request.setPathElements("/webapp", "/faces", "", "");

        mockResourceHandler = (MockResourceHandler) application.getResourceHandler();
        relativeResourceHandler = new RelativeResourceHandler(mockResourceHandler);
    }

    @After
    public void tearDown() throws Exception
    {
        mockResourceHandler = null;
        relativeResourceHandler = null;

        super.tearDown();
    }

    private void setResourceRequest(boolean resourceRequest)
    {
        mockResourceHandler.setResourceRequest(resourceRequest);
        facesContext.getAttributes().put(RelativeResourceHandler.HANDLING_RESOURCE_REQUEST, resourceRequest);
    }

    @Test
    public void testCreateResourceInResourceRequestWithLibraryName_validResource() throws Exception
    {
        // we are in a resource request
        setResourceRequest(true);

        // add test library as relative library
        relativeResourceHandler.getConfig().addLibrary(new Library("my-library"));

        // create the resource just like handleResourceRequest() would do
        Resource resource = relativeResourceHandler.createResource("/1/de/my-library/resource.css");

        Assert.assertNotNull(resource);
        Assert.assertEquals("my-library", resource.getLibraryName());
        Assert.assertEquals("resource.css", resource.getResourceName());
    }

    @Test
    public void testCreateResourceInResourceRequestWithLibraryName_validResource_localeSupportDisabled()
            throws Exception
    {
        // we are in a resource request
        setResourceRequest(true);

        // add test library as relative library
        relativeResourceHandler.getConfig().addLibrary(new Library("my-library"));

        // disable locale support
        relativeResourceHandler.getConfig().setLocaleSupportEnabled(false);

        // create the resource just like handleResourceRequest() would do
        Resource resource = relativeResourceHandler.createResource("/1/my-library/resource.css");

        Assert.assertNotNull(resource);
        Assert.assertEquals("my-library", resource.getLibraryName());
        Assert.assertEquals("resource.css", resource.getResourceName());
    }

    @Test
    public void testCreateResourceInResourceRequestNoLibraryName_nullResource() throws Exception
    {
        // we are in a resource request
        setResourceRequest(true);

        // create the resource just like handleResourceRequest() would do
        Resource resource = relativeResourceHandler.createResource("/de/resource.css");

        // resource must be null, b/c we can't map a libraryName that is null (there can't be a config entry for it)
        Assert.assertNull(resource);
    }

    @Test
    public void testCreateResource_trimSlashes() throws Exception
    {
        // this is not the resource request --> no special handling of resourceName
        setResourceRequest(false);

        // add test library as relative library
        relativeResourceHandler.getConfig().addLibrary(new Library("my-library"));

        // create the resource with additional slashes
        Resource resource = relativeResourceHandler.createResource("/resource.css", "/my-library/");

        // resource must exist, leading and trailing slashes in resourceName and libraryName must be removed
        Assert.assertNotNull(resource);
        Assert.assertEquals("my-library", resource.getLibraryName());
        Assert.assertEquals("resource.css", resource.getResourceName());
    }

    @Test
    public void testCreateResourceNormalRequest_LocaleSupport_usesViewRootLocale() throws Exception
    {
        // normal request
        setResourceRequest(false);

        // set ViewRoot locale to de_DE
        facesContext.getViewRoot().setLocale(Locale.GERMANY);

        // add localized test library as relative library
        Library localizedLibrary = new Library("css");
        localizedLibrary.setResourceProvider(new ClassPathResourceProvider("META-INF/localized-resources", true));
        relativeResourceHandler.getConfig().addLibrary(localizedLibrary);

        // enable locale support
        relativeResourceHandler.getConfig().setLocaleSupportEnabled(true);

        // set url version
        relativeResourceHandler.getConfig().setUrlVersion("1.0.0");

        // create the resource, DO NOT add locale prefix info
        Resource resource = relativeResourceHandler.createResource("style.css", "css");

        // do standard checks
        Assert.assertNotNull(resource);
        Assert.assertEquals("css", resource.getLibraryName());
        Assert.assertEquals("style.css", resource.getResourceName());

        // do special checks
        Assert.assertTrue("RelativeResource URL must contain locale prefix",
                resource.getURL().toExternalForm().contains("de"));

        Assert.assertTrue("RelativeResource request path must contain locale prefix",
                resource.getRequestPath().contains("de_DE"));
    }

    @Test
    public void testCreateResource_LocaleSupport_de_AT_fullMatch() throws Exception
    {
        Resource resource = genericResourceRequestLocaleTest("de_AT");

        Assert.assertTrue("RelativeResource URL must contain locale prefix",
                resource.getURL().toExternalForm().contains("de_AT"));
    }

    @Test
    public void testCreateResource_LocaleSupport_de_CH_LanguageMatch() throws Exception
    {
        Resource resource = genericResourceRequestLocaleTest("de_CH");

        Assert.assertTrue("RelativeResource URL must contain language prefix from locale",
                resource.getURL().toExternalForm().contains("de"));
        Assert.assertFalse("RelativeResource URL must not contain wrong country code from locale",
                resource.getURL().toExternalForm().contains("de_AT"));
    }

    @Test
    public void testCreateResource_LocaleSupport_de_LanguageMatch() throws Exception
    {
        Resource resource = genericResourceRequestLocaleTest("de");

        Assert.assertTrue("RelativeResource URL must contain language prefix from locale",
                resource.getURL().toExternalForm().contains("de"));
        Assert.assertFalse("RelativeResource URL must not contain wrong country code from locale",
                resource.getURL().toExternalForm().contains("de_AT"));
    }

    @Test
    public void testCreateResource_LocaleSupport_en_GB_LanguageMatch() throws Exception
    {
        Resource resource = genericResourceRequestLocaleTest("en_GB");

        Assert.assertTrue("RelativeResource URL must contain language prefix from locale",
                resource.getURL().toExternalForm().contains("en"));
    }

    @Test
    public void testCreateResource_LocaleSupport_en_LanguageMatch() throws Exception
    {
        Resource resource = genericResourceRequestLocaleTest("en");

        Assert.assertTrue("RelativeResource URL must contain language prefix from locale",
                resource.getURL().toExternalForm().contains("en"));
    }

    @Test
    public void testCreateResource_LocaleSupport_fr_FR_NoMatch() throws Exception
    {
        Resource resource = genericResourceRequestLocaleTest("fr_FR");

        Assert.assertTrue("RelativeResource URL must not contain locale information",
                resource.getURL().toExternalForm().endsWith("/localized-resources/css/style.css"));
    }

    @Test
    public void testCreateResource_LocaleSupport_fr_NoMatch() throws Exception
    {
        Resource resource = genericResourceRequestLocaleTest("fr");

        Assert.assertTrue("RelativeResource URL must not contain locale information",
                resource.getURL().toExternalForm().endsWith("/localized-resources/css/style.css"));
    }

    @Test
    public void testCreateResource_LocaleSupport_RequestLocaleDiffersFromViewLocale() throws Exception
    {
        // set view locale to en_GB
        facesContext.getViewRoot().setLocale(Locale.UK);

        // request resource in de_AT
        Resource resource = genericResourceRequestLocaleTest("de_AT");

        // do checks
        Assert.assertTrue("RelativeResource URL must contain locale prefix from request path",
                resource.getURL().toExternalForm().contains("de_AT"));

        Assert.assertTrue("RelativeResource request path must contain locale prefix from view root",
                resource.getRequestPath().contains("en_GB"));
    }

    private Resource genericResourceRequestLocaleTest(String localePrefix) throws Exception
    {
        setResourceRequest(true);

        // add localized test library as relative library
        Library localizedLibrary = new Library("css");
        localizedLibrary.setResourceProvider(new ClassPathResourceProvider("META-INF/localized-resources", true));
        relativeResourceHandler.getConfig().addLibrary(localizedLibrary);

        // enable locale support
        relativeResourceHandler.getConfig().setLocaleSupportEnabled(true);

        // set url version
        relativeResourceHandler.getConfig().setUrlVersion("1.0.0");

        // create the resource just like handleResourceRequest() would do
        Resource resource = relativeResourceHandler.createResource("1.0.0/" + localePrefix + "/css/style.css");

        // do standard checks
        Assert.assertNotNull(resource);
        Assert.assertEquals("css", resource.getLibraryName());
        Assert.assertEquals("style.css", resource.getResourceName());

        // return resource to do special checks
        return resource;
    }

}
