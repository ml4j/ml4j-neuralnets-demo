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

package org.ml4j.nn.demo.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.ml4j.imaging.SerializableBufferedImageAdapter;
import org.ml4j.imaging.targets.ImageDisplay;

/**
 * Utility class for Mnist digit display.
 * 
 * @author Michael Lavelle
 *
 */
public class MnistUtils {

  /**
   * Renders the single Mnist image to the imageDisplay.
   * 
   * @param data The data of the Mnist image to display
   * @param imageDisplay The image display
   */
  public static void draw(float[] data, ImageDisplay<Long> imageDisplay) {
    double[] pixelData = new double[data.length];

    // Rearrange ordering of pixels for display purposes ( default image is
    // flipped),  and scale to greyscale range, and reverse zeros and ones to match reversed
    // input data
    int in = 0;
    for (int r1 = 0; r1 < 28; r1++) {
      for (int c = 0; c < 28; c++) {
        int pixelIndex = (c) * 28 + (r1);
        double originalPixelValue = data[pixelIndex] * 255;
        double reversedPixelValue = 255 - originalPixelValue;
        pixelData[in++] = reversedPixelValue;
      }
    }

    BufferedImage img = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);

    WritableRaster raster = img.getRaster();
    byte[] equiv = new byte[pixelData.length];
    for (int i = 0; i < equiv.length; i++) {
      equiv[i] = new Double(pixelData[i]).byteValue();
    }
    raster.setDataElements(0, 0, 28, 28, equiv);

    // Resize and display the image
    BufferedImage resized = new BufferedImage(280, 280, img.getType());
    Graphics2D graphics = resized.createGraphics();
    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics.drawImage(img, 0, 0, 280, 280, 0, 0, 28, 28, null);
    graphics.dispose();
    imageDisplay.onFrameUpdate(new SerializableBufferedImageAdapter(resized), 1000L);
  }
}
