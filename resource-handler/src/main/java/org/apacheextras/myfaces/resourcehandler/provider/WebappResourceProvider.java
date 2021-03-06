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
package org.apacheextras.myfaces.resourcehandler.provider;

import org.apacheextras.myfaces.resourcehandler.RelativeResource;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ResourceProvider for resources in the context root of the webapp.
 *
 * @author Jakob Korherr
 */
public class WebappResourceProvider implements ResourceProvider
{

    public static final String WEBAPP_RESOURCES = "/resources/";
    public static final String WEBAPP_META_INF_RESOURCES = "/META-INF/resources/";

    private final String baseDir;
    private final boolean includeLibraryName;

    public WebappResourceProvider(String baseDir)
    {
        this(baseDir, false);
    }

    public WebappResourceProvider(String baseDir, boolean includeLibraryName)
    {
        baseDir = baseDir.trim();

        // base dir must start and end with a slash
        if (!baseDir.startsWith("/"))
        {
            baseDir = "/" + baseDir; // add leading slash
        }
        if (!baseDir.endsWith("/"))
        {
            baseDir = baseDir + "/";  // add trailing slash
        }

        this.baseDir = baseDir;
        this.includeLibraryName = includeLibraryName;
    }

    public URL getUrl(FacesContext facesContext, RelativeResource relativeResource)
    {
        try
        {
            return facesContext.getExternalContext()
                    .getResource(baseDir + relativeResource.getResourceFilePath(includeLibraryName));
        }
        catch (MalformedURLException e)
        {
            return null;
        }
    }

    public InputStream getInputStream(FacesContext facesContext, RelativeResource relativeResource) throws IOException
    {
        return facesContext.getExternalContext()
                .getResourceAsStream(baseDir + relativeResource.getResourceFilePath(includeLibraryName));
    }

}
