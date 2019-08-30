/*
 * Copyright (C) 2010 Toni Menzel
 * Copyright (C) 2014 Guillaume Nodet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.url.mvn.internal;

import com.gkatzioura.maven.cloud.s3.S3StorageWagon;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.maven.wagon.ConnectionException;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.providers.file.FileWagon;
import org.eclipse.aether.transport.wagon.WagonProvider;
import org.ops4j.pax.url.mvn.internal.wagon.ConfigurableHttpWagon;
import org.ops4j.pax.url.mvn.s3.S3Constants;

/**
 * Simplistic wagon provider
 */
public class ManualWagonProvider implements WagonProvider
{

    private CloseableHttpClient client;
    private S3StorageWagon s3Wagon;
    private int readTimeout;
    private int connectionTimeout;

    public ManualWagonProvider( CloseableHttpClient client, int readTimeout )
    {
        this( client, readTimeout, readTimeout );
    }

    public ManualWagonProvider( CloseableHttpClient client, int readTimeout, int connectionTimeout )
    {
        this.client = client;
        this.readTimeout = readTimeout;
        this.connectionTimeout = connectionTimeout;
    }

    public Wagon lookup( String roleHint ) throws Exception
    {
        if( "file".equals( roleHint ) )
        {
            return new FileWagon();
        }
        else if( "http".equals( roleHint ) || "https".equals( roleHint) )
        {
            return new ConfigurableHttpWagon( client, readTimeout, connectionTimeout);
        }
        else if (S3Constants.PROTOCOL.equalsIgnoreCase(roleHint))
        {
            s3Wagon = new S3StorageWagon();
            s3Wagon.setPathStyleAccessEnabled("true");
            return s3Wagon;
        }

        return null;
    }

    public void release( Wagon wagon )
    {
        if (null != s3Wagon) {
            try {
                s3Wagon.disconnect();
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
        }
    }

}
