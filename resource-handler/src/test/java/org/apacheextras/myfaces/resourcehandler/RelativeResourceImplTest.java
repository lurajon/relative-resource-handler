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
import org.apacheextras.myfaces.resourcehandler.config.Library;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for RelativeResource.
 *
 * @author Jakob Korherr
 */
@RunWith(JUnit4.class)
public class RelativeResourceImplTest extends AbstractJsfTestCase
{

    @Test(expected = IllegalArgumentException.class)
    public void testSlashInLibraryName_shouldThrowIllegalArgumentException() throws Exception
    {
        String illegalLibraryName = "library/Name";

        new RelativeResourceImpl("style.css", new Library(illegalLibraryName), null, "de_AT", false, false, "1.0.0");
    }

    @Test
    public void testSlashAtBeginAndEndOfLibraryName_shouldNotThrowException() throws Exception
    {
        String validLibraryName = "/libraryName/";

        new RelativeResourceImpl("style.css", new Library(validLibraryName), null, "de_AT", false, false, "1.0.0");
    }

}

