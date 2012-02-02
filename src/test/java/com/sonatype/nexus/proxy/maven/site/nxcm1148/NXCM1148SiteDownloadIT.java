/**
 * Copyright (c) 2008-2012 Sonatype, Inc.
 *
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/pro/attributions
 * Sonatype and Sonatype Nexus are trademarks of Sonatype, Inc. Apache Maven is a trademark of the Apache Foundation.
 * M2Eclipse is a trademark of the Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package com.sonatype.nexus.proxy.maven.site.nxcm1148;

import java.net.URL;

import org.junit.Assert;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Response;
import org.sonatype.nexus.integrationtests.AbstractMavenNexusIT;
import org.sonatype.nexus.integrationtests.RequestFacade;
import org.testng.annotations.Test;

public class NXCM1148SiteDownloadIT
    extends AbstractMavenNexusIT
{
    @Test
    public void testCssMimeType()
        throws Exception
    {
        this.setTestRepositoryId( "nxcm1148site" );

        Response response = null;
        try
        {
            response = RequestFacade.doGetRequest( RequestFacade.SERVICE_LOCAL + "repositories/"
                + this.getTestRepositoryId() + "/content/project/css/site.css" );
            Assert.assertTrue( response.getStatus().isSuccess() );

            Assert.assertEquals( MediaType.TEXT_CSS, response.getEntity().getMediaType() );
        }
        finally
        {
            RequestFacade.releaseResponse( response );
        }
    }

    @Test
    public void testDirectoryListing()
        throws Exception
    {
        this.setTestRepositoryId( "nxcm1148site" );
        Response response = null;
        try
        {
            response = RequestFacade.doGetRequest( RequestFacade.SERVICE_LOCAL + "repositories/"
                + this.getTestRepositoryId() + "/content/project/" );
            Assert.assertTrue( response.getStatus().isSuccess() );
            Assert.assertEquals( MediaType.APPLICATION_XML, response.getEntity().getMediaType() );
            Assert.assertTrue( response.getEntity().getText().contains( "<content-item>" ) );
        }
        finally
        {
            RequestFacade.releaseResponse( response );
        }

        try
        {
            response = RequestFacade.sendMessage(
                new URL( this.getBaseNexusUrl() + "content/sites/" + this.getTestRepositoryId() + "/project/" ),
                Method.GET,
                null );
            Assert.assertTrue( response.getStatus().isSuccess() );
            Assert.assertEquals( MediaType.TEXT_HTML, response.getEntity().getMediaType() );
            Assert.assertTrue( response.getEntity().getText().contains( "<html" ) );
        }
        finally
        {
            RequestFacade.releaseResponse( response );
        }

    }
}
