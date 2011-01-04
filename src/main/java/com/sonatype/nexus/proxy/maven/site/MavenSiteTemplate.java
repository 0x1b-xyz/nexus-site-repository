/**
 * Copyright (c) 2008-2011 Sonatype, Inc.
 *
 * All rights reserved. Includes the third-party code listed at http://www.sonatype.com/products/nexus/attributions.
 * Sonatype and Sonatype Nexus are trademarks of Sonatype, Inc. Apache Maven is a trademark of the Apache Foundation.
 * M2Eclipse is a trademark of the Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package com.sonatype.nexus.proxy.maven.site;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.nexus.configuration.model.CRepository;
import org.sonatype.nexus.configuration.model.CRepositoryCoreConfiguration;
import org.sonatype.nexus.configuration.model.CRepositoryExternalConfigurationHolderFactory;
import org.sonatype.nexus.configuration.model.DefaultCRepository;
import org.sonatype.nexus.proxy.repository.RepositoryWritePolicy;
import org.sonatype.nexus.proxy.repository.WebSiteRepository;
import org.sonatype.nexus.templates.repository.AbstractRepositoryTemplate;

public class MavenSiteTemplate
    extends AbstractRepositoryTemplate
{

    public MavenSiteTemplate( MavenSiteTemplateProvider provider, String id, String description )
    {
        super( provider, id, description, new MavenSiteContentClass(), MavenSiteRepository.class );
    }

    @Override
    protected CRepositoryCoreConfiguration initCoreConfiguration()
    {
        CRepository repo = new DefaultCRepository();

        repo.setId( "" );

        repo.setProviderRole( WebSiteRepository.class.getName() );
        repo.setProviderHint( "maven-site" );

        Xpp3Dom ex = new Xpp3Dom( DefaultCRepository.EXTERNAL_CONFIGURATION_NODE_NAME );
        repo.setExternalConfiguration( ex );

        repo.setIndexable( false );

        repo.setWritePolicy( RepositoryWritePolicy.ALLOW_WRITE.name() );
        repo.setNotFoundCacheTTL( 1440 );

        CRepositoryCoreConfiguration result = new CRepositoryCoreConfiguration(
            getTemplateProvider().getApplicationConfiguration(),
            repo,
            new CRepositoryExternalConfigurationHolderFactory<DefaultMavenSiteRepositoryConfiguration>()
            {
                public DefaultMavenSiteRepositoryConfiguration createExternalConfigurationHolder( CRepository config )
                {
                    return new DefaultMavenSiteRepositoryConfiguration( (Xpp3Dom) config.getExternalConfiguration() );
                }
            } );

        return result;
    }
}
