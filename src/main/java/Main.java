import lombok.Getter;

public class Main {

  @Getter private static final String helloWorld = "Hello World!";

  /**
   * Entry point.
   * @param args {@link String}[] Shell arguments
   */
  public static void main(String[] args) {
    System.out.println(Main.getHelloWorld());
  }
}
