package online.shashki.rus.server;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 14.10.15
 * Time: 19:56
 */

import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {

  public static void main(String[] args) throws Exception {

    String webappDirLocation = "russhashki/server/src/main/webapp/";
    Tomcat tomcat = new Tomcat();

    //The port that we should run on can be set into an environment variable
    //Look for that variable and default to 8080 if it isn't there.
    String webPort = System.getenv("PORT");
    if(webPort == null || webPort.isEmpty()) {
      webPort = "8080";
    }

    tomcat.setPort(Integer.valueOf(webPort));

    tomcat.addWebapp("/rus", new File(webappDirLocation).getAbsolutePath());
    System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

    tomcat.start();
    tomcat.getServer().await();
  }
}

//public class Main {
//  private Random rand = new Random();
//
//  public void main(String[] args) {
//    int r = args[0];
//    int diam = r ^ 2;
//    int circleDot = 0;
//    int squareDot = 0;
//
//    for (int i = 0; i < 10000; i++) {
//      int dx = rand.nextInt();
//      int dy = rand.nextInt();
//      int sumSq = dx ^ 2 + dy ^ 2;
//      if (sumSq <= diam) {
//        circleDot++;
//      } else if (Math.abs(dx) <= r && Math.abs(dy) <= r) {
//        squareDot++;
//      }
//    }
//
//    double pi = 4 * circleDot / squareDot;
//    System.out.println(pi)
//  }
//}