/**
 * Copyright (C) 2014 Christopher Condit (condit@sdsc.edu)
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
package edu.sdsc.scigraph.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum EdgeType implements RelationshipType {
  SUBCLASS_OF, SUPERCLASS_OF, PARTOF, HASPART, REL, IS_A, TYPE, EQUIVALENT_TO, DISJOINT_WITH, SAME_AS, DIFFERENT_FROM, ANNOTATION,
  SUB_OBJECT_PROPETY_OF, SUPER_OBJECT_PROPETY_OF, CLASS, PROPERTY
}