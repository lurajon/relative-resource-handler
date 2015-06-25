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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Test class for RelativeResourceHandler (ProjectStage = Production).
 *
 * @author Jakob Korherr
 */
public class RelativeResourceHandlerProductionTest extends AbstractJsfTestCase
{

    private RelativeResourceHandler relativeResourceHandler;
    private MockResourceHandler mockResourceHandler;

    @Before
    public void setUp() throws Exception
    {
        super.setUp();

        // ProjectStage = Production --> enables GZIP compression
        servletContext.addInitParameter(ProjectStage.PROJECT_STAGE_PARAM_NAME, ProjectStage.Production.name());

        mockResourceHandler = (MockResourceHandler) application.getResourceHandler();
        relativeResourceHandler = new RelativeResourceHandler(mockResourceHandler);
    }

    @After
    public void tearDown() throws Exception
    {
        mockResourceHandler = null;
        relativeResourceHandler = null;

        // remove gzip files created by RelativeResourceHandler
        File tmpDir = ResourceUtils.getServletContextTmpDir(facesContext);
        File gzipDir = new File(tmpDir, RelativeResourceImpl.CACHE_BASE_DIR);
        deleteRecursively(gzipDir);

        super.tearDown();
    }

    private void setResourceRequest(boolean resourceRequest)
    {
        mockResourceHandler.setResourceRequest(resourceRequest);
        facesContext.getAttributes().put(RelativeResourceHandler.HANDLING_RESOURCE_REQUEST, resourceRequest);
    }

    @Test
    public void testResourceRequestNoAcceptEncodingHeader_unzippedResourceStream() throws Exception
    {
        // we are in a resource request
        setResourceRequest(true);

        // add test library as relative library
        relativeResourceHandler.getConfig().addLibrary(new Library("my-library"));

        // create the resource just like handleResourceRequest() would do
        Resource resource = relativeResourceHandler.createResource("/1/de/my-library/resource.css");

        // read resource directly from classpath
        InputStream directInputStream = RelativeResourceHandlerTest.class
                .getResourceAsStream("/META-INF/resources/my-library/resource.css");
        ByteArrayOutputStream directOutputStream = new ByteArrayOutputStream();
        ResourceUtils.pipeBytes(directInputStream, directOutputStream, new byte[4096]);
        directInputStream.close();
        directOutputStream.close();

        // read resource via ResourceHandler
        InputStream resourceHandlerInputStream = resource.getInputStream();
        ByteArrayOutputStream resourceHandlerOutputStream = new ByteArrayOutputStream();
        ResourceUtils.pipeBytes(resourceHandlerInputStream, resourceHandlerOutputStream, new byte[4096]);
        resourceHandlerInputStream.close();
        resourceHandlerOutputStream.close();

        // byte-arrays must be equal
        Assert.assertArrayEquals(directOutputStream.toByteArray(), resourceHandlerOutputStream.toByteArray());
    }


    // BEGIN: helper methods

    private static void deleteRecursively(File file)
    {
        if (!file.exists())
        {
            return;
        }

        if (file.isDirectory())
        {
            for (File child : file.listFiles())
            {
                deleteRecursively(child);
            }
        }

        file.delete();
    }

}
