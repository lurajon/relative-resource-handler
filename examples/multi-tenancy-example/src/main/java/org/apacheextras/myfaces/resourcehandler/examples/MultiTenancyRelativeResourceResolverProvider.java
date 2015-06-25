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

import org.apacheextras.myfaces.resourcehandler.resolver.DefaultRelativeResourceResolver;
import org.apacheextras.myfaces.resourcehandler.resolver.RelativeResourceResolver;
import org.apacheextras.myfaces.resourcehandler.spi.RelativeResourceResolverProvider;

import javax.faces.context.FacesContext;

/**
 * Provider SPI implementation for MultiTenancyRelativeResourceResolver.
 *
 * @author Jakob Korherr
 */
public class MultiTenancyRelativeResourceResolverProvider implements RelativeResourceResolverProvider
{

    public RelativeResourceResolver getRelativeResourceResolver(FacesContext facesContext)
    {
        return new MultiTenancyRelativeResourceResolver(new DefaultRelativeResourceResolver());
    }

}
