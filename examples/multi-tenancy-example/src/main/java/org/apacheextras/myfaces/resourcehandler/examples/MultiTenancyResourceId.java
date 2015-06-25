/*
 * Copyright 2012, Jakob Korherr
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

import org.apacheextras.myfaces.resourcehandler.ResourceId;

/**
 * Extended ResourceId, containing more information needed for multi tenancy support.
 *
 * @author Jakob Korherr
 */
public class MultiTenancyResourceId extends ResourceId
{

    private String clientLibraryName;

    public MultiTenancyResourceId(String resourceName, String libraryName,
                                  String requestedLocalePrefix, String clientLibraryName)
    {
        super(resourceName, libraryName, requestedLocalePrefix);
        this.clientLibraryName = clientLibraryName;
    }

    public String getClientLibraryName()
    {
        return clientLibraryName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }

        MultiTenancyResourceId that = (MultiTenancyResourceId) o;

        if (clientLibraryName != null ? !clientLibraryName.equals(that.clientLibraryName) : that.clientLibraryName != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (clientLibraryName != null ? clientLibraryName.hashCode() : 0);
        return result;
    }
}
