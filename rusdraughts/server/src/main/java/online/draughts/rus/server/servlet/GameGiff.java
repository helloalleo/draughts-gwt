package online.draughts.rus.server.servlet;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.service.GameService;
import online.draughts.rus.server.util.GaeGifSequenceWriter;
import online.draughts.rus.shared.model.Move;
import online.draughts.rus.shared.util.StringUtils;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.10.15
 * Time: 15:48
 */
@Singleton
public class GameGiff extends HttpServlet {

  @Inject
  private GameService gameService;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String gameIdStr = req.getParameter("game");
    if (StringUtils.isEmpty(gameIdStr)) {
      return;
    }

    String resolution = req.getParameter("r");
    int width = 4000, height = 4000;
    if (StringUtils.isNotEmpty(resolution)) {
      String[] resArr = resolution.split("x");
      width = Integer.valueOf(resArr[0]);
      height = Integer.valueOf(resArr[1]);
    }

    Long gameId = Long.valueOf(gameIdStr);
    List<Move> moves = gameService.findGameMoves(gameId);
    if (moves.isEmpty()) {
      return;
    }

    GaeGifSequenceWriter writer = new GaeGifSequenceWriter();
    final int imgSize = 400;
    for (Move move : moves) {
      final byte[] imageData = Base64.decodeBase64(move.getScreenshot()
          .replaceAll("data:image/[a-z]*;base64,", "")
          .getBytes());
      Image img = ImagesServiceFactory.makeImage(imageData);
      Transform resize = ImagesServiceFactory.makeResize(imgSize, imgSize);
      img = ImagesServiceFactory.getImagesService().applyTransform(resize, img);
      Transform cropImg = ImagesServiceFactory.makeCrop(0, 0, 0.9, 0.94);
      img = ImagesServiceFactory.getImagesService().applyTransform(cropImg, img);
      writer.writeToSequence(img);
    }

    Image image = writer.getImage(width, height, imgSize, imgSize, 0, ImagesService.OutputEncoding.PNG);
    resp.getOutputStream().write(image.getImageData());
  }
}
