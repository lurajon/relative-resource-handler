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
import org.apacheextras.myfaces.resourcehandler.RelativeResourceWrapper;

/**
 * RelativeResource implementation, which adds support for multi-tenancy.
 *
 * @author Jakob Korherr
 */
public class MultiTenancyRelativeResource extends RelativeResourceWrapper
{

    private final String clientLibraryName;
    private final String originalLibraryName;

    public MultiTenancyRelativeResource(String clientLibraryName, RelativeResource wrapped)
    {
        super(wrapped);

        this.clientLibraryName = clientLibraryName;
        this.originalLibraryName = wrapped.getLibraryName();

        // set the library name containing the client identifier in the wrapped resource
        wrapped.setLibraryName(clientLibraryName);
    }

    @Override
    public String getRequestPath()
    {
        // the request-path needs to be built with the original library name (no client identifier in it)
        getWrapped().setLibraryName(originalLibraryName);
        try
        {
            return getWrapped().getRequestPath();
        }
        finally
        {
            getWrapped().setLibraryName(clientLibraryName);
        }
    }

    @Override
    public String getLibraryName()
    {
        return clientLibraryName;
    }

}
