/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.services;

import org.apache.nifi.cluster.protocol.DataFlow;
import org.apache.nifi.lifecycle.LifeCycle;


import java.io.IOException;
import java.util.Calendar;


/**
 * Defines the API level services available for carrying out file-based dataflow operations.
 *
 */
public interface FlowService extends LifeCycle {


    /**
     * Asynchronously saves the flow controller. The flow controller will be copied and immediately returned. If another call to save is made within that time the latest called state of the flow
     * controller will be used. In database terms this technique is referred to as 'write-delay'.
     *
     */
    void saveFlowChangesAsync();

    /**
     * Stops the flow and underlying repository as determined by user
     *
     * @param force if true the controller is not allowed to gracefully shut down.
     */
    @Override
    void stop(boolean force);

    /**
     * Loads the flow controller with the given flow. Passing null means that the local flow on disk will used as the proposed flow. If loading the proposed flow configuration would cause the
     * controller to orphan flow files, then an UninheritableFlowException is thrown.
     *
     * If the FlowSynchronizationException is thrown, then the controller may have changed some of its state and should no longer be used.
     *
     * @param proposedFlow the flow to load
     *
     * @throws IOException if flow configuration could not be retrieved from disk
     */
    void load(DataFlow proposedFlow) throws IOException;

    /**
     * Backus the contents of the current flow with a  given timestamp, overwriting the backup if it exists
     *
     * @param timestamp the file to write the current flow to
     * @throws IOException if unable to read the current flow or unable to write to the given file
     */
    void backupCurrentFlow(Calendar timestamp) throws IOException;

    /**
     * Creates a DataFlow object by first looking for a flow on from disk, and falling back to the controller's flow otherwise.
     *
     * @return the created DataFlow object
     *
     * @throws IOException if unable to read the flow from disk
     */
    DataFlow createDataFlow() throws IOException;

    /**
     * Creates a DataFlow object by serializing the flow controller's flow.
     *
     * @return the created DataFlow object.
     */
    DataFlow createDataFlowFromController() throws IOException;

}
