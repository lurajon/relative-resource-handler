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

import javax.faces.FacesWrapper;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * Wrapper for RelativeResources.
 *
 * @author Jakob Korherr
 */
public abstract class RelativeResourceWrapper extends RelativeResource implements FacesWrapper<RelativeResource>
{

    private RelativeResource wrapped;

    public RelativeResourceWrapper(RelativeResource wrapped)
    {
        this.wrapped = wrapped;
    }

    public RelativeResource getWrapped()
    {
        return wrapped;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return getWrapped().getInputStream();
    }

    @Override
    public String getLibraryName()
    {
        return getWrapped().getLibraryName();
    }

    @Override
    public String getRequestPath()
    {
        return getWrapped().getRequestPath();
    }

    @Override
    public String getResourceName()
    {
        return getWrapped().getResourceName();
    }

    @Override
    public Map<String, String> getResponseHeaders()
    {
        return getWrapped().getResponseHeaders();
    }

    @Override
    public URL getURL()
    {
        return getWrapped().getURL();
    }

    @Override
    public void setContentType(String contentType)
    {
        getWrapped().setContentType(contentType);
    }

    @Override
    public void setLibraryName(String libraryName)
    {
        getWrapped().setLibraryName(libraryName);
    }

    @Override
    public void setResourceName(String resourceName)
    {
        getWrapped().setResourceName(resourceName);
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext facesContext)
    {
        return getWrapped().userAgentNeedsUpdate(facesContext);
    }

    @Override
    public boolean isInitialized()
    {
        return getWrapped().isInitialized();
    }

    @Override
    public void initialize(FacesContext facesContext)
    {
        getWrapped().initialize(facesContext);
    }

    @Override
    public boolean resourceExists()
    {
        return getWrapped().resourceExists();
    }

    @Override
    public String getResourceFilePath()
    {
        return getWrapped().getResourceFilePath();
    }

    @Override
    public String getResourceFilePath(boolean includeLibraryName)
    {
        return getWrapped().getResourceFilePath(includeLibraryName);
    }

    @Override
    public String getRelativePath()
    {
        return getWrapped().getRelativePath();
    }

    @Override
    public String getRequestedLocalePrefix()
    {
        return getWrapped().getRequestedLocalePrefix();
    }

    @Override
    public String getContentType()
    {
        return getWrapped().getContentType();
    }

}
