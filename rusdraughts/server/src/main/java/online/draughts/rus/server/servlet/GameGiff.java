package online.draughts.rus.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.service.GameService;
import online.draughts.rus.server.utils.GifSequenceWriter;
import online.draughts.rus.shared.model.Move;
import online.draughts.rus.shared.util.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    String gameIdStr = req.getParameter("gameId");
    if (StringUtils.isEmpty(gameIdStr)) {
      return;
    }

    Long gameId = Long.valueOf(gameIdStr);
    List<Move> moves = gameService.findGameMoves(gameId);
    if (moves.isEmpty()) {
      return;
    }

    String firstMoveScreenshot = "";
    BufferedImage firstImage = ImageIO.read(new ByteArrayInputStream(firstMoveScreenshot.getBytes()));

    // create a new BufferedOutputStream with the last argument
    OutputStream respOut = resp.getOutputStream();
    ImageOutputStream output =
        new MemoryCacheImageOutputStream(respOut);

    // create a gif sequence with the type of the first image, 1 second
    // between frames, which loops continuously
    GifSequenceWriter writer =
        new GifSequenceWriter(output, firstImage.getType(), 1, false);

    // write respOut the first image to our sequence...
    writer.writeToSequence(firstImage);
    for (Move move : moves) {
      BufferedImage nextImage = ImageIO.read(new ByteArrayInputStream("".getBytes()));
      writer.writeToSequence(nextImage);
    }

    writer.close();
    output.close();
  }
}
