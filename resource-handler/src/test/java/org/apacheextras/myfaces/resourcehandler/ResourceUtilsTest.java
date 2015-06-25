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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test cases for ResourceUtils.
 *
 * @author Jakob Korherr
 */
@RunWith(JUnit4.class)
public class ResourceUtilsTest
{

    @Test
    public void testIsGZIPEncodingAccepted()
    {
        // ACCEPT
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("gzip,deflate")); // most common header value
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("gzip"));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("gzip;q=0.001"));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("gzip;q=0.01"));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("gzip;q=0.1"));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("gzip; q=0.001"));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("gzip;   q=0.001    "));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("gzip;   q=0.001    ,  compress"));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("compress, gzip;   q=0.001 "));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("gzip;   q=0.001    ,  compress;q=0.2 "));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("identity; q=0.2 ,  gzip;   q=0.001    ,  compress"));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("*"));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("*;q=1"));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("*;q=0.001"));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("*; q=0.1  "));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("  *;   q=0.001    ,  compress"));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("identity; q=0.2 ,  *;   q=0.001   "));
        Assert.assertTrue(ResourceUtils.isGZIPEncodingAccepted("identity; q=0.2 ,  *;   q=0.001    ,  compress"));

        // DENY
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted(""));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("deflate"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("gzip;q=0"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("gzip;q=0."));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("gzip;q=0.0"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("gzip;q=0.00"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("gzip;q=0.000"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("gzip; q=0"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("deflate, gzip;   q=0   , compress"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("gzip;q=0, *"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("*, gzip;q=0"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("*;q=0, gzip;q=0"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("*;q=0.3, gzip;q=0"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("*;q=0"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("*;q=0."));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("*;q=0.0"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("*;q=0.00"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("*;q=0.000"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("deflate, *;q=0"));
        Assert.assertFalse(ResourceUtils.isGZIPEncodingAccepted("deflate, *;q=0, identity "));
    }

}
