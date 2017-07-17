/*
 * Copyright 2017 the original author or authors.
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

package org.gradle.workers;

import org.gradle.api.Action;
import org.gradle.api.Incubating;

/**
 * Allows work to be submitted for asynchronous execution.  Work should be submitted with a {@link Runnable} class
 * representing the implementation of the unit of work and an action to configure the unit of work (via {@link WorkerConfiguration}).
 *
 * <pre>
 *      workerExecutor.submit(RunnableWorkImpl.class) { WorkerConfiguration conf ->
 *          // Set the isolation mode for the worker
 *          conf.isolationMode = IsolationMode.NONE
 *
 *          // Set up the constructor parameters for the unit of work
 *          conf.params = [ "foo", file('bar') ]
 *      }
 * </pre>
 *
 * @since 3.5
 */
@Incubating
public interface WorkerExecutor {
    /**
     * Submits a piece of work to be executed asynchronously.
     *
     * Execution of the work may begin immediately.
     *
     * Work configured with {@link IsolationMode#PROCESS} will execute in an idle daemon that meets the requirements set
     * in the {@link WorkerConfiguration}.  If no idle daemons are available, a new daemon will be started.  Any errors
     * will be thrown from {@link #await()} or from the surrounding task action if {@link #await()} is not used.
     */
    void submit(Class<? extends Runnable> actionClass, Action<? super WorkerConfiguration> configAction);

    /**
     * Blocks until all work associated with the current build operation is complete.
     *
     * @throws WorkerExecutionException when a failure occurs while executing the work.
     */
    void await() throws WorkerExecutionException;
}
