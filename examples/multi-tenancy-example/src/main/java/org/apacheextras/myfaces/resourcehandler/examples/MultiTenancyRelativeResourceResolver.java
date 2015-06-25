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
package org.apacheextras.myfaces.resourcehandler.examples;

import org.apacheextras.myfaces.resourcehandler.RelativeResource;
import org.apacheextras.myfaces.resourcehandler.ResourceId;
import org.apacheextras.myfaces.resourcehandler.config.RelativeResourceHandlerConfig;
import org.apacheextras.myfaces.resourcehandler.resolver.RelativeResourceResolver;
import org.apacheextras.myfaces.resourcehandler.resolver.RelativeResourceResolverWrapper;

import javax.faces.context.FacesContext;

/**
 * RelativeResourceResolver implementation for adding multi-tenancy support.
 *
 * @author Jakob Korherr
 */
public class MultiTenancyRelativeResourceResolver extends RelativeResourceResolverWrapper
{

    public MultiTenancyRelativeResourceResolver(RelativeResourceResolver wrapped)
    {
        super(wrapped);
    }

    @Override
    public RelativeResource createRelativeResource(ResourceId resourceId, String contentType,
                                                   RelativeResourceHandlerConfig config)
    {
        RelativeResource relativeResource = super.createRelativeResource(resourceId, contentType, config);

        if (resourceId instanceof MultiTenancyResourceId)
        {
            relativeResource = new MultiTenancyRelativeResource(
                    ((MultiTenancyResourceId) resourceId).getClientLibraryName(), relativeResource);
        }

        return relativeResource;
    }

    @Override
    public ResourceId calculateRelativeId(String path, RelativeResourceHandlerConfig config)
    {
        ResourceId resourceId = super.calculateRelativeId(path, config);
        if (isClientSpecificResource(resourceId))
        {
            resourceId = new MultiTenancyResourceId(
                    resourceId.getResourceName(),
                    resourceId.getLibraryName(),
                    resourceId.getRequestedLocalePrefix(),
                    calculateClientLibraryName(resourceId.getLibraryName()));
        }
        return resourceId;
    }

    @Override
    public ResourceId calculateRelativeId(String resourceName, String libraryName,
                                          String requestedLocalePrefix,
                                          RelativeResourceHandlerConfig config)
    {
        ResourceId resourceId = super.calculateRelativeId(resourceName, libraryName, requestedLocalePrefix, config);
        if (isClientSpecificResource(resourceId))
        {
            resourceId = new MultiTenancyResourceId(
                    resourceId.getResourceName(),
                    resourceId.getLibraryName(),
                    resourceId.getRequestedLocalePrefix(),
                    calculateClientLibraryName(resourceId.getLibraryName()));
        }
        return resourceId;
    }

    private boolean isClientSpecificResource(ResourceId resource)
    {
        // It also makes sense to only wrap certain resources, as clients may share some resources.
        // In our example, each client has different css files, but shares the same images.
        return !"images".equals(resource.getLibraryName());
    }

    private String calculateClientLibraryName(String wrappedLibraryName)
    {
        String libraryName = getClientIdentifierLibraryPrefix();

        if (wrappedLibraryName != null)
        {
            libraryName += "/" + wrappedLibraryName;
        }

        return libraryName;
    }

    private String getClientIdentifierLibraryPrefix()
    {
        if ("localhost".equals(getClientIdentifier()))
        {
            return "client-a";
        }
        else  // e.g. 127.0.0.1 for our tests
        {
            return "client-b";
        }
    }

    private String getClientIdentifier()
    {
        // use the server name to decide which client is using the application
        // (for local tests this can be e.g. localhost or 127.0.0.1).
        // NOTE that we could also use something different (cookie, user-group, ...)
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getRequestServerName();
    }

}
