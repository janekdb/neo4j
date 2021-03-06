/*
 * Copyright (c) 2002-2020 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.server.http.cypher;

import java.util.Map;

import org.neo4j.internal.kernel.api.connectioninfo.ClientConnectionInfo;
import org.neo4j.internal.kernel.api.security.LoginContext;
import org.neo4j.kernel.GraphDatabaseQueryService;
import org.neo4j.kernel.api.KernelTransaction.Type;
import org.neo4j.kernel.impl.coreapi.InternalTransaction;
import org.neo4j.kernel.impl.factory.GraphDatabaseFacade;
import org.neo4j.kernel.impl.query.Neo4jTransactionalContextFactory;
import org.neo4j.kernel.impl.query.TransactionalContext;
import org.neo4j.kernel.impl.query.TransactionalContextFactory;
import org.neo4j.kernel.impl.util.ValueUtils;

public class TransitionalPeriodTransactionMessContainer
{
    private final GraphDatabaseFacade db;

    public TransitionalPeriodTransactionMessContainer( GraphDatabaseFacade db )
    {
        this.db = db;
    }

    TransitionalTxManagementKernelTransaction newTransaction( Type type, LoginContext loginContext, ClientConnectionInfo connectionInfo,
                                                              long customTransactionTimeout )
    {
        return new TransitionalTxManagementKernelTransaction( db, type, loginContext, connectionInfo, customTransactionTimeout );
    }

    public GraphDatabaseFacade getDb()
    {
        return db;
    }

    public TransactionalContext create(
            GraphDatabaseQueryService service,
            InternalTransaction transaction,
            String query,
            Map<String, Object> queryParameters )
    {
        TransactionalContextFactory contextFactory = Neo4jTransactionalContextFactory.create( service );
        return contextFactory.newContext( transaction, query, ValueUtils.asMapValue( queryParameters ) );
    }
}
