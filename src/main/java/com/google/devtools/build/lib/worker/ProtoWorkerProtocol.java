// Copyright 2020 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.devtools.build.lib.worker;

import com.google.devtools.build.lib.worker.WorkerProtocol.WorkRequest;
import com.google.devtools.build.lib.worker.WorkerProtocol.WorkResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** An implementation of a Bazel worker using Proto to communicate with the worker process. */
final class ProtoWorkerProtocol implements WorkerProtocolImpl {

  /** The worker process's stdin */
  private final InputStream stdin;

  /** The worker process's stdout. */
  private final OutputStream stdout;

  public ProtoWorkerProtocol(InputStream stdin, OutputStream stdout) {
    this.stdin = stdin;
    this.stdout = stdout;
  }

  @Override
  public void putRequest(WorkRequest request) throws IOException {
    request.writeDelimitedTo(stdout);
    stdout.flush();
  }

  @Override
  public WorkResponse getResponse() throws IOException {
    return WorkResponse.parseDelimitedFrom(stdin);
  }

  @Override
  public void close() {}
}
