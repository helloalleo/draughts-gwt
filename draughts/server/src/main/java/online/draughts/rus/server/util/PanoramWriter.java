package online.draughts.rus.server.util;//
//  GifSequenceWriter.java
//  
//  Created by Elliot Kroo on 2009-04-25.
//
// This work is licensed under the Creative Commons Attribution 3.0 Unported
// License. To view a copy of this license, visit
// http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
// Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.


import com.google.appengine.api.images.Composite;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class PanoramWriter {

  private CompositeBuilder compositeBuilder = new CompositeBuilder();

  /**
   * Creates a new GifSequenceWriter
   */
  public PanoramWriter() {
  }

  class CompositeBuilder {
    private int x = 0;
    private int y = 0;
    private int col = 1;
    private List<Composite> composites = new ArrayList<>();

    /**
     * @param img
     * @param columns колонок в изображении
     */
    public void writeToSequence(Image img, int columns) {
      Composite composite = ImagesServiceFactory.makeComposite(img, x, y, 1, Composite.Anchor.TOP_LEFT);
      x += img.getWidth();
      if (col >= columns) {
        y += img.getHeight();
        col = 0;
        x = 0;
      }
      col++;
      composites.add(composite);
    }

    public List<Composite> getComposites() {
      return composites;
    }

    public void clear() {
      composites.clear();
      col = 0;
      x = 0;
      y = 0;
    }
  }

  public void writeToSequence(Image img) {
    compositeBuilder.writeToSequence(img, 8);
  }

  public List<Composite> getSubImages() {
    return compositeBuilder.getComposites();
  }

  public Image getImage(int width, int height, int imgWidth, int imgHeight, long color, ImagesService.OutputEncoding encoding) {
    final List<Composite> composites = compositeBuilder.getComposites();
    if (composites.size() > 16) {
      List<Image> images = getSubImages(composites, width, height, imgWidth, imgHeight, color, encoding);
      composites.clear();
      int yOffset = 0;
      for (Image image : images) {
        Composite composite = ImagesServiceFactory.makeComposite(image, 0, yOffset, 1, Composite.Anchor.TOP_LEFT);
        composites.add(composite);
        yOffset += imgHeight;
      }
    }
    return ImagesServiceFactory.getImagesService().composite(composites,
        width, height, color, encoding);
  }

  private List<Image> getSubImages(List<Composite> composites,
                                   int width, int height, int imgWidth, int imgHeight, long color,
                                   ImagesService.OutputEncoding encoding) {
    int heights = 0;
    for (int i = 0; i < composites.size(); i++) {
      if (0 == ((i + 1) % 16)) {
        heights++;
      }
    }
    int subHeight = height / heights;
    List<Image> images = new ArrayList<>();
    List<Composite> currentComposites = new ArrayList<>();
    final int iw = imgWidth * 8;
    for (int i = 0; i < composites.size(); i++) {
      currentComposites.add(composites.get(i));
      if (0 == ((i + 1) % 16)) {
        Image img = ImagesServiceFactory.getImagesService().composite(currentComposites, iw, imgHeight, color,
            encoding);
        images.add(img);
        currentComposites.clear();
        break;
      }
    }
    if (!currentComposites.isEmpty()) {
      Image img = ImagesServiceFactory.getImagesService().composite(currentComposites, iw, imgHeight, color,
          encoding);
//      images.add(img);
    }
    if (images.size() > 16) {
      CompositeBuilder compositeBuilder = new CompositeBuilder();
      for (Image image : images) {
        compositeBuilder.writeToSequence(image, 1);
      }
      return getSubImages(compositeBuilder.getComposites(), width, height, imgWidth, imgHeight, color, encoding);
    } else {
      return images;
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length > 1) {
      // grab the output image type from the first image in the sequence
    }
  }
}