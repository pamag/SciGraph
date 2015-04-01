/**
 * Copyright (C) 2014 The SciGraph authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.sdsc.scigraph.internal;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

import com.google.common.base.Function;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class EvidenceAspect implements GraphAspect {

  static final RelationshipType HAS_SUBJECT = DynamicRelationshipType.withName("hasSubject");
  static final RelationshipType HAS_OBJECT = DynamicRelationshipType.withName("hasObject");
  
  private final GraphDatabaseService graphDb; 

  @Inject
  EvidenceAspect(GraphDatabaseService graphDb) {
    this.graphDb = graphDb;
  }

  @Override
  public void invoke(Graph graph) {
    Set<Long> nodeIds = newHashSet(transform(graph.getVertices(), new Function<Vertex, Long>() {
      @Override
      public Long apply(Vertex vertex) {
        return Long.valueOf((String)vertex.getId());
      }
    }
        ));
    try (Transaction tx = graphDb.beginTx()) {
      for (Vertex vertex: graph.getVertices()) {
        Node subject = graphDb.getNodeById(Long.valueOf((String)vertex.getId()));
        for (Relationship hasSubject: subject.getRelationships(HAS_SUBJECT, Direction.INCOMING)) {
          Node annotation = hasSubject.getOtherNode(subject);
          for (Relationship hasObject: annotation.getRelationships(HAS_OBJECT, Direction.OUTGOING)) {
            Node object = hasObject.getOtherNode(annotation);
            if (nodeIds.contains(object.getId())) {
              TinkerGraphUtil.addEdge(graph, hasSubject);
              TinkerGraphUtil.addEdge(graph, hasObject);
            }
          }
        }
      }
      tx.success();
    }
  }

}
