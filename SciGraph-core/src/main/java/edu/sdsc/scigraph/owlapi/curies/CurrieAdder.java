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
package edu.sdsc.scigraph.owlapi.curies;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.common.base.Optional;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

import edu.sdsc.scigraph.frames.CommonProperties;

final class CurrieAdder implements MethodInterceptor {

  @Inject
  CurieUtil curieUtil;

  void addCuries(Graph graph) {
    for (Vertex vertex: graph.getVertices()) {
      String uri = (String)vertex.getProperty(CommonProperties.URI);
      Optional<String> curie = curieUtil.getCurie(uri);
      if (curie.isPresent()) {
        vertex.setProperty(CommonProperties.CURIE, curie.get());
      }
    }
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Object result = invocation.proceed();
    if (result instanceof Graph) {
      addCuries((Graph)result);
    }
    return result;
  }

}
