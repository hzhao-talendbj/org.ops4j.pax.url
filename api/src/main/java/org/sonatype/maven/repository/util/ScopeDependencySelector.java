package org.sonatype.maven.repository.util;

/*
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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.sonatype.maven.repository.Dependency;
import org.sonatype.maven.repository.DependencySelector;
import org.sonatype.maven.repository.DependencyNode;

/**
 * A dependency selector based on dependency scopes. <em>Note:</em> This filter does not assume any relationships
 * between the scopes. In particular, the filter is not aware of scopes that logically include other scopes.
 * 
 * @author Benjamin Bentmann
 * @see Dependency#getScope()
 */
public class ScopeDependencySelector
    implements DependencySelector
{

    private final Collection<String> included = new HashSet<String>();

    private final Collection<String> excluded = new HashSet<String>();

    /**
     * Creates a new selector using the specified includes and excludes.
     * 
     * @param included The set of scopes to include, may be {@code null} or empty to include any scope.
     * @param excluded The set of scopes to exclude, may be {@code null} or empty to exclude no scope.
     */
    public ScopeDependencySelector( Collection<String> included, Collection<String> excluded )
    {
        if ( included != null )
        {
            this.included.addAll( included );
        }
        if ( excluded != null )
        {
            this.excluded.addAll( excluded );
        }
    }

    /**
     * Creates a new selector using the specified excludes.
     * 
     * @param excluded The set of scopes to exclude, may be {@code null} or empty to exclude no scope.
     */
    public ScopeDependencySelector( String... excluded )
    {
        if ( excluded != null )
        {
            this.excluded.addAll( Arrays.asList( excluded ) );
        }
    }

    public boolean selectDependency( DependencyNode node, Dependency dependency )
    {
        if ( node.getDependency() == null )
        {
            return true;
        }

        String scope = dependency.getScope();
        return ( included.isEmpty() || included.contains( scope ) )
            && ( excluded.isEmpty() || !excluded.contains( scope ) );
    }

    public DependencySelector deriveChildSelector( DependencyNode childNode )
    {
        return this;
    }

}
