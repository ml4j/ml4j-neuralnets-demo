/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ml4j.nn.demo.base.unsupervised;

import org.ml4j.MatrixFactory;
import org.ml4j.nn.NeuralNetworkContext;
import org.ml4j.nn.neurons.NeuronsActivation;
import org.ml4j.nn.unsupervised.UnsupervisedNeuralNetwork;

/**
 * Base class for test harness to train and showcase an UnsupervisedNeuralNetwork.
 * 
 * @author Michael Lavelle
 *
 * @param <N> The type of UnsupervisedNeuralNetwork we are showcasing
 * @param <C> The type of runtime NeuralNetworkContext for this UnsupervisedNeuralNetwork
 */
public abstract class UnsupervisedNeuralNetworkDemoBase<N 
    extends UnsupervisedNeuralNetwork<C, N>, C extends NeuralNetworkContext> {

  /**
   * Run the demo.
   * 
   * @throws Exception If any exceptions occur
   */
  public void runDemo() throws Exception {

    // Create a matrix factory we can use to create our NeuronsActivations
    // and contexts
    MatrixFactory matrixFactory = createMatrixFactory();

    // Create the training data NeuronActivations
    NeuronsActivation trainingDataInputActivations =
        createTrainingDataNeuronActivations(matrixFactory);

    // Determine the feature counts and bias inclusion of the training data
    int inputFeatureCount = trainingDataInputActivations.getActivations(matrixFactory).getColumns();

    // Create the network creation context
    C networkCreationContext = createNetworkCreationContext(matrixFactory);
    
    // Create the neural network for this feature count and bias
    N unsupervisedNeuralNetwork =
        createUnsupervisedNeuralNetwork(inputFeatureCount, networkCreationContext);

    // Create the training context
    C trainingContext = createTrainingContext(networkCreationContext);
    
    // Train the network
    unsupervisedNeuralNetwork.train(trainingDataInputActivations, trainingContext);
    
    // Create the test context
    C testContext = createTestContext(trainingContext);

    // Create the test set
    NeuronsActivation testSetInputActivations = createTestSetDataNeuronActivations(testContext);

    // Showcase the network on the test set
    showcaseTrainedNeuralNetwork(unsupervisedNeuralNetwork, testSetInputActivations, testContext);
  }



protected abstract C createNetworkCreationContext(MatrixFactory matrixFactory);


/**
 * Creates the NeuralNetworkContext we use in order to train the UnsupervisedNeuralNetwork.
 * 
 * @param unsupervisedNeuralNetwork The UnsupervisedNeuralNetwork we are training
 * @param matrixFactory The MatrixFactory we are using for this demo
 * @return the NeuralNetworkContext we use in order to train the UnsupervisedNeuralNetwork
 */
protected abstract C createTestContext(C trainingContext);

protected abstract C createTrainingContext(C networkCreationContext);


/**
   * Allows implementations to define the MatrixFactory they wish to use.
   * 
   * @return The MatrixFactory used for this demo
   */
  protected abstract MatrixFactory createMatrixFactory();

  /**
   * Constructs an input NeuronsActivations from the training data.
   * 
   * @param matrixFactory The MatrixFactory used to create the NeuronsActivations Matrix
   * @return The input NeuronsActivations from the training data
   */
  protected abstract NeuronsActivation createTrainingDataNeuronActivations(
      MatrixFactory matrixFactory);

  /**
   * Constructs an input NeuronsActivations from the test data.
   * 
   * @param testContext The context used to test the network
   * @return The input NeuronsActivations from the test data
   */
  protected abstract NeuronsActivation createTestSetDataNeuronActivations(
      C testContext);

  /**
   * Constructs the UnsupervisedNeuralNetwork we are demonstrating, given the input data's
   * featureCount.
   * 
   * @param featureCount The number of features in the input data this UnsupervisedNeuralNetwork
   *        supports
 *   @param networkCreationContext 
   * @return the UnsupervisedNeuralNetwork we are demonstrating
   */
  protected abstract N createUnsupervisedNeuralNetwork(int featureCount, C networkCreationContext);



  /**
   * Method to be implemented by subclasses to showcase the UnsupervisedNeuralNetwork.
   * 
   * @param unsupervisedNeuralNetwork The UnsupervisedNeuralNetwork we are showcasing
   * @param testDataInputActivations The input NeuronsActivation instance generated by the test data
   * @param testContext The context we use to showcase the network.
   * @throws Exception In the event of an exception
   */
  protected abstract void showcaseTrainedNeuralNetwork(N unsupervisedNeuralNetwork,
      NeuronsActivation testDataInputActivations, C testContext) throws Exception;
}